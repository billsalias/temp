package controllers;

/**
 * This class is used as the response when a controller encounters an error.
 * 
 * @author bill.clogston
 *
 */
public class ErrorResponse {
    /** Supported status values for an integration. **/
    public enum Status {
        EXCEPTION, INVALID_ARGUMENT;
    }

    public Status code;
    
    public String message;

    public ErrorResponse(Status code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
