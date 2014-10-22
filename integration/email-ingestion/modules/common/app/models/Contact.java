package models;

import org.jongo.marshall.jackson.oid.ObjectId;	

public class Contact {

//	@ObjectId 
    private long _id;

    public String firstName;
    public String lastName;

    public String toString() {
    	return "Contact[_id=" + _id + ", " +
                "firstName=" + firstName + "," +
                "lastName=" + lastName + "]";
    }
	
}
