package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum CleanState {
	OTHER, CLEAN, UNCLEAN;
	
	public static List<CleanState> list(){
		return Arrays.asList(CleanState.values());
	}
}
