package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class Bank implements IClusterable {
	private static final long serialVersionUID = 1L;

	public static final int TYPE_PRIMARY = 1;
	public static final int TYPE_SECONDARY = 2;

	private Long users_id;
	private String name;
	private String address;
	private String account_number;
	private String correspondent_account;
	private String identification_code;
	private IdAndValue type;

	public Bank() {
	}

	@Override
	public String toString() {
		return "{users_id=" + users_id + ", name=" + name + ", address=" + address + ", account_number=" + account_number
				+ ", correspondent_account=" + correspondent_account + ", identification_code=" + identification_code + ", type="
				+ (type != null ? type.getId() : null) + "}";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAccount_number() {
		return account_number;
	}

	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}

	public String getCorrespondent_account() {
		return correspondent_account;
	}

	public void setCorrespondent_account(String correspondent_account) {
		this.correspondent_account = correspondent_account;
	}

	public String getIdentification_code() {
		return identification_code;
	}

	public void setIdentification_code(String identification_code) {
		this.identification_code = identification_code;
	}

	public IdAndValue getType() {
		return type;
	}

	public void setType(IdAndValue type) {
		this.type = type;
	}

	public Long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Long users_id) {
		this.users_id = users_id;
	}
}
