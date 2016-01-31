package coolraw.process.handlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import coolraw.annotation.RequestMapping;
import coolraw.process.handler.SimpleURLHandler;
import coolraw.util.URIUtil;

/**
 * 现阶段以此进行适配
 * @author LiWei 
 */
public class SimpleURLHandlerAdapter extends ControllerHandlerAdapter{
	private final Log log=LogFactory.getLog(getClass());
	public SimpleURLHandlerAdapter(){
		super();
	}
	
	
	

	protected Method getMethod(Object handler, HttpServletRequest request) {
		String accessUri=URIUtil.getAccessUri(request);
		String methodUri=getMethodUri(handler,accessUri);
		
		Method[] methods=handler.getClass().getMethods();
		for(int i=0;i<methods.length;i++){
			if(methods[i].getAnnotation(RequestMapping.class).value().equals(methodUri)){
				return methods[i];
			}
		}
		log.warn("The Controller of "+accessUri+"does not hava method of "+methodUri);
		return null;
	}
	@Override
	protected Map getInParameters(HttpServletRequest request,Object handelr,Method method) {
		return request.getParameterMap();
		
	}

	
	
}
