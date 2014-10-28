package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.main;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoClient;

@org.springframework.stereotype.Component
public class Application extends Controller {

    @Autowired
    private MongoClient mongoClient;

    public static Result index() {
        return ok(main.render("Title", Html.apply("<h1>Hello</h1>")));
    }
}
