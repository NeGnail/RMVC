package coolraw.viewResolver;

import coolraw.exception.OperationException;
import coolraw.view.View;

public interface ViewResolver {
	View resolveView(String viewPath) throws OperationException;
}
