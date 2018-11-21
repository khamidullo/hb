package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class TourAgentAdditionalServiceGuest implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date create_date;
	private Date update_date;
	private long creator_user_id;
	private long guests_id;
	
	public TourAgentAdditionalServiceGuest() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public long getCreator_user_id() {
		return creator_user_id;
	}

	public void setCreator_user_id(long creator_user_id) {
		this.creator_user_id = creator_user_id;
	}

	public long getGuests_id() {
		return guests_id;
	}

	public void setGuests_id(long guests_id) {
		this.guests_id = guests_id;
	}
}
