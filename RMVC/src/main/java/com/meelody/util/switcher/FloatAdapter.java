package coolraw.util.switcher;

public class FloatAdapter implements Adapter {

	public boolean support(Object parameterType) {
		return parameterType==Float.class;
	}

	public Object Switch(Object parameter) {
		return Float.valueOf((String) parameter);
	}

}
