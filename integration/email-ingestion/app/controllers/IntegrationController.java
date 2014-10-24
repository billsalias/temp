package controllers;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import play.libs.Json;
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
	public Result addIntegration(Long orgId) {
		return ok("OK");
	}
}
