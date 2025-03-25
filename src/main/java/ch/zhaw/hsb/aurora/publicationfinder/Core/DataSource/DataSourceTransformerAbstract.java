/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.InternModel;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.ConverterUtil;

/**
 * This abstract class defines the characteristics of a data source transformer and provides defaults.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public abstract class DataSourceTransformerAbstract {

    protected Map<String, Map<String, Object>> data;
    protected String idName;
    protected JsonNode licenses;
    protected JsonNode types;
    protected String providerName;

    /*
     * Fields are intern specific
     * transformedData = {
     * "ID1": {"field1": "value1", "field2": "value2", etc.},
     * "ID2": {"field1": "value1", "field2": "value2", etc.},
     * etc.
     * }
     * 
     */

    /**
     * Method to get the data which is transformed into intern model
     * @return Map<String, InternModel>
     */
    public Map<String, InternModel> getTransformedData() {

        Map<String, InternModel> transformedData = new HashMap<String, InternModel>();

        for (Entry<String, Map<String, Object>> elementEntry : this.data.entrySet()) {
            transformedData.put(elementEntry.getValue().get(this.idName).toString(),
                    this.getTransformedElement(elementEntry.getValue()));
        }

        if (PropertyConfiguration.isTestingEnabled()) {
            ConverterUtil.internModelToCSV(transformedData,
                    "/outputs/data_" + this.providerName + ".csv");
        }

        return transformedData;

    }

    /**
     * Method to get one item of the data transformed into intern model
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @return InternModel
     */
    public abstract InternModel getTransformedElement(Map<String, Object> element);

    /**
     * Method to get the id of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where id is stored
     * @return Object
     */
    protected Object getId(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);
    }

    /**
     * Method to get the authors of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNamesAuthor field names where authors are stored
     * @param fieldNamesAffiliations field names where affiliation is stored
     * @return Object
     */
    protected Object getAuthors(Map<String, Object> element, String[] fieldNamesAuthor,
            String[] fieldNamesAffiliations) {
        return element.get(fieldNamesAuthor[0]);
    }

    /**
     * Method to get the author of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where author is stored
     * @return Object
     */
    protected Object getAuthor(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);
    }

    /**
     * Method to get the editors of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where editors are stored
     * @return Object
     */
    protected Object getEditors(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);
    }

    /**
     * Method to get the date issued of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where date issued is stored
     * @return Object
     */
    protected Object getDateIssued(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the doi of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where doi is stored
     * @return Object
     */
    protected Object getDOI(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the ISBN of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where isbn is stored
     * @return Object
     */
    protected Object getISBN(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the ISSN of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where issn is stored
     * @return Object
     */
    protected Object getISSN(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the language of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where language is stored
     * @return Object
     */
    protected Object getLanguage(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the PMID of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where pmid is stored
     * @return Object
     */
    protected Object getPMID(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the publisher of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where publisher is stored
     * @return Object
     */
    protected Object getPublisher(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the publisher type of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where publisher type is stored
     * @return Object
     */
    protected Object getPublisherType(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the relation ispartof of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where relation ispartof is stored
     * @return Object
     */
    protected Object getRelationIsPartOf(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the relation ispartofseries of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where relation ispartofseries is stored
     * @return Object
     */
    protected Object getRelationIsPartOfSeries(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the license of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where license is stored
     * @return Object
     */
    protected Object getLicense(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the title of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where title is stored
     * @return Object
     */
    protected Object getTitle(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the type of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where type is stored
     * @return Object
     */
    protected Object getType(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the volume of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where volume is stored
     * @return Object
     */
    protected Object getVolume(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the issue of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where issue is stored
     * @return Object
     */
    protected Object getIssue(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the publication status of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where publication status is stored
     * @return Object
     */
    protected Object getPublicationStatus(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the pages start of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where pages start is stored
     * @return Object
     */
    protected Object getPagesStart(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);
    }

    /**
     * Method to get the pages end of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where pages end is stored
     * @return Object
     */
    protected Object getPagesEnd(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the publisher place of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where publisher place is stored
     * @return Object
     */
    protected Object getPublisherPlace(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the event of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where event is stored
     * @return Object
     */
    protected Object getEvent(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the affiliations of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where affiliations are stored
     * @return Object
     */
    protected Object getAffiliations(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the abstract of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where abstract is stored
     * @return Object
     */
    protected Object getAbstract(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

    /**
     * Method to get the OA of the element
     * @param element one element of the data: "ID1": {"field1": "value1", "field2": "value2", etc.}
     * @param fieldNames field names where OA is stored
     * @return Object
     */
    protected Object getOA(Map<String, Object> element, String[] fieldNames) {
        return element.get(fieldNames[0]);

    }

}
