package controllers;

import play.libs.F.Function;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import play.libs.Json;
import play.libs.ws.WS;
import play.mvc.Result;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.mongodb.MongoClient;	

import org.jongo.Jongo;
import org.jongo.MongoCursor;
import org.jongo.Mapper;
import org.jongo.MongoCollection;

import services.BarService;
import models.Contact;
import views.html.index;
import utils.ExampleLogger;

@org.springframework.stereotype.Component
public class Application extends Controller {

    private static final String CONNURI = "mongodb://localhost:27017/coredb?maxConnectionCount=10";

	@Autowired
    public BarService barService;
	
	@Autowired
	private MongoClient mongoClient;

	/*
	public static Result feedComments(String feedUrl) {
	    return async(
	      WS.url(feedUrl).get().flatMap(
	        new Function<WS.Response, Promise<Result>>() {
	          public Promise<Result> apply(WS.Response response) {
	            return WS.url(response.asJson().findPath("commentsUrl").get().map(
	              new Function<WS.Response, Result>() {
	                public Result apply(WS.Response response) {
	                  return ok("Number of comments: " + response.asJson().findPath("count"));
	                }
	              }
	            ));
	          }
	        }
	      )
	    );
	  }
	*/
	
	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}
	
	public static Result index2() {
		return ok(index.render("Your new application is ready."));
	}

	public Result listContact() throws Exception {
		DB db = mongoClient.getDB("coredb");
		Jongo jongo = new Jongo(db);
		MongoCollection contacts = jongo.getCollection("contact");
		//MongoCursor<Contact> all = contacts.find("{name: 'Joe'}").as(Contact.class);
		Contact one = contacts.findOne("{firstName: 'Staff'}").as(Contact.class);
		ExampleLogger.log("Log from the api using the Common logger with contact=" + one);
		return ok(Json.toJson(one));
		//return ok(one.toString());
	}


    public Result listContacts(String firstName) throws Exception {
        Contact one = null;
        DB db = mongoClient.getDB("coredb");
        Jongo jongo = new Jongo(db);
        MongoCollection contacts = jongo.getCollection("contact");
        MongoCursor<Contact> all = contacts.find("{firstName: #}", firstName).as(Contact.class);
        if(all.hasNext()) {
            one = all.next();
        }
        ExampleLogger.log("Log from the api using the Common logger with contact=" + one);
        if (one == null) {
            return ok(index.render("No contact found for firstName=" + firstName));
        }
        else {
            return ok(Json.toJson(one));
        }
    }


	public Result listBars() {
		return ok(Json.toJson(barService.getAllBars()));
	}
	

}
