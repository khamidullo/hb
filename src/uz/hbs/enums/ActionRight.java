package uz.hbs.enums;

public enum ActionRight {
	UNKNOWN,
	// Used for audit logging
	SIGN_IN,
	// Admin user rights
	USER_LIST,
	USER_ADD,
	USER_VIEW,
	USER_EDIT,
	USER_DELETE,
	HOTEL_LIST,
	HOTEL_ADD,
	HOTEL_VIEW,
	HOTEL_EDIT,
	HOTEL_DELETE,
	HOTEL_MANAGE_RATE,
	HOTEL_MANAGE_ROOM_FOR_SALE,
	TOURAGENCY_LIST,
	TOURAGENCY_ADD,
	TOURAGENCY_VIEW,
	TOURAGENCY_EDIT,
	TOURAGENCY_DELETE,
	SPECTATOR_LIST,
	SPECTATOR_ADD,
	SPECTATOR_VIEW,
	SPECTATOR_EDIT,
	SPECTATOR_DELETE,
	API_USER_LIST,
	API_USER_ADD,
	API_USER_VIEW,
	API_USER_EDIT,
	API_USER_DELETE,
	ROLE_LIST,
	ROLE_ADD,
	ROLE_VIEW,
	ROLE_EDIT,
	ROLE_DELETE,
	ROLE_ASSIGN,
	// Hotel user rights
	HOTEL_USER_REPORTS,
	HOTEL_USER_ROOM_AND_RATE_MANAGEMENT,
	// Spectator user rights
	SPECTATOR_USER_REPORTS,
	// Touragent user rights
	TA_USER_REPORTS
	;

	public static ActionRight getByName(String name) {
		ActionRight[] values = values();
		for (ActionRight actionRight : values) {
			if (actionRight.name().equals(name)) {
				return actionRight;
			}
		}
		return UNKNOWN;
	}
}
