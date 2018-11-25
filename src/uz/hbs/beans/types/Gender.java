package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum Gender {
	MALE,FEMALE;
	
	public static List<Gender> list(){
		return Arrays.asList(Gender.values());
	}
}
