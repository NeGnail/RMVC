package coolraw.process.handler;

import coolraw.process.interceptor.Interceptor;


public class HandlerExecutionChain {
	private Object handler;
	private Interceptor[] interceptors;
	
	
	public HandlerExecutionChain(Object handler, Interceptor[] interceptors) {
		super();
		this.handler = handler;
		this.interceptors = interceptors;
	}
	public Object getHandler() {
		return handler;
	}
	public void setHandler(Object handler) {
		this.handler = handler;
	}
	public Interceptor[] getInterceptors() {
		return interceptors;
	}
	public void setInterceptors(Interceptor[] interceptors) {
		this.interceptors = interceptors;
	}
	
	
}
