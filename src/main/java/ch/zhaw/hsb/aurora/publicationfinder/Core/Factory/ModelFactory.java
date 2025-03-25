/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Factory;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.InternModel;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.PersonModel;

/**
 * This class acts as a factory for models, responsible for creating them.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class ModelFactory {

    

    /**
     * Method to create a PersonModel
     * @return PersonModel
     */
    public static PersonModel createPersonModel(){

        return new PersonModel();

    }

     /**
     * Method to create an InternModel
     * @return Internmodel
     */
    public static InternModel createInternModel(){

        return new InternModel();

    }

}
