/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import ch.zhaw.hsb.aurora.publicationfinder.Main;

/**
 * This class retrieves configuration properties from the organisation.properties file. 
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class PropertyProviderConfiguration {

    private static Properties prop;

    /**
     * A private constructor, to avoid creating multiple instances
     */
    private PropertyProviderConfiguration() {

    }

    /**
     * Method to get Properties instance
     * @return Properties
     * @throws Exception
     */
    public static Properties getInstance() throws Exception {

        if (prop == null) {

            try (InputStream input = Main.class.getClassLoader().getResourceAsStream(
                    "assets/config/organisation.properties"); InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                Properties property = new Properties();
                property.load(reader);
                prop = property;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new Exception("assets/config/organisation.properties could not be read.");
            }

        }

        return prop;

    }

    /**
     * Method to get ror ids of the organisation
     * @return String[]
     */
    public static String[] getRors() {

        Object value;
        try {
            value = getInstance().get("organisation.rors");
            if (value != null) {
                String rorsString = value.toString();
                String[] rors = rorsString.split("\\|", 0);
                return rors;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Method to get affiliations of the organisation for a specific provider
     * @param provider name of the provider
     * @return String[]
     */
    public static String[] getAffiliations(String provider) {

        Object value;
        try {
            value = getInstance().get(provider + ".affiliations");
            if (value != null) {
                String affiliationsString = value.toString();
                String[] affiliations = affiliationsString.split("\\|", 0);
                return affiliations;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

     /**
     * Method to get affiliation exceptions of the organisation
     * @return String[]
     */
    public static String[] getAffiliationExceptions() {

       
        Object value;
        try {
            value = getInstance().get("organisation.affiliations.exceptions");
            if (value != null && !value.equals("")) {
                String affiliationsExceptionString = value.toString();
                String[] affiliationExceptions = affiliationsExceptionString.split("\\|", 0);
                return affiliationExceptions;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Method to get speicifc field of the provider
     * @param provider name of the provider
     * @param fieldName name of the field
     * @return String
     */
    public static String getFieldByName(String provider, String fieldName) {

        Object value;
        try {
            value = getInstance().get(provider + "." + fieldName);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       

        return null;

    }

    /**
     * Method to get the API url of the organisation repository
     * @return String
     */
    public static String getRepositoryAPIUrl() {

        Object value;
        try {
            value = getInstance().get("organisation.repositoryAPIUrl");
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Method to get special characters of the organisation repository
     * @return String[]
     */
    public static String[] getSpecialCharacters() {

        Object value;
        try {
            value = getInstance().get("organisation.specialCharacters");
            if (value != null) {
                return value.toString().split("\\|", 0);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Method to get the collection id where publications will be saved
     * @return String
     */
    public static String getCollectionId() {

        Object value;
        try {
            value = getInstance().get("organisation.collectionId");
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Method to get the "match" string for the Matcher class
     * @return String
     */
    public static String getMatchString() {

        Object value;
        try {
            value = getInstance().get("match.string");
            if (value != null) {
                return value.toString();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "dc.identifier.doi";
    }

    
    public static String getExternalFilePath() {

        Object value;
        try {
            value = getInstance().get("externalfile.path");
            if (value != null) {

                return value.toString();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // default
        return null;

    }

    public static String getCSRFTokenEndpoint() {

        Object value;
        try {
            value = getInstance().get("dspace.version");
            
            if (value != null) {

                String version = ((String)value).split("\\.")[0];

                if(version.equals("7")){

                    return "/authn/status";

                }else{
                    
                    return "/security/csrf";

                }

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // default
        return null;

    }

}
