package uz.hbs.beans.filters;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.Nationality;

public class InsuranceFilter implements IClusterable {
	private static final long serialVersionUID = 1L;
	
	private String first_name;
	private String last_name;
	private String passport_number;
	private Nationality nationality;
	private long additionalserviceorders_id;
	
	private long creator_user_id;
	
	public InsuranceFilter(long creator_user_id, long additionalserviceorders_id) {
		this.creator_user_id = creator_user_id;
		this.additionalserviceorders_id = additionalserviceorders_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getPassport_number() {
		return passport_number;
	}

	public void setPassport_number(String passport_number) {
		this.passport_number = passport_number;
	}

	public Nationality getNationality() {
		return nationality;
	}

	public void setNationality(Nationality nationality) {
		this.nationality = nationality;
	}

	public long getCreator_user_id() {
		return creator_user_id;
	}

	public void setCreator_user_id(long creator_user_id) {
		this.creator_user_id = creator_user_id;
	}

	public long getAdditionalserviceorders_id() {
		return additionalserviceorders_id;
	}

	public void setAdditionalserviceorders_id(long additionalserviceorders_id) {
		this.additionalserviceorders_id = additionalserviceorders_id;
	}
}
