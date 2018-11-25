package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum ExceedType {
	NUMBER_OF_GUESTS,NUMBER_OF_ROOMS;
	
	public static List<ExceedType> list() {
		return Arrays.asList(ExceedType.values());
	}
}
