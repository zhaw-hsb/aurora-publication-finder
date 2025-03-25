/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Organisation.Item;

/**
 * This class provides information about a workflow item.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class WorkflowItem {

    int id;
    String uuid;

    /**
     * Constructor
     * @param id   the id of the workflow item
     * @param uuid the uuid of the workflow item
     */
    public WorkflowItem(int id, String uuid) {
        this.id = id;
        this.uuid = uuid;
    }

    /**
     * Method to get the id of the workflow item
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Method to get the uuid of the workflow item
     * @return String
     */
    public String getUuid() {
        return uuid;
    }

}
