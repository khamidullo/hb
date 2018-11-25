package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum PriceType {
	IN_PERCENT,FIXED_AMOUNT;
	
	public static List<PriceType> list() {
		return Arrays.asList(PriceType.values());
	}
}
