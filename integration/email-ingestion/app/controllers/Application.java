package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.main;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoClient;

@org.springframework.stereotype.Component
public class Application extends Controller {

//	@Autowired
//    public BarService barService;
	
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
		return ok(main.render("Title",Html.apply("<h1>Hello</h1>")));
	}

	/*
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

    public Result addBar(String name) throws Exception {
        Bar newBar = new Bar();
        newBar.name = name;
        DB db = mongoClient.getDB("coredb");
        Jongo jongo = new Jongo(db);
        MongoCollection bars = jongo.getCollection("bars");
        WriteResult result = bars.insert(newBar);
        return ok(newBar._id + ":" + result.toString());
    }

	public Result listBars() {
        DB db = mongoClient.getDB("coredb");
        Jongo jongo = new Jongo(db);
        MongoCollection bars = jongo.getCollection("bars");
        MongoCursor<Bar> all = bars.find().as(Bar.class);
        List<Bar> barList = new ArrayList<Bar>();
        while ( all.hasNext() ) {
        	barList.add(all.next());
        }
		return ok(Json.toJson(barList));
	}
	
*/
}
