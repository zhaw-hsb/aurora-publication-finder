/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class offers utilities for files.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class FileUtil {


    /**
     * Method to read a file and get the content
     * @param path path to the file to be read
     * @return String
     */
    public static String readFile(String path){
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to write to a file
     * @param path path to the file
     * @param text text to be written
     * @return boolean
     */
    public static boolean writeToFile(Path path, String text){
        try {
            Files.write(path, text.getBytes());
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    
}
