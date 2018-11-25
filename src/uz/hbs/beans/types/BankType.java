package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum BankType {
	PRIMARY, SECONDARY;
	
	public static List<BankType> list(){
		return Arrays.asList(BankType.values());
	}
}
