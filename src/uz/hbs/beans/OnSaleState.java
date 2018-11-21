package uz.hbs.beans;

public class OnSaleState extends IdByteAndName {
	private static final long serialVersionUID = 1L;
	public static final byte UNDER_REPAIR = 0;
	public static final byte CAN_SALE = 1;
	
	public OnSaleState() {
		
	}
	
	public OnSaleState(OnSaleState onsale) {
		this.id = onsale.getId();
	}
	
	public OnSaleState(byte id) {
		this.id = id;
	}
	
	public OnSaleState(byte id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
