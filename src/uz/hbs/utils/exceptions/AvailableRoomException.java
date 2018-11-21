package uz.hbs.utils.exceptions;

public class AvailableRoomException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public AvailableRoomException() {
		this(null);
	}
	
	public AvailableRoomException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
