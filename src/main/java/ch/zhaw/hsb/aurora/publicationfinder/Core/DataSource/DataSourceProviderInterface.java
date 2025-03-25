/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ArrayNode;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping.Provider2InternMapping;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.InternModel;

/**
 * This interface defines the characteristics of a data source provider.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public interface DataSourceProviderInterface {
    
    /**
     * Method to get the id name of the provider
     * @return String
     */
    public String getIdName();

    /**
     * Method to get the name of the provider
     * @return String
     */
    public String getName();

    /**
     * Method to get the url affiliations of the provider
     * @return String
     */
    public String getUrlAffiliations();

    /**
     * Method to get the url ror ids of the provider
     * @return String
     */
    public String getUrlRor();

    /**
     * Method to get the organisation affiliations of the provider
     * @return String[]
     */
    public String[] getAffiliations();

    /**
     * Method to get the organisation affiliation of the provider by index
     * @param index index of the affiliation
     * @return String
     */
    public String getAffiliation(Integer index);

    /**
     * Method to get the organisation ror ids of the provider
     * @return String[]
     */
    public String[] getRors();

    /**
     * Method to get the organisation ror id of the provider by index
     * @param index index of the ror id
     * @return String
     */
    public String getRor(Integer index);

    /**
     * Method to get the field where the maximum items per page is defined
     * @return String
     */
    public String getMaxItemField();

    /**
     * Method to get the maximum items per page
     * @return Integer
     */
    public Integer getMaxItem();

    /**
     * Method to get the next cursor field name
     * @return String
     */
    public String getNextCursorField();

    /**
     * Method to get the start cursor
     * @return String
     */
    public String getStartCursor();

    /**
     * Method to get the name of the field to specify from which date data should be collected
     * @return String
     */
    public String getFromDateField();

    /**
     * Method to get the element to connect other elements in the url
     * @return String
     */
    public String getConnectionElement();

    /**
     * Method to get the metadata of a provider
     * @return String
     */
    public String getMetadata();

    /**
     * Method to get the items section
     * @return String
     */
    public String getItemsSection();

    /**
     * Method to get the data which was retrieved from the source
     * @return JSONArray
     */
    public ArrayNode getRetrievedData();

    /**
     * Method to get the data which was built with the retrieved data
     * @return Map<String,Map<String, Object>>
     */
    public Map<String,Map<String, Object>> getBuiltData();

    /**
     * Method to get the mapping from provider to intern fields
     * @return Provider2InternMapping
     */
    public Provider2InternMapping getMapping();

    /**
     * Method to get the data which was transformed from the built data into a intern model
     * @return Map<String,InternModel>
     */
    public Map<String,InternModel> getTransformedData();

    /**
     * Method to set the transformed data
     * @param transformedData
     */
    public void setTransformedData(Map<String,InternModel> transformedData);

    /**
     * Method to get the organisation data which was transformed from the intern data
     * @return Map<String, Map<String, Object>>
     */
    public Map<String, Map<String, Object>> getOrganisationData();

    /**
     * Method to set the organisation data
     * @param organisationData
     */
    public void setOrganisationData(Map<String, Map<String, Object>> organisationData);

    /**
     * Method to start the setup and get back the data source provider
     * @return DataSourceProviderInterface
     */
    public DataSourceProviderInterface setup();
    /**
     * Method to start the retrieve and get back the data source provider
     * @return DataSourceProviderInterface
     */
    public DataSourceProviderInterface retrieve();
    /**
     * Method to start the build and get back the data source provider
     * @return DataSourceProviderInterface
     */
    public DataSourceProviderInterface build();
    /**
     * Method to start the transform and get back the data source provider
     * @return DataSourceProviderInterface
     */
    public DataSourceProviderInterface transform();
    /**
     * Method to start the transform for organisation and get back the data source provider
     * @return DataSourceProviderInterface
     */
    public DataSourceProviderInterface transformForOrganisation();
    
}
    
