package uz.hbs.beans;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.rate.RatePlane;
import uz.hbs.beans.types.BedType;

public class Room implements IClusterable {
	private static final long serialVersionUID = 1L;
	private long rowid;
	private Long id;
	private String room_number;
	private Short room_floor;
	private Float room_area;
	private HoldingCapacity holding_capacity;
	private BedType bed_type;
	private AdditionalBed additional_bed;
	private long initiator_user_id;
	private long hotel_id;
	private int roomtype_id;
	private String roomtype;
	private short floor;
	private CleanState clean_state 		= new CleanState(CleanState.UNCLEAN);
	private OnSaleState onsale_state 	= new OnSaleState(OnSaleState.CAN_SALE);
	private RoomState room_state        = new RoomState(RoomState.VACANT);
	private RatePlane rateplane;
	private Date update_date;
	private BigDecimal rate;
	
	public Room() {
		
	}
	
	public Room(long id, String room_number) {
		this.id = id;
		this.room_number = room_number;
	}
	
	public Room(Room room) {
		this.id = room.getId();
		this.room_number = room.getRoom_number();
	}
	
	public Room(HoldingCapacity holding_capacity) {
		this.holding_capacity = holding_capacity;
	}
	
	public Room(Long id, CleanState clean_state) {
		this.id = id;
		this.clean_state = clean_state;
	}
	
	public Room(Long id, RatePlane rateplane) {
		this.id = id;
		this.setRateplane(rateplane);
	}
	
	public Room(Long id, OnSaleState onsale_state) {
		this.id = id;
		this.onsale_state = onsale_state;
	}

	public Room(Long id) {
		this.id = id;
	}
	
	public String getRoom_number() {
		return room_number;
	}

	public void setRoom_number(String room_number) {
		this.room_number = room_number;
	}

	
	public Float getRoom_area() {
		return room_area;
	}

	public void setRoom_area(Float room_area) {
		this.room_area = room_area;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HoldingCapacity getHolding_capacity() {
		return holding_capacity;
	}

	public void setHolding_capacity(HoldingCapacity holding_capacity) {
		this.holding_capacity = holding_capacity;
	}

	public BedType getBed_type() {
		return bed_type;
	}

	public void setBed_type(BedType bed_type) {
		this.bed_type = bed_type;
	}

	public AdditionalBed getAdditional_bed() {
		return additional_bed;
	}

	public void setAdditional_bed(AdditionalBed additional_bed) {
		this.additional_bed = additional_bed;
	}

	public long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public long getHotelsusers_id() {
		return hotel_id;
	}

	public void setHotelsusers_id(long hotel_id) {
		this.hotel_id = hotel_id;
	}

	public int getRoomtypes_id() {
		return roomtype_id;
	}

	public void setRoomtypes_id(int roomtype_id) {
		this.roomtype_id = roomtype_id;
	}

	public String getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}

	public short getFloor() {
		return floor;
	}

	public void setFloor(short floor) {
		this.floor = floor;
	}

	public CleanState getClean_state() {
		return clean_state;
	}

	public void setClean_state(CleanState clean_state) {
		this.clean_state = clean_state;
	}

	public OnSaleState getOnsale_state() {
		return onsale_state;
	}

	public void setOnsale_state(OnSaleState onsale_state) {
		this.onsale_state = onsale_state;
	}

	public RoomState getRoom_state() {
		return room_state;
	}

	public void setRoom_state(RoomState room_state) {
		this.room_state = room_state;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Short getRoom_floor() {
		return room_floor;
	}

	public void setRoom_floor(Short room_floor) {
		this.room_floor = room_floor;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public long getRowid() {
		return rowid;
	}

	public void setRowid(long rowid) {
		this.rowid = rowid;
	}

	public RatePlane getRateplane() {
		return rateplane;
	}

	public void setRateplane(RatePlane rateplane) {
		this.rateplane = rateplane;
	}
}
