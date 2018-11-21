package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class Spectator implements IClusterable {
	private static final long serialVersionUID = 1L;

	private Long users_id;
	private String name;
	private Byte status;
	private String primary_phone;
	private String email;
	private String first_name;
	private String middle_name;
	private String last_name;
	private String contact_number;
	private String contact_email;
	
	private String representative_name;

	public Spectator() {
	}

	@Override
	public String toString() {
		return "{user_id=" + users_id + ", name=" + name + ", primary_phone=" + primary_phone + ", emai=" + email + ", first_name=" + first_name
				+ ", middle_name=" + middle_name + ", last_name=" + last_name + ", contact_number=" + contact_number + ", contact_email="
				+ contact_email + "}";
	}

	public Long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Long users_id) {
		this.users_id = users_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	public String getContact_email() {
		return contact_email;
	}

	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}

	public String getPrimary_phone() {
		return primary_phone;
	}

	public void setPrimary_phone(String primary_phone) {
		this.primary_phone = primary_phone;
	}

	public String getRepresentative_name() {
		return representative_name;
	}

	public void setRepresentative_name(String representative_name) {
		this.representative_name = representative_name;
	}
}
