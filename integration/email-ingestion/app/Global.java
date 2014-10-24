import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

import play.GlobalSettings;
import play.Application;

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
}