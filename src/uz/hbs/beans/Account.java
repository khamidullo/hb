package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.types.Status;

public class Account implements IClusterable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private long user_id;
	private Double balance;
	private Status status;
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

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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

	@Override
	public String toString() {
		return "Account [id=" + id + ", user_id=" + user_id + ", balance=" + balance + ", status=" + status
				+ ", create_date=" + create_date + ", update_date=" + update_date + "]";
	}
}