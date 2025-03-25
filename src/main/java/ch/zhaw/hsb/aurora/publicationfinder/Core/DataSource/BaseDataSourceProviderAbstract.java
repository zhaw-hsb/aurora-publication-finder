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

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyProviderConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping.Intern2OrganisationMapping;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping.Provider2InternMapping;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.InternModel;
import ch.zhaw.hsb.aurora.publicationfinder.Organisation.OrganisationFieldsTransformer;

/**
 * This abstract class defines the characteristics of a data source provider and provides defaults.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public abstract class BaseDataSourceProviderAbstract implements DataSourceProviderInterface {

    private String idName;
    private ArrayNode retrievedData;
    private Map<String,Map<String, Object>> builtData;
    private Map<String,InternModel> transformedData;
    private Map<String, Map<String, Object>> organisationData;
    private String urlAffiliations;
    private String urlRor;
    private String[] affiliations;
    private String[] rors;
    private String maxItemField;
    private Integer maxItem;
    private String nextCursorField;
    private String startCursor;
    private String fromDateField;
    private String connectionElement;
    private String metadata;
    private String itemsSection;
    private Provider2InternMapping mapping;
    private String providerName;
    

    /**
     * Constructor
     * @param provider
     */
    public BaseDataSourceProviderAbstract(String provider) {
        this.providerName = provider;
    }

    @Override
    public String getName() {
        System.out.println("Provider name: " + this.providerName);

        return this.providerName;
    }
    
    @Override
    public String getIdName() {
        return this.idName;
    }
    
    @Override
    public String getUrlAffiliations() {
        return this.urlAffiliations;
    }
    
    @Override
    public String getUrlRor() {
        return this.urlRor;
    }
    
    @Override
    public String[] getAffiliations() {
        return this.affiliations;
    }
    
    @Override
    public String getAffiliation(Integer index) {
        if (index < this.affiliations.length) {
            return this.affiliations[index];
        }
        else return null;
    }
    
    @Override
    public String[] getRors() {
        return this.rors;
    }
    
    @Override
    public String getRor(Integer index) {
        if (index < this.rors.length) {
            return this.rors[index];
        }
        else return null;
    }
    
    @Override
    public String getMaxItemField() {
        return this.maxItemField;
    }
    
    @Override
    public Integer getMaxItem() {
        return this.maxItem;
    }
    
    @Override
    public String getNextCursorField() {
        return this.nextCursorField;
    }
    
    @Override
    public String getStartCursor() {
        return this.startCursor;
    }

    @Override
    public String getFromDateField(){
        return this.fromDateField;
    }
    @Override
    public String getConnectionElement(){
        return this.connectionElement;
    }
    
    @Override
    public String getMetadata() {
        return this.metadata;
    }
    
    @Override
    public String getItemsSection() {
        return this.itemsSection;
    }
    
    @Override
    public ArrayNode getRetrievedData() {
        return this.retrievedData;
    }
    
    @Override
    public Map<String,Map<String, Object>> getBuiltData() {
        return this.builtData;
    }
    
    @Override
    public Provider2InternMapping getMapping() {
        return this.mapping;
    }

     
    @Override
    public Map<String,InternModel> getTransformedData() {
        System.out.println("Size of transformed data: " + this.transformedData.size());
        return this.transformedData;
    }
    
    @Override
    public void setTransformedData(Map<String,InternModel> transformedData) {
        this.transformedData = transformedData;
    }

    @Override
    public Map<String, Map<String, Object>> getOrganisationData() {
        System.out.println("Size of organisation data: " + this.organisationData.size());
        return this.organisationData;
    }

    @Override
    public void setOrganisationData(Map<String, Map<String, Object>> organisationData) {
        this.organisationData = organisationData;
    }

    @Override
    public DataSourceProviderInterface setup() {

        this.mapping = new Provider2InternMapping(this.getName());

        // get all the properties
        this.idName = PropertyProviderConfiguration.getFieldByName(providerName,"id");
        this.urlAffiliations = PropertyProviderConfiguration.getFieldByName(providerName,"url.affiliations");
        this.urlRor = PropertyProviderConfiguration.getFieldByName(providerName,"url.ror");
        this.affiliations = PropertyProviderConfiguration.getAffiliations(providerName);
        this.rors =PropertyProviderConfiguration.getRors();
        this.maxItemField = PropertyProviderConfiguration.getFieldByName(providerName,"maxItemField");
        this.maxItem = Integer.valueOf(PropertyProviderConfiguration.getFieldByName(providerName,"maxItem"));
        this.nextCursorField = PropertyProviderConfiguration.getFieldByName(providerName,"nextCursorField");
        this.startCursor = PropertyProviderConfiguration.getFieldByName(providerName,"startCursor");
        this.fromDateField = PropertyProviderConfiguration.getFieldByName(providerName,"fromDateField");
        this.connectionElement = PropertyProviderConfiguration.getFieldByName(providerName,"connectionElement");
        this.metadata = PropertyProviderConfiguration.getFieldByName(providerName,"metadata");
        this.itemsSection = PropertyProviderConfiguration.getFieldByName(providerName,"itemsSection");

        return this;
    }

    @Override
    public DataSourceProviderInterface retrieve() {
        
        DataSourceRetriever retriever = new DataSourceRetriever(this);

        this.retrievedData = retriever.getDataFromAllTypes();
        //System.out.println(this.retrievedData);
        return this;

    }

    @Override
    public DataSourceProviderInterface build() {

        System.out.println("build: ");

        DataSourceBuilder builder = new DataSourceBuilder(this.providerName, this.retrievedData, this.mapping, this.idName);

        this.builtData = builder.getData();

        return this;
    }

    @Override
    public DataSourceProviderInterface transform() {

        System.out.println("transform: ");

        return this;
    }
    
    @Override
    public DataSourceProviderInterface transformForOrganisation() {

        System.out.println("transform for organisation: ");
        
        Intern2OrganisationMapping mapping = new Intern2OrganisationMapping();

        //OrganisationJsonMapping mapping = new OrganisationJsonMapping();
        OrganisationFieldsTransformer organisationTransformer = new OrganisationFieldsTransformer(this.getTransformedData(),
                this.getName(), mapping);

        this.setOrganisationData(organisationTransformer.getTransformedData());

        return this;
    }

   

}
