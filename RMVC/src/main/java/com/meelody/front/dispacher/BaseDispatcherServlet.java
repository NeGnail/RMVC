package coolraw.front.dispacher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import coolraw.beanfactory.BeanFactory;
import coolraw.process.factory.ControllerHandlerFactory;
import coolraw.process.factory.HandlerFactory;
import coolraw.process.handlerAdapter.HandlerAdapter;
import coolraw.process.pathHandler.PathHandler;
import coolraw.viewResolver.ViewResolver;

/**
 * 
 * @author LiWei
 *
 */
public abstract class BaseDispatcherServlet extends HttpServlet{
	private final Log log=LogFactory.getLog(getClass());
	protected ViewResolver viewResolver;
	protected BeanFactory beanFactory;
	protected ControllerHandlerFactory controllerHandlerFactory;
	protected List<HandlerAdapter> handlerAdapters;
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	@Override
	public void destroy() {
	
	}

	@Override
	public void init() throws ServletException {
		beforeInit();
		try {
			initBeanFacotry();
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("The BeanFactory initialization failed!");
		}
		try {
			initPathHandlers();
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("The PathHandler initialization failed!");
		}
		initHandlerAdapters();
		initViewResolver();
	log.info("DispatcherServlet initialization is completed");
	}
	
	private void initViewResolver() {
		this.viewResolver=(ViewResolver) this.beanFactory.getBean("ViewResolver");
		
	}

	
	private void beforeInit() {
		try {
			initHandlerFactory();
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("The HandlerFactory initialization failed");
		}
		
	}
	/**
	 * 初始化HandlerFactory
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	protected void initHandlerFactory() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.controllerHandlerFactory=new ControllerHandlerFactory();
	}
	
	
	private void initHandlerAdapters() {
		handlerAdapters=new ArrayList<HandlerAdapter>();
		handlerAdapters.add(controllerHandlerFactory.createHandlerAdapter());
		
	}
	/**
	 * 初始化PathHandlers
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	protected abstract void initPathHandlers() throws InstantiationException, IllegalAccessException, ClassNotFoundException;
	/**
	 * 初始化容器
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	protected abstract void initBeanFacotry() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException;
	
}
