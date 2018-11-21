package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.db.MyBatisHelper;

public class Guest implements IClusterable {
	private static final long serialVersionUID = 1L;
	public final static short MAIN_GUEST = 1; 
	private Long id;
	private short guest_index;
	private String guest_value;
	private String first_name;
	private String last_name;
	private PersonTitle person_title;
	private String date_and_place_of_birth;
	private Gender gender;
	private String relationship;
	private Nationality nationality;
	private String passport_number;
	private Date passport_date_of_issue;
	private String passport_issue_place;
	private String address;
	private String postal_index;
	private Country country;
	private Region region;
	private City city;
	private String email;
	private String company;
	private String purpose_of_arrival;
	private String occupation;
	private long reservationrooms_id;
	private Long reservations_id;
	private Long additionalservicedetails_id;
	private Room room;
	private Long rooms_id;
	private RoomType roomtype;
	private Integer roomtypes_id;
	private Address guest_address;
	private String visa_type;
	private String visa_number;
	private Date visa_valid_from;
	private Date visa_valid_to;
	private String source_of_reservation;
	private long initiator_user_id;
	private long address_id;
	private ChildAge childAge;
	
	private long guest_detail_id;
	
	public Guest() {
	}
	
	public Guest(short guest_index) {
		setGuest_index(guest_index);
	}
	
	public Guest(short guest_index, String first_name, String last_name, String person_title) {
		this.guest_index = guest_index;
		this.first_name = first_name;
		this.last_name = last_name;
		this.person_title = new PersonTitle(PersonTitle.MR);
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
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

	public Date getPassport_date_of_issue() {
		return passport_date_of_issue;
	}

	public void setPassport_date_of_issue(Date passport_date_of_issue) {
		this.passport_date_of_issue = passport_date_of_issue;
	}

	public String getPassport_issue_place() {
		return passport_issue_place;
	}

	public void setPassport_issue_place(String passport_issue_place) {
		this.passport_issue_place = passport_issue_place;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPurpose_of_arrival() {
		return purpose_of_arrival;
	}

	public void setPurpose_of_arrival(String purpose_of_arrival) {
		this.purpose_of_arrival = purpose_of_arrival;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public long getGuest_detail_id() {
		return guest_detail_id;
	}

	public void setGuest_detail_id(long guest_detail_id) {
		this.guest_detail_id = guest_detail_id;
	}

	public short getGuest_index() {
		return guest_index;
	}

	public void setGuest_index(short guest_index) {
		this.guest_index = guest_index;
		//setGuest_value(new StringResourceModel("hotels.guest.details.guest", null).getString() + " #" + String.valueOf(guest_index));
	}

	public String getDate_and_place_of_birth() {
		return date_and_place_of_birth;
	}

	public void setDate_and_place_of_birth(String date_and_place_of_birth) {
		this.date_and_place_of_birth = date_and_place_of_birth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
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
	
	public long getReservationrooms_id() {
		return reservationrooms_id;
	}

	public void setReservationrooms_id(long reservationrooms_id) {
		this.reservationrooms_id = reservationrooms_id;
	}

	public String getGuest_value() {
		return guest_value;
	}

	public void setGuest_value(String guest_value) {
		this.guest_value = guest_value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReservations_id() {
		return reservations_id;
	}

	public void setReservations_id(Long reservations_id) {
		this.reservations_id = reservations_id;
	}

	public Address getGuest_address() {
		return guest_address;
	}

	public void setGuest_address(Address guest_address) {
		this.guest_address = guest_address;
	}

	public String getVisa_number() {
		return visa_number;
	}

	public void setVisa_number(String visa_number) {
		this.visa_number = visa_number;
	}

	public Date getVisa_valid_from() {
		return visa_valid_from;
	}

	public void setVisa_valid_from(Date visa_valid_from) {
		this.visa_valid_from = visa_valid_from;
	}

	public String getSource_of_reservation() {
		return source_of_reservation;
	}

	public void setSource_of_reservation(String source_of_reservation) {
		this.source_of_reservation = source_of_reservation;
	}

	public long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public Date getVisa_valid_to() {
		return visa_valid_to;
	}

	public void setVisa_valid_to(Date visa_valid_to) {
		this.visa_valid_to = visa_valid_to;
	}

	public long getAddress_id() {
		return address_id;
	}

	public void setAddress_id(long address_id) {
		this.address_id = address_id;
	}

	public Long getRooms_id() {
		return rooms_id;
	}

	public void setRooms_id(Long rooms_id) {
		this.rooms_id = rooms_id;
	}

	public Integer getRoomtypes_id() {
		return roomtypes_id;
	}

	public void setRoomtypes_id(Integer roomtypes_id) {
		this.roomtypes_id = roomtypes_id;
	}

	public String getVisa_type() {
		return visa_type;
	}

	public void setVisa_type(String visa_type) {
		this.visa_type = visa_type;
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

	public PersonTitle getPerson_title() {
		return person_title;
	}

	public void setPerson_title(PersonTitle person_title) {
		this.person_title = person_title;
	}

	public String getPostal_index() {
		return postal_index;
	}

	public void setPostal_index(String postal_index) {
		this.postal_index = postal_index;
	}
	
	public void init_gender(){
		gender = new Gender((person_title != null ? (person_title.getTitle().equals(PersonTitle.MR) ? Gender.MALE : Gender.FEMALE) : Gender.UNKNOWN));
	}

	public ChildAge getChildAge() {
		return childAge;
	}

	public void setChildAge(ChildAge childAge) {
		this.childAge = childAge;
	}
	
	public boolean isCorrect(){
		return person_title != null && person_title.getTitle() != null && first_name != null && last_name != null;
	}
	
	public static Guest getMainGuest(long reserve_id){
		return new MyBatisHelper().selectOne("selectMainGuestReservation", reserve_id);
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Long getAdditionalservicedetails_id() {
		return additionalservicedetails_id;
	}

	public void setAdditionalservicedetails_id(Long additionalservicedetails_id) {
		this.additionalservicedetails_id = additionalservicedetails_id;
	}
}
