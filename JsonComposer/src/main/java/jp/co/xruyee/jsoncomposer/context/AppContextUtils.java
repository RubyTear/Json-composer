package jp.co.xruyee.jsoncomposer.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppContextUtils {

	// initialize IoC Container
	private ApplicationContext _appContext = new ClassPathXmlApplicationContext("applicationContext.xml");

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		_appContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return _appContext;
	}

	public Object getBean(String name) throws BeansException {
		return _appContext.getBean(name);
	}

}
