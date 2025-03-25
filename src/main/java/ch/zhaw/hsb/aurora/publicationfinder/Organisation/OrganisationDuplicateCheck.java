/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Organisation;

import java.util.Map;

import ch.zhaw.hsb.aurora.publicationfinder.Modules.DSpace.DuplicateCheck.DuplicateCheck;

/**
 * This class is the extended duplicate check with organisation specific requirements.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class OrganisationDuplicateCheck extends DuplicateCheck {

    /**
     * Constructor
     * @param allData the whole organisation specific data as map of maps 
     */
    public OrganisationDuplicateCheck(Map<String, Map<String, Object>> allData) {
        super(allData);
    }

    @Override
    public String getSection(String type) {
        return "zhawdescriptionclassic";
    }
    
}
