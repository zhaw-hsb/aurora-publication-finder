/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder;


import ch.zhaw.hsb.aurora.publicationfinder.Core.LogCollector.AdminLogCollector;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.FormatCheckUtil;

/**
 * This class starts the publication finder application
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class Main {
        /**
     * Method to create app and run it
     * @param args arguments
     */
    public static void main(String[] args) {

        Application app = Application.createApplication();


        for (int i = 0; i < args.length; i++) {

            switch (args[i]) {
                case "-date":
                    String date = args[++i];
                    if(FormatCheckUtil.isValidDate(date)){
                        app.setDate(date);
                    }else{
                        AdminLogCollector.logErrorAndExit("Date format is wrong, must be yyyy-MM-dd",null);
                    }
                    
                    break;

                default:
                    break;
            }

        }

        app.run();
    }

}