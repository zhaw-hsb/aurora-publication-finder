/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Fusion;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.ConverterUtil;

/**
 * This class fuses different kind of data sets.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class Fusion {


    /**
     * Method to fuse merged data with non matched data
     * @param mergedData the merged data map
     * @param listOfMaps the list of non matched data maps
     * @return Map<String, Map<String, Object>>
     */
    public static Map<String, Map<String, Object>> fuseMergedAndNonMatched( Map<String, Map<String, Object>> mergedData, List<Map<String, Map<String, Object>>> listOfMaps){

        // add merged list and non-matched items together
        Map<String, Map<String, Object>> allData = mergedData;
        for (Map<String, Map<String, Object>> mapOfProvider : listOfMaps) {
            for (Entry<String, Map<String, Object>> entry : mapOfProvider.entrySet()) {

                if (entry.getValue().get("dc.identifier.doi") != null) {
                    if (allData.keySet().contains(((String[]) entry.getValue().get("dc.identifier.doi"))[0])) {
                        // do not add if doi contained in mergedData
                        continue;
                    }
                }

                allData.put((String) entry.getValue().get("id"), entry.getValue());

            }

        }
        System.out.println("Alldata size: " + allData.size());
        if (PropertyConfiguration.isTestingEnabled()) {
            ConverterUtil.organisationModelToCSV(allData, "/outputs/data_organisation_final_data.csv");
        }

        return allData;
       
    }
    
}
