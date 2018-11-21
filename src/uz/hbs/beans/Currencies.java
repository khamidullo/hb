package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.MyWebApplication;
import uz.hbs.utils.DateUtil;

public class Currencies implements IClusterable {
	private static final long serialVersionUID = 1L;

	public static final byte STATUS_NEW = 0;
	public static final byte STATUS_ACTIVE = 1;
	public static final byte STATUS_DISABLED = 2;

	public static final int ID_UZS = 1;

	private Integer id;
	private String name;
	private Double value;
	private Boolean is_default;
	private Byte status;
	private Date create_date;
	private Date update_date;
	private Date history_date;

	public Currencies() {
	}

	@Override
	public String toString() {
		return "{id=" + id + ", name=" + name + ", value=" + value + ", is_default=" + is_default + ", status=" + status + ", create_date="
				+ DateUtil.toString(create_date, MyWebApplication.DATE_TIME_FORMAT) + ", update_date="
				+ DateUtil.toString(update_date, MyWebApplication.DATE_TIME_FORMAT) + "}";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
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

	public Date getHistory_date() {
		return history_date;
	}

	public void setHistory_date(Date history_date) {
		this.history_date = history_date;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Boolean getIs_default() {
		return is_default;
	}

	public void setIs_default(Boolean is_default) {
		this.is_default = is_default;
	}
}
