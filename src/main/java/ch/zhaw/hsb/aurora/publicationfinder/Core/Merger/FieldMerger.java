/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Merger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Map.Entry;

import ch.zhaw.hsb.aurora.publicationfinder.Main;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource.DataSourceProviderInterface;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.ConverterUtil;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.JSONUtil;

/**
 * This class combines matched items into a single item based on specified merging criteria.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class FieldMerger implements MergerInterface {

    List<Map<String, Map<String, Object>>> allMatchedData;
    Set<String> metadataFields;
    Map<String, String> mergeCriteriaMap;
    Map<String, Integer> providerIndexMap;


     /* Fields are organisation specific
     * allMatchedData = [
     *      {
     *          "ID1": {"field1": "value1", "field2": "value2", etc.},
     *          "ID2": {"field1": "value1", "field2": "value2", etc.},
     *          etc.
     *      }, {
     *          "ID1": {"field1": "value1", "field2": "value2", etc.},
     *          "ID2": {"field1": "value1", "field2": "value2", etc.},
     *          etc.
     *      }, 
     *      etc.
     *  ]
     * 
     */

    /**
     * Constructor
     * @param listOfMapsMatched list with maps of the matched data
     * @param dataSourceProviders the provider of the source data
     */
    public FieldMerger(List<Map<String, Map<String, Object>>> listOfMapsMatched,
            ArrayList<DataSourceProviderInterface> dataSourceProviders) {

        this.allMatchedData = listOfMapsMatched;
        this.mergeCriteriaMap = this.getMergeCriterias();

        //indexing of different providers
        Map<String, Integer> providerIndexMap = new HashMap<>();
        for (int i = 0; i < dataSourceProviders.size(); i++) {
            //todo: for test purposes, testing 3 sources
            //if(i==1){
            //    providerIndexMap.put("openalex2", i);

            //}else{
                providerIndexMap.put(dataSourceProviders.get(i).getName(), i);

            //}
        }
        this.providerIndexMap = providerIndexMap;

    }

     /* Fields are organisation specific
     * allMergedData =
     *      {
     *          "ID1": {"field1": "value1", "field2": "value2", etc.},
     *          "ID2": {"field1": "value1", "field2": "value2", etc.},
     *          etc.
     *      }
     * 
     */

    /**
     * Method to get all merged data as a map
     * @return Map<String, Map<String, Object>>
     */
    public Map<String, Map<String, Object>> getAllMergedData() {

        Map<String, Map<String, Object>> allMergedData = new HashMap<String, Map<String, Object>>();

        // example {"field123":[listnumber1, listnumber2]}
        Map<String, ArrayList<Integer>> fields = new HashMap<String, ArrayList<Integer>>();

        for (int i = 0; i < allMatchedData.size(); i++) {
            for (String key : allMatchedData.get(i).keySet()) {

                //get the list of metadataFields
                if (this.metadataFields == null) {
                    this.metadataFields = this.allMatchedData.get(i).get(key).keySet();
                }

                //add field with listnumber to fields map
                if (!fields.containsKey(key)) {
                    ArrayList<Integer> newList = new ArrayList<>();
                    newList.add(i);

                    fields.put(key, newList);

                } else {
                    //add listnumber to field already existing in the map
                    ArrayList<Integer> newList = fields.get(key);
                    newList.add(i);
                    fields.put(key, newList);
                }

            }
        }

        for (Entry<String, ArrayList<Integer>> field : fields.entrySet()) {

            allMergedData.put(field.getKey(), this.getMergedElement(field));

        }


        
        System.out.println("All merged data size: "+allMergedData.size());
        if (PropertyConfiguration.isTestingEnabled()) {
            ConverterUtil.organisationModelToCSV(allMergedData, "/outputs/data_organisation_merged.csv");
        }
        return allMergedData;
    }

    /**
     * Method to get one merged element
     * @param field name of field to merge the elements
     * @return Map<String, Object>
     */
    private Map<String, Object> getMergedElement(Entry<String, ArrayList<Integer>> field) {

        Map<String, Object> newMergedElement = new HashMap<>();

        String key = field.getKey(); //field
        ArrayList<Integer> values = field.getValue(); //all numbers of lists which contain field element

        for (String metadataField : this.metadataFields) {
            // f.e. 0 empty, 1 nonempty, 2 nonempty
            // [false, true, true]
            List<Integer> nonEmptyFields = new ArrayList<Integer>();
            int countNonEmptyFields = 0;
            for (Integer value : values) {

                if (allMatchedData.get(value).get(key).get(metadataField) != null) {
                    nonEmptyFields.add(value);
                    countNonEmptyFields++;
                }

            }

            if (countNonEmptyFields <= 0) {
                newMergedElement.put(metadataField, null);

            } else if (countNonEmptyFields == 1) {
                newMergedElement.put(metadataField,
                        allMatchedData.get(nonEmptyFields.get(0)).get(key).get(metadataField));
            } else {
                // check mergeCriteria, calc prioritySource with nonEmptyFields
                int prioritySource = getPriorityProviderIndex(metadataField); // example Openalex 0, crossref 1,
                                                                              // thirdsource 2, etc..
                if (nonEmptyFields.contains(prioritySource)) {
                    newMergedElement.put(metadataField, allMatchedData.get(prioritySource).get(key).get(metadataField));

                } else {
                    // take first random from nonemptyfields
                    newMergedElement.put(metadataField,
                            allMatchedData.get(nonEmptyFields.get(0)).get(key).get(metadataField));

                }

            }

        }

        return newMergedElement;

    }

    /**
     * Method to get priority provider index to know which source to prioritize
     * @param metadataField the name of the field
     * @return Integer
     */
    private Integer getPriorityProviderIndex(String metadataField) {

        String provider = this.mergeCriteriaMap.get(metadataField);
        if(providerIndexMap.containsKey(provider)){
            return providerIndexMap.get(provider);
        }else{
            //default
            return 0;
        }

    }


    /**
     * Method the get the merge criterias as a map
     * @return Map<String, String>
     */
    private Map<String, String> getMergeCriterias() {

        try {

            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("assets/config/organisation.json");
            if (inputStream == null) {
                throw new FileNotFoundException("Config file not found: organisation.json");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode sourceJSON = objectMapper.readTree(inputStream);


            Map<String, String> mergeCriteriaMap = new HashMap<String, String>();

            ArrayNode records2 = (ArrayNode) JSONUtil.getField(sourceJSON, "map:record");

            for (JsonNode record : records2) {

                String field_name = (String) JSONUtil.getField(record, "field_name");

                String mergeCriteria = (String) JSONUtil.getField(record, "merge_criteria");

                mergeCriteriaMap.put(field_name, mergeCriteria);

            }
            return mergeCriteriaMap;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }

    }
}
