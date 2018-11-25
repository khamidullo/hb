package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum SaleState {
	UNDER_REPAIR, CAN_SALE;
	
	public static List<SaleState> list() {
		return Arrays.asList(SaleState.values());
	}
}
