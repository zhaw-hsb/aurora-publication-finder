/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Merger;

import java.util.Map;

/**
 * This interface defines the characteristics of a merger.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
interface MergerInterface {

    /**
     * Method to get all the merged data after the elements have been merged
     * @return Map<String, Map<String, Object>>
     */
    public Map<String, Map<String, Object>> getAllMergedData();
    
}
