package controllers;

import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.Logger;

/**
 * This class is a place to collect acctions that are common to all
 * controllers. TODO: Consider moving token validation here.
 * 
 * @author bill.clogston
 *
 */
public class CommonInterceptors extends Action.Simple {
	public Promise<Result> call(Http.Context ctx) throws Throwable {
		return delegate.call(ctx);
	}
}