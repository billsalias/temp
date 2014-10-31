package services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import play.Logger;
import play.libs.F.Promise;
import play.libs.F.Function;
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
    public Promise<Integration> addIntegration(final Integration newIntegration) {
        // verify the template id and variable ids asynchronously
        // Fake it for now, these will be asynchronous service calls to the EB
        // API server through WS eventually.
        List<Promise<Boolean>> validators = new ArrayList<Promise<Boolean>>(3);
        validators.add(Promise.<Boolean> pure(Boolean.TRUE));
        validators.add(Promise.<Boolean> pure(Boolean.TRUE));
        validators.add(Promise.<Boolean> pure(Boolean.TRUE));

        // See if there is an existing integration entry point that matches the
        // new integration destination email address

        // If there is an existing entry point use it, if not create one

        // Update the entry point id in the new integration

        // Wait for asynchronous validations to complete then complete the
        // insert
        Promise<List<Boolean>> validationResults = Promise
                .<Boolean> sequence(validators);
        Promise<Integration> result = validationResults
                .map(new Function<List<Boolean>, Integration>() {
                    public Integration apply(List<Boolean> validation)
                            throws Throwable {
                        // Check the validation results
                        for ( Boolean valid : validation ) {
                            if ( !valid ) {
                                throw new IllegalArgumentException("Invalid id passed in Integration");
                            }
                        }

                        // Save the update and validated integration to mongo
                        return integrationDAO.insert(newIntegration);
                    }
                });

        return result;
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
