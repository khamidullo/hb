package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum BedType {
	SINGLE,	DOUBLE, TWIN, TRIPLE, QUADRUPLE;
	
	public static List<BedType> list() {
		return Arrays.asList(BedType.values());
	}
	
	public static byte holdingCapacity(BedType bedType) {
		switch (bedType) {
			case SINGLE: return 1;
			case DOUBLE: 
			case TWIN: return 2;
			case TRIPLE: return 3;
			default: return 4;
		}
	}
}
