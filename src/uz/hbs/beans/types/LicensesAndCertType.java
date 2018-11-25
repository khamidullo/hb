package uz.hbs.beans.types;

import java.util.Arrays;
import java.util.List;

public enum LicensesAndCertType {
	LICENSE, CERTIFICATE;
	
	public static List<LicensesAndCertType> list() {
		return Arrays.asList(LicensesAndCertType.values());
	}
}
