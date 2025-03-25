/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Modules.DSpace.DuplicateCheck;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyProviderConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping.Intern2OrganisationMapping;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.ConverterUtil;
import ch.zhaw.hsb.aurora.publicationfinder.Modules.DSpace.Service.HTTPService;

/**
 * This abstract class defines characteristics of a duplicate check and provides
 * defaults.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public abstract class DuplicateCheck {

    // checks if entries already exist in organisation's repository

    Map<String, Map<String, Object>> allData;
    Map<String, Map<String, Object>> duplicateData;
    JsonNode typesMapping;
    Map<String, String> fieldMapping;
    String workflowConfig;
    String publishedConfig;
    String doiFieldName;
    String titleFieldName;
    String typeFieldName;
    String repositoryAPIUrl;
    String[] specialCharacters;
    ObjectMapper objectMapper = new ObjectMapper();

    /*
     * Fields are organisation specific
     * allData =
     * {
     * "ID1": {"field1": "value1", "field2": "value2", etc.},
     * "ID2": {"field1": "value1", "field2": "value2", etc.},
     * etc.
     * 
     * }
     * 
     */
    /**
     * Constructor
     * 
     * @param allData whole organisation specific data as a map of maps
     */
    public DuplicateCheck(Map<String, Map<String, Object>> allData) {

        // insert whole dataset
        this.allData = allData;
        this.duplicateData = new HashMap<String, Map<String, Object>>();

        Intern2OrganisationMapping mapping = new Intern2OrganisationMapping();
        this.typesMapping = (JsonNode) mapping.getByFieldName("types");
        this.fieldMapping = (Map<String, String>) mapping.getByFieldName("map:record:fields:inverted");

        this.doiFieldName = this.fieldMapping.get("DOI").toString();
        this.titleFieldName = this.fieldMapping.get("title").toString();
        this.typeFieldName = this.fieldMapping.get("type").toString();

        this.publishedConfig = "";
        this.workflowConfig = "&configuration=workflow";

        this.repositoryAPIUrl = PropertyProviderConfiguration.getRepositoryAPIUrl();
        this.specialCharacters = PropertyProviderConfiguration.getSpecialCharacters();

    }

    /**
     * Method to get the deduplicated data as map of maps
     * 
     * @return Map<String, Map<String, Object>>
     */
    public Map<String, Map<String, Object>> getDeduplicatedData() {

        long start = System.currentTimeMillis();

        for (Entry<String, Map<String, Object>> entry : this.allData.entrySet()) {

            String doi = null;
            String title = null;
            String type = null;

            String id = entry.getKey();

            if (entry.getValue().get(this.doiFieldName) != null) {
                doi = ((String[]) entry.getValue().get(this.doiFieldName))[0];
            }

            if (entry.getValue().get(this.titleFieldName) != null) {
                title = ((String[]) entry.getValue().get(this.titleFieldName))[0];
            }

            type = ((String[]) entry.getValue().get(this.typeFieldName))[0];

            if (doi != null || title != null) {

                Map<String, Object> duplicateItem = getDuplicate(doi, title, type);
                if (duplicateItem != null) {
                    this.duplicateData.put(id, duplicateItem);
                }

            }

        }

        for (String id : this.duplicateData.keySet()) {
            this.allData.remove(id);
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;

        System.out.println("Deduplicated data size: " + this.allData.size());
        System.out.println("Duplicated data size: " + this.duplicateData.size());

        if (PropertyConfiguration.isTestingEnabled()) {
            System.out.println("Deduplication completed in " + timeElapsed / 1000 + " seconds.");
            ConverterUtil.organisationModelToCSV(this.allData, "/outputs/deduplicatedData.csv");
            ConverterUtil.organisationModelToCSV(this.duplicateData, "/outputs/duplicateData.csv");
        }

        return this.allData;
    }

    /**
     * Method to get the duplicate element as a map
     * 
     * @param doi   the doi of the publication
     * @param title the title of the publication
     * @param type  the type of the publication
     * @return Map<String, Object>
     */
    private Map<String, Object> getDuplicate(String doi, String title, String type) {

        Map<String, Object> duplicateItem = new HashMap<>();
        duplicateItem.put("doi", doi);
        duplicateItem.put("title", title);
        duplicateItem.put("responseDois", null);
        duplicateItem.put("responseTitles", null);
        duplicateItem.put("doiURL", null);
        duplicateItem.put("titleURL", null);
        duplicateItem.put("type", type);
        // für Testzwecke
        duplicateItem.put("duplicate", true);

        if (doi != null) {

            duplicateItem = this.checkWithDoi(duplicateItem);

        } else {

            duplicateItem = this.checkWithTitle(duplicateItem);

        }

        return duplicateItem;

    }

    /**
     * Method to check the duplication of the publication in the repository with
     * title
     * 
     * @param duplicateItem the item to be checked
     * @return Map<String, Object>
     */
    private Map<String, Object> checkWithTitle(Map<String, Object> duplicateItem) {

        // check if there exists an entry with same title
        String shortestTitle = "";

        for (String specialCharacter : specialCharacters) {
            // Pattern.quote is escaping special chars for the regex in split
            String temp = duplicateItem.get("title").toString().split(Pattern.quote(specialCharacter))[0];
            if (shortestTitle == "" || shortestTitle.length() > temp.length()) {

                shortestTitle = temp;
            }
        }

        for (String config : new String[] { this.publishedConfig, this.workflowConfig }) {

            duplicateItem.put("config", config);

            // "contains" is needed because we only have a part of the title
            String titleUrl = this.repositoryAPIUrl + "/discover/search/objects?f.title="
                    + URLEncoder.encode(shortestTitle) + ",contains" + config;

            duplicateItem.put("titleURL", titleUrl);


            String data = this.getData(config, titleUrl);

            if (data != null) {

                JsonNode responseObjectTitle;
                try {
                    responseObjectTitle = objectMapper.readTree(data);
                    ArrayNode responseTitleArrayNode = (ArrayNode) responseObjectTitle.get("_embedded")
                            .get("searchResult")
                            .get("_embedded").get("objects");

                    for (JsonNode jsonNode : responseTitleArrayNode) {
                        String responseTitle = getTitle(config, jsonNode,
                                duplicateItem.get("type").toString());

                        if (customTitleEquals(responseTitle, duplicateItem.get("title").toString(),
                                specialCharacters)) {
                            duplicateItem.put("responseTitles", responseTitle);
                            return duplicateItem;
                        }

                    }

                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }
        return null;

    }

    /**
     * Method to check the duplication of the publication in the repository with doi
     * 
     * @param duplicateItem the item to be checked
     * @return Map<String, Object>
     */
    private Map<String, Object> checkWithDoi(Map<String, Object> duplicateItem) {

        // set to 0, so default is duplicate = false
        int responseDoiLength = 0;

        // check if there exists an entry with same doi
        for (String config : new String[] { this.publishedConfig, this.workflowConfig }) {

            duplicateItem.put("config", config);

            String doiUrl = this.repositoryAPIUrl + "/discover/search/objects?f.doi="
                    + URLEncoder.encode(duplicateItem.get("doi").toString()) + ",equals"
                    + config;
            duplicateItem.put("doiURL", doiUrl);

            List<String> responseDois = new ArrayList<>();
            List<String> responseTitles = new ArrayList<>();

            try {

                String data = this.getData(config, doiUrl);

                boolean isDuplicate = false;

                if (data != null) {

                    JsonNode responseObjectDoi = objectMapper.readTree(data);

                    ArrayNode responseDoiArrayNode = (ArrayNode) responseObjectDoi.get("_embedded")
                            .get("searchResult")
                            .get("_embedded").get("objects");

                    for (JsonNode responseDoiJsonNode : responseDoiArrayNode) {

                        responseDois.add(duplicateItem.get("doi").toString());

                        responseTitles.add(
                                this.getTitle(config, responseDoiJsonNode, duplicateItem.get("type").toString()));

                        // check title when type is book/book part/conference paper/ conference
                        // proceedings
                        if (duplicateItem.get("type").toString().equals(this.typesMapping.get("book").asText())
                                || duplicateItem.get("type").toString().equals(this.typesMapping.get("book part").asText())
                                || duplicateItem.get("type").toString()
                                        .equals(this.typesMapping.get("conference paper").asText())
                                || duplicateItem.get("type").toString()
                                        .equals(this.typesMapping.get("conference proceedings").asText())) {

                            // title match -> Dublette
                            for (int j = 0; j < responseTitles.size(); j++) {

                                if (this.customTitleEquals(responseTitles.get(j), duplicateItem.get("title").toString(),
                                        specialCharacters)) {

                                    // duplicate checked with doi and title, stop checking titles
                                    isDuplicate = true;
                                    break;

                                }

                            }

                        } else {

                            // all other types -> Dublette because same doi was found
                            isDuplicate = true;
                            break;

                        }

                    }
                }

                if (PropertyConfiguration.isTestingEnabled()) {
                    if (responseDoiLength > 0
                            && (duplicateItem.get("type").toString().equals(this.typesMapping.get("book").asText())
                                    || duplicateItem.get("type").toString()
                                            .equals(this.typesMapping.get("book part").asText())
                                    || duplicateItem.get("type").toString()
                                            .equals(this.typesMapping.get("conference paper").asText())
                                    || duplicateItem.get("type").toString()
                                            .equals(this.typesMapping.get("conference proceedings").asText()))
                            && !isDuplicate) {

                        // für Testzwecke wird es ins csv mitgegeben
                        duplicateItem.put("responseTitles", responseTitles);
                        duplicateItem.put("responseDois", responseDois.toArray(new String[0]));
                        duplicateItem.put("duplicate", false);
                        return duplicateItem;
                    }
                }

                if (isDuplicate) {
                    duplicateItem.put("responseTitles", responseTitles);
                    duplicateItem.put("responseDois", responseDois.toArray(new String[0]));
                    return duplicateItem;
                }

                duplicateItem.put("responseDois", responseDois.toArray(new String[0]));

            } catch (IOException exception) {
                // label item as no duplicate when link is not working
            }

        }

        // check title if doi doesn't exist

        return null;

    }

    /**
     * Method to get the title of a published publication in the repository
     * 
     * @param responseDoiJsonNode the JSONNode of the response from the
     *                             repository
     * @return String
     */
    private String getTitle(JsonNode responseDoiJsonNode) {

        String title = responseDoiJsonNode.get("_embedded")
                .get("indexableObject").get("metadata")
                .get(this.titleFieldName).get(0).get("value").asText();

        return title;
    }

    /**
     * Method to get the title of a workflow publication in the repository
     * 
     * @param responseDoiJsonNode  the JSON Array of the response from the
     *                             repository
     * @param type                 type of the publication
     * @return String
     */
    private String getTitleWorkflow(JsonNode responseDoiJsonNode, String type) {

        String title = responseDoiJsonNode.get("_embedded")
                .get("indexableObject").get("_embedded")
                .get("workflowitem").get("sections")
                .get(this.getSection(type)).get(this.titleFieldName).get(0)
                .get("value").asText();

        return title;
    }

    /**
     * Method to get the title depending on the status of the publication in the
     * repository (published or workflow)
     * 
     * @param config               the configuration status of the publication
     * @param responseDoiJsonNode  the json node of the response from the
     *                             repository
     * @param type                 the type of the publication
     * @return String
     */
    private String getTitle(String config, JsonNode responseDoiJsonNode, String type) {
        if (config == this.publishedConfig) {
            return getTitle(responseDoiJsonNode);
        } else if (config == this.workflowConfig) {
            return getTitleWorkflow(responseDoiJsonNode, type);
        } else {
            return null;
        }
    }

    /**
     * Method to get the data from a response of a http request
     * 
     * @param url url to do the http request
     * @return String
     */
    private String getData(String url) {

        HttpResponse<String> response = HTTPService.sendRequest(null, null, url, "GET", null);
        if (response != null) {
            return response.body();

        }

        return null;

    }

    /**
     * Method to get the data from a response of a hrrp request where authentication
     * is needed
     * 
     * @param url url to do the http request
     * @return String
     */
    private String getDataAuthenticated(String url) {

        HttpResponse<String> response = HTTPService
                .sendRequestWithAuthentication(null, null,
                        url,
                        "GET");

        if (response != null) {

            return response.body();
        }

        return null;

    }

    /**
     * Method to get the data depending on the status of a publication in the
     * repository (published or workflow)
     * 
     * @param config the configuration status of the publication
     * @param url    url to do the http request
     * @return String
     */
    private String getData(String config, String url) {
        if (config == this.publishedConfig) {
            return getData(url);
        } else if (config == this.workflowConfig) {
            return getDataAuthenticated(url);
        } else {
            return null;
        }
    }

    /**
     * Method to get the duplicate data
     * 
     * @return Map<String, Map<String, Object>>
     */
    public Map<String, Map<String, Object>> getDuplicateData() {
        return this.duplicateData;
    }

    /**
     * Method to do a custom equals for titles
     * 
     * @param responseTitle     the title from the publication in the repository
     * @param title             the title to be compared
     * @param specialCharacters the special characters to define the substring of
     *                          the title
     * @return boolean
     */
    private boolean customTitleEquals(String responseTitle, String title, String[] specialCharacters) {

        if (responseTitle == null || title == null) {
            return false;
        }

        for (String specialCharacter : specialCharacters) {

            // had to replace all spaces because of one exception having unbreakable space
            String responseTitleTemp = responseTitle.split(Pattern.quote(specialCharacter))[0].trim().toLowerCase()
                    .replaceAll("[\\s|\\u00A0]+", "");

            String titleTemp = title.split(Pattern.quote(specialCharacter))[0].trim().toLowerCase()
                    .replaceAll("[\\s|\\u00A0]+", "");
            if (responseTitleTemp.equals(titleTemp)) {
                return true;
            }

        }
        return false;

    }

    protected abstract String getSection(String type);

}
