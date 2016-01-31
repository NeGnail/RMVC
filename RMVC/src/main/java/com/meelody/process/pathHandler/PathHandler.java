package coolraw.process.pathHandler;

import java.util.Set;

public interface PathHandler {
	public void addHandler(String uri,Object handler);
	public Object getHandler(String uri);
	public Set<String> getPathHandlerUri();
}
