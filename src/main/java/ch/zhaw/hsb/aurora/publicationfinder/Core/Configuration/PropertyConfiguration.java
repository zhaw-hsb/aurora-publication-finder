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
 * This class retrieves configuration properties from the application.properties file. 
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class PropertyConfiguration {

    private static Properties prop;

    /**
     * A private constructor, to avoid creating multiple instances
     */
    private PropertyConfiguration() {

    }

    /**
     * Method to get Properties instance
     * @return Properties
     * @throws Exception
     */
    public static Properties getInstance() throws Exception {

        if (prop == null) {

            try (InputStream input = Main.class.getClassLoader().getResourceAsStream(
                    "assets/config/application.properties"); InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                        
                Properties property = new Properties();
                property.load(reader);
                prop = property;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new Exception("config/application.properties could not be read.");
            }

        }

        return prop;

    }

    /**
     * Method to get CSV end of line String
     * @return String
     */
    public static String getCsvEol() {

        Object value;
        try {
            value = getInstance().get("csv.eol");
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // default
        return System.getProperty("line.separator");
    }

    /**
     * Method to get CSV field seperator String
     * @return String
     */
    public static String getCsvFieldSeparator() {

        Object value;
        try {
            value = getInstance().get("csv.fieldseparator");
            if (value != null) {

                return value.toString();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // default
        return ",";

    }

    /**
     * Method to get CSV value separator String
     * @return String
     */
    public static String getCsvValueSeparator() {

        Object value;
        try {
            value = getInstance().get("csv.valueseparator");
            if (value != null) {

                return value.toString();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // default
        return "||";

    }

    /**
     * Method to check if testing is enabled
     * @return boolean
     */
    public static boolean isTestingEnabled(){

        Object value;
        try {
            value = getInstance().get("testing.enabled");
            if (value != null) {

                return Boolean.parseBoolean(value.toString());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // default
        return false;

    }



}
