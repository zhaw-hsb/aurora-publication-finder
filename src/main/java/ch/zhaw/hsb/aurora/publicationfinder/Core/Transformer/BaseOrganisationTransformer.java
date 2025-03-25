/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Transformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping.Intern2OrganisationMapping;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.InternModel;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.ConverterUtil;

/**
 * This class tramsforms the data in intern to organisation format.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class BaseOrganisationTransformer {

    Map<String, String[]> fieldDict;
    Map<String, Boolean> multValuesDict;

    public JsonNode licenses;
    public JsonNode types;
    public JsonNode publicationStatuses;
    public JsonNode language;
    public JsonNode oaCategories;
    public String providerName;

    Map<String, InternModel> data;

    /**
     * Constructor
     * @param map map of intern models
     * @param provider provider name
     * @param mapping intern to organisation mapping
     */
    @SuppressWarnings("unchecked")
    public BaseOrganisationTransformer(Map<String, InternModel> map, String provider,
            Intern2OrganisationMapping mapping) {
        // read json here
        this.licenses = (JsonNode) mapping.getByFieldName("licenses");
        this.types = (JsonNode) mapping.getByFieldName("types");
        this.publicationStatuses = (JsonNode) mapping.getByFieldName("publication_statuses");
        this.language = (JsonNode) mapping.getByFieldName("language");
        this.oaCategories = (JsonNode) mapping.getByFieldName("oa_categories");

        this.fieldDict = (Map<String, String[]>) mapping.getByFieldName("map:record:fields");
        this.multValuesDict = (Map<String, Boolean>) mapping.getByFieldName("map:record:multVals");

        this.data = map;

        this.providerName = provider;
    }

    /* Fields are organisation specific
     * transformedData = {
     *      "ID1": {"field1": "value1", "field2": "value2", etc.},
     *      "ID2": {"field1": "value1", "field2": "value2", etc.},
     *      etc.
     * }
     * 
     */

    /**
     * Method to get all the transformed data
     * @return Map<String, Map<String, Object>>
     */
    public Map<String, Map<String, Object>> getTransformedData() {

        Map<String, Map<String, Object>> transformedData = new HashMap<String, Map<String, Object>>();

        for (Entry<String, InternModel> elementEntry : this.data.entrySet()) {
            transformedData.put(elementEntry.getValue().getId(), this.getTransformedElement(elementEntry.getValue()));
        }

        if (PropertyConfiguration.isTestingEnabled()) {
            ConverterUtil.organisationModelToCSV(transformedData,
                    "/outputs/data_" + this.providerName + "_organisation.csv");
        }

        return transformedData;

    }

    /**
     * Method to get one transformed element
     * @param internModel publications as intern model
     * @return Map<String, Object>
     */
    public Map<String, Object> getTransformedElement(InternModel internModel) {

        Map<String, Object> organisationMap = new HashMap<>();

        for (Entry<String, String[]> entry : fieldDict.entrySet()) {

            String organisationKey = entry.getKey();
            String[] internalKeys = entry.getValue();

            // always take first get even if there are multiple_fields in config -->
            // Reihenfolge in Config wichtig
            if (internalKeys.length > 0) {
                String methodName = "get" + internalKeys[0].substring(0, 1).toUpperCase()
                        + internalKeys[0].substring(1);
                Method method;
                try {
                    try {
                        method = this.getClass().getDeclaredMethod(methodName, InternModel.class);

                    } catch (NoSuchMethodException ex) {
                        method = this.getClass().getSuperclass().getDeclaredMethod(methodName, InternModel.class);
                    }
                    Object returnedValue = method.invoke(this, internModel);
                    if (!this.multValuesDict.get(organisationKey) && returnedValue instanceof String[]) {
                        if (returnedValue != null) {
                            organisationMap.put(organisationKey, returnedValue);
                            continue;

                        }
                    }
                    organisationMap.put(organisationKey, returnedValue);

                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }

        return organisationMap;

    }

    /**
     * Method to get the id of the intern model
     * @param internModel publication as intern model
     * @return String
     */
    public String getId(InternModel internModel) {
        return internModel.getId();
    }

    /**
     * Method to get the authors of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getAuthors(InternModel internModel) {
        // TODO: no authors list in intermodel
        return null;
    }

    /**
     * Method to get the editors of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getEditors(InternModel internModel) {
        return internModel.getEditors();
    }

    /**
     * Method to get the date issued of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getDateIssued(InternModel internModel) {
        return internModel.getDateIssued();

    }

    /**
     * Method to get the DOI of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getDOI(InternModel internModel) {
        return internModel.getDOI();
    }

    /**
     * Method to get the ISBN of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getISBN(InternModel internModel) {
        return internModel.getISBN();

    }

    /**
     * Method to get the ISSN of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getISSN(InternModel internModel) {
        return internModel.getISSN();
    }

    /**
     * Method to get the language of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getLanguage(InternModel internModel) {
        return internModel.getLanguage();
    }

    /**
     * Method to get the PMID of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getPMID(InternModel internModel) {
        return internModel.getPMID();

    }

    /**
     * Method to get the publisher of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getPublisher(InternModel internModel) {
        return internModel.getPublisher();
    }

    /**
     * Method to get the publisher type of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getPublisherType(InternModel internModel) {
        return internModel.getPublisherType();
    }

    /**
     * Method to get the relation ispartof of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getRelationIspartof(InternModel internModel) {
        return internModel.getRelationIspartof();
    }

    /**
     * Method to get the relation ispartofseries of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getRelationIspartofseries(InternModel internModel) {
        return internModel.getRelationIspartof();
    }

    /**
     * Method to get the licenses of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getLicenses(InternModel internModel) {
        return internModel.getLicenses();

    }

    /**
     * Method to get the title of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getTitle(InternModel internModel) {
        return internModel.getTitle();
    }

    /**
     * Method to get the type of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getType(InternModel internModel) {
        return internModel.getType();
    }

    /**
     * Method to get the volume of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getVolume(InternModel internModel) {
        return internModel.getVolume();
    }

    /**
     * Method to get the issue of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getIssue(InternModel internModel) {
        return internModel.getIssue();
    }

    /**
     * Method to get the publication status of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getPublicationStatus(InternModel internModel) {
        return internModel.getPublicationStatus();
    }

    /**
     * Method to get the pages start of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getPagesStart(InternModel internModel) {
        return internModel.getPagesStart();
    }

    /**
     * Method to get the pages end of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getPagesEnd(InternModel internModel) {
        return internModel.getPagesEnd();
    }

    /**
     * Method to get the publisher place of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getPublisherPlace(InternModel internModel) {
        return internModel.getPublisherPlace();
    }

    /**
     * Method to get the event of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getEvent(InternModel internModel) {
        return internModel.getEvent();
    }

    // TODO: ZHAW and FHNW do not use this field
    /**
     * Method to get the affiliations of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getAffiliations(InternModel internModel) {
        // TODO: there is no getAffiliations in internModel
        return null;
    }

    /**
     * Method to get the abstract text of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getAbstractText(InternModel internModel) {
        return internModel.getAbstractText();

    }

    /**
     * Method to get the OA of the intern model
     * @param internModel publication as intern model
     * @return String[]
     */
    public String[] getOA(InternModel internModel) {
        return internModel.getOA();

    }

}
