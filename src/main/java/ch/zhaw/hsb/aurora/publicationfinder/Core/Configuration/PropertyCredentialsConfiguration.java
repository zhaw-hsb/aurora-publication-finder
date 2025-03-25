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

import java.util.Properties;

import ch.zhaw.hsb.aurora.publicationfinder.Main;

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

            try (InputStream input = Main.class.getClassLoader().getResourceAsStream(
                    "assets/config/credentials.properties")) {
                Properties property = new Properties();
                property.load(input);
                prop = property;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new Exception("assets/config/credentials.properties could not be read.");
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
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

}
