package uz.hbs.beans;

public class ReservationStatus extends IdByteAndName {
	private static final long serialVersionUID = 1L;
	public static final byte RESERVED = 0;
	public static final byte CHECKED_IN = 1;
	public static final byte CANCELLED = 2;
	public static final byte NO_SHOW = 3;
	public static final byte CHECKED_OUT = 4;
	
	public ReservationStatus() {
	}
	
	public ReservationStatus(byte id) {
		this.id = id;
	}
	
	public ReservationStatus(byte id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
