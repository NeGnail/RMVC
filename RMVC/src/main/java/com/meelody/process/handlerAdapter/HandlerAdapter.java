package coolraw.process.handlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coolraw.exception.OperationException;
import coolraw.front.dispacher.DispatcherServlet;
import coolraw.process.pathHandler.PathHandler;


/**
 * 执行扩展
 * @author LiWei
 *
 */
public interface HandlerAdapter {
	boolean isSupport(Object handler);
	Object process(Object handler,HttpServletRequest request,HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, OperationException;
//	boolean isSupport(String path);
//	void addController(String controllerUri, Object controller, List<PathHandler> pathHandlers);
}
