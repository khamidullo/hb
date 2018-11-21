package uz.hbs.beans.filters;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.ReservationType;

public class ReservationFilter implements IClusterable {
	private static final long serialVersionUID = 1L;
	private long hotelsusers_id;
	private String group_name;
	private String first_name;
	private String last_name;
	private Date check_in;
	private Date check_out;
	private String tour_agent;
	private ReservationType reservation_type;
	private ReservationStatus reservation_status;
	private Date date_from;
	private Date date_to;
	private Long id;
	
	public ReservationFilter(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}
	
	public ReservationFilter(ReservationDetail reserv) {
		this.id = reserv.getId();
		this.hotelsusers_id = reserv.getHotelsusers_id();
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

	public String getTour_agent() {
		return tour_agent;
	}

	public void setTour_agent(String tour_agent) {
		this.tour_agent = tour_agent;
	}
	
	public ReservationType getReservation_type() {
		return reservation_type;
	}

	public void setReservation_type(ReservationType reservation_type) {
		this.reservation_type = reservation_type;
	}

	public Date getDate_from() {
		return date_from;
	}

	public void setDate_from(Date date_from) {
		this.date_from = date_from;
	}

	public Date getDate_to() {
		return date_to;
	}

	public void setDate_to(Date date_to) {
		this.date_to = date_to;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public ReservationStatus getReservation_status() {
		return reservation_status;
	}

	public void setReservation_status(ReservationStatus reservation_status) {
		this.reservation_status = reservation_status;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
}
