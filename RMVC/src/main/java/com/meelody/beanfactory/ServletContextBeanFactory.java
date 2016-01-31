package coolraw.beanfactory;



import javax.servlet.ServletContext;


import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public abstract class ServletContextBeanFactory implements BeanFactory{

	private final ServletContext servletContext;
	protected ApplicationContext applicationContext;
	
	public ServletContextBeanFactory(ServletContext servletContext){
		this.servletContext=servletContext;
	}
	public void init() {
		this.applicationContext =WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}
	
}
