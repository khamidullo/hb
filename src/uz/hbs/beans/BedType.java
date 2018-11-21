package uz.hbs.beans;

public class BedType extends IdByteAndName {
	private static final long serialVersionUID = 1L;
	public static final byte SINGLE = 1;
	public static final byte DOUBLE = 2;
	public static final byte TWIN = 3;
	public static final byte TRIPLE = 4;
	public static final byte QUADRUPLE = 5;
	
	private byte holding_capacity;
	
	public BedType() {
	}
	
	public BedType(byte id) {
		this.id = id;
		init_holding_capacity(id);
	}
	
	public BedType(byte id, String name) {
		this.id = id;
		this.name = name;
		init_holding_capacity(id);
	}

	public byte getHolding_capacity() {
		return holding_capacity;
	}

	public void setHolding_capacity(byte holding_capacity) {
		this.holding_capacity = holding_capacity;
	}
	
	private void init_holding_capacity(byte b) {
		if (b == SINGLE) 		 holding_capacity = 1;
		else if (b == DOUBLE) 	 holding_capacity = 2;
		else if (b == TWIN) 	 holding_capacity = 2;
		else if (b == TRIPLE) 	 holding_capacity = 3;
		else if (b == QUADRUPLE) holding_capacity = 4;
		else holding_capacity = (byte) (b - 1);
	}
}
