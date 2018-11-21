package uz.hbs.beans.reports;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class HotelReport implements IClusterable {
	private static final long serialVersionUID = 1L;
	private String hotel;
	private String city;
	private Date regdate;
	private Date login;
	private Date putting_number;
	private Date manage_rate;
	private Short month_1;
	private Short month_3;
	private Short month_6;
	private Short month_12;
	private Short month;
	private Short total;
	private boolean tentative;
	
	public HotelReport() {
		// TODO Auto-generated constructor stub
	}

	public String getHotel() {
		return hotel;
	}

	public void setHotel(String hotel) {
		this.hotel = hotel;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public Date getLogin() {
		return login;
	}

	public void setLogin(Date login) {
		this.login = login;
	}

	public Date getPutting_number() {
		return putting_number;
	}

	public void setPutting_number(Date putting_number) {
		this.putting_number = putting_number;
	}

	public Date getManage_rate() {
		return manage_rate;
	}

	public void setManage_rate(Date manage_rate) {
		this.manage_rate = manage_rate;
	}

	public Short getMonth_1() {
		return month_1;
	}

	public void setMonth_1(Short month_1) {
		this.month_1 = month_1;
	}

	public Short getMonth_3() {
		return month_3;
	}

	public void setMonth_3(Short month_3) {
		this.month_3 = month_3;
	}

	public Short getMonth_6() {
		return month_6;
	}

	public void setMonth_6(Short month_6) {
		this.month_6 = month_6;
	}

	public Short getMonth_12() {
		return month_12;
	}

	public void setMonth_12(Short month_12) {
		this.month_12 = month_12;
	}

	public Short getMonth() {
		return month;
	}

	public void setMonth(Short month) {
		this.month = month;
	}

	public Short getTotal() {
		return total;
	}

	public void setTotal(Short total) {
		this.total = total;
	}

	public boolean isTentative() {
		return tentative;
	}

	public void setTentative(boolean tentative) {
		this.tentative = tentative;
	}
}
