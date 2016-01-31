package coolraw.process.handlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

import coolraw.util.switcher.Adapter;
import coolraw.util.switcher.DoubleAdapter;
import coolraw.util.switcher.FloatAdapter;
import coolraw.util.switcher.IntegerAdapter;

public abstract class AbstractHandlerAdapter implements HandlerAdapter{
	private final Log log=LogFactory.getLog(getClass());
	private List<Adapter> switcherAdapters=new ArrayList<Adapter>();
	public AbstractHandlerAdapter(){
		switcherAdapters.add(new DoubleAdapter());
		switcherAdapters.add(new FloatAdapter());
		switcherAdapters.add(new IntegerAdapter());
	}
	
	

	protected Adapter getAdapter(Class parameterType) {
		for(Adapter adapter:switcherAdapters){
			if(adapter.support(parameterType)){
				return adapter;
			}
		}
		return null;
	}
	
	/**
	 * 得到方法参数名
	 * @param method
	 * @return
	 */
	protected String[] getMethodParameterNames(Method method) {
		String[] paramNames=null;
		ClassPool pool=ClassPool.getDefault();
		 
		  
		try {
			Class clazz=method.getDeclaringClass();
			pool.insertClassPath(new ClassClassPath(clazz)); 
			CtClass cc=pool.get(clazz.getName());
			CtMethod cm=cc.getDeclaredMethod(method.getName());
			
			MethodInfo methodInfo=cm.getMethodInfo();
			CodeAttribute codeAttribute=methodInfo.getCodeAttribute();
			LocalVariableAttribute attr=(LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
			if(attr!=null){
				paramNames=new String[cm.getParameterTypes().length];
				int pos=Modifier.isStatic(cm.getModifiers())?0:1;
				for(int i=0;i<paramNames.length;i++){
					paramNames[i]=attr.variableName(i+pos);
				}
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paramNames;
		
	}

}
