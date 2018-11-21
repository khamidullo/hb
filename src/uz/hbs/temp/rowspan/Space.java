package uz.hbs.temp.rowspan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Space implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Map<String, Integer> stateCountMap;

	public Space(String name) {
		this.name = name;
	}

	public void addStateCount(String state, int count) {
		if (stateCountMap == null) {
			stateCountMap = new LinkedHashMap<String, Integer>();
		}
		stateCountMap.put(state, count);
	}

	public String getName() {
		return name;
	}

	public int getTotalCount() {
		int total = 0;
		for (int i : stateCountMap.values()) {
			total += i;
		}
		return total;
	}

	public int getCountForState(String name) {
		return stateCountMap.get(name);
	}

	public List<String> getStates() {
		return new ArrayList<String>(stateCountMap.keySet());
	}
}
