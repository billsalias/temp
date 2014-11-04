package services;

import play.libs.F.Promise;


/**
 * This service provides methods to interact with the mailgun service to manage
 * incoming email routes and to access stored email.
 * 
 * @author bill.clogston
 *
 */
public class MailgunAPIConnectorService {
    /**
     * The API key from mailgun for the account to use with this server. This is
     * used as the password when authenticating with the mailgun api server.
     */
    protected String apiKey;

    /**
     * The base URL for interaction with the mailgun API server. This URL is
     * specific to the mailgun account and email domain to be used.
     */
    protected String apiBaseURL;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiBaseURL() {
        return apiBaseURL;
    }

    public void setApiBaseURL(String apiBaseURL) {
        this.apiBaseURL = apiBaseURL;
    }
    
    Promise<String> addRouteForEmail(String email) {
        return Promise.<String>pure("testid");
    }
}
