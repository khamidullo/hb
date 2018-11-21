package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class ReservationCancellationPolicy implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final int NoPenaltyOnCancelation = 0;
	
	private long id;
	private IdAndValue no_penalty_before_days;
	private IdAndValue late_cancel_penalty;
	private IdAndValue no_show_penalty;
	private IdAndValue notify_ta_before_days;
    private boolean support_tentative_reservation;
    private boolean support_tentative_reservation_warn;
    
    private boolean no_show_penalty_first_night;
    private boolean late_cancel_penalty_first_night;
	
	public ReservationCancellationPolicy() {
		support_tentative_reservation = false;
		support_tentative_reservation_warn = false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public IdAndValue getNo_penalty_before_days() {
		return no_penalty_before_days;
	}

	public void setNo_penalty_before_days(IdAndValue no_penalty_before_days) {
		this.no_penalty_before_days = no_penalty_before_days;
	}

	public IdAndValue getLate_cancel_penalty() {
		return late_cancel_penalty;
	}

	public void setLate_cancel_penalty(IdAndValue late_cancel_penalty) {
		this.late_cancel_penalty = late_cancel_penalty;
	}

	public IdAndValue getNo_show_penalty() {
		return no_show_penalty;
	}

	public void setNo_show_penalty(IdAndValue no_show_penalty) {
		this.no_show_penalty = no_show_penalty;
	}

	public IdAndValue getNotify_ta_before_days() {
		return notify_ta_before_days;
	}

	public void setNotify_ta_before_days(IdAndValue notify_ta_before_days) {
		this.notify_ta_before_days = notify_ta_before_days;
	}

	public boolean isSupport_tentative_reservation() {
		return support_tentative_reservation;
	}

	public void setSupport_tentative_reservation(
			boolean support_tentative_reservation) {
		this.support_tentative_reservation = support_tentative_reservation;
	}

	public boolean isSupport_tentative_reservation_warn() {
		return support_tentative_reservation_warn;
	}

	public void setSupport_tentative_reservation_warn(
			boolean support_tentative_reservation_warn) {
		this.support_tentative_reservation_warn = support_tentative_reservation_warn;
	}

	public boolean isNo_show_penalty_first_night() {
		return no_show_penalty_first_night;
	}

	public void setNo_show_penalty_first_night(boolean no_show_penalty_first_night) {
		this.no_show_penalty_first_night = no_show_penalty_first_night;
	}

	public boolean isLate_cancel_penalty_first_night() {
		return late_cancel_penalty_first_night;
	}

	public void setLate_cancel_penalty_first_night(boolean late_cancel_penalty_first_night) {
		this.late_cancel_penalty_first_night = late_cancel_penalty_first_night;
	}
	
	public boolean isNoPenaltyOnCancelation(){
		return no_penalty_before_days != null && no_penalty_before_days.getId() == ReservationCancellationPolicy.NoPenaltyOnCancelation;
	}
}
