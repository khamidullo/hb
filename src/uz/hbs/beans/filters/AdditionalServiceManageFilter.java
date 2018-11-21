package uz.hbs.beans.filters;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.ServiceType;

public class AdditionalServiceManageFilter implements IClusterable{
	private static final long serialVersionUID = 1L;
	private Date datefrom;
	private Date dateto;
	private String fullname;
	
	private Long id;
	private ServiceType service_type;
	private String touragent;
	private Date create_date;
	private Date arrival_date;
	private String guest;
	private Double total;
	
	public AdditionalServiceManageFilter() {
		// TODO Auto-generated constructor stub
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ServiceType getService_type() {
		return service_type;
	}

	public void setService_type(ServiceType service_type) {
		this.service_type = service_type;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
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
}
