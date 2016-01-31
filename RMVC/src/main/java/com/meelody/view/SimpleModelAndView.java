package coolraw.view;

public class SimpleModelAndView implements ModelAndView{
	private Model model;
	private String viewPath;
	
	public SimpleModelAndView(){
		this.model=new Model();
	}
	public Model getModel() {
		return model;
	}

	public void addObject(String key,Object value) {
		this.model.addObject(key, value);
		
	}
	

	public void setViewPath(String viewPath) {
		this.viewPath=viewPath;
		
	}

	public String getViewPath() {
		return viewPath;
	}
	

}
