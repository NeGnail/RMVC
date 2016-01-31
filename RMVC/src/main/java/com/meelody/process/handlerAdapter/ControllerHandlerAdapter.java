package coolraw.process.handlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import coolraw.annotation.RequestMapping;
import coolraw.exception.OperationException;
import coolraw.util.URIUtil;
import coolraw.util.switcher.Adapter;
import coolraw.util.switcher.DoubleAdapter;
import coolraw.util.switcher.FloatAdapter;
import coolraw.util.switcher.IntegerAdapter;
import coolraw.view.ModelAndView;
/**
 * Controller适配
 * @author LiWei
 *
 */
public abstract class ControllerHandlerAdapter extends AbstractHandlerAdapter{
	public ControllerHandlerAdapter(){
		super();
	}
	public boolean isSupport(Object handler) {
		return handler.getClass().isAnnotationPresent(Controller.class);
	}
	
	public Object process(Object handler, HttpServletRequest request,
			HttpServletResponse response) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, OperationException {
		Method method=getMethod(handler,request);
		if(method==null){
			throw new OperationException("The Controller does not hava matched method");
		}
		Object[] parameters=getParameters(method,request,handler);
		if(parameters==null){
			throw new OperationException("The parameters of this method does not match the incoming parameters");
		}
		return method.invoke(handler, parameters);
	}
	
	
	protected abstract Method getMethod(Object handler, HttpServletRequest request);
	
	
	protected String getMethodUri(Object handler,String accessUri) {
		
		String ControllerUri=handler.getClass().getAnnotation(RequestMapping.class).value();
		String methodUri=URIUtil.sub(accessUri, ControllerUri);
		return methodUri;
	}
	
	protected Object[] getParameters(Method method, HttpServletRequest request,Object handler) {
		Object[] parameters=null;
		Map parameterMap=getInParameters(request,handler,method);
		if(parameterMap==null){
			return null;
		}
		Set parameterKey=parameterMap.keySet();
				
		String[] methodParameterNames=getMethodParameterNames(method);
		Class[] methodParameterTypes=method.getParameterTypes();
		if(isLegal(parameterKey,methodParameterNames)){
			parameters=initParameters(parameterMap,methodParameterNames,methodParameterTypes);//将传入的参数进行处理，得到方法可用参数
			
		}
		return parameters;
	}
	
	
	private Object[] initParameters(Map parameterMap,
			String[] methodParameterNames, Class[] methodParameterTypes) {
		Object[] parameters=new Object[methodParameterNames.length];
		for(int i=0;i<methodParameterNames.length;i++){
			String[] parameterArray=(String[]) parameterMap.get(methodParameterNames[i]);
			String parameter=parameterArray[0];
			Class parameterType=methodParameterTypes[i];
			if(parameterType==String.class){
				parameters[i]=parameter;
			}else{
				Adapter adapter=getAdapter(parameterType);
				parameters[i]=adapter.Switch(parameter);					
			}
		}
		return parameters;
	}

	

	
	protected abstract Map getInParameters(HttpServletRequest request,Object handler, Method method);

	private boolean isLegal(Set parameterKey, String[] methodParameterNames) {
		if(parameterKey.size()==methodParameterNames.length){
			for(int i=0;i<methodParameterNames.length;i++){
				String parameterName=methodParameterNames[i];
				if(!parameterKey.contains(parameterName)){
					return false; 
				}
				
			}			
		}
		return true;
	}

	
	
	
}
