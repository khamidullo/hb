package uz.hbs.beans;

public class Gender extends IdByteAndName {
	private static final long serialVersionUID = 1L;
	public static final byte UNKNOWN = -1;
	public static final byte MALE = 0;
	public static final byte FEMALE = 1;
	
	public Gender() {
	}
	
	public Gender(byte id) {
		this.id = id;
	}
	
	public Gender(byte id, String name) {
		this.id = id;
		this.name = name;
	}
}
