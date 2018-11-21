package uz.hbs.beans.filters;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.ReservationType;

public class ReserveManageFilter implements IClusterable{
	private static final long serialVersionUID = 1L;
	private Date datefrom;
	private Date dateto;
	private String fullname;
	
	
	private Long id;
	private Date create_date;
	private String touragent;
	private String hotel;
	private Date check_in;
	private Date check_out;
	private ReservationType type;
	private ReservationStatus status;
	private Double total;
	
	public ReserveManageFilter() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTouragent() {
		return touragent;
	}

	public void setTouragent(String touragent) {
		this.touragent = touragent;
	}

	public String getHotel() {
		return hotel;
	}

	public void setHotel(String hotel) {
		this.hotel = hotel;
	}

	public Date getCheck_in() {
		return check_in;
	}

	public void setCheck_in(Date check_in) {
		this.check_in = check_in;
	}

	public Date getCheck_out() {
		return check_out;
	}

	public void setCheck_out(Date check_out) {
		this.check_out = check_out;
	}

	public ReservationType getType() {
		return type;
	}

	public void setType(ReservationType type) {
		this.type = type;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Date getDatefrom() {
		return datefrom;
	}

	public void setDatefrom(Date datefrom) {
		this.datefrom = datefrom;
	}

	public Date getDateto() {
		return dateto;
	}

	public void setDateto(Date dateto) {
		this.dateto = dateto;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
}
