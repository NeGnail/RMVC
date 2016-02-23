package coolraw.front.dispacher;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;


import coolraw.decoration.LocalRequest;
import coolraw.exception.OperationException;
import coolraw.process.handler.HandlerExecutionChain;
import coolraw.process.handlerAdapter.HandlerAdapter;
import coolraw.process.interceptor.Interceptor;
import coolraw.util.URIUtil;
import coolraw.view.Model;
import coolraw.view.ModelAndView;
import coolraw.view.View;
/**
 * 
 * @author LiWei 
 *
 */
public class ConcurrentDispatcherServlet extends DispatcherServlet{

	private final CompletionService<HandlerExecutionChain> dispacherCompletionService;
	private final CompletionService<View> processCompletionService;
	private ThreadLocal<Integer> size=new ThreadLocal<Integer>();
	public ConcurrentDispatcherServlet(){
		dispacherCompletionService=new ExecutorCompletionService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2));
		processCompletionService=new ExecutorCompletionService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2+1));
	}
	@Override
	protected HandlerExecutionChain[] dispatch(HttpServletRequest request) throws InterruptedException {
		String accessUri=request.getRequestURI().substring(request.getContextPath().length());
		String[] accessUris=URIUtil.resolveConcurrentURI(accessUri);
		size.set(accessUris.length);
		for(final String eveAccessUri:accessUris){
			dispacherCompletionService.submit(new Callable<HandlerExecutionChain>() {
				public HandlerExecutionChain call() throws Exception {
					Object handler=getHandler(eveAccessUri);
					Interceptor[] interceptors=getInterceptors(eveAccessUri);
					return new HandlerExecutionChain(handler,interceptors);
				}
			});			
		}
		return null;
	}

	@Override
	protected View process(
			HandlerExecutionChain[] handlerExecutionChains,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InterruptedException, ExecutionException, OperationException {
			final LocalRequest localRequest=new LocalRequest(request);
		for(int i=0;i<size.get();i++){
			processCompletionService.submit(new Callable<View>() {
				public View call() throws Exception {
					HandlerExecutionChain handlerExecutionChain=(HandlerExecutionChain) dispacherCompletionService.take().get();
					localRequest.decorateRequestURI(handlerExecutionChain.getHandler());
					return doProcess(handlerExecutionChain,localRequest,response);
				}
			});
			
		}
		return getView();
	}
	private View getView() throws InterruptedException, ExecutionException, OperationException {
		View viewFlag=null;
		for(int i=0;i<size.get();i++){
			View view=processCompletionService.take().get();
			if(viewFlag==null){
				viewFlag=view;
			}else{
				if(!viewFlag.equals(view)){
					throw new OperationException("Multiple views are not identical!");
				}
			}
		}
		return viewFlag;
		
	}

	
	


}
