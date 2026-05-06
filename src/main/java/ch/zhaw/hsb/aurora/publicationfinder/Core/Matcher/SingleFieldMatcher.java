/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyProviderConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource.DataSourceProviderInterface;
import ch.zhaw.hsb.aurora.publicationfinder.Core.LogCollector.HelpdeskLogCollector;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.ConverterUtil;

/**
 * This class attempts to compare items by checking if a specific field has the same value in both.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class SingleFieldMatcher implements MatcherInterface {

    List<Map<String, Map<String, Object>>> allData;
    ArrayList<DataSourceProviderInterface> dataSourceProviders;

    /**
     * Constructor
     * @param listOfMaps list of maps where the items are stored
     * @param dataSourceProviders the provider of the source data
     */
    public SingleFieldMatcher(List<Map<String, Map<String, Object>>> listOfMaps, ArrayList<DataSourceProviderInterface> dataSourceProviders) {

        this.allData = listOfMaps;
        this.dataSourceProviders = dataSourceProviders;
    }

    /*
     * Fields are organisation specific
     * allMatchedData = [
     * {
     * "ID1": {"field1": "value1", "field2": "value2", etc.},
     * "ID2": {"field1": "value1", "field2": "value2", etc.},
     * etc.
     * }, {
     * "ID1": {"field1": "value1", "field2": "value2", etc.},
     * "ID2": {"field1": "value1", "field2": "value2", etc.},
     * etc.
     * },
     * etc.
     * ]
     * 
     */


    /**
     * Method to get all the data where there is a match, each source data has own list
     * @return List<Map<String, Map<String, Object>>>
     */
    public List<Map<String, Map<String, Object>>> getAllMatchedData() {

        List<Map<String, Map<String, Object>>> allMatchedData = new ArrayList<Map<String, Map<String, Object>>>();
        int index = 0;
        // for every source
        for (Map<String, Map<String, Object>> sourceData : this.allData) {

            // we will get matchedData for every sourceData
            Map<String, Map<String, Object>> matchedMap = this.getMatchedData(sourceData, index);
            allMatchedData.add(matchedMap);

            HelpdeskLogCollector.logInfo("Matched "+this.dataSourceProviders.get(index).getName()+" items: "+matchedMap.size());
            if (PropertyConfiguration.isTestingEnabled()) {
            ConverterUtil.organisationModelToCSV(matchedMap,
                    "/outputs/data_" + index + "_organisation_matched.csv");
            }

            index++;

        }

        return allMatchedData;
    }

    /**
     * Method to get the matched data for one source data
     * @param sourceData the data form one source
     * @param index the index of the source data inside the list
     * @return Map<String, Map<String, Object>>
     */
    private Map<String, Map<String, Object>> getMatchedData(Map<String, Map<String, Object>> sourceData, int index) {
        Object field = null;
        Object field2 = null;

        String matchingString = PropertyProviderConfiguration.getMatchString();
        Map<String, Map<String, Object>> matchedData = new HashMap<String, Map<String, Object>>();
        // for every element field
        for (Entry<String, Map<String, Object>> sourceDataEntry : sourceData.entrySet()) {
            if (sourceDataEntry.getValue().get(matchingString) != null) {
                field = ((String[]) sourceDataEntry.getValue().get(matchingString))[0];
            }
            if (field == null) {
                break;
            }
            int index2 = 0;
            // for every other source
            for (Map<String, Map<String, Object>> sourceData2 : this.allData) {
                // not same as the initial source
                if (index != index2) {

                    for (Entry<String, Map<String, Object>> sourceDataEntry2 : sourceData2.entrySet()) {

                        if (sourceDataEntry2.getValue().get(matchingString) != null) {

                            field2 = ((String[]) sourceDataEntry2.getValue().get(matchingString))[0];
                        }

                        if (field2 == null) {
                            break;
                        }

                        if (field.equals(field2)) {

                            // only add object from first
                            matchedData.put(field.toString(), sourceDataEntry.getValue());
                        }

                    }

                }
                index2++;
            }
        }

        return matchedData;

    }

}
