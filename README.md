    This is a MVC framework。RMVC is committed to support for RESTful。
    RMVC support to URL request parameter, and support the concurrent requests. In a request to the operation of the multiple resources.
    ![image](https://github.com/meelody/RMVC/blob/master/lw.1.png)
Configuration
    RMVC provide container interface to the user, the user can implement this interface to use their own containers.
    
    public interface BeanFactory {
	     void init();
	     Object getBean(String name);
	     List<Object> getControllers();
	     List<Interceptor> getInterceptor();
    }
    
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

    RMVC default by the Spring IOC container containers as the default, 
    so if using the default Settings will need to provide to the Spring under the web environment configuration
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/applicationContext.xml</param-value>
  </context-param>
  <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
  <servlet>
        <servlet-name>MVC</servlet-name>
        <servlet-class>coolraw.front.dispacher.SingleDispatcherServlet</servlet-class>
        <init-param>
            <param-name>BeanFactory</param-name>
            <param-value>coolraw.beanfactory.DefaultBeanFactory</param-value>
        </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
        <servlet-name>MVC</servlet-name>
        <url-pattern>/</url-pattern>
  </servlet-mapping>
  
  RMVC implementation depends on the servlet, so need to be configured using the servlet。
  The default by SingleDispatcherServlet。If you need the support of concurrency can choose ConcurrentDispatcherServlet
    
