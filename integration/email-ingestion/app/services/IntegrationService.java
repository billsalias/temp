package services;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import models.Integration;
import dao.IntegrationDAO;

/**
 * This class provides methods for working with Integrations and
 * IntegrationEntryPoints. The methods provided here will enforce constraints on
 * any requested changes to ensure integrity of the data is maintained.
 * 
 * @author bill.clogston
 *
 */
@Service("integrationService")
public class IntegrationService {
	@Resource(name = "integrationDAO")
	private IntegrationDAO integrationDAO;
	
	/**
	 * Add a new integration to mongo. Before adding the integration we will
	 * verify the template id and variable ids and will associate the
	 * integration with an IntegrationEntryPoint (creating one if necessary).
	 * 
	 * @param newIntegration
	 * @return The passed Integration with entryPointId and _id updated.
	 */
	public Integration addIntegration(Integration newIntegration) {
		// verify the template id and variable ids asynchronously

		// See if there is an existing integration entry point that matches the
		// new integration destination email address

		// If there is an existing entry point use it, if not create one

		// Update the entry point id in the new integration

		// Wait for asynchronous validations to complete

		// Save the update and validated integration to mongo
		return integrationDAO.insert(newIntegration);
	}

	/**
	 * 
	 * @param orgId
	 *            The organization to limit the search to.
	 * @param offset
	 *            The zero based index of the first object to return.
	 * @param batchSize
	 *            The maximum number of objects to return, fewer will be
	 *            returned if there are not enough documents.
	 * @return A list of integration objects.
	 */
	public List<Integration> listIntegrationsByOrganization(long orgId,
			int offset, int batchSize) {
		return integrationDAO.listIntegrations(orgId, offset, batchSize);
	}
}
