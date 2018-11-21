package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class AdditionalServiceManage implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final byte ARRIVAL_AIR_TAXI = 1;
	public static final byte ARRIVAL_TRAIN_TAXI = 2;
	public static final byte DEPARTURE_AIR_TAXI = 3;
	public static final byte DEPARTURE_TRAIN_TAXI = 4;
	public static final byte ARRIVAL_AIR_GREEN_HALL = 5;
	public static final byte ARRIVAL_AIR_CIP_HALL = 6;
	public static final byte DEPARTURE_AIR_GREEN_HALL = 7;
	public static final byte DEPARTURE_AIR_CIP_HALL = 8;
	public static final byte INSURANCE = 9;
	
	private long id;
	private String servicetype;
	private String touragent;
	private Date create_date;
	private Date arrival_date;
	private Date departure_date;
	private String guest;
	private Double total;
	private byte status;
	
	public AdditionalServiceManage() {
		// TODO Auto-generated constructor stub
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getArrival_date() {
		return arrival_date;
	}

	public void setArrival_date(Date arrival_date) {
		this.arrival_date = arrival_date;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getTouragent() {
		return touragent;
	}

	public void setTouragent(String touragent) {
		this.touragent = touragent;
	}

	public String getServicetype() {
		return servicetype;
	}

	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Date getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(Date departure_date) {
		this.departure_date = departure_date;
	}
}
