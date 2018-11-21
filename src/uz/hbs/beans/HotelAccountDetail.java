package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class HotelAccountDetail implements IClusterable {
	private static final long serialVersionUID = 1L;
	private String manager;
	private String manager_email;
	private String corporeate_email;
	private String legal_name;
	private String primary_phone;
	private String secondary_phone;
	private Date create_date;

	public HotelAccountDetail() {
	}

	@Override
	public String toString() {
		return "{manager=" + manager + ", manager_email=" + manager_email + ", corporate_email=" + corporeate_email + ", legal_name=" + legal_name
				+ ", primary_phone=" + primary_phone + ", secondary_phone=" + secondary_phone + "}";
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManager_email() {
		return manager_email;
	}

	public void setManager_email(String manager_email) {
		this.manager_email = manager_email;
	}

	public String getCorporeate_email() {
		return corporeate_email;
	}

	public void setCorporeate_email(String corporeate_email) {
		this.corporeate_email = corporeate_email;
	}

	public String getLegal_name() {
		return legal_name;
	}

	public void setLegal_name(String legal_name) {
		this.legal_name = legal_name;
	}

	public String getPrimary_phone() {
		return primary_phone;
	}

	public void setPrimary_phone(String primary_phone) {
		this.primary_phone = primary_phone;
	}

	public String getSecondary_phone() {
		return secondary_phone;
	}

	public void setSecondary_phone(String secondary_phone) {
		this.secondary_phone = secondary_phone;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
}
