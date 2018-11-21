package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class Insurance implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date create_date;
	private Date update_date;
	private Date birth_date;
	private byte status;
	private long creator_user_id;
	private Long additionalserviceorders_id;
	private String first_name;
	private String last_name;
	private Nationality nationality;
	private String passport_number;
	private Date passport_issue_date;
	private Date period_from_date;
	private Date period_to_date;
	private Long initiator_user_id;
	private boolean with_hotel = true;
	private Integer email_sequence;

	public Insurance() {
	}

	public Insurance(long creator_user_id) {
		this.creator_user_id = creator_user_id;
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

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public long getCreator_user_id() {
		return creator_user_id;
	}

	public void setCreator_user_id(long creator_user_id) {
		this.creator_user_id = creator_user_id;
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

	public Nationality getNationality() {
		return nationality;
	}

	public void setNationality(Nationality nationality) {
		this.nationality = nationality;
	}

	public String getPassport_number() {
		return passport_number;
	}

	public void setPassport_number(String passport_number) {
		this.passport_number = passport_number;
	}

	public Date getPassport_issue_date() {
		return passport_issue_date;
	}

	public void setPassport_issue_date(Date passport_issue_date) {
		this.passport_issue_date = passport_issue_date;
	}

	public Date getPeriod_from_date() {
		return period_from_date;
	}

	public void setPeriod_from_date(Date period_from_date) {
		this.period_from_date = period_from_date;
	}

	public Date getPeriod_to_date() {
		return period_to_date;
	}

	public void setPeriod_to_date(Date period_to_date) {
		this.period_to_date = period_to_date;
	}

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public Date getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(Date birth_date) {
		this.birth_date = birth_date;
	}

	public boolean isWith_hotel() {
		return with_hotel;
	}

	public void setWith_hotel(boolean with_hotel) {
		this.with_hotel = with_hotel;
	}

	public Long getAdditionalserviceorders_id() {
		return additionalserviceorders_id;
	}

	public void setAdditionalserviceorders_id(Long additionalserviceorders_id) {
		this.additionalserviceorders_id = additionalserviceorders_id;
	}

	public short getDays() {
		return ((short) (((period_to_date.getTime() - period_from_date.getTime()) / (24 * 60 * 60 * 1000)) + 1));
	}

	public String getFullName() {
		return first_name + " " + last_name;
	}

	public Integer getEmail_sequence() {
		return email_sequence;
	}

	public void setEmail_sequence(Integer email_sequence) {
		this.email_sequence = email_sequence;
	}
}
