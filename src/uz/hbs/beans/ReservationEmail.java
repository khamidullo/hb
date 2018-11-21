package uz.hbs.beans;

import java.io.Serializable;
import java.util.Date;

import uz.hbs.enums.ReservationEmailStatus;
import uz.hbs.enums.ReservationEmailType;

public class ReservationEmail implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long reservations_id;
	private Long messages_id;
	private ReservationEmailType type;
	private ReservationEmailStatus status;
	private Date create_date;
	private Date update_date;

	public ReservationEmail() {
	}

	public Long getReservations_id() {
		return reservations_id;
	}

	public void setReservations_id(Long reservations_id) {
		this.reservations_id = reservations_id;
	}

	public Long getMessages_id() {
		return messages_id;
	}

	public void setMessages_id(Long messages_id) {
		this.messages_id = messages_id;
	}

	public ReservationEmailType getType() {
		return type;
	}

	public void setType(ReservationEmailType type) {
		this.type = type;
	}

	public ReservationEmailStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationEmailStatus status) {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
