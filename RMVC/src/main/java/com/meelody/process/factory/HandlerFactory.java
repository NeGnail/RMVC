package coolraw.process.factory;

import coolraw.beanfactory.BeanFactory;
import coolraw.process.handler.Handler;
import coolraw.process.handlerAdapter.HandlerAdapter;
import coolraw.process.pathHandler.PathHandler;


/**
 * 工厂
 * @author LiWei 
 *
 */
public interface HandlerFactory {
	PathHandler createPathHandler();
	HandlerAdapter createHandlerAdapter();
}

