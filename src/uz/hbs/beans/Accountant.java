package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class Accountant implements IClusterable {
	private static final long serialVersionUID = 1L;
	private long user_id;
	private String first_name;
	private String middle_name;
	private String last_name;
	private String contact_number;
	private String contact_email;

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
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

	@Override
	public String toString() {
		return "Accountant [user_id=" + user_id + ", first_name=" + first_name + ", middle_name=" + middle_name
				+ ", last_name=" + last_name + ", contact_number=" + contact_number + ", contact_email=" + contact_email
				+ "]";
	}
}