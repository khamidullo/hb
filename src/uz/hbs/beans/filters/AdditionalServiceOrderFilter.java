package uz.hbs.beans.filters;

import org.apache.wicket.util.io.IClusterable;

public class AdditionalServiceOrderFilter implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long creator_user_id;
	private Long id;
	private Long reservations_id;
	private Long touragent_id;
	
	public AdditionalServiceOrderFilter(long creator_user_id) {
		this.creator_user_id = creator_user_id;
	}
	
	public AdditionalServiceOrderFilter(long touragent_id, boolean bool) {
		this.touragent_id = touragent_id;
	}

	public Long getCreator_user_id() {
		return creator_user_id;
	}

	public void setCreator_user_id(Long creator_user_id) {
		this.creator_user_id = creator_user_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReservations_id() {
		return reservations_id;
	}

	public void setReservations_id(Long reservations_id) {
		this.reservations_id = reservations_id;
	}

	public Long getTouragent_id() {
		return touragent_id;
	}

	public void setTouragent_id(Long touragent_id) {
		this.touragent_id = touragent_id;
	}
}
