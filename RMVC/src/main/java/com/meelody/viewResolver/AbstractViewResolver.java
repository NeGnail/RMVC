package coolraw.viewResolver;

import coolraw.exception.OperationException;

public abstract class AbstractViewResolver implements ViewResolver{
	protected final String JSP=".jsp";
	protected void isLegalForjumpMode(String jumpMode) throws OperationException {
		if(!jumpMode.equals("forward")&&!jumpMode.equals("redirect")){
			throw new OperationException("Jump way is illegal");
		}
		
	}
}
