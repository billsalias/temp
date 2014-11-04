package dao;

import javax.annotation.Resource;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * This class provides base functionality for all DAO objects.
 * 
 * @author bill.clogston
 *
 */
public abstract class BaseDAO {
    @Resource
    private MongoClient mongoClient;

    /**
     * Implement this method when subclassing to tell this class the name of the
     * collection to work with.
     * 
     * @return
     */
    public abstract String getCollectionName();

    /**
     * This method abstracts the details of accessing a collection from the
     * mongo database,
     * 
     * @param collection
     *            The name of the collection to access.
     * @return The MongoCollection instance to access the desired collection.
     */
    protected MongoCollection getCollection() {
        DB db = mongoClient.getDB("coredb");
        Jongo jongo = new Jongo(db);
        return jongo.getCollection(getCollectionName());
    }
}
