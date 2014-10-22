import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;

import play.GlobalSettings;
import play.Application;
import play.Application;
import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.Logger;

import java.lang.reflect.InvocationTargetException;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.kenshoo.play.metrics.MetricsRegistry;
import com.kenshoo.play.metrics.MetricsFilter;


public class Global extends GlobalSettings {

/*	
	@Override
	public <T extends EssentialFilter> Class<T>[] filters() {
		return (Class<T>[]) new Class[] { MetricsFilter.class };
	}
*/
		
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
	 

/*	
	@Override
	public void onStart(Application app) {

		// this is due to 'default' is a reserved word in Java
		Object defaultRegistry = null;

		try {
			defaultRegistry = MetricsRegistry.class.getMethod("default")
					.invoke(null);
			JmxReporter jmxReporter = JmxReporter.forRegistry(
					(MetricRegistry) defaultRegistry).build();

			// JmxReporter jmxReporter = JmxReporter.forRegistry(new
			// MetricRegistry()).build();
			jmxReporter.start();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/
	
}