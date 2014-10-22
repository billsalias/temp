package controllers.api;

import play.mvc.Controller;
import play.mvc.Result;
import utils.ExampleLogger;

public class StaticPages extends Controller {

    public static Result index() {
    	ExampleLogger.log("Log from the api using the Common logger");
        return ok("Welcome on the API side of your sub project :)");
    }

}
