package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum MealType {
	BREAKFAST, LUNCH, DINNER;
	
	public static List<MealType> list(){
		return Arrays.asList(MealType.values());
	}
}