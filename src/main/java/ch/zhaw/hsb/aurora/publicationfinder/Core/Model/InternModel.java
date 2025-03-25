/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Model;

import java.util.List;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.StringUtil;

/**
 * This class serves as the model for the data in its internal format.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class InternModel implements ModelInterface{

    private String provider;
    private String id;
    private List<PersonModel> authors;
    private String[] editors;
    private String[] dateIssued;
    private String[] DOI;
    private String[] ISBN;
    private String[] ISSN;
    private String[] language;
    private String[] publisher;
    private String[] publisherType;
    private String[] relationIspartof;
    private String[] licenses;
    private String[] title;
    private String[] type;
    private String[] publicationStatus;
    private String[] volume;
    private String[] issue;
    private String[] pagesStart;
    private String[] pagesEnd;
    private String[] publisherPlace;
    private String[] PMID;
    private String[] event;
    private String[] abstractText;
    private String[] OA;


    /**
     * Method to get the name of the provider
     * @return String
     */
    public String getProvider() {
        return this.provider;
    }

    /**
     * Method to set the name of the provider
     * @param provider name of the provider
     */
    public void setProvider(String provider) {
        this.provider = provider.trim();
    }

    /**
     * Method to get the id of the intern model
     * @return String
     */
    public String getId() {
        return this.id;
    }

    /**
     * Method to set the id of a publication
     * @param id id of the model
     */
    public void setId(String id) {
        this.id = id.trim();
    }

    /**
     * Method to get the authors of the intern model
     * @return List<PersonModel>
     */
    public List<PersonModel> getAuthors() {
        return this.authors;
    }

    /**
     * Method to set the authors of the intern model
     * @param authors a list of Personmodel being the authors of a publication
     */
    public void setAuthors(List<PersonModel>  authors) {
        this.authors = authors;
    }

    /**
     * Method to get the editors of the intern model
     * @return String[]
     */
    public String[] getEditors() {
        return this.editors;
    }

    /**
     * Method to set the editors of the intern model
     * @param editors the editors of a publication
     */
    public void setEditors(String[] editors) {
        this.editors= StringUtil.trimStringsInStringArray(editors);
    }

    /**
     * Method to get the date issued of the intern model
     * @return String[]
     */
    public String[] getDateIssued() {
        return this.dateIssued;
    }

    /**
     * Method to set the date issued of the intern model
     * @param dateIssued date issued of a publication
     */
    public void setDateIssued(String[] dateIssued) {
        this.dateIssued = StringUtil.trimStringsInStringArray(dateIssued);
    }

    /**
     * Method to get the DOI of the intern model
     * @return String[]
     */
    public String[] getDOI() {
        return this.DOI;
    }

    /**
     * Method to set the DOI of the intern model
     * @param DOI DOI of a publication
     */
    public void setDOI(String[] DOI) {
        this.DOI = StringUtil.trimStringsInStringArray(DOI);
    }

    /**
     * Method to get the ISBN of the intern model
     * @return String[]
     */
    public String[] getISBN() {
        return this.ISBN;
    }

    /**
     * Method to set the ISBN of the intern model
     * @param ISBN ISBN of a publication
     */
    public void setISBN(String[] ISBN) {
        this.ISBN = StringUtil.trimStringsInStringArray(ISBN);
    }

    /**
     * Method to get the ISSN of the intern model
     * @return String[]
     */
    public String[] getISSN() {
        return this.ISSN;
    }

    /**
     * Method to set the ISSN of the intern model
     * @param ISSN ISSN of a publication
     */
    public void setISSN(String[] ISSN) {
        this.ISSN = StringUtil.trimStringsInStringArray(ISSN);
    }

    /**
     * Method to get the language of the intern model
     * @return String[]
     */
    public String[] getLanguage() {
        return this.language;
    }

    /**
     * Method to set the language of the intern model
     * @param language language of a publication
     */
    public void setLanguage(String[] language) {
        this.language = StringUtil.trimStringsInStringArray(language);
    }

    /**
     * Method to get the publisher of the intern model
     * @return String[]
     */
    public String[] getPublisher() {
        return this.publisher;
    }


    /**
     * Method to set the publisher of the intern model
     * @param publisher publisher of a publication
     */
    public void setPublisher(String[] publisher) {
        this.publisher = StringUtil.trimStringsInStringArray(publisher);
    }

    /**
     * Method to get the publisher type of the intern model
     * @return String[]
     */
    public String[] getPublisherType() {
        return this.publisherType;
    }

    /**
     * Method to set the publisher type of the intern model
     * @param publisherType publisher type of a publication
     */
    public void setPublisherType(String[] publisherType) {
        this.publisherType = StringUtil.trimStringsInStringArray(publisherType);
    }

    /**
     * Method to get the relation ispartof of the intern model
     * @return String[]
     */
    public String[] getRelationIspartof() {
        return this.relationIspartof;
    }

    /**
     * Method to set the relation ispartof of the intern model
     * @param relationIspartof relation ispartof of a publication
     */
    public void setRelationIspartof(String[] relationIspartof) {
        this.relationIspartof = StringUtil.trimStringsInStringArray(relationIspartof);
    }

    /**
     * Method to get the licenses of the intern model
     * @return String[]
     */
    public String[] getLicenses() {
        return this.licenses;
    }

    /**
     * Method to set the licenses of the intern model
     * @param licenses licenses of a publication
     */
    public void setLicenses(String[] licenses) {
        this.licenses = StringUtil.trimStringsInStringArray(licenses);
    }

    /**
     * Method to get the title of the intern model
     * @return String[]
     */
    public String[] getTitle() {
        return this.title;
    }


    /**
     * Method to set the title of the intern model
     * @param title title of a publication
     */
    public void setTitle(String[] title) {
        this.title = StringUtil.trimStringsInStringArray(title);
    }

    /**
     * Method to get the type of the intern model
     * @return String[]
     */
    public String[] getType() {
        return this.type;
    }

    /**
     * Method to set the type of the intern model
     * @param type type of a publication
     */
    public void setType(String[] type) {
        this.type = StringUtil.trimStringsInStringArray(type);
    }

    /**
     * Method to get the publication status of the intern model
     * @return String[]
     */
    public String[] getPublicationStatus() {
        return this.publicationStatus;
    }

    /**
     * Method to set the publication status of the intern model
     * @param publicationStatus publication status of a publication
     */
    public void setPublicationStatus(String[] publicationStatus) {
        this.publicationStatus = StringUtil.trimStringsInStringArray(publicationStatus);
    }


    /**
     * Method to get the volume of the intern model
     * @return String[]
     */
    public String[] getVolume() {
        return this.volume;
    }

    /**
     * Method to set the volume of the intern model
     * @param volume volume of a publication
     */
    public void setVolume(String[] volume) {
        this.volume = StringUtil.trimStringsInStringArray(volume);
    }

    /**
     * Method to get the issue of the intern model
     * @return String[]
     */
    public String[] getIssue() {
        return this.issue;
    }


    /**
     * Method to set the issue of the intern model
     * @param issue issue of a publication
     */
    public void setIssue(String[] issue) {
        this.issue = StringUtil.trimStringsInStringArray(issue);
    }

    /**
     * Method to get the pages start of the intern model
     * @return String[]
     */
    public String[] getPagesStart() {
        return this.pagesStart;
    }

    /**
     * Method to set the pages start of the intern model
     * @param pagesStart pages start of a publication
     */
    public void setPagesStart(String[] pagesStart) {
        this.pagesStart = StringUtil.trimStringsInStringArray(pagesStart);
    }

    /**
     * Method to get the pages end of the intern model
     * @return String[]
     */
    public String[] getPagesEnd() {
        return this.pagesEnd;
    }

    /**
     * Method to set the pages end of the intern model
     * @param pagesEnd pages end of a publication
     */
    public void setPagesEnd(String[] pagesEnd) {
        this.pagesEnd = StringUtil.trimStringsInStringArray(pagesEnd);
    }

    /**
     * Method to get the publisher place of the intern model
     * @return String[]
     */
    public String[] getPublisherPlace() {
        return this.publisherPlace;
    }

    /**
     * Method to set the publisher place of the intern model
     * @param publisherPlace publisher place of a publication
     */
    public void setPublisherPlace(String[] publisherPlace) {
        this.publisherPlace = StringUtil.trimStringsInStringArray(publisherPlace);
    }

    /**
     * Method to get the PMID of the intern model
     * @return String[]
     */
    public String[] getPMID() {
        return this.PMID;
    }
    
    /**
     * Method to set the PMID of the intern model
     * @param PMID PMID of a publication
     */
    public void setPMID(String[] PMID) {
        this.PMID = StringUtil.trimStringsInStringArray(PMID);
    }

    /**
     * Method to get the event of the intern model
     * @return String[]
     */
    public String[] getEvent() {
        return this.event;
    }

    /**
     * Method to set the event of the intern model
     * @param event event of a publication
     */
    public void setEvent(String[] event) {
        this.event = StringUtil.trimStringsInStringArray(event);
    }

    /**
     * Method to get the abstract text of the intern model
     * @return String[]
     */
    public String[] getAbstractText() {
        return this.abstractText;
    }

    /**
     * Method to set the abstract text of the intern model
     * @param abstractText abstract text of a publication
     */
    public void setAbstractText(String[] abstractText) {
        this.abstractText = StringUtil.trimStringsInStringArray(abstractText);
    }

    /**
     * Method to get the OA of the intern model
     * @return String[]
     */
    public String[] getOA() {
        return this.OA;
    }

    /**
     * Method to set the OA of the intern model
     * @param OA OA of a publication
     */
    public void setOA(String[] OA) {
        this.OA = StringUtil.trimStringsInStringArray(OA);
    }

}
