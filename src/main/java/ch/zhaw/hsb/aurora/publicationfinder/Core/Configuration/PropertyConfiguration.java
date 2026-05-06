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
import ch.zhaw.hsb.aurora.publicationfinder.Core.LogCollector.AdminLogCollector;

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

            String path = "assets/config/application.properties";

            try (InputStream input = Main.class.getClassLoader().getResourceAsStream(path)) {

                if (input == null){
                    AdminLogCollector.logErrorAndExit(path+" doesn't exist.", null);
                }

                Properties property = new Properties();
                property.load(new InputStreamReader(input, StandardCharsets.UTF_8));
                prop = property;

            } catch (IOException e) {
                AdminLogCollector.logErrorAndExit(path+" could not be read.", e);
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
            AdminLogCollector.logWarning("Error retrieving csv.eol", e);
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
            AdminLogCollector.logWarning("Error retrieving csv.eol", e);
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
            AdminLogCollector.logWarning("Error retrieving csv.valueseparator", e);
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
            AdminLogCollector.logWarning("Error retrieving testing.enabled", e);
        }

        // default
        return false;

    }



}
