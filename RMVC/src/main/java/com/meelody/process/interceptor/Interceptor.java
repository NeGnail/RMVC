package coolraw.process.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coolraw.view.ModelAndView;

public interface Interceptor {

	boolean preHandle(HttpServletRequest request, HttpServletResponse response);

	void postHandle(HttpServletRequest request, HttpServletResponse response,
			ModelAndView modelAndView);

	void afterHandle(HttpServletRequest request, HttpServletResponse response);
	
}
