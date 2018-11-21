package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class Account implements IClusterable {
	private static final long serialVersionUID = 1L;
	
	public static final Byte STATUS_NEW = 0;
	public static final Byte STATUS_ACTIVE = 1;
	public static final Byte STATUS_DISABLED = 2;

	private Long id;
	private Long users_id;
	private Double balance;
	private Byte status;
	private Date create_date;
	private Date update_date;

	public Account() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Long users_id) {
		this.users_id = users_id;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
}
