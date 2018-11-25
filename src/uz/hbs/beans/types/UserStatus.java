package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum UserStatus {
	NEW, ACTIVE, DISABLED, DELETED;
	
	public static List<UserStatus> list() {
		return Arrays.asList(UserStatus.values());
	}
}
