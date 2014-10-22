package controllers.common;

import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	public static Result status() {
		// ExampleLogger.log("Log from the api using the Common logger");
		return ok("Doing OK.");
	}

}
