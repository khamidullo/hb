package uz.hbs.beans;

public class PriceType extends IdByteAndName {
	private static final long serialVersionUID = 1L;
	public static final byte IN_PERCENT = 0;
	public static final byte FIXED_AMOUNT = 1;
	
	public PriceType() {
	}
	
	public PriceType(byte id) {
		this.id = id;
	}
	
	public PriceType(byte id, String name) {
		this.id = id;
		this.name = name;
	}
}
