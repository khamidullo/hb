package uz.hbs.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.rate.RatePlane;

public class ReservationDetail implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final boolean INDIVIDUAL = false;
	public static final boolean GROUP = true;
	public static final short EXTRA_BED_ONE = 1;
	
	protected Long id;
	protected String group_name;
	protected Short adults = 2;
	protected Short children;
	protected Date check_in;
	protected Date check_out;
	private short check_in_day;
	private short check_out_day;
	private String check_out_in;
	protected Short number_of_nights;
	protected RatePlane rateplane;
	protected AdditionalBed additional_bed = new AdditionalBed(AdditionalBed.INAPPLICABLE);
	protected TourAgent tour_agent;
	protected String guest_comments;
	protected Byte reservation_type;
	protected String reception_comments;
	protected BigDecimal total;
	protected long hotel_id;
	protected ReservationStatus status;
	protected boolean is_group;
	protected long initiator_user_id;
	private Short guest_count;
	private Short room_count;
	private String room_number;
	private String room_type;
	private Date create_date;
	private List<ChildAge> childAgeList;
	protected String auto_cancel_time;
	private Long creator_user_id;
	protected boolean resident;
	protected String payment_method;
	private Boolean payment_confirmation;
	
	protected double breakfast_cost;
	protected double lunch_cost;
	protected double dinner_cost;
	
	protected boolean agree;
	
	protected String ta_comments;
	private String creator;
	
	private GuestDetail details;
	
	private String arrival_time;
	
	private boolean insurance;
	
	public boolean newlyReserve = false;
	
	protected List<ReservationRoom> reserverooms =  new ArrayList<ReservationRoom>();
	
	private Guest main_guest;
	
	private double additional_service_cost;
	private boolean payment_owner;
	
	private AdditionalServiceDetail arrival = new AdditionalServiceDetail(AdditionalServiceDetail.SERVICE_TYPE_ARRIVED);
	private AdditionalServiceDetail departure = new AdditionalServiceDetail(AdditionalServiceDetail.SERVICE_TYPE_DEPARTED);
	private List<Insurance> insuranceList = new ArrayList<Insurance>();
	

	private String guest_name;
	private String hotel_name;
	private Double total_sum;
	private String hotel_legal_name;
	
	private int emails_sent;

	public ReservationDetail() {
	}
	
	public ReservationDetail(long id) {
		this.id = id;
	}
	
	public ReservationDetail(ReservationDetail reserve) {
		this.id = reserve.getId();
		this.group_name = reserve.getGroup_name();
		this.adults = reserve.getAdults();
		this.children = reserve.getChildren();
		this.additional_bed = reserve.getAdditional_bed();
		this.hotel_id = reserve.getHotelsusers_id();
		this.check_in = reserve.getCheck_in();
		this.check_out = reserve.getCheck_out();
		this.is_group = reserve.isIs_group();
	}

	public Short getAdults() {
		return adults;
	}

	public void setAdults(Short adults) {
		this.adults = adults;
	}

	public Short getChildren() {
		return children;
	}

	public void setChildren(Short children) {
		this.children = children;
	}

	public Date getCheck_in() {
		return check_in;
	}

	public void setCheck_in(Date check_in) {
		this.check_in = check_in;
	}

	public Date getCheck_out() {
		return check_out;
	}

	public void setCheck_out(Date check_out) {
		this.check_out = check_out;
	}

	public Short getNumber_of_nights() {
		if (number_of_nights == null) return (short) calcNumberOfNights();
		return number_of_nights;
	}

	public void setNumber_of_nights(Short number_of_nights) {
		this.number_of_nights = number_of_nights;
	}

	public AdditionalBed getAdditional_bed() {
		return additional_bed;
	}

	public void setAdditional_bed(AdditionalBed additional_bed) {
		this.additional_bed = additional_bed;
	}

	public TourAgent getTour_agent() {
		return tour_agent;
	}

	public void setTour_agent(TourAgent tour_agent) {
		this.tour_agent = tour_agent;
	}

	public String getGuest_comments() {
		return guest_comments;
	}

	public void setGuest_comments(String guest_comments) {
		this.guest_comments = guest_comments;
	}

	public Byte getReservation_type() {
		return reservation_type;
	}

	public void setReservation_type(Byte reservation_type) {
		this.reservation_type = reservation_type;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public long getHotelsusers_id() {
		return hotel_id;
	}

	public void setHotelsusers_id(long hotel_id) {
		this.hotel_id = hotel_id;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GuestDetail getDetails() {
		return details;
	}

	public void setDetails(GuestDetail details) {
		this.details = details;
	}

	public boolean isIs_group() {
		return is_group;
	}

	public void setIs_group(boolean is_group) {
		this.is_group = is_group;
	}

	public long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public String getCheck_out_in() {
		return check_out_in;
	}

	public void setCheck_out_in(String check_out_in) {
		this.check_out_in = check_out_in;
	}

	public Short getGuest_count() {
		return guest_count;
	}

	public void setGuest_count(Short guest_count) {
		this.guest_count = guest_count;
	}

	public String getRoom_number() {
		return room_number;
	}

	public void setRoom_number(String room_number) {
		this.room_number = room_number;
	}

	public String getRoom_type() {
		return room_type;
	}

	public void setRoom_type(String room_type) {
		this.room_type = room_type;
	}

	public Short getRoom_count() {
		return room_count;
	}

	public void setRoom_count(Short room_count) {
		this.room_count = room_count;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
	private long calcNumberOfNights(){
		if (check_in == null) check_in = new Date();
		if (check_out == null) check_out = new Date();
		return ((check_out.getTime() - check_in.getTime())/24/60/60/1000);
	}

	public String getReception_comments() {
		return reception_comments;
	}

	public void setReception_comments(String reception_comments) {
		this.reception_comments = reception_comments;
	}

	public List<ChildAge> getChildAgeList() {
		return childAgeList;
	}

	public void setChildAgeList(List<ChildAge> childAgeList) {
		this.childAgeList = childAgeList;
	}

	public String getAuto_cancel_time() {
		return auto_cancel_time;
	}

	public void setAuto_cancel_time(String auto_cancel_time) {
		this.auto_cancel_time = auto_cancel_time;
	}

	public short getCheck_in_day() {
		return check_in_day;
	}

	public void setCheck_in_day(short check_in_day) {
		this.check_in_day = check_in_day;
	}

	public short getCheck_out_day() {
		return check_out_day;
	}

	public void setCheck_out_day(short check_out_day) {
		this.check_out_day = check_out_day;
	}

	public double getBreakfast_cost() {
		return breakfast_cost;
	}

	public void setBreakfast_cost(double breakfast_cost) {
		this.breakfast_cost = breakfast_cost;
	}

	public double getLunch_cost() {
		return lunch_cost;
	}

	public void setLunch_cost(double lunch_cost) {
		this.lunch_cost = lunch_cost;
	}

	public double getDinner_cost() {
		return dinner_cost;
	}

	public void setDinner_cost(double dinner_cost) {
		this.dinner_cost = dinner_cost;
	}
	
	public List<ChildAge> parseChildAge(String childslist){
		List<ChildAge> list = new ArrayList<ChildAge>();
		if (childslist != null && childslist.length() > 0){
			String[] age = childslist.split(",");
			for (short b = 0; b < age.length; b++){
				list.add(new ChildAge(b, Short.valueOf(age[b])));
			}
		}
		return list;
	}
	
	public String concatChildAge(List<ChildAge> list){
		String s = "";
		if (list != null){
			for (ChildAge age : list){
				s = s.concat(String.valueOf(age.getAge())).concat(";");
			}
		}
		return s;
	}

	public RatePlane getRateplane() {
		return rateplane;
	}

	public void setRateplane(RatePlane rateplane) {
		this.rateplane = rateplane;
	}

	public boolean isResident() {
		return resident;
	}

	public void setResident(boolean resident) {
		this.resident = resident;
	}

	public List<ReservationRoom> getReserverooms() {
		return reserverooms;
	}

	public void setReserverooms(List<ReservationRoom> reserverooms) {
		this.reserverooms = reserverooms;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getArrival_time() {
		return arrival_time;
	}

	public void setArrival_time(String arrival_time) {
		this.arrival_time = arrival_time;
	}

	public boolean isInsurance() {
		return insurance;
	}

	public void setInsurance(boolean insurance) {
		this.insurance = insurance;
	}

	public Long getCreator_user_id() {
		return creator_user_id;
	}

	public void setCreator_user_id(Long creator_user_id) {
		this.creator_user_id = creator_user_id;
	}

	public Guest getMain_guest() {
		return main_guest;
	}

	public void setMain_guest(Guest main_guest) {
		this.main_guest = main_guest;
	}

	public AdditionalServiceDetail getArrival() {
		return arrival;
	}

	public void setArrival(AdditionalServiceDetail arrival) {
		this.arrival = arrival;
	}

	public List<Insurance> getInsuranceList() {
		return insuranceList;
	}

	public void setInsuranceList(List<Insurance> insuranceList) {
		this.insuranceList = insuranceList;
	}

	public double getAdditional_service_cost() {
		return additional_service_cost;
	}

	public void setAddition_service_cost(double additional_service_cost) {
		this.additional_service_cost = additional_service_cost;
	}

	public AdditionalServiceDetail getDeparture() {
		return departure;
	}

	public void setDeparture(AdditionalServiceDetail departure) {
		this.departure = departure;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public boolean isPayment_owner() {
		return payment_owner;
	}

	public void setPayment_owner(boolean payment_owner) {
		this.payment_owner = payment_owner;
	}

	public String getGuest_name() {
		return guest_name;
	}

	public void setGuest_name(String guest_name) {
		this.guest_name = guest_name;
	}

	public String getHotel_name() {
		return hotel_name;
	}

	public void setHotel_name(String hotel_name) {
		this.hotel_name = hotel_name;
	}

	public Double getTotal_sum() {
		return total_sum;
	}

	public void setTotal_sum(Double total_sum) {
		this.total_sum = total_sum;
	}

	public String getHotel_legal_name() {
		return hotel_legal_name;
	}

	public void setHotel_legal_name(String hotel_legal_name) {
		this.hotel_legal_name = hotel_legal_name;
	}

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public Boolean getPayment_confirmation() {
		return payment_confirmation;
	}

	public void setPayment_confirmation(Boolean payment_confirmation) {
		this.payment_confirmation = payment_confirmation;
	}

	public int getEmails_sent() {
		return emails_sent;
	}

	public void setEmails_sent(int emails_sent) {
		this.emails_sent = emails_sent;
	}
}
