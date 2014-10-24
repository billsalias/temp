package models;

/**
 * This enum represents potential filter operations that can be used on an
 * email. All operations are assumed to be case insensitive when comparing
 * text. 
 * 
 * @author bill.clogston
 *
 */
public enum FilterOperation {
	STARTS_WITH, 
	ENDS_WITH, 
	INCLUDES, 
	EQUALS
}
