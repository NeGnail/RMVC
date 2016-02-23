package coolraw.decoration;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


import coolraw.annotation.RequestMapping;
import coolraw.util.URIUtil;
/**
 * 
 * @author LiWei 
 *
 */
public class LocalRequest extends HttpServletRequestWrapper{
	private HttpServletRequest request;

	private String contextPath;
	private String requestURI;
	private Map parameterMap;
	private ThreadLocal<String> threadLocalRequestURI=new ThreadLocal<String>();
	
	public LocalRequest(HttpServletRequest request) {
		super(request);
		this.request=request;
		init();
	}
	
	private void init(){
		this.contextPath=this.request.getContextPath();
		this.requestURI=this.request.getRequestURI();
		this.parameterMap=this.request.getParameterMap();
	}
	@Override
	public String getContextPath() {
		return this.contextPath;
	}

	@Override
	public String getRequestURI() {
		return this.threadLocalRequestURI.get();
	}

	@Override
	public Map getParameterMap() {
		return this.parameterMap;
	}
	
	
	public void decorateRequestURI(Object handler) {
		String requestUri=null;
		
		String contextPath=this.getContextPath();
		String controllerUri=handler.getClass().getAnnotation(RequestMapping.class).value();
		String requstUri=this.requestURI;
		String accessUri=requstUri.substring(contextPath.length());
		String[] uris= URIUtil.resolveConcurrentURI(accessUri);
		
		for(String uri:uris){
			if(URIUtil.isMatch(controllerUri, uri)){//有可能出现/a/b/c和/a/b/d两个handler相同的路径，这种情况下任意一个handler都可以
				requestUri=contextPath+uri;
			}
		}
		
		this.threadLocalRequestURI.set(requestUri);
	}
}
