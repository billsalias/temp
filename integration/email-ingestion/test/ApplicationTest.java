import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

/**
 *
 * Simple (JUnit) tests that can call all parts of a play app. If you are
 * interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

    @Test
    public void testAddIntegration() {
        assertThat(false).isEqualTo(true);
        Result result = callAction(controllers.routes.ref.IntegrationController
                .addIntegration(1));
        assertThat(status(result)).isEqualTo(OK);
        assertThat(contentType(result)).isEqualTo("text/html");
        assertThat(charset(result)).isEqualTo("utf-8");
        assertThat(contentAsString(result)).contains("Hello Kiki");
    }

    @Test
    public void renderTemplate() {
        // Content html =
        // views.html.index.render("Your new application is ready.");
        // assertThat(contentType(html)).isEqualTo("text/html");
        // assertThat(contentAsString(html)).contains("Your new application is ready.");
    }

}
