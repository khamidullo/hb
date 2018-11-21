package uz.hbs.beans;

public class HoldingCapacity extends IdByteAndName {
	private static final long serialVersionUID = 1L;
	public static final byte HOLDING_CAPACITY_1 = 1;
	public static final byte HOLDING_CAPACITY_2 = 2;
	public static final byte HOLDING_CAPACITY_3 = 3;
	public static final byte HOLDING_CAPACITY_4 = 4;
	public static final byte HOLDING_CAPACITY_5 = 5;
	public static final byte HOLDING_CAPACITY_6 = 6;
//	public static final byte HOLDING_CAPACITY_MORE = 5;

	public HoldingCapacity() {
		super();
	}
	
	public HoldingCapacity(byte id) {
		super();
		this.id = id;
	}

	public HoldingCapacity(byte id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
}
