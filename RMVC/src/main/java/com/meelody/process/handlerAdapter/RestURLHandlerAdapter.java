package coolraw.process.handlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import coolraw.annotation.RequestMapping;
import coolraw.exception.OperationException;
import coolraw.util.URIUtil;
/**
 * 
 * @author LiWei
 *
 */
public class RestURLHandlerAdapter extends ControllerHandlerAdapter{
	private final Log log=LogFactory.getLog(getClass());
	public RestURLHandlerAdapter(){
		super();
	}
	

	

	protected Method getMethod(Object handler, HttpServletRequest request) {
		String accessUri=URIUtil.getAccessUri(request);
		String methodUri=getMethodUri(handler,accessUri);
		Method[] methods=handler.getClass().getMethods();
		for(int i=0;i<methods.length;i++){
			String methodUriAndPar=methods[i].getAnnotation(RequestMapping.class).value();
			if(URIUtil.isMatchForRest(methodUri, methodUriAndPar)){
				return methods[i];
			}
		}

		log.warn("The Controller of "+accessUri+"does not hava method of "+methodUri);
		return null;
		
	}


	@Override
	protected Map getInParameters(HttpServletRequest request,Object handler,Method method) {
		String accessUri=URIUtil.getAccessUri(request);
		String methodUri=getMethodUri(handler,accessUri);
		String methodUriAndPar=method.getAnnotation(RequestMapping.class).value();
		Map<String,String[]> parametersForRest=URIUtil.getParametersForRest(methodUri,methodUriAndPar);
		Map<String,String[]> parameters=request.getParameterMap();
		parametersForRest.putAll(parameters);
		return parametersForRest;
	}

}
