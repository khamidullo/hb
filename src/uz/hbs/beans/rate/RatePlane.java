package uz.hbs.beans.rate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

public class RatePlane implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String description;
	private boolean internal;
	private Long hotel_id;
	private Long initiator_user_id;
	private Date create_date;
	private Date update_date;
	
	private List<RateSeason> seasonlist = new ArrayList<RateSeason>();
	
	public RatePlane() {
	}
	
	public RatePlane(RatePlane plane) {
		this.id = plane.getId();
		this.internal = plane.isInternal();
	}
	
	public RatePlane(int id) {
		this.id = id;
	}

	public RatePlane(Long hotel_id) {
		this.hotel_id = hotel_id;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isInternal() {
		return internal;
	}

	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	public Long getHotelsusers_id() {
		return hotel_id;
	}

	public void setHotelsuser_id(Long hotel_id) {
		this.hotel_id = hotel_id;
	}

	public List<RateSeason> getSeasonlist() {
		return seasonlist;
	}

	public void setSeasonlist(List<RateSeason> seasonlist) {
		this.seasonlist = seasonlist;
	}
	
	public short getSeasonCount(){
		return (short) seasonlist.size();
	}
	
	public short nextSeason(){
		return (short) (seasonlist.size() + 1);
	}

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
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
}
