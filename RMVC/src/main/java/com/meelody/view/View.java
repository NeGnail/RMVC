package coolraw.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
	void render(Model model, HttpServletRequest request,
			HttpServletResponse response);
	void jump(HttpServletRequest request,
			HttpServletResponse response);
}
