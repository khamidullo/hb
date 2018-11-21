package uz.hbs.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.Booking.RoomsCount;

public class BookingSearchResult implements IClusterable {
	private static final long serialVersionUID = 1L;

	private Long hotelsusers_id;
	private String display_name;
	private Integer stars;
	private String region_name;
	private String city;
	private Double longitude;
	private Double latitude;
	private Short holding_capacity;
	private Boolean additional_bed;
	private String roomtypes_name;
	private Integer roomtypes_id;
	private Byte meal_type;
	private Boolean meal_option_included_to_room_rate;
	private Float room_area;
	private Short rooms_count;
	private Boolean free_cancelation;
	private Double room_rate;
	private Double extra_bed_cost;
	private Long rooms_id;
	private List<Equipment> room_equipments;
	private boolean selected;
	private String address;
	private Integer no_penalty_before_days;

	private String holding_capacity_text;
	private Boolean is_group;
	private Integer room_index;
	private Long prev_room_id;
	private int rownum;
	private short guests;
	private String childAges; 
	private RoomsCount roomsCount;
	private List<RoomType> roomtypeslist = new ArrayList<RoomType>();
	private short bedType;
	
	public BookingSearchResult() {
	}

	@Override
	public String toString() {
		return "{hotelsusers_id=" + hotelsusers_id + ", display_name=" + display_name + ", stars=" + stars + ", region_name=" + region_name
				+ ", longitude=" + longitude + ", latitude=" + latitude + ", holding_capacity=" + holding_capacity + ", additional_bed="
				+ additional_bed + ", roomtypes_name=" + roomtypes_name + ", roomtype_id=" + roomtypes_id + ", meal_type=" + meal_type
				+ ", meal_option_included_to_room_rate=" + meal_option_included_to_room_rate + ", room_area=" + room_area + ", rooms_count="
				+ rooms_count + ", room_rate=" + room_rate + ", room_equipments=" + (room_equipments == null ? 0 : room_equipments.size())
				+ ", reserve type=" + is_group
				+ ", selected=" + selected + ", address=" + address + ", no_penalty_before_days=" + no_penalty_before_days + "}";
	}

	public Long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(Long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Short getHolding_capacity() {
		return holding_capacity;
	}

	public void setHolding_capacity(Short holding_capacity) {
		this.holding_capacity = holding_capacity;
	}

	public Boolean getAdditional_bed() {
		return additional_bed;
	}

	public void setAdditional_bed(Boolean additional_bed) {
		this.additional_bed = additional_bed;
	}

	public String getRoomtypes_name() {
		return roomtypes_name;
	}

	public void setRoomtypes_name(String roomtypes_name) {
		this.roomtypes_name = roomtypes_name;
	}

	public Byte getMeal_type() {
		return meal_type;
	}

	public void setMeal_type(Byte meal_type) {
		this.meal_type = meal_type;
	}

	public Boolean getMeal_option_included_to_room_rate() {
		return meal_option_included_to_room_rate;
	}

	public void setMeal_option_included_to_room_rate(Boolean meal_option_included_to_room_rate) {
		this.meal_option_included_to_room_rate = meal_option_included_to_room_rate;
	}

	public Float getRoom_area() {
		return room_area;
	}

	public void setRoom_area(Float room_area) {
		this.room_area = room_area;
	}

	public Short getRooms_count() {
		return rooms_count;
	}

	public void setRooms_count(Short rooms_count) {
		this.rooms_count = rooms_count;
	}

	public List<Equipment> getRoom_equipments() {
		return room_equipments;
	}

	public void setRoom_equipments(List<Equipment> room_equipments) {
		this.room_equipments = room_equipments;
	}

	public Boolean getFree_cancelation() {
		return free_cancelation;
	}

	public void setFree_cancelation(Boolean free_cancelation) {
		this.free_cancelation = free_cancelation;
	}

	public Integer getRoomtypes_id() {
		return roomtypes_id;
	}

	public void setRoomtypes_id(Integer roomtypes_id) {
		this.roomtypes_id = roomtypes_id;
	}

	public Double getRoom_rate() {
		return room_rate;
	}

	public void setRoom_rate(Double room_rate) {
		this.room_rate = room_rate;
	}

	public Long getRooms_id() {
		return rooms_id;
	}

	public void setRooms_id(Long rooms_id) {
		this.rooms_id = rooms_id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getNo_penalty_before_days() {
		return no_penalty_before_days;
	}

	public void setNo_penalty_before_days(Integer no_penalty_before_days) {
		this.no_penalty_before_days = no_penalty_before_days;
	}

	public String getHolding_capacity_text() {
		return holding_capacity_text;
	}

	public void setHolding_capacity_text(String holding_capacity_text) {
		this.holding_capacity_text = holding_capacity_text;
	}

	public Boolean getIs_group() {
		return is_group;
	}

	public void setIs_group(Boolean is_group) {
		this.is_group = is_group;
	}

	public Integer getRoom_index() {
		return room_index;
	}

	public void setRoom_index(Integer room_index) {
		this.room_index = room_index;
	}

	public Long getPrev_room_id() {
		return prev_room_id;
	}

	public void setPrev_room_id(Long prev_room_id) {
		this.prev_room_id = prev_room_id;
	}

	public int getRownum() {
		return rownum;
	}

	public void setRownum(int rownum) {
		this.rownum = rownum;
	}

	public RoomsCount getRoomsCount() {
		return roomsCount;
	}

	public void setRoomsCount(RoomsCount roomsCount) {
		this.roomsCount = roomsCount;
	}

	public short getGuests() {
		return guests;
	}

	public void setGuests(short guests) {
		this.guests = guests;
	}

	public String getChildAges() {
		return childAges;
	}

	public void setChildAges(String childAges) {
		this.childAges = childAges;
	}

	public Double getExtra_bed_cost() {
		return extra_bed_cost;
	}

	public void setExtra_bed_cost(Double extra_bed_cost) {
		this.extra_bed_cost = extra_bed_cost;
	}

	public List<RoomType> getRoomtypeslist() {
		return roomtypeslist;
	}

	public void setRoomtypeslist(List<RoomType> roomtypeslist) {
		this.roomtypeslist = roomtypeslist;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public short getBedType() {
		return bedType;
	}

	public void setBedType(short bedType) {
		this.bedType = bedType;
	}
}
