package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.rate.RatePlane;

public class Hotel implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final byte HOTEL_ACCOUNT_DETAILS = 0;
	public static final byte HOTEL_DETAILS = 1;
	public static final byte RESERVATION_RULES = 2;
	public static final byte REFERENCE_INFO = 3;
	public static final byte ROOM_SETUP = 4;
	public static final byte ROOM_RATES = 5;

	private Long users_id;
	private String name;
	private String display_name;
	private Byte status;
	private IdAndValue hotelstatus;
	private String manager;
	private String manager_email;
	private String corporate_email;
	private String legal_name;
	private String primary_phone;
	private String secondary_phone;
	private String city;
	private Short floors;
	private Integer hotelstars_id;
	private String country_name;
	private String region_name;
	private String address;
	private Date operday;
	private Integer hotelscategories_id;
	private Integer rooms;
	private boolean self_payment_possibility;
	private String accountant;
	private String accountant_phone;
	private String reservation_dep;
	private String reservation_dep_phone;

	private HotelAccountDetail accountDetails;
	private HotelDetail hotelsDetails;
	private ReservationRule reservationRules;
	private ReferenceInfo referenceInfo;
	private RoomSetup roomSetup;
	private RatePlane rateplane;
	private byte step = HOTEL_ACCOUNT_DETAILS;
	private Contract contract = new Contract();
	
	private Double b2c_individual_price;
	private Double b2c_group_price;
	private Boolean b2c_is_enabled;
	private IdAndValue b2c_status;
	private Integer reservations_count;
	private Country countries;
	
	public Hotel() {
		/*
		 * manager = "asd@asd.uz";
		 * manager_email = "asd@asd.uz";
		 * corporate_email = "asd@asd.uz";
		 * legal_name = "asd@asd.uz";
		 * primary_phone = "asd@asd.uz";
		 * secondary_phone = "asd@asd.uz";
		 */
	}

	public Long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Long users_id) {
		this.users_id = users_id;
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

	public HotelAccountDetail getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(HotelAccountDetail accountDetails) {
		this.accountDetails = accountDetails;
	}

	public HotelDetail getHotelsDetails() {
		return hotelsDetails;
	}

	public void setHotelsDetails(HotelDetail hotelsDetails) {
		this.hotelsDetails = hotelsDetails;
	}

	public ReservationRule getReservationRules() {
		return reservationRules;
	}

	public void setReservationRules(ReservationRule reservationRules) {
		this.reservationRules = reservationRules;
	}

	public ReferenceInfo getReferenceInfo() {
		return referenceInfo;
	}

	public void setReferenceInfo(ReferenceInfo referenceInfo) {
		this.referenceInfo = referenceInfo;
	}

	public RoomSetup getRoomSetup() {
		return roomSetup;
	}

	public void setRoomSetup(RoomSetup roomSetup) {
		this.roomSetup = roomSetup;
	}

	public byte getStep() {
		return step;
	}

	public void setStep(byte step) {
		this.step = step;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManager_email() {
		return manager_email;
	}

	public void setManager_email(String manager_email) {
		this.manager_email = manager_email;
	}

	public String getCorporate_email() {
		return corporate_email;
	}

	public void setCorporate_email(String corporate_email) {
		this.corporate_email = corporate_email;
	}

	public String getLegal_name() {
		return legal_name;
	}

	public void setLegal_name(String legal_name) {
		this.legal_name = legal_name;
	}

	public String getPrimary_phone() {
		return primary_phone;
	}

	public void setPrimary_phone(String primary_phone) {
		this.primary_phone = primary_phone;
	}

	public String getSecondary_phone() {
		return secondary_phone;
	}

	public void setSecondary_phone(String secondary_phone) {
		this.secondary_phone = secondary_phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Short getFloors() {
		return floors;
	}

	public void setFloors(Short floors) {
		this.floors = floors;
	}

	public Integer getHotelstars_id() {
		return hotelstars_id;
	}

	public void setHotelstars_id(Integer hotelstars_id) {
		this.hotelstars_id = hotelstars_id;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getOperday() {
		return operday;
	}

	public void setOperday(Date operday) {
		this.operday = operday;
	}

	public IdAndValue getHotelstatus() {
		return hotelstatus;
	}

	public void setHotelstatus(IdAndValue hotelstatus) {
		this.hotelstatus = hotelstatus;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public RatePlane getRateplane() {
		return rateplane;
	}

	public void setRateplane(RatePlane rateplane) {
		this.rateplane = rateplane;
	}

	public Integer getHotelscategories_id() {
		return hotelscategories_id;
	}

	public void setHotelscategories_id(Integer hotelscategories_id) {
		this.hotelscategories_id = hotelscategories_id;
	}

	public Integer getRooms() {
		return rooms;
	}

	public void setRooms(Integer rooms) {
		this.rooms = rooms;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Double getB2c_individual_price() {
		return b2c_individual_price;
	}

	public void setB2c_individual_price(Double b2c_individual_price) {
		this.b2c_individual_price = b2c_individual_price;
	}

	public Double getB2c_group_price() {
		return b2c_group_price;
	}

	public void setB2c_group_price(Double b2c_group_price) {
		this.b2c_group_price = b2c_group_price;
	}

	public Boolean getB2c_is_enabled() {
		return b2c_is_enabled;
	}

	public void setB2c_is_enabled(Boolean b2c_is_enabled) {
		this.b2c_is_enabled = b2c_is_enabled;
	}

	public IdAndValue getB2c_status() {
		return b2c_status;
	}

	public void setB2c_status(IdAndValue b2c_status) {
		this.b2c_status = b2c_status;
	}

	public boolean getSelf_payment_possibility() {
		return self_payment_possibility;
	}

	public void setSelf_payment_possibility(boolean self_payment_possibility) {
		this.self_payment_possibility = self_payment_possibility;
	}

	public Integer getReservations_count() {
		return reservations_count;
	}

	public void setReservations_count(Integer reservations_count) {
		this.reservations_count = reservations_count;
	}

	public Country getCountries() {
		return countries;
	}

	public void setCountries(Country countries) {
		this.countries = countries;
	}

	public String getAccountant() {
		return accountant;
	}

	public void setAccountant(String accountant) {
		this.accountant = accountant;
	}

	public String getAccountant_phone() {
		return accountant_phone;
	}

	public void setAccountant_phone(String accountant_phone) {
		this.accountant_phone = accountant_phone;
	}

	public String getReservation_dep() {
		return reservation_dep;
	}

	public void setReservation_dep(String reservation_dep) {
		this.reservation_dep = reservation_dep;
	}

	public String getReservation_dep_phone() {
		return reservation_dep_phone;
	}

	public void setReservation_dep_phone(String reservation_dep_phone) {
		this.reservation_dep_phone = reservation_dep_phone;
	}
}
