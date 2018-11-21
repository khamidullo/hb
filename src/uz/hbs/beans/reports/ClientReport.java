package uz.hbs.beans.reports;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class ClientReport implements IClusterable{
	private static final long serialVersionUID = 1L;
	private String company;
	private String manager;
	private Date regdate;
	private Short request_count;
	private Short reserve_count;
	private Short cancelled_count;
	private Float hotel_1;
	private Float service_1;
	private Float hotel_3;
	private Float service_3;
	private Float hotel_6;
	private Float service_6;
	private Float hotel_12;
	private Float service_12;
	private Float hotel;
	private Float service;
	private long touragent_id;
	
	public ClientReport() {
		// TODO Auto-generated constructor stub
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public Short getRequest_count() {
		return request_count;
	}

	public void setRequest_count(Short request_count) {
		this.request_count = request_count;
	}

	public Short getReserve_count() {
		return reserve_count;
	}

	public void setReserve_count(Short reserve_count) {
		this.reserve_count = reserve_count;
	}

	public Short getCancelled_count() {
		return cancelled_count;
	}

	public void setCancelled_count(Short cancelled_count) {
		this.cancelled_count = cancelled_count;
	}

	public Float getHotel_1() {
		return hotel_1;
	}

	public void setHotel_1(Float hotel_1) {
		this.hotel_1 = hotel_1;
	}

	public Float getService_1() {
		return service_1;
	}

	public void setService_1(Float service_1) {
		this.service_1 = service_1;
	}

	public Float getHotel_3() {
		return hotel_3;
	}

	public void setHotel_3(Float hotel_3) {
		this.hotel_3 = hotel_3;
	}

	public Float getService_3() {
		return service_3;
	}

	public void setService_3(Float service_3) {
		this.service_3 = service_3;
	}

	public Float getHotel_6() {
		return hotel_6;
	}

	public void setHotel_6(Float hotel_6) {
		this.hotel_6 = hotel_6;
	}

	public Float getService_6() {
		return service_6;
	}

	public void setService_6(Float service_6) {
		this.service_6 = service_6;
	}

	public Float getHotel_12() {
		return hotel_12;
	}

	public void setHotel_12(Float hotel_12) {
		this.hotel_12 = hotel_12;
	}

	public Float getService_12() {
		return service_12;
	}

	public void setService_12(Float service_12) {
		this.service_12 = service_12;
	}

	public Float getHotel() {
		return hotel;
	}

	public void setHotel(Float hotel) {
		this.hotel = hotel;
	}

	public Float getService() {
		return service;
	}

	public void setService(Float service) {
		this.service = service;
	}

	public double getTotal(double d, double d1) {
		return (d1 * 100) / d;
	}

	public long getTouragent_id() {
		return touragent_id;
	}

	public void setTouragent_id(long touragent_id) {
		this.touragent_id = touragent_id;
	}
}
