package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.types.BankType;

public class Bank implements IClusterable {
	private static final long serialVersionUID = 1L;

	private Long user_id;
	private String name;
	private String address;
	private String account_number;
	private String correspondent_account;
	private String identification_code;
	private BankType type;

	public Bank() {
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
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

	public BankType getType() {
		return type;
	}

	public void setType(BankType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Bank [user_id=" + user_id + ", name=" + name + ", address=" + address + ", account_number="
				+ account_number + ", correspondent_account=" + correspondent_account + ", identification_code="
				+ identification_code + ", type=" + type + "]";
	}
}