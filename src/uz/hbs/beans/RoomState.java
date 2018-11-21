package uz.hbs.beans;

public class RoomState extends IdByteAndName {
	private static final long serialVersionUID = 1L;
	public static final byte VACANT = 0;
	public static final byte OCCUPIED = 1;
	
	public RoomState() {
	}

	public RoomState(byte id) {
		this.id = id;
	}
	
	public RoomState(byte id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
