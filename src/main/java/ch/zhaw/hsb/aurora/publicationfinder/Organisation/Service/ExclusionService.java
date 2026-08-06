package ch.zhaw.hsb.aurora.publicationfinder.Organisation.Service;

/**
 * This service checks criterias to exclude publications
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class ExclusionService {



    /**
     * Method to check if updateTo exists. If it does the publication is excluded.
     * @param value updateTo value
     * @return boolean
     */
    public boolean updateToExclusionCriteria(boolean value){

        //if updateTo is true, we should exclude the publication
        return value;

    }


    
    /**
     * Method to check if an empty type exists. If it does, the publications is excluded.
     * @param values type values
     * @return boolean
     */
    public boolean typeExclusionCriteria(String[] values){

        if(values == null){
                return true;
        }

        for(String value : values){
            if("".equals(value)){
                return true;
            }
        }

        
        return false;

    }


    /**
     * Method to check if a openalex publication status is different from publishedVersion. If it does, the publication is excluded.
     * @param provider name of the provider
     * @param values publication status values
     * @return boolean
     */
    public boolean publicationStatusExclusionCriteria(String provider, String[] values){

        if ("openalex".equals(provider)){

            if(values == null){
                return true;
            }

            //exclude all openalex where publication status isn't published version
            for(String value: values){
                if(!"publishedVersion".equals(value)){
                    return true;
                }
            }
         


        }

        return false;
        
    }
    
}
