package uz.hbs.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.util.io.IClusterable;

import uz.hbs.utils.CommonUtil;

public class RoomType implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final byte STATUS_ACTIVE = 1;
	
	private Integer id;
	private String name;
	private Byte status;
	private Date create_date;
	private Date update_date;
	private Long initiator_user_id;
	private Integer number_of_rooms;
	private Float room_area;
	private AdditionalBed additional_bed = new AdditionalBed(AdditionalBed.INAPPLICABLE);
	private HoldingCapacity holding_capacity = new HoldingCapacity(HoldingCapacity.HOLDING_CAPACITY_1);
	
	private String description;
	private boolean selected;
	private Double min_room_area;
	private Double max_room_area;
	
	private long hotel_id;
	
	private List<Equipment> equipments;
	private List<Condition> conditions;
	private List<Image> images;
	private List<Room> rooms;
	
	private FileUploadField roomImageField;
	private String room_images;
	
	private BigDecimal room_rate;
	private BigDecimal check_in_rate;
	private BigDecimal check_out_rate;
	
	public RoomType() {
		super();
	}
	
	public RoomType(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public RoomType(int id) {
		super();
		this.id = id;
	}
	
	public RoomType(String name) {
		super();
		this.name = name;
	}
	
	public RoomType(byte number_of_season, byte number_of_person, boolean exists_group, RoomType roomtype) {
		super();
		this.id = roomtype.getId();
		this.name = roomtype.getName();
		this.holding_capacity = new HoldingCapacity(number_of_person);
	}
	
	public RoomType(Integer id, String name, byte number_of_person, boolean additional_bed, int number_of_rooms) {
		super();
		this.id = id;
		this.name = name;
		this.holding_capacity = new HoldingCapacity(number_of_person);
		this.additional_bed = new AdditionalBed(additional_bed);
		this.number_of_rooms = number_of_rooms;
	}
	
	public RoomType(RoomType roomtype) {
		this.id = roomtype.getId();
		this.name = roomtype.getName();
	}
	
	public RoomType(int id, String name, int numberofrooms) {
		super();
		this.id = id;
		this.name = name;
		equipments = new ArrayList<Equipment>();
		conditions = new ArrayList<Condition>();
		images = new ArrayList<Image>();
		rooms = new ArrayList<Room>(numberofrooms);
		for (int i = 1; i <= numberofrooms;i++){
			rooms.add(new Room());
		}
	}
	
	public RoomType(RoomType roomtype, boolean editable) {
		super();
		this.id = roomtype.id;
		this.name = roomtype.name;
		this.additional_bed = roomtype.additional_bed;
		this.room_area = roomtype.room_area;
		this.holding_capacity = roomtype.holding_capacity;
		if (editable) {
			equipments = new ArrayList<Equipment>();
			conditions = new ArrayList<Condition>();
			images = new ArrayList<Image>();
			rooms = new ArrayList<Room>(roomtype.number_of_rooms);
			for (int i = 1; i <= roomtype.number_of_rooms;i++){
				rooms.add(new Room());
			}
		}
	}
	
	public RoomType(int id, int room_number, String name) {
		super();
		this.id = id;
		this.name = name;
		this.number_of_rooms = room_number;
	}
	
	public RoomType(int id, int room_number, String name, BigDecimal room_rate) {
		super();
		this.id = id;
		this.name = name;
		this.number_of_rooms = room_number;
		this.room_rate = CommonUtil.nvl(room_rate);
	}
	
	public RoomType(int id, int room_number, String name, BigDecimal room_rate, BigDecimal check_in_rate, BigDecimal check_out_rate) {
		super();
		this.id = id;
		this.name = name;
		this.number_of_rooms = room_number;
		this.room_rate = CommonUtil.nvl(room_rate);
		this.check_in_rate = CommonUtil.nvl(check_in_rate);
		this.check_out_rate = CommonUtil.nvl(check_out_rate);
	}
	
	public RoomType(int id, String name, String description, List<Equipment> equipments, List<Condition> conditions, List<Image> images, List<Room> rooms, long hotel_id) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.hotel_id = hotel_id;
		this.equipments = new ArrayList<Equipment>();
		this.equipments.addAll(equipments);
		this.conditions = new ArrayList<Condition>();
		this.conditions.addAll(conditions);
		this.images = new ArrayList<Image>();
		this.images.addAll(images);
		this.rooms = new ArrayList<Room>();
		this.rooms.addAll(rooms);
	}
	
	public RoomType(RoomType roomtype, List<Equipment> equipments, List<Condition> conditions, List<Image> images, List<Room> rooms, long hotel_id) {
		super();
		this.id = roomtype.id;
		this.name = roomtype.name;
		this.description = roomtype.description;
		this.additional_bed = roomtype.additional_bed;
		this.holding_capacity = roomtype.holding_capacity;
		this.room_area = roomtype.room_area;
		this.hotel_id = hotel_id;
		this.equipments = new ArrayList<Equipment>();
		this.equipments.addAll(equipments);
		this.conditions = new ArrayList<Condition>();
		this.conditions.addAll(conditions);
		this.images = new ArrayList<Image>();
		this.images.addAll(images);
		this.rooms = new ArrayList<Room>();
		this.rooms.addAll(rooms);
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
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

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public Integer getNumber_of_rooms() {
		return number_of_rooms;
	}

	public void setNumber_of_rooms(Integer number_of_rooms) {
		this.number_of_rooms = number_of_rooms;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (id != null) return id.equals(((RoomType) obj).getId());
			else if (name != null) return name.equals(((RoomType) obj).getName());
		}
		return false;
	}

	public List<Equipment> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public String toString() {
		return "RoomType {id = " + id + ", name = " + name + "}";
	}

	public long getHotelsusers_id() {
		return hotel_id;
	}

	public void setHotelsusers_id(long hotel_id) {
		this.hotel_id = hotel_id;
	}

	public FileUploadField getRoomImageField() {
		return roomImageField;
	}

	public void setRoomImageField(FileUploadField roomImageField) {
		this.roomImageField = roomImageField;
	}

	public String getRoom_images() {
		return room_images;
	}

	public void setRoom_images(String room_images) {
		this.room_images = room_images;
	}

	public Double getMin_room_area() {
		return min_room_area;
	}

	public void setMin_room_area(Double min_room_area) {
		this.min_room_area = min_room_area;
	}

	public Double getMax_room_area() {
		return max_room_area;
	}

	public void setMax_room_area(Double max_room_area) {
		this.max_room_area = max_room_area;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getRoom_area() {
		return room_area;
	}

	public void setRoom_area(Float room_area) {
		this.room_area = room_area;
	}

	public AdditionalBed getAdditional_bed() {
		return additional_bed;
	}

	public void setAdditional_bed(AdditionalBed additional_bed) {
		this.additional_bed = additional_bed;
	}

	public HoldingCapacity getHolding_capacity() {
		return holding_capacity;
	}

	public void setHolding_capacity(HoldingCapacity holding_capacity) {
		this.holding_capacity = holding_capacity;
	}

	public BigDecimal getRoom_rate() {
		return room_rate;
	}

	public void setRoom_rate(BigDecimal room_rate) {
		this.room_rate = room_rate;
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
