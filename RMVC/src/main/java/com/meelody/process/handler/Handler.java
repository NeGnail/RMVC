package coolraw.process.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {
	public Object process(HttpServletRequest request,HttpServletResponse response);
}
