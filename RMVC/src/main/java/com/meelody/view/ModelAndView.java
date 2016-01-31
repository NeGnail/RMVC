package coolraw.view;

public interface ModelAndView {
	Model getModel();
	public void addObject(String key,Object value);
	void setViewPath(String viewPath);
	String getViewPath();
}
