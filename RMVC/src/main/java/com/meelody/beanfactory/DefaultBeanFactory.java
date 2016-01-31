package coolraw.beanfactory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Controller;

import coolraw.process.interceptor.Interceptor;

public class DefaultBeanFactory extends ServletContextBeanFactory{

	public DefaultBeanFactory(ServletContext servletContext) {
		super(servletContext);
	}

	public Object getBean(String name) {
		Object bean=applicationContext.getBean(name);
		return bean;
	}

	public List<Object> getControllers() {
		List<Object> controllers = new ArrayList<Object>();
		String[] beansName=applicationContext.getBeanDefinitionNames();
		for(String name:beansName){
			Object bean=getBean(name);
			if(bean.getClass().isAnnotationPresent(Controller.class)){
				controllers.add(bean);
			}
		}
		return controllers;
	}

	public List<Interceptor> getInterceptor() {
		List<Interceptor> interceptors=new ArrayList<Interceptor>();
		String[] beansName=applicationContext.getBeanDefinitionNames();
		for(String name:beansName){
			Object bean=getBean(name);
			if(bean.getClass().isAnnotationPresent(coolraw.annotation.Interceptor.class)){
				interceptors.add((Interceptor) bean);
			}
		}
		return interceptors;
	}

}
