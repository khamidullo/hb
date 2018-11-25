package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum PaymentMethod {
	CASH,BANK_CARD_VISA,BANK_CARD_MASTER,BANK_TRANSFER,OTHER;
	
	public static List<PaymentMethod> list() {
		return Arrays.asList(PaymentMethod.values());
	}
}
