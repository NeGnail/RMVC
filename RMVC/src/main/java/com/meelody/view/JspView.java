package coolraw.view;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 默认jsp
 * @author LiWei
 *
 */
public class JspView extends AbstractView{
	
	public JspView(String path, String jumpMode) {
		super(path, jumpMode);
	}

	public void render(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if(model!=null){
			Map<String,Object> data=model.getMap();
			for(Entry<String, Object> entry:data.entrySet()){
				String name=entry.getKey();
				Object value=entry.getValue();
				
				request.setAttribute(name, value);
				request.getSession().setAttribute(name, value);
				request.getSession().getServletContext().setAttribute(name, value);
			}			
		}
		
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof JspView){
			JspView view=(JspView) obj;
			if(this.path.equals(view.path)&&this.jumpModel.equals(view.jumpModel)){
				return true;
			}
		}
		return false;
	}
	
	

}
