package coolraw.front.dispacher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import coolraw.annotation.RequestMapping;
import coolraw.beanfactory.BeanFactory;
import coolraw.exception.OperationException;
import coolraw.process.factory.ControllerHandlerFactory;
import coolraw.process.factory.HandlerFactory;
import coolraw.process.handler.HandlerExecutionChain;
import coolraw.process.handlerAdapter.HandlerAdapter;
import coolraw.process.interceptor.Interceptor;
import coolraw.process.pathHandler.ControllerPathHandler;
import coolraw.process.pathHandler.PathHandler;
import coolraw.util.URIUtil;
import coolraw.view.Model;
import coolraw.view.ModelAndView;
import coolraw.view.View;

public abstract class DispatcherServlet extends BaseDispatcherServlet{
	private final Log log=LogFactory.getLog(getClass());
	
	private final List<PathHandler> pathHandlers=new ArrayList<PathHandler>();
	
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HandlerExecutionChain[] handlerExecutionChains = null;
		try {
			handlerExecutionChains = dispatch(request);
		} catch (InterruptedException e1) {
			log.warn("The process of looking for processing HandlerExecutionChain is interrupted");
			e1.printStackTrace();
		}
		View view = null;
		try {
			view=process(handlerExecutionChains,request,response);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("The handler run wrong");
		}
		jump(view,request, response);
	
		
	}
	protected View render(Object result, HttpServletRequest request,
			HttpServletResponse response) throws OperationException {
		View view=null;
		String viewPath=null;
		Model model=null;
		if(result instanceof ModelAndView){
			viewPath=((ModelAndView) result).getViewPath();
			model=((ModelAndView) result).getModel();
		}else{
			viewPath=(String) result;
		}
		if(viewPath==null){
			log.warn("No point out the path of the view!");
			throw new OperationException("No point out the path of the view!");
		}
		view=viewResolver.resolveView(viewPath);
		view.render(model,request,response);
		return view;
	}

	protected void jump(View view, HttpServletRequest request,
			HttpServletResponse response) {
		view.jump(request, response);
	}

	protected abstract View process(HandlerExecutionChain[] handlerExecutionChains,HttpServletRequest request,HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, OperationException, InterruptedException, ExecutionException;
	
	protected abstract HandlerExecutionChain[] dispatch(HttpServletRequest request) throws InterruptedException;
	/**
	 * 获得指定handler的拦截器
	 * @param handler
	 * @return
	 */
	protected Interceptor[] getInterceptors(String handlerUri) {
		List<Interceptor> fitInterceptors=new ArrayList<Interceptor>();
		List<Interceptor> interceptors=this.beanFactory.getInterceptor();
		for(Interceptor interceptor:interceptors){
			String interceptorUri=interceptor.getClass().getAnnotation(RequestMapping.class).value();//不用springioc的注解，因为若换为其它容器则无效，所以统一用自定义的注解。不要依赖容器
			if(URIUtil.isMatch(interceptorUri, handlerUri)){
				fitInterceptors.add(interceptor);
			}
		}
		return fitInterceptors.toArray(new Interceptor[fitInterceptors.size()]);
	}
	/**
	 * 根据accessUri找到对应的handler
	 * @param accessUri
	 * @return
	 */
	protected Object getHandler(String accessUri) {
		Object handler=null;
		for(PathHandler pathHandler:pathHandlers){
			Set<String> handlerUris = pathHandler.getPathHandlerUri();
			for(String handlerUri:handlerUris){
				if(URIUtil.isMatch(handlerUri, accessUri)){
					return pathHandler.getHandler(handlerUri);
				}
			}
		}
		log.warn("The handelr of URI does not exist!");
		return null;
	}
	

	@Override
	protected void initPathHandlers() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Object> controllers=this.beanFactory.getControllers();
		for(Object controller:controllers){
			String controllerUri=controller.getClass().getAnnotation(RequestMapping.class).value();//不用springioc的注解，因为若换为其它容器则无效，所以统一用自定义的注解。不要依赖容器
			doInitPathHandlers(controllerUri,controller);
		}
	}
	
	
	private void doInitPathHandlers(String controllerUri, Object controller) {
		for(PathHandler pathHandler:pathHandlers){
			if(pathHandler.getClass()==ControllerPathHandler.class){
				pathHandler.addHandler(controllerUri, controller);
				return;
			}
		}
		PathHandler pathHandler=this.controllerHandlerFactory.createPathHandler();
		pathHandler.addHandler(controllerUri, controller);
		pathHandlers.add(pathHandler);
	}
	@Override
	protected void initBeanFacotry() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class beanFactoryClass=getBeanFactoryClass();
		this.beanFactory=(BeanFactory) beanFactoryClass.getConstructor(ServletContext.class).newInstance(this.getServletContext());
		this.beanFactory.init();
	}
	
	private Class getBeanFactoryClass() throws ClassNotFoundException {
		String beanFactoryName=this.getInitParameter("BeanFactory");	
		return Class.forName(beanFactoryName);
	}
	
	/**
	 * 执行前置拦截器链
	 * @param interceptors
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean processInterceptorPre(Interceptor[] interceptors,HttpServletRequest request,HttpServletResponse response) {
		for(int i=0;i<interceptors.length;i++){
			boolean result=interceptors[i].preHandle(request,response);
			if(result==false){
				return false;
			}
		}
		return true;
	}
	/**
	 * 执行渲染试图前拦截器链
	 * @param interceptors
	 * @param request
	 * @param response
	 * @param modelAndView
	 */
	protected void processInterceptorPost(Interceptor[] interceptors,HttpServletRequest request,
			HttpServletResponse response, ModelAndView modelAndView) {
		for(int i=interceptors.length-1;i>=0;i--){
			interceptors[i].postHandle(request, response, modelAndView);
		}
		
	}
	/**
	 * 执行后置拦截器链
	 * @param interceptors
	 * @param request
	 * @param response
	 */
	protected void processInterceptorAfter(Interceptor[] interceptors,
			HttpServletRequest request, HttpServletResponse response) {
		for(int i=interceptors.length-1;i>=0;i--){
			interceptors[i].afterHandle(request, response);
		}
		
	}
	/**
	 * 获得handler的adapter
	 * @param handler
	 * @return
	 */
	protected HandlerAdapter getPathAdaper(Object handler){
		for(HandlerAdapter handlerAdapter:handlerAdapters){
			if(handlerAdapter.isSupport(handler)){
				return handlerAdapter;
			}
		}
		log.warn("The kind of handler is not exist");	
		return null;
	}
	
	protected View doProcess(HandlerExecutionChain handlerExecutionChain, HttpServletRequest request, HttpServletResponse response) throws OperationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object handler=handlerExecutionChain.getHandler();
		Interceptor[] interceptors=handlerExecutionChain.getInterceptors();
		
		HandlerAdapter handlerAdapter=getPathAdaper(handler);
		if(handlerAdapter==null){
			throw new OperationException("The handelr of URI does not exist!");
		}
		
		
		boolean flag=processInterceptorPre(interceptors,request,response);//Interceptor前置
		if(flag==false){
			return null;
		}
		Object result=handlerAdapter.process(handler, request,response);
		
		if(result instanceof ModelAndView){
			processInterceptorPost(interceptors, request, response, (ModelAndView) result);			
		}else{
			processInterceptorPost(interceptors, request, response, null);			
		}
		
		View view=render(result, request, response);
		
		processInterceptorAfter(interceptors, request, response);//Interceptor后置
		
		return view;
	}
}
