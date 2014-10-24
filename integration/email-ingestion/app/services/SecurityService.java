package services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * This service provides methods to generate, validate and inspect security
 * tokens. The purpose of the tokens is to ensure the holder has permission to
 * access the requested API. Tokens are base64 encoded strings that include a
 * client id (optional), expiration date (optional) and a list of resources
 * access is granted to (required) and a salted digest of the data.
 * 
 * @author bill.clogston
 *
 */
@Service("securityService")
public class SecurityService {
	/**
	 * This key is used as the name for the token when it is passed as a key
	 * value pair, e.g. as an HTTP header or query argument.
	 */
	public static final String TOKEN_KEY = "EBI_AUTH_TOKEN";

	/** The result of a token validation. **/
	public enum ValidationStatus {
		ACCESS_GRANTED, ACCESS_DENIED, BAD_SIGNATURE, TOKEN_EXPIRED;
	}

	/** Resources that tokens can be checked against. **/
	public enum ProtectedResource {
		INTEGRATIONS, POST_EVENT;
	}

	// This is used to salt the hash to make faking a token more difficult.
	// private String salt;

	/**
	 * Validate that the passed token grants access to the passed resource.
	 * Currently the token has no information that can be used to validate the
	 * source, e.g. ip address, so no context is passed in.
	 * 
	 * @param resource Requested resource.
	 * @param token The token for the client requesting it.
	 * @return The ValidationStatus for the request.
	 */
	public ValidationStatus validateToken(ProtectedResource resource, String token) {
		return ValidationStatus.ACCESS_GRANTED;
	}

	/**
	 * This method will generate a new access token to grant access to the
	 * specified list of resources until the specified date. The passed
	 * information is stored in the token and signed.
	 * 
	 * @param resources
	 *            The resources this token is granting access to, must contain
	 *            at least one resource.
	 * @param clientId
	 *            A string id for the client, this can be null or an empty
	 *            string if an anonymous token is desired.
	 * @param endDate
	 *            When this token expires, if null the token never expires.
	 * @return A a new access token. The token is based64 encoded.
	 */
	public String generateToken(List<ProtectedResource> resources,
			String clientId, Date endDate) {
		return null;
	}

	/**
	 * Extract a client id from a token.
	 * 
	 * @param token
	 *            The token to extract the client id from.
	 * @return The client id or null if one is not included in this token.
	 */
	public String getClientId(String token) {
		return "";
	}

	/**
	 * Extract the expiration date from a token.
	 * 
	 * @param token
	 *            The token to extract the expiration date from.
	 * @return The expiration date or null if none is set.
	 */
	public Date getExpirationDate(String token) {
		return null;
	}
}
