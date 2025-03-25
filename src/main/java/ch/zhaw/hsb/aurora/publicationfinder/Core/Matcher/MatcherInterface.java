/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Matcher;

import java.util.List;
import java.util.Map;

/**
 * This interface defines the characteristics of a matcher.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
interface MatcherInterface {

     /**
      * Method to get the whole data from the matcher
      * @return List<Map<String, Map<String, Object>>>
      */
     public List<Map<String, Map<String, Object>>> getAllMatchedData();
    
}
