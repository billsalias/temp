package services;

import java.util.List;

import javax.annotation.Resource;

import models.Integration;
import models.IntegrationEntryPoint;

import org.springframework.stereotype.Service;

import play.libs.F.Function;
import play.libs.F.Promise;
import dao.IntegrationDAO;
import dao.IntegrationEntryPointDAO;

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
    @Resource(name = "mailgunService")
    private MailgunAPIConnectorService mailgunService;

    @Resource(name = "ebAPIService")
    private EBRestAPIConnectorService ebAPIService;

    @Resource(name = "integrationDAO")
    private IntegrationDAO integrationDAO;

    @Resource(name = "integrationEntryPointDAO")
    private IntegrationEntryPointDAO entryPointDAO;

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
        Promise<Boolean> validator = ebAPIService.validateTemplate(
                newIntegration.organizationId, newIntegration.templateId, null);

        // Wait for asynchronous validations to complete then based on status
        // continue the add process
        Promise<Integration> result = validator
                .flatMap(new Function<Boolean, Promise<Integration>>() {
                    public Promise<Integration> apply(Boolean valid)
                            throws Throwable {
                        // Check the validation results
                        if (!valid) {
                            throw new IllegalArgumentException(
                                    "Invalid id passed in Integration");
                        }

                        // See if there is an existing integration entry point
                        // that matches the new integration destination email
                        // address
                        IntegrationEntryPoint entryPoint = entryPointDAO
                                .findByEmail(newIntegration.entryPointAddress);

                        // If there is an existing entry point use it, if not
                        // create one
                        Promise<IntegrationEntryPoint> newEP;
                        if (entryPoint != null) {
                            // Create a promise with the already found value
                            newEP = Promise
                                    .<IntegrationEntryPoint> pure(entryPoint);
                        } else {
                            // Need to create one, first step add a route for it
                            // to mailgun
                            newEP = mailgunService.addRouteForEmail(
                                    newIntegration.entryPointAddress)
                            // Map the added route id to the new entry
                            // point
                                    .map(new Function<String, IntegrationEntryPoint>() {
                                        public IntegrationEntryPoint apply(
                                                String routeId)
                                                throws Throwable {
                                            // Got the new route id, insert it
                                            // into the DB
                                            IntegrationEntryPoint entryPoint = new IntegrationEntryPoint();
                                            entryPoint.organizationId = newIntegration.organizationId;
                                            entryPoint.entryPointAddress = newIntegration.entryPointAddress;
                                            entryPoint.status = IntegrationEntryPoint.Status.ACTIVE;
                                            entryPoint.token = routeId;
                                            entryPointDAO.insert(entryPoint);

                                            // Finally return the new entry
                                            // point
                                            return entryPoint;
                                        }
                                    });
                        }

                        // once we have the new entry point we can complete the
                        // insert of the Integration
                        return newEP
                                .map(new Function<IntegrationEntryPoint, Integration>() {
                                    public Integration apply(
                                            IntegrationEntryPoint entryPoint) {
                                        // Update the entry point id in the new
                                        // integration
                                        newIntegration.entryPointId = entryPoint._id;

                                        // Save the update and validated
                                        // integration to mongo
                                        return integrationDAO
                                                .insert(newIntegration);
                                    }
                                });
                    }
                });

        return result;
    }

    /**
     * Add a new integration to mongo. Before adding the integration we will
     * verify the template id and variable ids and will associate the
     * integration with an IntegrationEntryPoint (creating one if necessary).
     * 
     * @param newIntegration
     * @return The passed Integration with entryPointId and _id updated.
     */
    public Promise<Integration> addIntegration2(final Integration newIntegration) {
        // verify the template id and variable ids asynchronously
        Promise<Boolean> validator = ebAPIService.validateTemplate(
                newIntegration.organizationId, newIntegration.templateId, null);

        // Based on validity continue process by looking for an existing entry
        // point
        Promise<IntegrationEntryPoint> existingEP = validator
                .map(new Function<Boolean, IntegrationEntryPoint>() {
                    public IntegrationEntryPoint apply(Boolean valid)
                            throws Throwable {
                        // Check the validation results
                        if (!valid) {
                            throw new IllegalArgumentException(
                                    "Invalid id passed in Integration");
                        }

                        // See if there is an existing integration entry point
                        // that matches the new integration destination email
                        // address
                        IntegrationEntryPoint entryPoint = entryPointDAO
                                .findByEmail(newIntegration.entryPointAddress);

                        return entryPoint;
                    }

                });

        // Now based on based on the lookup of the existing entry point either
        // use it or create one from scratch
        Promise<IntegrationEntryPoint> newEP = existingEP
                .flatMap(new Function<IntegrationEntryPoint, Promise<IntegrationEntryPoint>>() {
                    public Promise<IntegrationEntryPoint> apply(
                            IntegrationEntryPoint existingEP) throws Throwable {
                        Promise<IntegrationEntryPoint> newEP;
                        if (existingEP != null) {
                            // Create a promise with the already found value
                            newEP = Promise
                                    .<IntegrationEntryPoint> pure(existingEP);
                        } else {
                            // Need to create one, first step add a route for it
                            // to mailgun
                            newEP = mailgunService
                                    .addRouteForEmail(
                                            newIntegration.entryPointAddress)
                                    .map(new Function<String, IntegrationEntryPoint>() {
                                        // Map the added route id to the new
                                        // entry point
                                        public IntegrationEntryPoint apply(
                                                String routeId)
                                                throws Throwable {
                                            // Got the new route id, insert it
                                            // into the DB
                                            IntegrationEntryPoint entryPoint = new IntegrationEntryPoint();
                                            entryPoint.organizationId = newIntegration.organizationId;
                                            entryPoint.entryPointAddress = newIntegration.entryPointAddress;
                                            entryPoint.status = IntegrationEntryPoint.Status.ACTIVE;
                                            entryPoint.token = routeId;
                                            entryPointDAO.insert(entryPoint);

                                            // Finally return the new entry
                                            // point
                                            return entryPoint;
                                        }
                                    });
                        }

                        return newEP;
                    }
                });

        // once we have the new entry point we can complete the
        // insert of the Integration
        return newEP.map(new Function<IntegrationEntryPoint, Integration>() {
            public Integration apply(IntegrationEntryPoint entryPoint) {
                // Update the entry point id in the new
                // integration
                newIntegration.entryPointId = entryPoint._id;

                // Save the update and validated
                // integration to mongo
                return integrationDAO.insert(newIntegration);
            }
        });

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
