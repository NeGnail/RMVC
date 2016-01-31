package coolraw.front.dispacher;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import coolraw.exception.OperationException;
import coolraw.process.handler.HandlerExecutionChain;
import coolraw.process.handlerAdapter.HandlerAdapter;
import coolraw.process.interceptor.Interceptor;
import coolraw.view.Model;
import coolraw.view.ModelAndView;
import coolraw.view.View;
/**
 * 默认dispatcher
 * @author LiWei 
 *
 */
public class SingleDispatcherServlet extends DispatcherServlet{
	private final Log log=LogFactory.getLog(getClass());
	@Override
	protected HandlerExecutionChain[] dispatch(HttpServletRequest request) {
		String accessUri=request.getRequestURI().substring(request.getContextPath().length());
		Object handler=getHandler(accessUri);
		Interceptor[] interceptors=getInterceptors(accessUri);
		
		return new HandlerExecutionChain[]{new HandlerExecutionChain(handler, interceptors)};
	}

	@Override
	protected View process(HandlerExecutionChain[] handlerExecutionChains,HttpServletRequest request,HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, OperationException {
		HandlerExecutionChain handlerExecutionChain=handlerExecutionChains[0];
		return doProcess(handlerExecutionChain, request, response);
		
	}



}
