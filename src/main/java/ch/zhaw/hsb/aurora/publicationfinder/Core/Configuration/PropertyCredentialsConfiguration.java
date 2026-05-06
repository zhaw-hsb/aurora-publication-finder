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
 * This class retrieves configuration properties from the credentials.properties file. 
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class PropertyCredentialsConfiguration {

    private static Properties prop;

    /**
     * A private constructor, to avoid creating multiple instances
     */
    private PropertyCredentialsConfiguration() {

    }

    /**
     * Method to get Properties instance
     * @return Properties
     * @throws Exception
     */
    public static Properties getInstance() throws Exception {

        if (prop == null) {

            String path = "assets/config/credentials.properties";

            try (InputStream input = Main.class.getClassLoader().getResourceAsStream(
                    path)) {

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
     * Method to get username
     * @return String
     */
    public static String getUsername() {

        try {
            return getInstance().getProperty("username");
        } catch (Exception e) {
            AdminLogCollector.logErrorAndExit("Error retrieving username.", e);
        }
        return null;

    }

    /**
     * Method to get password
     * @return String
     */
    public static String getPassword() {

        try {
            return getInstance().getProperty("password");
        } catch (Exception e) {
            AdminLogCollector.logErrorAndExit("Error retrieving password.", e);
        }

        return null;

    }

    /**
     * Method to get mail address
     * @return String
     */
    public static String getMail() {

        try {
            return getInstance().getProperty("mail");
        } catch (Exception e) {
            AdminLogCollector.logErrorAndExit("Error retrieving mail.", e);
        }
        return null;

    }

    /**
     * Method to get mail password
     * @return String
     */
    public static String getMailPassword() {

        try {
            return getInstance().getProperty("mail.password");
        } catch (Exception e) {
            AdminLogCollector.logErrorAndExit("Error retrieving mail password.", e);
        }

        return null;

    }

}
