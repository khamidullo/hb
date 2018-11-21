package uz.hbs.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.rate.RatePlane;

public class ReservationRoom implements IClusterable {
	private static final long serialVersionUID = 1L;
	
	public static final byte STATUS_NEW = 0;
	public static final byte STATUS_NO_SHOW = 1;
	
	private Long id; 
	private Room room;
	private Long rooms_id;
	private Long previously_rooms_id;
	private RoomType roomtype;
	private Integer roomtypes_id;
	private RatePlane rateplane;
	private Date create_date;
	private Date update_date;
	private long initiator_user_id;
	private long reservations_id;
	private BigDecimal rate;
	private BigDecimal total;
	
	private Short room_count;
	private Short guest_count;
	private String guests;
	private String room_number;
	private short holding_capacity;
	
	private Long hotelsusers_id;
	private Date check_in;
	
	private short index;
	
	private List<Guest> guestlist = new ArrayList<Guest>();
	private List<ChildAge> childAgeList = new ArrayList<ChildAge>();
	
	private boolean non_smokers;
	private boolean city_view;
	private boolean extra_bed_needed;
	
	private BigDecimal extra_bed_cost;
	private BigDecimal meal_cost;
	private BigDecimal early_check_in_cost;
	private BigDecimal late_check_out_cost;
	
	private byte meal_options; //0 - BB; 1,2- HB; 3 - FB
	
	private boolean custom_rate;
	
	private BigDecimal check_in_rate = new BigDecimal(0.0);
	private BigDecimal check_out_rate = new BigDecimal(0.0);
	
	public ReservationRoom() {
	}
	
	public ReservationRoom(ReservationRoom reserveroom) {
		this.id = reserveroom.getId();
		this.room = reserveroom.getRoom();
		this.rooms_id = reserveroom.getRoom().getId();
		this.roomtype = reserveroom.getRoomtype();
		this.roomtypes_id = reserveroom.getRoomtype().getId();
	}
	
	public ReservationRoom(long id) {
		this.id = id;
	}
	
	public ReservationRoom(ReservationDetail reserve, int roomtypes_id, Long rooms_id) {
		this(reserve.getId(), roomtypes_id, rooms_id, reserve.getRateplane(), null);
	}
//	
//	public ReservationRoom(ReservationDetail reserve, int roomtypes_id, Long rooms_id, Long initiator_user_id) {
//		this(reserve.getId(), roomtypes_id, rooms_id, reserve.getRateplane(), reserve.getRate());
//		this.initiator_user_id = initiator_user_id;
//		this.rooms_id = rooms_id;
//		this.roomtypes_id = roomtypes_id;
//	}

	public ReservationRoom(long reservations_id, int roomtype_id, long rooms_id, RatePlane rateplane, BigDecimal rate) {
		this.room = new Room(rooms_id);
		this.roomtype = new RoomType(roomtype_id);
		this.reservations_id = reservations_id;
		this.rateplane = new RatePlane(rateplane);
		this.rate = rate;
	}
	
	public ReservationRoom(long reservations_id, int roomtype_id, long rooms_id) {
		room = new Room(rooms_id);
		roomtype = new RoomType(roomtype_id);
		this.reservations_id = reservations_id;
	}
	
	public ReservationRoom(long reservations_id, int roomtype_id) {
		roomtype = new RoomType(roomtype_id);
		this.reservations_id = reservations_id;
	}
	
	public ReservationRoom(int roomtype_id, String roomtype_name, byte holding_capacity) {
		roomtype = new RoomType(roomtype_id, roomtype_name);
		room = new Room(new HoldingCapacity(holding_capacity));
	}
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public RoomType getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(RoomType roomtype) {
		this.roomtype = roomtype;
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

	public long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public long getReservations_id() {
		return reservations_id;
	}

	public void setReservations_id(long reservations_id) {
		this.reservations_id = reservations_id;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Short getRoom_count() {
		return room_count;
	}

	public void setRoom_count(Short room_count) {
		this.room_count = room_count;
	}

	public Integer getRoomtypes_id() {
		return roomtypes_id;
	}

	public void setRoomtypes_id(Integer roomtypes_id) {
		this.roomtypes_id = roomtypes_id;
	}

	public Long getRooms_id() {
		return rooms_id;
	}

	public void setRooms_id(Long rooms_id) {
		this.rooms_id = rooms_id;
	}

	public Long getPreviously_rooms_id() {
		return previously_rooms_id;
	}

	public void setPreviously_rooms_id(Long previously_rooms_id) {
		this.previously_rooms_id = previously_rooms_id;
	}

	public RatePlane getRateplane() {
		return rateplane;
	}

	public void setRateplane(RatePlane rateplane) {
		this.rateplane = rateplane;
	}

	public Short getGuest_count() {
		return guest_count;
	}

	public void setGuest_count(Short guest_count) {
		this.guest_count = guest_count;
	}

	public String getGuests() {
		return guests;
	}

	public void setGuests(String guests) {
		this.guests = guests;
	}

	public String getRoom_number() {
		return room_number;
	}

	public void setRoom_number(String room_number) {
		this.room_number = room_number;
	}

	public List<Guest> getGuestlist() {
		return guestlist;
	}

	public void setGuestlist(List<Guest> guestlist) {
		this.guestlist = guestlist;
	}

	public short getHolding_capacity() {
		return holding_capacity;
	}

	public void setHolding_capacity(short holding_capacity) {
		this.holding_capacity = holding_capacity;
	}

	public boolean isNon_smokers() {
		return non_smokers;
	}

	public void setNon_smokers(boolean non_smokers) {
		this.non_smokers = non_smokers;
	}

	public boolean isCity_view() {
		return city_view;
	}

	public void setCity_view(boolean city_view) {
		this.city_view = city_view;
	}

	public boolean isExtra_bed_needed() {
		return extra_bed_needed;
	}

	public void setExtra_bed_needed(boolean extra_bed_needed) {
		this.extra_bed_needed = extra_bed_needed;
	}

	public byte getMeal_options() {
		return meal_options;
	}

	public void setMeal_options(byte meal_options) {
		this.meal_options = meal_options;
	}

	public short getIndex() {
		return index;
	}

	public void setIndex(short index) {
		this.index = index;
	}

	public List<ChildAge> getChildAgeList() {
		return childAgeList;
	}

	public void setChildAgeList(List<ChildAge> childAgeList) {
		this.childAgeList = childAgeList;
	}

	public Long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(Long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public Date getCheck_in() {
		return check_in;
	}

	public void setCheck_in(Date check_in) {
		this.check_in = check_in;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getExtra_bed_cost() {
		return extra_bed_cost;
	}

	public void setExtra_bed_cost(BigDecimal extra_bed_cost) {
		this.extra_bed_cost = extra_bed_cost;
	}

	public BigDecimal getMeal_cost() {
		return meal_cost;
	}

	public void setMeal_cost(BigDecimal meal_cost) {
		this.meal_cost = meal_cost;
	}

	public BigDecimal getEarly_check_in_cost() {
		return early_check_in_cost;
	}

	public void setEarly_check_in_cost(BigDecimal early_check_in_cost) {
		this.early_check_in_cost = early_check_in_cost;
	}

	public BigDecimal getLate_check_out_cost() {
		return late_check_out_cost;
	}

	public void setLate_check_out_cost(BigDecimal late_check_out_cost) {
		this.late_check_out_cost = late_check_out_cost;
	}
	
	public short getAdults_count(){
		return (short) guestlist.size();
	}
	
	public short getChildren_count(){
		return (short) childAgeList.size();
	}

	public boolean isCustom_rate() {
		return custom_rate;
	}

	public void setCustom_rate(boolean custom_rate) {
		this.custom_rate = custom_rate;
	}

	public BigDecimal getCheck_in_rate() {
		return check_in_rate;
	}

	public void setCheck_in_rate(BigDecimal check_in_rate) {
		this.check_in_rate = check_in_rate;
	}

	public BigDecimal getCheck_out_rate() {
		return check_out_rate;
	}

	public void setCheck_out_rate(BigDecimal check_out_rate) {
		this.check_out_rate = check_out_rate;
	}
}
