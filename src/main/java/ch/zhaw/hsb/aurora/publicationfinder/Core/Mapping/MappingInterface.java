/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping;

import java.util.Map;

/**
 * This interface defines the characteristics of a mapping.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public interface MappingInterface {
    /**
     * Method to get the map record
     * @return Map<String, String[]>
     */
    public Map<String, String[]> getMapRecord();
    /**
     * Method to get the value by field name
     * @param fieldName the name of a specific field
     * @return Object
     */
    public Object getByFieldName(String fieldName);

}
