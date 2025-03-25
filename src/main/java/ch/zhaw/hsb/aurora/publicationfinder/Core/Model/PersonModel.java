/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Model;

/**
 * This class serves as the model for the person data.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class PersonModel implements ModelInterface{

    private String author;
    private String[] affiliations;

    /**
     * Method to get author of the person model
     * @return String
     */
    public String getAuthor(){
        return this.author;
    }

    /**
     * Method to set author of the person model
     * @param author author of a publication
     */
    public void setAuthor(String author){
        this.author = author;
    }

    /**
     * Method to get affiliations of the person model
     * @return String[]
     */
    public String[] getAffiliations(){
        return this.affiliations;
    }

    /**
     * Method to set affiliations of the person model
     * @param affiliations authors affiliations of a publication
     */
    public void setAffiliations(String[] affiliations){
        this.affiliations = affiliations;
    }
    
}
