package coolraw.view;

import java.util.HashMap;
import java.util.Map;

public class Model {
	private Map<String,Object> map=new HashMap<String,Object>();

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public void addObject(String key,Object value){
		this.map.put(key, value);
	}
	
}
