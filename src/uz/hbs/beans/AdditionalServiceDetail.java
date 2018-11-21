package uz.hbs.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.utils.CommonUtil;

public class AdditionalServiceDetail implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final byte SERVICE_TYPE_UNKNOWN = -1;
	public static final byte SERVICE_TYPE_ARRIVED = 0;
	public static final byte SERVICE_TYPE_DEPARTED = 1;
	
	public static final byte TRANSPORT_TYPE_UNKNOWN = -1;
	public static final byte TRANSPORT_TYPE_AIRPORT = 0;
	public static final byte TRANSPORT_TYPE_TRAIN = 1;
	
	public static final byte AIR_CLASS_ECONOM = 0;
	public static final byte AIR_CLASS_BUSINESS = 1;
	
	public static final byte AIR_TERMINAL_2 = 2;
	public static final byte AIR_TERMINAL_3 = 3;
	
	public static final byte AIR_SERVICE_TYPE_UNKNOWN = -1;
	public static final byte AIR_SERVICE_TYPE_GREEN_HALL = 0;
	public static final byte AIR_SERVICE_TYPE_VIP_HALL = 1;
	
	public static final byte LOCATION_TYPE_RESERVED_HOTEL = 0;
	public static final byte LOCATION_TYPE_ADDRESS = 1;
	
	private Long id;
	private Long additionalserviceorders_id;
	private Date create_date;
	private Date update_date;
	private byte status;
	private Date sent_date;
	private byte service_type; // 0 - arrived, 1 - departed
	private Byte transport_type = TRANSPORT_TYPE_UNKNOWN; // -- 0 - air, 1 - train
	private String air_numb;
	private String air_time;
	private String train_numb;
	private String train_time;
	private String guest;
	private byte air_class = AIR_CLASS_ECONOM; // 0 - econom, 1 - business
	private byte air_terminal = AIR_TERMINAL_2;
	private byte air_service_type = AIR_SERVICE_TYPE_UNKNOWN; // -- 0 - green hall, 1 - vip hall
	private Float air_service_value;
	private byte location_type; //-- 0 - reserved hotel, 1 - address
	private String location_value;
	private boolean taxi_order;
	private Short taxi_order_car;
	private Float taxi_order_cost;
	private String taxi_order_time;
	private String phone_number;
	private String destination;
	
	private short guest_count;
	private String guest_name;
	
	private long creator_user_id;
	private Long initiator_user_id;
	private boolean with_hotel = true;
	
	private Short person = 1;
	private List<Guest> guestlist = new ArrayList<Guest>();
	
	public AdditionalServiceDetail() {
		
	}

	public AdditionalServiceDetail(byte service_type) {
		this.service_type = service_type;
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

	public Date getSent_date() {
		return sent_date;
	}

	public void setSent_date(Date sent_date) {
		this.sent_date = sent_date;
	}

	public byte getAir_class() {
		return air_class;
	}

	public void setAir_class(byte air_class) {
		this.air_class = air_class;
	}

	public byte getAir_terminal() {
		return air_terminal;
	}

	public void setAir_terminal(byte air_terminal) {
		this.air_terminal = air_terminal;
	}

	public byte getAir_service_type() {
		return air_service_type;
	}

	public void setAir_service_type(byte air_service_type) {
		this.air_service_type = air_service_type;
	}

	public Float getAir_service_value() {
		return air_service_value;
	}

	public void setAir_service_value(Float air_service_value) {
		this.air_service_value = air_service_value;
	}

	public byte getLocation_type() {
		return location_type;
	}

	public void setLocation_type(byte location_type) {
		this.location_type = location_type;
	}

	public String getLocation_value() {
		return location_value;
	}

	public void setLocation_value(String location_value) {
		this.location_value = location_value;
	}

	public boolean isTaxi_order() {
		return taxi_order;
	}

	public void setTaxi_order(boolean taxi_order) {
		this.taxi_order = taxi_order;
	}

	public Short getTaxi_order_car() {
		return taxi_order_car;
	}

	public void setTaxi_order_car(Short taxi_order_car) {
		this.taxi_order_car = taxi_order_car;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getTaxi_order_time() {
		return taxi_order_time;
	}

	public void setTaxi_order_time(String taxi_order_time) {
		this.taxi_order_time = taxi_order_time;
	}

	public Float getTaxi_order_cost() {
		return taxi_order_cost;
	}

	public void setTaxi_order_cost(Float taxi_order_cost) {
		this.taxi_order_cost = taxi_order_cost;
	}

	public byte getService_type() {
		return service_type;
	}

	public void setService_type(byte service_type) {
		this.service_type = service_type;
	}

	public Byte getTransport_type() {
		return transport_type;
	}

	public void setTransport_type(Byte transport_type) {
		this.transport_type = CommonUtil.nvl(transport_type, TRANSPORT_TYPE_UNKNOWN);
	}

	public String getAir_numb() {
		return air_numb;
	}

	public void setAir_numb(String air_numb) {
		this.air_numb = air_numb;
	}

	public String getAir_time() {
		return air_time;
	}

	public void setAir_time(String air_time) {
		this.air_time = air_time;
	}

	public String getTrain_numb() {
		return train_numb;
	}

	public void setTrain_numb(String train_numb) {
		this.train_numb = train_numb;
	}

	public String getTrain_time() {
		return train_time;
	}

	public void setTrain_time(String train_time) {
		this.train_time = train_time;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public long getCreator_user_id() {
		return creator_user_id;
	}

	public void setCreator_user_id(long creator_user_id) {
		this.creator_user_id = creator_user_id;
	}

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public boolean isWith_hotel() {
		return with_hotel;
	}

	public void setWith_hotel(boolean with_hotel) {
		this.with_hotel = with_hotel;
	}

	public Short getPerson() {
		return person;
	}

	public void setPerson(Short person) {
		this.person = person;
	}

	public List<Guest> getGuestlist() {
		return guestlist;
	}

	public void setGuestlist(List<Guest> guestlist) {
		this.guestlist = guestlist;
	}

	public Long getAdditionalserviceorders_id() {
		return additionalserviceorders_id;
	}

	public void setAdditionalserviceorders_id(Long additionalserviceorders_id) {
		this.additionalserviceorders_id = additionalserviceorders_id;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	public short getGuest_count() {
		return guest_count;
	}

	public void setGuest_count(short guest_count) {
		this.guest_count = guest_count;
	}

	public String getGuest_name() {
		return guest_name;
	}

	public void setGuest_name(String guest_name) {
		this.guest_name = guest_name;
	}
}
