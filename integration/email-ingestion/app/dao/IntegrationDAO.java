package dao;

import java.util.ArrayList;
import java.util.List;

import org.jongo.MongoCursor;
import org.springframework.stereotype.Service;

import models.Integration;

/**
 * This class provides data access for the Integration object using MongoClient
 * and Jongo.
 * 
 * @author bill.clogston
 *
 */
@Service("integrationDAO")
public class IntegrationDAO extends BaseDAO {
	/**
	 * Insert the passed Integration into the integration collection. If no _id
	 * is set one will be allocated, if one is set it will be respected.
	 * 
	 * @param integration
	 * @return The passed integration with a new _id if one was not provided on
	 *         input.
	 */
	public Integration insert(Integration integration) {
		getCollection("integration").insert(integration);
		return integration;
	}

	/**
	 * This method will retrieve a list of integrations ordered by _id starting
	 * with the zero based offset and returning batchSize objects.
	 * 
	 * @param offset
	 *            The zero based index of the first object to return.
	 * @param batchSize
	 *            The maximum number of objects to return, fewer will be
	 *            returned if there are not enough documents.
	 * @return A list of integration objects.
	 */
	public List<Integration> listIntegrations(long orgId, int offset, int batchSize) {
		MongoCursor<Integration> cursor = getCollection("integration").find("{organizationId:#}", orgId)
				.sort("{id:1}").skip(offset).limit(batchSize)
				.as(Integration.class);
		List<Integration> list = new ArrayList<Integration>();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list;
	}
}
