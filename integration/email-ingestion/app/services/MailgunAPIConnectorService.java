package services;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

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

    /**
     * The email domain we are using on the specified mailgun account.
     */
    protected String domain;

    /**
     * This method will add a route for the specified email address to post to
     * this server. If successful the id of the new route will be returned. If a
     * route already exists for this email address or the address is not valud
     * and exception will be thrown.
     * 
     * @param email
     *            The email address to route to this server.
     * @return A promise for the ID of the new route
     */
    Promise<String> addRouteForEmail(String email) {
        // List existing routes to make sure this is not a duplicate
        WSRequestHolder listRoutes = WS.url(getApiBaseURL() + "/v2/routes");
        listRoutes.setAuth("api", getApiKey(), WSAuthScheme.BASIC);
        Promise<WSResponse> routesResponse = listRoutes.get();

        // Map the existing routes response to a call to add a new route if
        // appropriate
        routesResponse.map(new Function<WSResponse, WSResponse>() {
            public WSResponse apply(WSResponse routes) throws Throwable {
                // Confirm the request completed successfully
                if (routes.getStatus() != 200) {
                    Logger.of(EBRestAPIConnectorService.class).error(
                            "validateTemplate bad response code:"
                                    + routes.toString());

                    // Bad response
                    // TODO: Create appropriate exception
                    throw new Exception("Bad response from mailgun server");
                }

                // Parse the data into json
                JsonNode json = routes.asJson();

                // Find the variables in this template
                JsonNode items = json.get("items");

                return null;
            }

        });

        // Map the add route response to return the new route id

        // OR just fake it for now
        return Promise.<String> pure("testid");
    }

    // PROPERTIES
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getApiBaseURL() {
        return apiBaseURL;
    }

    public void setApiBaseURL(String apiBaseURL) {
        this.apiBaseURL = apiBaseURL;
    }
}
