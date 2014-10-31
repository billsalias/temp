package models;

import java.util.List;

import org.jongo.marshall.jackson.oid.ObjectId;

/**
 * This object models an integration between an email and an incident template.
 * An integration provides data to perform two main tasks: filter email to
 * identify which should be processed and map data from those email to incident
 * template variables.
 * 
 * The email filtering is done by comparing various parts of the email with data
 * configured in the integration. All filtering conditions specified in the
 * integration must be met for an email to be processed. If a given filter is
 * null it is ignored. For example of fromAddress is null then email from any
 * source will be considered, but it still must match the andConditions and
 * orConditions if specified.
 * 
 * @author bill.clogston
 *
 */
public class Integration {
    /**
     * Update this when any non backward compatible change is made. This will be
     * the default value for schemaVersion and will be used to compare to stored
     * values in the DB to verify matching versions.
     **/
    public static final int EXPECTED_SCHEMA_VERSION = 1;

    /** Supported status values for an integration. **/
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

    /**
     * This represents a single condition used to filter email before
     * processing.
     **/
    public class Condition {
        /** An identifier for the part of the message to operate on. **/
        public EmailPart dataSource;

        /** What operator to use to compare the dataSource and operand. **/
        public FilterOperation operator;

        /** The value to combine with the dataSource using operation. **/
        public String operand;
    }

    /**
     * This represents a mapping from a part of an email to a template variable.
     * The mapping is done by a regular expression.
     **/
    public class Variable {
        /**
         * The id of the variable this mapping is for. This is an internal id
         * used with the EB Suite REST API.
         **/
        public long templateVariableId;

        /** What part of the email to extract the data from. */
        public EmailPart dataSource;

        /**
         * A regular expression to use to extract the data, the desired data is
         * assumed to be in the first capture group. This regular expression is
         * evaluated with the java.util.regex package.
         **/
        public String regEx;
    }

    /** The mongodb assigned id for this document. **/
    @ObjectId
    public String _id;

    /** The schema version used when this object was created. **/
    public int schemaVersion = EXPECTED_SCHEMA_VERSION;

    /** The organization this integration is associated with. **/
    public long organizationId;

    /**
     * The status of this integration, if inactive no messages will be
     * processed.
     **/
    public Status status = Status.ACTIVE;

    /**
     * The incident template this integration will trigger when a matching email
     * is found and processed.
     **/
    public long templateId;

    /**
     * The email address this integration will monitor for messages, this is
     * denormalized data from the associated IntegrationEntryPoint.
     **/
    public String entryPointAddress;

    /**
     * This is the ID of the IntegrationEntryPoint this integration is
     * associated with.
     **/
    public String entryPointId;

    /**
     * An from email address to use to filter incoming messages, only email with
     * a from address that matches this exactly will be considered for
     * processing. If this is null any from address is accepted.
     **/
    public String fromAdressFilter;

    /**
     * A list of conditions that ALL must be met for an email to be considered
     * for this integration. If this is null any email is considered to have
     * passed this condition. If an or condition is specified it must still be
     * met as well as this condition.
     **/
    public List<Condition> andConditions;

    /**
     * A list of conditions that ONE must be met for an email to be considered
     * for this integration. If this is null then any email is considered to
     * have matched this condition. If an and condition is specified it must be
     * met as well.
     **/
    public List<Condition> orConditions;

    /** A list of variable mapping configurations. **/
    public List<Variable> variableMap;
}
