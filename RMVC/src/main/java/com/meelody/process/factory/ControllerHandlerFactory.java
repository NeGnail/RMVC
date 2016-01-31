package coolraw.process.factory;

import java.util.List;

import coolraw.annotation.RequestMapping;
import coolraw.beanfactory.BeanFactory;
import coolraw.process.handler.Handler;
import coolraw.process.handler.SimpleURLHandler;
import coolraw.process.handlerAdapter.ControllerHandlerAdapter;
import coolraw.process.handlerAdapter.HandlerAdapter;
import coolraw.process.handlerAdapter.RestURLHandlerAdapter;
import coolraw.process.handlerAdapter.SimpleURLHandlerAdapter;
import coolraw.process.pathHandler.PathHandler;
import coolraw.process.pathHandler.ControllerPathHandler;

public class ControllerHandlerFactory implements HandlerFactory{

	public PathHandler createPathHandler() {
		return new ControllerPathHandler();
	}

	public ControllerHandlerAdapter createHandlerAdapter() {
//		return new SimpleURLHandlerAdapter();
		return new RestURLHandlerAdapter();
	}

	
	

	
}
