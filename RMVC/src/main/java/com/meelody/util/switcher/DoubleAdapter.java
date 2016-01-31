package coolraw.util.switcher;

public class DoubleAdapter implements Adapter {

	public boolean support(Object parameterType) {
		return parameterType==Double.class ;
	}

	public Object Switch(Object parameter) {
		return Double.valueOf((String) parameter);
	}
	
}
