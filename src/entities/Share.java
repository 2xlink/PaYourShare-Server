package entities;

import java.util.HashMap;
import java.util.Map;

public class Share {
	private Map<User, String> map = new HashMap<>();
	
	public Share(){}
	public Share(Map<User, String> map){
		setMap(map);
	}

	public Map<User, String> getMap() {
		return map;
	}

	public void setMap(Map<User, String> map) {
		this.map = map;
	}
}
