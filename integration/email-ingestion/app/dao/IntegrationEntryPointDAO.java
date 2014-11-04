package dao;

import models.IntegrationEntryPoint;

import org.springframework.stereotype.Service;

/**
 * This class uses a MongoClient provided by the base class to manage
 * IntegrationEntryPoint document instances.
 * 
 * @author bill.clogston
 *
 */
@Service("integrationEntryPointDAO")
public class IntegrationEntryPointDAO extends BaseDAO {
    /** The name of the collection this DAO manages */
    public static final String COLLECTION = "integrationEntryPoint";

    @Override
    public String getCollectionName() {
        // TODO Auto-generated method stub
        return COLLECTION;
    }

    /**
     * Insert the passed IntegrationEntryPoint into the integrationEntryPoint
     * collection. If no _id is set one will be allocated, if one is set it will
     * be respected.
     * 
     * @param newEP
     * @return The passed IntegrationEntryPoint with a new _id if one was not
     *         provided on input.
     */
    public IntegrationEntryPoint insert(IntegrationEntryPoint newEP) {
        getCollection().insert(newEP);
        
        return newEP;
    }

    /**
     * Find an IntegrationEntryPoint by the email token it uses.
     * 
     * @param token
     * @return
     */
    public IntegrationEntryPoint findByToken(String token) {
        return getCollection().findOne("{token:#}", token)
                .as(IntegrationEntryPoint.class);
    }

    /**
     * Find an IntegrationEntryPoint by the email address it uses.
     * 
     * @param emailAddress
     * @return
     */
    public IntegrationEntryPoint findByEmail(String emailAddress) {
        return getCollection().findOne("{entryPointAddress:#}", emailAddress)
                .as(IntegrationEntryPoint.class);
    }
}
