import static play.mvc.Results.internalServerError;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import controllers.ErrorResponse;

public class Global extends GlobalSettings {

    private ApplicationContext ctx;

    @Override
    public void onStart(Application app) {
        super.onStart(app);
        // start Spring config using XML or Config class
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Override
    public <A> A getControllerInstance(Class<A> clazz) {
        return ctx.getBean(clazz);
    }

    /**
     * For any unhandled exception in our controllers return a standard error.
     */
    @Override
    public Promise<Result> onError(RequestHeader req, Throwable t) {
        // Log the error
        Logger.of(GlobalSettings.class).error(
                "Unhandled exception in controller", t);
        
        // Report a standard error objects as json to the client
        return Promise.<Result> pure(internalServerError(Json
                .toJson(new ErrorResponse(ErrorResponse.Status.EXCEPTION, t
                        .getMessage()))));
    }
}