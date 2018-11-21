package uz.hbs.beans;

public class ReservationType extends IdByteAndName {
	private static final long serialVersionUID = 1L;
	public static final byte DEFINITE = 1;
	public static final byte TENTATIVE = 2;
	
	public ReservationType() {
	}
	
	public ReservationType(byte id) {
		this.id = id;
	}

	public ReservationType(byte id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
