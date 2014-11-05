package services;

import java.util.Iterator;

import play.Logger;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

import com.fasterxml.jackson.databind.JsonNode;

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
     * route already exists for this email address or the address is not valid
     * and exception will be thrown.
     * 
     * @param email
     *            The email address to route to this server.
     * @return A promise for the ID of the new route
     */
    Promise<String> addRouteForEmail(String email) {
        // The proposed route expression and action
        final String routeFilter = "match_recipient(\"" + email + "\")";
        final String routeAction = "store(notify=\"http://requestb.in/14inus71\")";

        // List existing routes to make sure this is not a duplicate
        WSRequestHolder listRoutes = WS.url(getApiBaseURL() + "routes");
        listRoutes.setAuth("api", getApiKey(), WSAuthScheme.BASIC);
        Promise<WSResponse> routesResponse = listRoutes.get();

        // Map the existing routes response to a boolean indicating if it exists
        // already
        Promise<Boolean> routeExists = routesResponse
                .map(new Function<WSResponse, Boolean>() {
                    public Boolean apply(WSResponse routes) throws Throwable {
                        // Confirm the request completed successfully
                        if (routes.getStatus() != 200) {
                            Logger.of(EBRestAPIConnectorService.class).error(
                                    "validateTemplate bad response code:"
                                            + routes.toString());

                            // Bad response
                            // TODO: Create appropriate exception
                            throw new Exception(
                                    "Bad response from mailgun server");
                        }

                        // Parse the data into json
                        JsonNode json = routes.asJson();

                        // Find the existing routes
                        JsonNode items = json.get("items");

                        // If there are routes scan them
                        if (items != null && !items.isNull()
                                && items.size() > 0) {

                            // Scan the routes and make sure none use this
                            // address
                            Iterator<JsonNode> ite = items.elements();
                            while (ite.hasNext()) {
                                // Found one with the same expression, report it
                                String expression = ite.next().asText();
                                if (routeFilter.equals(expression)) {
                                    Logger.of(EBRestAPIConnectorService.class)
                                            .error("route exists in mailgun but not mongo:"
                                                    + routeFilter);

                                    // TODO: Create appropriate exception
                                    throw new Exception(
                                            "route exists in mailgun but not mongo:"
                                                    + routeFilter);
                                }
                            }
                        }

                        return false;
                    }

                });

        // Map the add route response to return the new route id
        return routeExists.map(new Function<Boolean, String>() {
            public String apply(Boolean found) throws Throwable {
                WSRequestHolder addRoute = WS.url(getApiBaseURL() + "routes");
                addRoute.setAuth("api", getApiKey(), WSAuthScheme.BASIC);
                Promise<WSResponse> routesResponse = addRoute.setContentType(
                        "application/x-www-form-urlencoded").post(
                        "key1=value1&key2=value2");
                return null;
            }

        });
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
