package controllers;

import java.util.List;

import javax.annotation.Resource;

import models.Integration;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.F.Promise;
import play.libs.Json;
import play.libs.F.Function;
import play.mvc.Controller;
import play.mvc.Result;
import services.IntegrationService;
import services.SecurityService;

/**
 * This controller handles all methods for managing and accessing integrations
 * and integration logs.
 * 
 * @author bill.clogston
 *
 */
@Service("integrationController")
public class IntegrationController extends Controller {
    @Resource(name = "integrationService")
    private IntegrationService integrationService;

    @Resource(name = "securityService")
    SecurityService securityService;

    /**
     * 
     * @param orgId
     * @param offset
     * @param batchSize
     * @return
     */
    public Result listIntegrations(Long orgId, int offset, int batchSize) {
        // Validate the security token, check for a header first then fall back
        // to query argument
        String token[] = request().headers().get(SecurityService.TOKEN_KEY);
        if (token == null) {
            token = request().queryString().get(SecurityService.TOKEN_KEY);
        }
        if (token == null
                || token.length != 1
                || securityService.validateToken(
                        SecurityService.ProtectedResource.INTEGRATIONS,
                        token[0]) != SecurityService.ValidationStatus.ACCESS_GRANTED)
            return forbidden();

        // Get the requested integrations and return them as JSON
        return ok(Json.toJson(integrationService
                .listIntegrationsByOrganization(orgId, offset, batchSize)));
    }

    /**
     * 
     * @param orgId
     * @return
     */
    public Promise<Result> addIntegration(Long orgId) {
        // Get the new integration data
        JsonNode json = request().body().asJson();
        
        // If the json is missing fail hard
        if (json == null) {
            return Promise.<Result>pure(badRequest(Json.toJson(new ErrorResponse(
                    ErrorResponse.Status.INVALID_ARGUMENT,
                    "Post body expected to contain an Integration object!"))));
        }

        // Convert the json to a pojo
        Integration newInt = Json.fromJson(json, Integration.class);

        // Add it to the system, this is an async call that will hit the API
        // server, the mailgun server and the DB
        return integrationService.addIntegration(newInt).map(
                new Function<Integration, Result>() {
                    public Result apply(Integration newInt) {
                        return ok(Json.toJson(newInt));
                    }
                });

    }
}
