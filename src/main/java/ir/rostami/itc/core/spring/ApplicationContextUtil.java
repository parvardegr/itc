package ir.rostami.itc.core.spring;

import org.springframework.context.ApplicationContext;

public class ApplicationContextUtil {

	public static ApplicationContext getApplicationContext() {
		return ApplicationContextHolder.getApplicationContext();
	}
	
	public static String getProperty(String name) {
		return getApplicationContext().getEnvironment().getProperty(name);
	}
	
	public static String getProperty(String name, String defaultValue) {
		return getApplicationContext().getEnvironment().getProperty(name, defaultValue);
	}
}
