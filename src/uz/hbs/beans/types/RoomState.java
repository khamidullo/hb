package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum RoomState {
	VACANT, OCCUPIED;
	
	public static List<RoomState> list() {
		return Arrays.asList(RoomState.values());
	}
}
