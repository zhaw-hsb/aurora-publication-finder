/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Modules.DSpace.Service;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyCredentialsConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyProviderConfiguration;

/**
 * This class provides the service for authenticating with the repository.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class AuthenticationService {

    String repositoryAPIUrl;
    HttpClient client;
    String bearer;

    /**
     * Constructor
     */
    public AuthenticationService() {

        this.repositoryAPIUrl = PropertyProviderConfiguration.getRepositoryAPIUrl();


    }

    /**
     * Method to login a user
     */
    public void login() {

        this.client = HTTPService.getInstance();
        this.bearer = generateBearer();

    }

    /**
     * Method to get generate the bearer of the login connection
     * @return String
     */
    private String generateBearer() {


        String username = PropertyCredentialsConfiguration.getUsername();
        String password = URLEncoder.encode(PropertyCredentialsConfiguration.getPassword());

        HttpResponse<String> response = HTTPService.sendRequest("", "text/plain",
                this.repositoryAPIUrl + "/authn/login?user=" + username + "&password=" +
                        password,
                "POST", null);

        if (response != null) {

            return response.headers().firstValue("Authorization").get();

        }

        return null;
    }

    /**
     * Method to get the bearer
     * @return String
     */
    public String getBearer() {
        return this.bearer;
    }

    /**
     * Method to get the client of a http connection
     * @return HttpClient
     */
    public HttpClient getClient() {
        return this.client;
    }

    /**
     * Method to get the API url of a repository
     * @return String
     */
    public String getRepositoryAPIUrl() {
        return this.repositoryAPIUrl;
    }

}

