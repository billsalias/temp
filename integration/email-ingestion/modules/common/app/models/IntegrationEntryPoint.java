package models;

import org.jongo.marshall.jackson.oid.ObjectId;

/**
 * This model represents an entry point used by one or more Integrations to
 * receive incoming messages at a specific email address. Entry points must all
 * have unique emailAddress and token properties. Entry points can be shared
 * only between Integrations that share an organizationId.
 * 
 * @author bill.clogston
 *
 */
public class IntegrationEntryPoint {
    /**
     * Update this when any non backward compatible change is made. This will be
     * the default value for schemaVersion and will be used to compare to stored
     * values in the DB to verify matching versions.
     **/
    public static final int EXPECTED_SCHEMA_VERSION = 1;

    /** Supported status values for an IntegrationEntryPoint. **/
    public enum Status {
        /** This integration is active and can process emails. */
        ACTIVE,
        /** This integration will not process any emails. */
        INACTIVE,
        /**
         * This integration in inactive and not returned by standard
         * enumerations
         */
        DELETED;
    }

    /** The mongodb assigned id for this document. **/
    @ObjectId
    public String _id;

    /** The schema version used when this object was created. **/
    public int schemaVersion = EXPECTED_SCHEMA_VERSION;

    /**
     * The status of this IntegrationEntryPoint, if inactive no messages will be
     * processed.
     **/
    public Status status = Status.ACTIVE;

    /** The organization this IntegrationEntryPoint is associated with. **/
    public long organizationId;

    /**
     * The email address for this entry point, this is denormalized to
     * Integration and must be kept in sync.
     **/
    public String entryPointAddress;

    /**
     * This is the unique id for the route configured in mailgun that processes
     * email for this entry point address.
     **/
    public String token;

}
