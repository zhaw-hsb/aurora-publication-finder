/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This class offers utilities for String.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class StringUtil {

    /**
     * Method to replace last substring of a string
     * @param string the string
     * @param substring the substring of the string
     * @param replacement the replacement string
     * @return String
     */
    public static String replaceLast(String string, String substring, String replacement) {
        int index = string.lastIndexOf(substring);
        if (index == -1)
            return string;
        return string.substring(0, index) + replacement
                + string.substring(index + substring.length());
    }

    /**
     * Method to get a string builder of http request
     * @param connection the input stream of a connection
     * @param url the url for the http request
     * @return StringBuilder
     */
    public static StringBuilder getStringBuilder(InputStream connection, URL url) {
        // read return message and convert to string
        StringBuilder sb = new StringBuilder();

        if (connection != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return sb;
        }

        return null;

    }

    /**
     * Method to change a string to a string array
     * @param elem the string
     * @return String[]
     */
    public static String[] stringToStringArray(String elem) {

        return new String[] { elem };
    }

    /**
     * Method to trim strings inside a string array
     * @param array the array with strings
     * @return String[]
     */
    public static String[] trimStringsInStringArray(String[] array) {

        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                array[i] = array[i].trim();
            }
        }

        return array;
    }

}
