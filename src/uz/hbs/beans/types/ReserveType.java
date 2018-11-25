package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum ReserveType {
	INDIVIDUAL, GROUP;
	
	public static List<ReserveType> list() {
		return Arrays.asList(ReserveType.values());
	}
}
