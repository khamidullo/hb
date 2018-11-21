package uz.hbs.beans.rate;

import java.math.BigDecimal;

import org.apache.wicket.util.io.IClusterable;

public class RateDetails implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final boolean INDIVIDUAL = false;
	public static final boolean GROUP = true;
	private Integer id;
	private Integer rateplaneseasons_id;
	private Short season_number;
	private BigDecimal rate;// = new BigDecimal("1");
	private BigDecimal rate_uz;// = new BigDecimal("1");
	private Integer roomtypes_id;
	private short person_number;
	private boolean is_group;
	private Integer rateplane_id;
	
	public RateDetails() {
	}
	
	public RateDetails(short person_number) {
		this.person_number = person_number;
	}
	
	public RateDetails(Short season_number, Integer roomtypes_id, short person_number, boolean is_group) {
		this.season_number = season_number;
		this.roomtypes_id = roomtypes_id;
		this.person_number = person_number;
		this.is_group = is_group;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getRoomtypes_id() {
		return roomtypes_id;
	}

	public void setRoomtypes_id(Integer roomtypes_id) {
		this.roomtypes_id = roomtypes_id;
	}

	public Short getSeason_number() {
		return season_number;
	}

	public void setSeason_number(Short season_number) {
		this.season_number = season_number;
	}

	public short getPerson_number() {
		return person_number;
	}

	public void setPerson_number(short person_number) {
		this.person_number = person_number;
	}

	public boolean isIs_group() {
		return is_group;
	}

	public void setIs_group(boolean is_group) {
		this.is_group = is_group;
	}
	
	@Override
	public String toString() {
		return is_group ? "Group" : "Individual"; 
	}

	public boolean isCorrect() {
		return rate != null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRateplaneseasons_id() {
		return rateplaneseasons_id;
	}

	public void setRateplaneseasons_id(Integer rateplaneseasons_id) {
		this.rateplaneseasons_id = rateplaneseasons_id;
	}
	
	public Integer getRateplane_id() {
		return rateplane_id;
	}

	public void setRateplane_id(Integer rateplane_id) {
		this.rateplane_id = rateplane_id;
	}

	public BigDecimal getRate(boolean resident) {
		return resident ? (rate_uz != null ? rate_uz : rate) : rate;
	}

	public BigDecimal getRate_uz() {
		return rate_uz;
	}

	public void setRate_uz(BigDecimal rate_uz) {
		this.rate_uz = rate_uz;
	}
}
