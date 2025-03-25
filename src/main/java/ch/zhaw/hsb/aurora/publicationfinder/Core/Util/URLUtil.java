/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Util;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class offers utilities for URLs.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class URLUtil {

    /**
     * Method to get the next cursor of a url
     * 
     * @param jObject       json object of the last request
     * @param metadataField metadata field where the metadata of the request object
     *                      is stored
     * @param nextCursor    the next cursor field of the website
     * @return String
     */
    public static String getNextCursor(JsonNode jObject, String metadataField, String nextCursor) {

        // JSON Object with meta data
        JsonNode metadata = jObject.get(metadataField);
        // cursor to get to next page
        return metadata.get(nextCursor).asText();

    }

}
