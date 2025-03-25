/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyProviderConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping.Provider2InternMapping;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.JSONUtil;

/**
 * This class builds a hash map from the retrieved JSON data.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class DataSourceBuilder {

    ArrayNode arrayNode;
    ArrayList<String> fieldNames;
    ArrayNode builtData;
    String idField;
    String affiliationField;
    String providerName;

    /**
     * Constructor
     * 
     * @param providerName name of the used provider
     * @param arrayNode    array of the retrieved data
     * @param mapping      mapping from the provider model to the intern model
     * @param idField      the name of the field where id is stored
     */
    public DataSourceBuilder(String providerName, ArrayNode arrayNode, Provider2InternMapping mapping, String idField) {

        this.providerName = providerName;
        this.arrayNode = arrayNode;
        System.out.println("DataSourceBuilder: ");
        Map<String, String[]> fieldDict = mapping.getMapRecord();
        this.fieldNames = new ArrayList<>();

        this.affiliationField = fieldDict.get("affiliations")[0];
        for (String[] elemList : fieldDict.values()) {

            for (String elem : elemList) {

                fieldNames.add(elem);
            }

        }
        this.idField = idField;
        System.out.println("FieldsName: " + this.fieldNames);

    }

    /**
     * Method to get built data as map with key provider id and value map of each
     * field with its value
     * 
     * @return Map<String,Map<String, Object>>
     */
    public Map<String, Map<String, Object>> getData() {

        Map<String, Map<String, Object>> all = new HashMap<String, Map<String, Object>>();

        for (int i = 0; i < this.arrayNode.size(); i++) {

            Map<String, Object> elementMap = new HashMap<String, Object>();
            JsonNode element = (JsonNode) this.arrayNode.get(i);

            ArrayNode affiliationValue = (ArrayNode) JSONUtil.getField(element, this.affiliationField);
            if (!this.affiliationCheck(affiliationValue)) {
                continue;
            }
            ;

            for (String fieldName : this.fieldNames) {

                Object value = JSONUtil.getField(element, fieldName);
                elementMap.put(fieldName, value);

            }

            all.put(elementMap.get(this.idField).toString(), elementMap);

        }

        System.out.println("Built data: " + all.size());
        return all;
    }

    /**
     * Method to compare affiliation from publication with configured list and
     * except defined affiliations
     * 
     * @param affiliations affiliations from publication
     * @return boolean
     */
    private boolean affiliationCheck(ArrayNode affiliations) {

        String[] providerAffiliations = PropertyProviderConfiguration.getAffiliations(this.providerName);
        String[] exceptionAffiliations = PropertyProviderConfiguration.getAffiliationExceptions();
        if (affiliations != null) {
            for (int i = 0; i < affiliations.size(); i++) {
                Object affiliationObject = affiliations.get(i);
                if (affiliationObject instanceof ArrayNode) {
                    ArrayNode affiliationArrayNode = (ArrayNode) affiliationObject;
                    for (int j = 0; j < affiliationArrayNode.size(); j++) {
                        if (affiliationObject instanceof ArrayNode) {

                            String affiliation = affiliations.get(i).get(j).asText();

                            if (exceptionAffiliations != null &&
                                    affiliation.matches(".*(" + String.join("|", exceptionAffiliations) + ").*")) {

                                continue;

                            }
                            if (affiliation
                                    .matches(".*(" + String.join("|", providerAffiliations) + ").*")) {
                                return true;

                            }
                        }

                    }
                }

            }

        }

        return false;

    }

}
