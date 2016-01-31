package coolraw.util.switcher;

public class IntegerAdapter implements Adapter{

	public boolean support(Object parameterType) {
		return (parameterType==Integer.class);
		
	}

	public Object Switch(Object parameter) {
		return Integer.valueOf((String) parameter);
	}
	
}
