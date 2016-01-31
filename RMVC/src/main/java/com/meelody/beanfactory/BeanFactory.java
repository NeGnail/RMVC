package coolraw.beanfactory;

import java.util.List;

import coolraw.process.interceptor.Interceptor;
/**
 * 
 * @author LiWei
 *
 */
public interface BeanFactory {
	void init();
	Object getBean(String name);
	List<Object> getControllers();
	List<Interceptor> getInterceptor();
}
