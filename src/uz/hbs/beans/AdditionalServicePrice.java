package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class AdditionalServicePrice implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final String ArrivalAirServiceTypeGreenHall = "ARRIVAL_AIR_SERVICETYPE_GREEN_HALL";
	public static final String ArrivalAirServiceTypeVipHall = "ARRIVAL_AIR_SERVICETYPE_VIP_HALL";
	public static final String ArrivalTransfer = "ARRIVAL_TRANSFER";
	public static final String DepartureAirServiceTypeGreenHall = "DEPARTURE_AIR_SERVICETYPE_GREEN_HALL";
	public static final String DepartureAirServiceTypeVipHall = "DEPARTURE_AIR_SERVICETYPE_VIP_HALL";
	public static final String DepartureTransfer = "DEPARTURE_TRANSFER";
	public static final String Insurance = "INSURANCE";

	private Integer id;
	private Date create_date;
	private Long initiator_user_id;
	
	private Double arrival;
	private Double arrival_air_green_hall;
	private Double arrival_air_vip_hall;
	private Double departure;
	private Double departure_air_green_hall;
	private Double departure_air_vip_hall;
	private Double insurance;
	
	private String email_send_request;
	private String email_send_request_green;
	private String email_send_request_vip;
	private String email_send_request_insurance;
	
	public AdditionalServicePrice() {
		// TODO Auto-generated constructor stub
	}

	public Double getArrival() {
		return arrival;
	}

	public void setArrival(Double arrival) {
		this.arrival = arrival;
	}

	public Double getArrival_air_green_hall() {
		return arrival_air_green_hall;
	}

	public void setArrival_air_green_hall(Double arrival_air_green_hall) {
		this.arrival_air_green_hall = arrival_air_green_hall;
	}

	public Double getArrival_air_vip_hall() {
		return arrival_air_vip_hall;
	}

	public void setArrival_air_vip_hall(Double arrival_air_vip_hall) {
		this.arrival_air_vip_hall = arrival_air_vip_hall;
	}

	public Double getDeparture() {
		return departure;
	}

	public void setDeparture(Double departure) {
		this.departure = departure;
	}

	public Double getDeparture_air_green_hall() {
		return departure_air_green_hall;
	}

	public void setDeparture_air_green_hall(Double departure_air_green_hall) {
		this.departure_air_green_hall = departure_air_green_hall;
	}

	public Double getInsurance() {
		return insurance;
	}
	
	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}

	public String getEmail_send_request() {
		return email_send_request;
	}

	public void setEmail_send_request(String email_send_request) {
		this.email_send_request = email_send_request;
	}

	public String getEmail_send_request_green() {
		return email_send_request_green;
	}

	public void setEmail_send_request_green(String email_send_request_green) {
		this.email_send_request_green = email_send_request_green;
	}

	public String getEmail_send_request_vip() {
		return email_send_request_vip;
	}

	public void setEmail_send_request_vip(String email_send_request_vip) {
		this.email_send_request_vip = email_send_request_vip;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public Double getDeparture_air_vip_hall() {
		return departure_air_vip_hall;
	}

	public void setDeparture_air_vip_hall(Double departure_air_vip_hall) {
		this.departure_air_vip_hall = departure_air_vip_hall;
	}

	public String getEmail_send_request_insurance() {
		return email_send_request_insurance;
	}

	public void setEmail_send_request_insurance(String email_send_request_insurance) {
		this.email_send_request_insurance = email_send_request_insurance;
	}
}
