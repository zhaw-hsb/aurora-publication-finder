/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder;

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
        app.run();
    }

}