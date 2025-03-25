/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Factory;

import ch.zhaw.hsb.aurora.publicationfinder.Modules.DSpace.Service.AuthenticationService;

/**
 * This class acts as a factory for services, responsible for creating them.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class ServiceFactory {

    /**
     * Method to create the authentication service
     * @return AuthenticationService
     */
    public static AuthenticationService createAuthenticationService(){
        return new AuthenticationService();
    }
    
}
