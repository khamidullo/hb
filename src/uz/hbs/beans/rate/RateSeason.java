package uz.hbs.beans.rate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

public class RateSeason implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final short FIRST_SEASON = 1;
	private Integer id;
	private Short season_number;
	private Date season_from;
	private Date season_to;
	private Integer rateplanes_id;
	private HashMap<Short, HashMap<Integer, List<RateDetails>>> details = new HashMap<Short, HashMap<Integer, List<RateDetails>>>();
	
	public RateSeason() {
	}
	
	public RateSeason(Integer rateplanes_id, Short season_number) {
		this.rateplanes_id = rateplanes_id;
		this.season_number = season_number;
		if (season_number == FIRST_SEASON) season_from = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getSeason_from() {
		return season_from;
	}

	public void setSeason_from(Date season_from) {
		this.season_from = season_from;
	}

	public Date getSeason_to() {
		return season_to;
	}

	public void setSeason_to(Date season_to) {
		this.season_to = season_to;
	}

	public Integer getRateplanes_id() {
		return rateplanes_id;
	}

	public void setRateplane_id(Integer rateplanes_id) {
		this.rateplanes_id = rateplanes_id;
	}

	public Short getSeason_number() {
		return season_number;
	}

	public void setSeason_number(Short season_number) {
		this.season_number = season_number;
	}

	public HashMap<Short, HashMap<Integer, List<RateDetails>>> getDetails() {
		return details;
	}

	public void setDetails(HashMap<Short, HashMap<Integer, List<RateDetails>>> details) {
		this.details = details;
	}
}
