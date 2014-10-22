package models;

import play.data.validation.Constraints;


public class Bar {

    public String id;

    @Constraints.Required(message = "The name is required")
    public String name;

}