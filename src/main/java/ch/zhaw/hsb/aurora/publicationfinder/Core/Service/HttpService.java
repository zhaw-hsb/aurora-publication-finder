/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.StringUtil;

/**
 * This class provides the service for handling HTTP requests.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class HttpService {

    /**
     * Method to get the string builder
     * @param url url to do http request
     * @return StringBuilder
     */
    public static StringBuilder getStringBuilder(URL url) {

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            return StringUtil.getStringBuilder(conn.getInputStream(), url);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        return null;

    }


}
