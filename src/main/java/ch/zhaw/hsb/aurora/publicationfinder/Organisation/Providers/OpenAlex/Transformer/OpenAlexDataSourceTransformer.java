/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Organisation.Providers.OpenAlex.Transformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource.DataSourceTransformerAbstract;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Factory.ModelFactory;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping.Provider2InternMapping;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.InternModel;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.PersonModel;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.ConverterUtil;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.StringUtil;

/**
 * This class transforms json data from openalex to the intern format.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class OpenAlexDataSourceTransformer extends DataSourceTransformerAbstract {

    Map<String, String[]> fieldDict;

    JsonNode publicationStatuses;
    JsonNode oaCategories;

    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor
     * 
     * @param map          map of the built data from the provider
     * @param mapping      mapping from provider to intern model
     * @param idName       the name of the identifier of publications
     * @param providerName the name of the provider
     */
    public OpenAlexDataSourceTransformer(Map<String, Map<String, Object>> map, Provider2InternMapping mapping,
            String idName, String providerName) {

        // read json here
        this.licenses = mapping.getByFieldName("licenses");
        this.types = mapping.getByFieldName("types");
        this.publicationStatuses = mapping.getByFieldName("publication_statuses");
        this.oaCategories = mapping.getByFieldName("oa_categories");
        this.fieldDict = mapping.getMapRecord();
        this.idName = idName;
        this.data = map;
        this.providerName = providerName;

    }

    @SuppressWarnings("unchecked")
    @Override
    public InternModel getTransformedElement(Map<String, Object> element) {

        InternModel internModel = ModelFactory.createInternModel();

        internModel.setProvider("openalex");

        try {
            internModel.setId((String) this.getId(element, this.fieldDict.get("id")));
        } catch (Exception e) {
            internModel.setId(null);
        }

        try {
            internModel.setAuthors((List<PersonModel>) this.getAuthors(element, this.fieldDict.get("authors"),
                    this.fieldDict.get("affiliations")));
        } catch (Exception e) {
            internModel.setAuthors(null);
        }

        try {
            internModel.setDateIssued(new String[] { ((JsonNode) this.getDateIssued(element, this.fieldDict.get("dateIssued"))).asText() });
        } catch (Exception e) {
            internModel.setDateIssued(null);
        }

        try {
            internModel.setDOI(new String[] { (String) this.getDOI(element, this.fieldDict.get("DOI")) });
        } catch (Exception e) {
            internModel.setDOI(null);
        }
        try {
            internModel.setISSN(ConverterUtil
                    .JSONArraytoStringArray((ArrayNode) this.getISSN(element, this.fieldDict.get("ISSN"))));
        } catch (Exception e) {
            internModel.setISSN(null);

        }
        try {
            internModel
                    .setLanguage(new String[] { (String) this.getLanguage(element, this.fieldDict.get("language")) });
        } catch (Exception e) {
            internModel.setLanguage(null);
        }
        try {
            internModel.setPublisher(
                    new String[] { (String) this.getPublisher(element, this.fieldDict.get("publisher")) });
        } catch (Exception e) {
            internModel.setPublisher(null);
        }
        try {
            internModel.setPublisherType(
                    new String[] { (String) this.getPublisherType(element, this.fieldDict.get("publisherType")) });
        } catch (Exception e) {
            internModel.setPublisherType(null);
        }
        try {
            internModel.setRelationIspartof(
                    new String[] {
                        removeQuotes((String) this.getRelationIsPartOf(element, this.fieldDict.get("relationIspartof"))) });
        } catch (Exception e) {
            internModel.setRelationIspartof(null);
        }
        try {
            internModel.setLicenses(ConverterUtil
                    .JSONArraytoStringArray((ArrayNode) this.getLicense(element, this.fieldDict.get("licenses"))));
        } catch (Exception e) {
            internModel.setLicenses(null);
        }
        try {
            internModel.setTitle(new String[] {
                    ((String) this.getTitle(element, this.fieldDict.get("title"))).replaceAll("<[^>]*>", "") });
        } catch (Exception e) {
            internModel.setTitle(null);
        }
        try {
            internModel.setType(new String[] { (String) this.getType(element, this.fieldDict.get("type")) });
        } catch (Exception e) {
            internModel.setType(null);
        }
        try {
            internModel.setPublicationStatus(
                    new String[] {
                        removeQuotes((String) this.getPublicationStatus(element, this.fieldDict.get("publicationStatus"))) });
        } catch (Exception e) {
            internModel.setPublicationStatus(null);
        }
        try {
            internModel.setVolume(new String[] { (String) this.getVolume(element, this.fieldDict.get("volume")) });
        } catch (Exception e) {
            internModel.setVolume(null);
        }
        try {
            internModel.setIssue(new String[] { (String) this.getIssue(element, this.fieldDict.get("issue")) });
        } catch (Exception e) {
            internModel.setIssue(null);
        }
        try {
            internModel.setPagesStart(
                    new String[] { (String) this.getPagesStart(element, this.fieldDict.get("pagesStart")) });
        } catch (Exception e) {
            internModel.setPagesStart(null);
        }
        try {
            internModel
                    .setPagesEnd(new String[] { (String) this.getPagesEnd(element, this.fieldDict.get("pagesEnd")) });
        } catch (Exception e) {
            internModel.setPagesEnd(null);
        }
        try {
            internModel.setPMID(new String[] { (String) this.getPMID(element, this.fieldDict.get("PMID")) });
        } catch (Exception e) {
            internModel.setPMID(null);
        }
        try {
            internModel.setAbstractText(
                    new String[] { (String) this.getAbstract(element, this.fieldDict.get("abstractText")) });
        } catch (Exception e) {
            internModel.setAbstractText(null);
        }
        try {
            internModel.setOA(new String[] { (String) this.getOA(element, this.fieldDict.get("OA")) });
        } catch (Exception e) {
            internModel.setOA(null);
        }

        return internModel;
    }

    @Override
    protected Object getAuthors(Map<String, Object> element, String[] fieldNamesAuthor,
            String[] fieldNamesAffiliations) {

        List<PersonModel> authors = new ArrayList<>();

        ArrayNode authorArrayNode = (ArrayNode) this.getAuthor(element, fieldNamesAuthor);
        ArrayNode affArrayNode = (ArrayNode) this.getAffiliations(element, fieldNamesAffiliations);

        for (int i = 0; i < authorArrayNode.size(); i++) {
            PersonModel person = ModelFactory.createPersonModel();
            person.setAuthor(authorArrayNode.get(i).asText());
            if (affArrayNode.get(i).asText() != "null") {
                person.setAffiliations(ConverterUtil.JSONArraytoStringArray((ArrayNode) affArrayNode.get(i)));
            }
            authors.add(person);
        }

        return authors;
    }

    @Override
    protected Object getAuthor(Map<String, Object> element, String[] fieldNames) {

        Object authorsObj = element.get(fieldNames[0]);
        ArrayNode results = new ArrayNode(factory);
        if (authorsObj != "null") {
            ArrayNode authors = (ArrayNode) authorsObj;
            for (int i = 0; i < authors.size(); i++) {
                String author = authors.get(i).asText();
                String result = StringUtil.replaceLast(author, " ", ",");
                String[] firstLastName = result.split(",");
                if (result.contains(",")) {
                    result = String.join(", ", firstLastName[1], firstLastName[0]);
                } else {
                    result = firstLastName[0];
                }
                results.add(result);
            }

        }
        return results;

    }

    @Override
    protected Object getPublisher(Map<String, Object> element, String[] fieldNames) {

        Object result1 = element.get(fieldNames[0]);
        Object result2 = element.get(fieldNames[1]);
        String publisherType = (String) this.getPublisherType(element, this.fieldDict.get("publisherType"));
        if (publisherType.equals("ebook platform")) {
            return result1.toString();
        } else if (publisherType.equals("journal")) {

            return result2.toString();

        }

        return "";
    }

    @Override
    protected Object getDOI(Map<String, Object> element, String[] fieldNames) {

        Object doi = element.get(fieldNames[0]);

        if(doi == null){
            return null;
        }

        return doi.toString().replace("https://doi.org/", "");
    }

    @Override
    protected Object getLicense(Map<String, Object> element, String[] fieldNames) {

        ArrayNode arrayNode = new ArrayNode(factory);
        Object license = element.get(fieldNames[0]);
        JsonNode licenseNode = this.licenses.get(license.toString());
        if (licenseNode != null) {
            arrayNode.add(licenseNode);

        } else {
            arrayNode.add(this.licenses.get("Default"));

        }

        return arrayNode;

    }

    @Override
    protected Object getType(Map<String, Object> element, String[] fieldNames) {

        String type = element.get(fieldNames[0]).toString();
        JsonNode typeNode = this.types.get(type);
        if (typeNode != null) {
            return typeNode.asText();

        } else {
            return this.types.get("Default").asText();

        }
    }

    @Override
    protected Object getPMID(Map<String, Object> element, String[] fieldNames) {
        Object pmid = element.get(fieldNames[0]);

        if (pmid != null) {
            return pmid.toString().substring(pmid.toString().lastIndexOf("/") + 1);
        }

        return null;

    }

    @Override
    protected Object getAbstract(Map<String, Object> element, String[] fieldNames) {

        // get abstract
        Object abstractElem = null;
        try {
            abstractElem = element.get(fieldNames[0].toString());
            // undo inverted index

            if (!abstractElem.equals(null)) {

                JsonNode abstractJson = (JsonNode) abstractElem;

                Iterator<String> words = abstractJson.fieldNames();

                Map<Integer, String> treeMap = new TreeMap<>();
                String resAbstract = "";

                // iterate over each word from inverted index
                while (words.hasNext()) {
                    String word = words.next();
                    ArrayNode positions = (ArrayNode) abstractJson.get(word);

                    for (int x = 0; x < positions.size(); x++) {
                        int position = positions.get(x).asInt();
                        treeMap.put(position, word);
                    }

                }
                for (Integer key : treeMap.keySet()) {

                    resAbstract = resAbstract.concat(" " + treeMap.get(key));

                }

                return this.cleanAbstract(resAbstract);
            }

            return null;

        } catch (ClassCastException e) {
            System.out.println(abstractElem.getClass());
            System.out.println(e);
            return null;
        }

    }

    /**
     * Method to clean the abstract text
     * 
     * @param abstractText the abstract text
     * @return Object
     */
    protected Object cleanAbstract(String abstractText) {
        Pattern pat = Pattern.compile("<[^>]*>");
        if (pat.matcher(abstractText) != null) {
            abstractText = abstractText.replaceAll("<[^>]*>", "").trim();
            String[] splitArr = abstractText.split(" ", 2);
            if (splitArr[0].toLowerCase().matches("abstract")) {
                abstractText = splitArr[1];
            } else if (abstractText.substring(0, 8).toLowerCase().matches("abstract")) {
                abstractText = abstractText.substring(8);
            }
        }
        return abstractText;

    }

    @Override
    protected Object getPublicationStatus(Map<String, Object> element, String[] fieldNames) {
        Object publicationStatus = super.getPublicationStatus(element, fieldNames);
        JsonNode publicationStatusNode = this.publicationStatuses.get(publicationStatus.toString());
        if (publicationStatusNode != null) {
            return publicationStatusNode.asText();

        } else {

            return this.publicationStatuses.get("Default").asText();
        }
    }

    @Override
    protected Object getOA(Map<String, Object> element, String[] fieldNames) {
        Object oa = super.getOA(element, fieldNames);
        JsonNode oaNode = this.oaCategories.get(oa.toString());
        if(oaNode != null) {
            return oaNode.asText();

        } else {

            return this.oaCategories.get("Default").asText();
        }
    }

    public static String removeQuotes(String input) {
        if (input != null && input.length() > 1) {
            if (input.startsWith("\"") && input.endsWith("\"")) {
                return input.substring(1, input.length() - 1);
            }
        }
        return input;
    }

}
