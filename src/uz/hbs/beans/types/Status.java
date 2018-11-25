package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum Status {
	NEW, ACTIVE, INACTIVE;
	
	public static List<Status> list() {
		return Arrays.asList(Status.values());
	}
}
