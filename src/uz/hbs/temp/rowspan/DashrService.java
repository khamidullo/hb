package uz.hbs.temp.rowspan;

import java.util.ArrayList;
import java.util.List;

public class DashrService {
	private static DashrService instance = new DashrService();

	private DashrService() {
	}

	public static DashrService getInstance() {
		if (instance == null) {
			instance = new DashrService();
		}
		return instance;
	}

	public List<Space> getSpace() {
		List<Space> spaces = new ArrayList<Space>();
		
		Space s1 = new Space("Space One");
		s1.addStateCount("Assigned", 5);
		s1.addStateCount("In progress", 4);
		s1.addStateCount("Closed", 7);
		spaces.add(s1);

		Space s2 = new Space("Space Two");
		s2.addStateCount("Open", 3);
		s2.addStateCount("Closed", 2);
		spaces.add(s2);
		
		return spaces;
	}
}
