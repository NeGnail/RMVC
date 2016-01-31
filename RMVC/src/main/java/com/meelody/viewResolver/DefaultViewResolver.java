package coolraw.viewResolver;

import coolraw.exception.OperationException;
import coolraw.view.AbstractView;
import coolraw.view.JspView;
import coolraw.view.View;

public class DefaultViewResolver extends AbstractViewResolver{
	private String prefix;
	private String suffix;

	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public View resolveView(String viewPath) throws OperationException {
		String jumpMode;
		String path;
		if(viewPath.contains(":")){
			int index=viewPath.indexOf(":");
			
			jumpMode=viewPath.substring(0,index);
			isLegalForjumpMode(jumpMode);
			path=this.prefix+viewPath.substring(index+1)+this.suffix;
		}else{
			jumpMode="forward";
			path=this.prefix+viewPath+this.suffix;
		}
		if(suffix.equals(JSP)){
			return new JspView(path, jumpMode);			
		}else{
			throw new OperationException("Type does not support this view");
		}
	}

	
}
