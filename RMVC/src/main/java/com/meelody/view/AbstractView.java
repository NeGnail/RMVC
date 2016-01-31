package coolraw.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public abstract class AbstractView implements View{
 
	protected final String path;
	protected final String jumpModel;
	
	public AbstractView(String path,String jumpModel){
		this.path=path;
		this.jumpModel=jumpModel;
	}
	public void jump(HttpServletRequest request, HttpServletResponse response) {
		if(jumpModel.equals("forward")){
			try {
				request.getRequestDispatcher(path).forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(jumpModel.equals("redirect")){
			try {
				response.sendRedirect(request.getContextPath()+path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
