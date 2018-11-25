package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum UserType {
	TOURAGENT, HOTEL_USER, ADMIN, SPECTATOR, TOURAGENCY, API, HOTEL;
	
	public static List<UserType> list() {
		return Arrays.asList(UserType.values());
	}
}
