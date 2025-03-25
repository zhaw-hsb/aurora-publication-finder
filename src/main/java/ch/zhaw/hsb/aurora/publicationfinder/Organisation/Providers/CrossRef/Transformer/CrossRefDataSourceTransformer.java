/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Organisation.Providers.CrossRef.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

/**
 * This class transforms json data from crossref to the intern format.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class CrossRefDataSourceTransformer extends DataSourceTransformerAbstract {

    Map<String, String[]> fieldDict;
    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor
     * 
     * @param map          map of the built crossref data
     * @param mapping      mapping from provider to intern model
     * @param idName       the name of the identifier of publications
     * @param providerName the name of the provider
     */
    public CrossRefDataSourceTransformer(Map<String, Map<String, Object>> map,
            Provider2InternMapping mapping, String idName, String providerName) {

        // read json here
        this.licenses = mapping.getByFieldName("licenses");
        this.types = mapping.getByFieldName("types");
        this.fieldDict = mapping.getMapRecord();
        this.idName = idName;
        this.data = map;
        this.providerName = providerName;

    }

    @SuppressWarnings("unchecked")
    @Override
    public InternModel getTransformedElement(Map<String, Object> element) {

        InternModel internModel = ModelFactory.createInternModel();

        internModel.setProvider("crossref");

        try {
            internModel.setId((String) this.getId(element, this.fieldDict.get("id")));
        } catch (Exception e) {
            internModel.setId("");
        }
        try {
            internModel.setAuthors((List<PersonModel>) this.getAuthors(element, this.fieldDict.get("authors"),
                    this.fieldDict.get("affiliations")));
        } catch (Exception e) {
            internModel.setAuthors(null);
        }
        try {
            internModel.setEditors(ConverterUtil
                    .JSONArraytoStringArray((ArrayNode) this.getEditors(element, this.fieldDict.get("editors"))));
        } catch (Exception e) {
            internModel.setEditors(null);
        }
        try {
            internModel.setDateIssued(
                    new String[] { (String) this.getDateIssued(element, this.fieldDict.get("dateIssued")) });
        } catch (Exception e) {
            System.out.println(e);
            internModel.setDateIssued(null);
        }

        try {
            internModel.setDOI(new String[] { (String) this.getDOI(element, this.fieldDict.get("DOI")) });
        } catch (Exception e) {
            internModel.setDOI(null);
        }
        try {
            internModel.setISBN((String[]) ConverterUtil
                    .JSONArraytoStringArray((ArrayNode) this.getISBN(element, this.fieldDict.get("ISBN"))));
        } catch (Exception e) {
            internModel.setISBN(null);

        }
        try {
            internModel.setISSN((String[]) this.getISSN(element, this.fieldDict.get("ISSN")));
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
            internModel.setRelationIspartof(
                    new String[] {
                        this.removeQuotes((String) this.getRelationIsPartOf(element, this.fieldDict.get("relationIspartof"))) });
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
            internModel
                    .setTitle(new String[] { this.removeQuotes((String) this.getTitle(element, this.fieldDict.get("title"))).replaceAll("<[^>]*>", "").replace("\n", "").replace("\r", "") });
        } catch (Exception e) {
            internModel.setTitle(null);
        }
        try {
            internModel.setType(new String[] { (String) this.getType(element, this.fieldDict.get("type")) });
        } catch (Exception e) {
            internModel.setType(null);
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
            internModel
                    .setPublisherPlace(new String[] {
                            (String) this.getPublisherPlace(element, this.fieldDict.get("publisherPlace")) });
        } catch (Exception e) {
            internModel.setPublisherPlace(null);
        }
        try {
            internModel.setEvent(new String[] { (String) this.getEvent(element, this.fieldDict.get("event")) });
        } catch (Exception e) {
            internModel.setEvent(null);
        }
        try {

            internModel.setAbstractText(new String[] {
                    ((String) this.getAbstract(element, this.fieldDict.get("abstractText"))) });
        } catch (Exception e) {
            internModel.setAbstractText(null);
        }

        return internModel;

    }

    @Override
    protected Object getAbstract(Map<String, Object> element, String[] fieldNames) {
        String result = (String) super.getAbstract(element, fieldNames);
        Pattern pat = Pattern.compile("<[^>]*>");
        if (pat.matcher(result) != null) {
            result = result.replaceAll("<[^>]*>", "");
            if (result.substring(0, 8).toLowerCase().matches("abstract")) {
                result = result.substring(8);
            }
            return result;
        } else {
            return super.getAbstract(element, fieldNames);
        }
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

        Object authorsFam = element.get(fieldNames[1].toString());
        Object authorsGiven = element.get(fieldNames[0].toString());

        ArrayNode authors_family = authorsFam instanceof ArrayNode ? (ArrayNode) authorsFam : null;
        ArrayNode authors_given = authorsGiven instanceof ArrayNode ? (ArrayNode) authorsGiven : null;

        ArrayNode results = new ArrayNode(factory);

        if (authors_family != null && authors_given != null) {
            for (int i = 0; i < authors_family.size(); i++) {

                String author_family = authors_family.get(i).asText();
                String author_given = authors_given.get(i).asText();

                String result = author_family + ", " + author_given;
                results.add(result);
            }
        } else if (authors_family != null) {

            for (JsonNode author_family : authors_family) {

                results.add(author_family.asText());
            }
        } else if (authors_given != null) {
            for (JsonNode author_given : authors_given) {

                results.add(author_given.asText());
            }
        }

        return results;
    }

    @Override
    protected Object getEditors(Map<String, Object> element, String[] fieldNames) {

        Object editorsFam = element.get(fieldNames[1].toString());
        Object editorsGiven = element.get(fieldNames[0].toString());

        ArrayNode editors_family = editorsFam instanceof ArrayNode ? (ArrayNode) editorsFam : null;
        ArrayNode editors_given = editorsGiven instanceof ArrayNode ? (ArrayNode) editorsGiven : null;

        ArrayNode results = new ArrayNode(factory);

        if (editors_family != null && editors_given != null) {
            for (int i = 0; i < editors_family.size(); i++) {

                String editor_family = editors_family.get(i).asText();
                String editor_given = editors_given.get(i).asText();

                String result = editor_family + ", " + editor_given;
                results.add(result);
            }
        } else if (editors_family != null) {

            for (JsonNode editor_family : editors_family) {

                results.add(editor_family.asText());
            }

        } else if (editors_given != null) {

            for (JsonNode editor_given : editors_given) {

                results.add(editor_given.asText());
            }

        }

        return results;

    }

    @Override
    protected Object getLicense(Map<String, Object> element, String[] fieldNames) {

        Object license = super.getLicense(element, fieldNames);
        ArrayNode results = new ArrayNode(factory);

        if (license instanceof ArrayNode) {
            ArrayNode licenseArray = (ArrayNode) license;
            for (int i = 0; i < licenseArray.size(); i++) {

                JsonNode licenseNode = this.licenses.get(licenseArray.get(i).asText());
                if (licenseNode != null) {
                    results.add(licenseNode);

                } else {
                    results.add(this.licenses.get("Default"));

                }

            }
        }

        return results;

    }

    @Override
    protected Object getTitle(Map<String, Object> element, String[] fieldNames) {
        Object title = element.get(fieldNames[0].toString());

        Object subtitle = element.get(fieldNames[1].toString());

        String result = null;
        if (title != null) {

            result = ((ArrayNode) title).get(0).asText().replaceAll("<[^>]*>", "").replace("\n", "").replace("\r", "");

        }

        if (subtitle != null && subtitle.toString().length() != 0) {
            result = removeQuotes(result) + ": " + removeQuotes(((ArrayNode) subtitle).get(0).asText()).replaceAll("<[^>]*>", "").replace("\n", "").replace("\r", "");
        }
        return result;
    }

    @Override
    protected Object getType(Map<String, Object> element, String[] fieldNames) {

        String type = super.getType(element, fieldNames).toString();
        JsonNode typeNode = this.types.get(type);
        if (typeNode != null) {
            return typeNode.asText();

        } else {

            return this.types.get("Default").asText();

        }

    }

    @Override
    protected Object getPagesStart(Map<String, Object> element, String[] fieldNames) {
        Object page = super.getPagesStart(element, fieldNames);
        if (page != null) {
            String pageString = (String) page;
            return pageString.indexOf("-") != -1 ? pageString.substring(0, pageString.indexOf("-")) : pageString;

        }
        return null;
    }

    @Override
    protected Object getPagesEnd(Map<String, Object> element, String[] fieldNames) {
        Object page = super.getPagesStart(element, fieldNames);
        if (page != null) {
            String pageString = (String) page;
            return pageString.indexOf("-") != -1 ? pageString.substring(pageString.indexOf("-") + 1) : null;

        }
        return null;

    }

    @Override
    protected Object getDateIssued(Map<String, Object> element, String[] fieldNames) {
        Object dateIssued = super.getDateIssued(element, fieldNames);
        if (dateIssued != null) {
            ArrayNode dateIssuedJson = (ArrayNode) dateIssued;
            return dateIssuedJson.get(0).get(0).toString();

        }
        return null;

    }

    @Override
    protected Object getRelationIsPartOf(Map<String, Object> element, String[] fieldNames) {
        Object relationIsPartOf = super.getRelationIsPartOf(element, fieldNames);
        if (relationIsPartOf != null) {
            ArrayNode relationIsPartOfJson = (ArrayNode) relationIsPartOf;
            return relationIsPartOfJson.get(0).toString();

        }
        return null;

    }

    @Override
    protected Object getISSN(Map<String, Object> element, String[] fieldNames) {
        Object issn = super.getISSN(element, fieldNames);
        if (issn != null && issn instanceof ArrayNode) {
            Map<String, Integer> map = new HashMap<>();
            ArrayNode issnJA = (ArrayNode) issn;

            for (JsonNode issnJANode : issnJA) {
                map.put(issnJANode.asText(), null);
            }

            return map.keySet().toArray(new String[0]);

        }
        return null;
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
