package coolraw.process.pathHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerPathHandler implements PathHandler{
	private final Map<String,Object> handlers=new HashMap<String,Object>();

	public ControllerPathHandler() {
		
	} 
	public void addHandler(String uri,Object handler){
		this.handlers.put(uri, handler);
	}
	public Object getHandler(String uri) {
		return handlers.get(uri);
	}
	public Set<String> getPathHandlerUri() {
		return this.handlers.keySet();
	}
	
}
