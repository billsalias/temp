package services;

import java.util.Iterator;
import java.util.List;

import play.Logger;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class wraps required methods in the EB REST API server to validate
 * templates and variables as well as creating new incidents.
 * 
 * @author bill.clogston
 *
 */
public class EBRestAPIConnectorService {
    /**
     * The user name to authenticate with the EB REST API server. This is done
     * as a proxy user and the owner of the integration we are acting on behalf
     * of is passed as the user of record.
     */
    protected String proxyUser;

    /** The hashed password for the proxy user. */
    protected String proxyPasswordHash;

    /** The base URL for the EB REST API server. */
    protected String apiBaseURL;

    /**
     * This method with validate that the passed templateId exists on the
     * specified organization. If variableIds are passed these will be verified
     * to exist on the specified template.
     * 
     * @param orgId
     *            The organization to search for the template on.
     * @param templateId
     *            The template to look up and verify.
     * @param variableIds
     *            The optional list of variables to verify exist on the
     *            specified template.
     * @return A promise returning true if all data is valid, false if not.
     */
    public Promise<Boolean> validateTemplate(final long orgId,
            final long templateId, final List<Long> variableIds) {
        // Setup the request to get the specified template
        WSRequestHolder getTemplateReq = WS.url(this.getApiBaseURL()
                + "/incidentTemplates/" + orgId + "/" + templateId);
        getTemplateReq.setAuth(this.getProxyUser(),
                this.getProxyPasswordHash(), WSAuthScheme.BASIC);
        Promise<WSResponse> responsePromise = getTemplateReq.get();

        // Map the result when it comes in to handle the validation and setting
        // the response
        return responsePromise.map(new Function<WSResponse, Boolean>() {
            public Boolean apply(WSResponse response) throws Throwable {
                // Confirm the request completed successfully
                if (response.getStatus() != 200)
                    return false;

                // Parse the data into json
                JsonNode json = response.asJson();

                // Make sure the template was found
                JsonNode msg = json.path("message");
                if (msg == null || !msg.textValue().equals("OK")) {
                    Logger.of(EBRestAPIConnectorService.class).error(
                            "validateTemplate bad response:" + json.toString());
                    return false;
                }

                // If there are variables to check, check them
                if (variableIds != null && variableIds.size() > 0) {
                    // Find the variables in this template
                    JsonNode templateVarsNode = json
                            .path("result.phaseTemplates[0].formTemplate.formVariableItems");

                    // If there are no variables fail
                    if (templateVarsNode == null || templateVarsNode.isNull()
                            || templateVarsNode.size() == 0)
                        return false;

                    // Scan the templates variables and make sure all the passed
                    // ones are present
                    Iterator<JsonNode> ite = templateVarsNode.elements();
                    while (ite.hasNext()) {
                        long varId = ite.next().asLong();
                        if (!variableIds.contains(varId))
                            return false;
                    }
                }

                return true;
            }
        });
    }

    // PROPERTIES
    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPasswordHash() {
        return proxyPasswordHash;
    }

    public void setProxyPasswordHash(String proxyPasswordHash) {
        this.proxyPasswordHash = proxyPasswordHash;
    }

    public String getApiBaseURL() {
        return apiBaseURL;
    }

    public void setApiBaseURL(String apiBaseURL) {
        this.apiBaseURL = apiBaseURL;
    }
}
