package uz.hbs.beans;

public class PaymentMethod extends IdByteAndName {
	private static final long serialVersionUID = 1L;
	public static final byte OTHER = 0;
	public static final byte CASH = 1;
	public static final byte BANK_CARD_VISA = 2;
	public static final byte BANK_CARD_MASTER = 3;
	public static final byte BANK_TRANSFER = 4;
	
	public PaymentMethod() {
	}

	public PaymentMethod(byte id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public PaymentMethod(byte id) {
		this.id = id;
	}
}
