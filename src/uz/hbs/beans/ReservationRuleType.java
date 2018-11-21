package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class ReservationRuleType implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final byte NUMBER_OF_GUESTS = 0;
	public static final byte NUMBER_OF_ROOMS = 1;
	public static final byte HOUR = 23;
	public static final byte MINUTE = 59;
	
	public static final boolean INDIVIDUAL = false;
	public static final boolean GROUP = true;
	
	private String check_in_from_time;
	private String check_in_to_time;
	private String check_in_half_charge_from_time;
	private String check_in_half_charge_to_time;
	private String check_in_full_charge_from_time;
	private String check_in_full_charge_to_time;

	private String check_out_from_time;
	private String check_out_to_time;
	private String check_out_half_charge_from_time;
	private String check_out_half_charge_to_time;
	private String check_out_full_charge_from_time;
	private String check_out_full_charge_to_time;
	
    private Double check_in_half_charge_service_charge;
    private byte check_in_half_charge_service_charge_type; 
    private Double check_in_full_charge_service_charge;
    private byte check_in_full_charge_service_charge_type; 
    private Double check_out_half_charge_service_charge;
    private byte check_out_half_charge_service_charge_type; 
    private Double check_out_full_charge_service_charge;
    private byte check_out_full_charge_service_charge_type; 
    private boolean is_group;
    private Byte exceed_type;
    private Short exceed_type_value;
    
    private boolean guide_escort;
    private Byte guide_escort_type;
    private Short guide_escort_type_value;
    private Byte guide_escort_price_type;
    private Float guide_escort_price_type_value;
    
    private ReservationCancellationPolicy reservationcancellationpolicy;
    private Short minimum_free_age;
    private Double extra_bed_price_type_value;
    private Byte extra_bed_price_type;
    private Float system_commission;
    private Float city_tax;
    
    private Short maximum_discount_age;
    private Float maximum_discount_age_value;
    private byte maximum_discount_age_type;
        
    private long hotelsusers_id;
    
    public ReservationRuleType() {
    	reservationcancellationpolicy = new ReservationCancellationPolicy();
	}
    
    public ReservationRuleType(long hotelsusers_id, boolean is_group) {
    	this.hotelsusers_id = hotelsusers_id;
    	this.is_group = is_group;
    }
    
    
    public ReservationRuleType(boolean is_group) {
    	this.is_group = is_group;
    	this.reservationcancellationpolicy = new ReservationCancellationPolicy();
    	this.check_in_half_charge_service_charge_type = 0; 
    	this.check_in_full_charge_service_charge_type = 0; 
    	this.check_out_half_charge_service_charge_type = 0; 
    	this.check_out_full_charge_service_charge_type = 0;
    	
    	this.check_in_from_time = "00:00";
    	this.check_in_to_time = "00:00";
    	this.check_in_half_charge_from_time = "00:00";
    	this.check_in_half_charge_to_time = "00:00";
    	this.check_in_full_charge_from_time = "00:00";
    	this.check_in_full_charge_to_time = "00:00";

    	this.check_out_from_time = "00:00";
    	this.check_out_to_time = "00:00";
    	this.check_out_half_charge_from_time = "00:00";
    	this.check_out_half_charge_to_time = "00:00";
    	this.check_out_full_charge_from_time = "00:00";
    	this.check_out_full_charge_to_time = "00:00";
    	
    	this.exceed_type = NUMBER_OF_GUESTS;
    	this.guide_escort_type = NUMBER_OF_GUESTS;
    	this.guide_escort_price_type = PriceType.IN_PERCENT;
    	this.extra_bed_price_type = PriceType.FIXED_AMOUNT;
    }
    
	public boolean isIs_group() {
		return is_group;
	}

	public byte getCheck_out_full_charge_service_charge_type() {
		return check_out_full_charge_service_charge_type;
	}

	public void setCheck_out_full_charge_service_charge_type(
			byte check_out_full_charge_service_charge_type) {
		this.check_out_full_charge_service_charge_type = check_out_full_charge_service_charge_type;
	}
	
	public ReservationCancellationPolicy getReservationcancellationpolicy() {
		return reservationcancellationpolicy;
	}

	public void setReservationcancellationpolicy(
			ReservationCancellationPolicy reservationcancellationpolicy) {
		this.reservationcancellationpolicy = reservationcancellationpolicy;
	}
	
	public Short getMinimum_free_age() {
		return minimum_free_age;
	}

	public void setMinimum_free_age(Short minimum_free_age) {
		this.minimum_free_age = minimum_free_age;
	}

	public Float getcity_tax() {
		return city_tax;
	}

	public void setcity_tax(Float city_tax) {
		this.city_tax = city_tax;
	}

	public void setIs_group(boolean is_group) {
		this.is_group = is_group;
	}

	public long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public Double getCheck_in_half_charge_service_charge() {
		return check_in_half_charge_service_charge;
	}

	public void setCheck_in_half_charge_service_charge(Double check_in_half_charge_service_charge) {
		this.check_in_half_charge_service_charge = check_in_half_charge_service_charge;
	}

	public byte getCheck_in_half_charge_service_charge_type() {
		return check_in_half_charge_service_charge_type;
	}

	public void setCheck_in_half_charge_service_charge_type(byte check_in_half_charge_service_charge_type) {
		this.check_in_half_charge_service_charge_type = check_in_half_charge_service_charge_type;
	}

	public Double getCheck_in_full_charge_service_charge() {
		return check_in_full_charge_service_charge;
	}

	public void setCheck_in_full_charge_service_charge(Double check_in_full_charge_service_charge) {
		this.check_in_full_charge_service_charge = check_in_full_charge_service_charge;
	}

	public byte getCheck_in_full_charge_service_charge_type() {
		return check_in_full_charge_service_charge_type;
	}

	public void setCheck_in_full_charge_service_charge_type(byte check_in_full_charge_service_charge_type) {
		this.check_in_full_charge_service_charge_type = check_in_full_charge_service_charge_type;
	}

	public Double getCheck_out_half_charge_service_charge() {
		return check_out_half_charge_service_charge;
	}

	public void setCheck_out_half_charge_service_charge(Double check_out_half_charge_service_charge) {
		this.check_out_half_charge_service_charge = check_out_half_charge_service_charge;
	}

	public byte getCheck_out_half_charge_service_charge_type() {
		return check_out_half_charge_service_charge_type;
	}

	public void setCheck_out_half_charge_service_charge_type(byte check_out_half_charge_service_charge_type) {
		this.check_out_half_charge_service_charge_type = check_out_half_charge_service_charge_type;
	}

	public Double getCheck_out_full_charge_service_charge() {
		return check_out_full_charge_service_charge;
	}

	public void setCheck_out_full_charge_service_charge(Double check_out_full_charge_service_charge) {
		this.check_out_full_charge_service_charge = check_out_full_charge_service_charge;
	}
	
	public void copyFrom(ReservationRuleType rule){
    	check_in_from_time = rule.getCheck_in_from_time();
    	check_in_to_time = rule.getCheck_in_to_time();
    	check_in_half_charge_from_time = rule.getCheck_in_half_charge_from_time();
    	check_in_half_charge_to_time = rule.getCheck_in_half_charge_to_time();
    	check_in_full_charge_from_time = rule.getCheck_in_full_charge_from_time();
    	check_in_full_charge_to_time = rule.getCheck_in_full_charge_to_time();

    	check_out_from_time = rule.getCheck_out_from_time();
    	check_out_to_time = rule.getCheck_out_to_time();
    	check_out_half_charge_from_time = rule.getCheck_out_half_charge_from_time();
    	check_out_half_charge_to_time = rule.getCheck_out_half_charge_to_time();
    	check_out_full_charge_from_time = rule.getCheck_out_full_charge_from_time();
    	check_out_full_charge_to_time = rule.getCheck_out_full_charge_to_time();

	    check_in_half_charge_service_charge = rule.getCheck_in_half_charge_service_charge();
	    check_in_half_charge_service_charge_type = rule.getCheck_in_half_charge_service_charge_type();  
	    check_in_full_charge_service_charge = rule.getCheck_in_full_charge_service_charge();
	    check_in_full_charge_service_charge_type = rule.getCheck_in_full_charge_service_charge_type(); 
	    check_out_half_charge_service_charge = rule.getCheck_out_half_charge_service_charge();
	    check_out_half_charge_service_charge_type = rule.getCheck_out_half_charge_service_charge_type(); 
	    check_out_full_charge_service_charge = rule.getCheck_out_full_charge_service_charge();
	    check_out_full_charge_service_charge_type = rule.getCheck_out_full_charge_service_charge_type();
	    
	    reservationcancellationpolicy.setLate_cancel_penalty(rule.getReservationcancellationpolicy().getLate_cancel_penalty());
	    reservationcancellationpolicy.setNo_penalty_before_days(rule.getReservationcancellationpolicy().getNo_penalty_before_days());
	    reservationcancellationpolicy.setNo_show_penalty(rule.getReservationcancellationpolicy().getNo_show_penalty());
	    reservationcancellationpolicy.setNotify_ta_before_days(rule.getReservationcancellationpolicy().getNotify_ta_before_days());
	    reservationcancellationpolicy.setSupport_tentative_reservation(rule.getReservationcancellationpolicy().isSupport_tentative_reservation());
	    reservationcancellationpolicy.setSupport_tentative_reservation_warn(rule.getReservationcancellationpolicy().isSupport_tentative_reservation_warn());
	    
	    reservationcancellationpolicy.setLate_cancel_penalty_first_night(rule.getReservationcancellationpolicy().isLate_cancel_penalty_first_night());
	    reservationcancellationpolicy.setNo_show_penalty_first_night(rule.getReservationcancellationpolicy().isNo_show_penalty_first_night());
	    
	    minimum_free_age = rule.getMinimum_free_age();
	    extra_bed_price_type_value = rule.getExtra_bed_price_type_value(); 
	    extra_bed_price_type = rule.getExtra_bed_price_type(); 
	    system_commission = rule.getSystem_commission();
	    city_tax = rule.getcity_tax();
	    
	    maximum_discount_age = rule.getMaximum_discount_age();
	    maximum_discount_age_type = rule.getMaximum_discount_age_type();
	    maximum_discount_age_value = rule.getMaximum_discount_age_value();
	}

	public Short getMaximum_discount_age() {
		return maximum_discount_age;
	}

	public void setMaximum_discount_age(Short maximum_discount_age) {
		this.maximum_discount_age = maximum_discount_age;
	}

	public Float getMaximum_discount_age_value() {
		return maximum_discount_age_value;
	}

	public void setMaximum_discount_age_value(Float maximum_discount_age_value) {
		this.maximum_discount_age_value = maximum_discount_age_value;
	}

	public byte getMaximum_discount_age_type() {
		return maximum_discount_age_type;
	}

	public void setMaximum_discount_age_type(byte maximum_discount_age_type) {
		this.maximum_discount_age_type = maximum_discount_age_type;
	}

	public Float getSystem_commission() {
		return system_commission;
	}

	public void setSystem_commission(Float system_commission) {
		this.system_commission = system_commission;
	}

	public Byte getExtra_bed_price_type() {
		return extra_bed_price_type;
	}

	public void setExtra_bed_price_type(Byte extra_bed_price_type) {
		this.extra_bed_price_type = extra_bed_price_type;
	}

	public Short getExceed_type_value() {
		return exceed_type_value;
	}

	public void setExceed_type_value(Short exceed_type_value) {
		this.exceed_type_value = exceed_type_value;
	}

	public boolean isGuide_escort() {
		return guide_escort;
	}

	public void setGuide_escort(boolean guide_escort) {
		this.guide_escort = guide_escort;
	}

	public Short getGuide_escort_type_value() {
		return guide_escort_type_value;
	}

	public void setGuide_escort_type_value(Short guide_escort_type_value) {
		this.guide_escort_type_value = guide_escort_type_value;
	}

	public Float getGuide_escort_price_type_value() {
		return guide_escort_price_type_value;
	}

	public void setGuide_escort_price_type_value(Float guide_escort_price_type_value) {
		this.guide_escort_price_type_value = guide_escort_price_type_value;
	}

	public String getCheck_in_from_time() {
		return check_in_from_time;
	}

	public void setCheck_in_from_time(String check_in_from_time) {
		this.check_in_from_time = check_in_from_time;
	}

	public String getCheck_in_to_time() {
		return check_in_to_time;
	}

	public void setCheck_in_to_time(String check_in_to_time) {
		this.check_in_to_time = check_in_to_time;
	}

	public String getCheck_in_half_charge_from_time() {
		return check_in_half_charge_from_time;
	}

	public void setCheck_in_half_charge_from_time(String check_in_half_charge_from_time) {
		this.check_in_half_charge_from_time = check_in_half_charge_from_time;
	}

	public String getCheck_in_half_charge_to_time() {
		return check_in_half_charge_to_time;
	}

	public void setCheck_in_half_charge_to_time(String check_in_half_charge_to_time) {
		this.check_in_half_charge_to_time = check_in_half_charge_to_time;
	}

	public String getCheck_in_full_charge_from_time() {
		return check_in_full_charge_from_time;
	}

	public void setCheck_in_full_charge_from_time(String check_in_full_charge_from_time) {
		this.check_in_full_charge_from_time = check_in_full_charge_from_time;
	}

	public String getCheck_in_full_charge_to_time() {
		return check_in_full_charge_to_time;
	}

	public void setCheck_in_full_charge_to_time(String check_in_full_charge_to_time) {
		this.check_in_full_charge_to_time = check_in_full_charge_to_time;
	}

	public String getCheck_out_from_time() {
		return check_out_from_time;
	}

	public void setCheck_out_from_time(String check_out_from_time) {
		this.check_out_from_time = check_out_from_time;
	}

	public String getCheck_out_to_time() {
		return check_out_to_time;
	}

	public void setCheck_out_to_time(String check_out_to_time) {
		this.check_out_to_time = check_out_to_time;
	}

	public String getCheck_out_half_charge_from_time() {
		return check_out_half_charge_from_time;
	}

	public void setCheck_out_half_charge_from_time(String check_out_half_charge_from_time) {
		this.check_out_half_charge_from_time = check_out_half_charge_from_time;
	}

	public String getCheck_out_half_charge_to_time() {
		return check_out_half_charge_to_time;
	}

	public void setCheck_out_half_charge_to_time(String check_out_half_charge_to_time) {
		this.check_out_half_charge_to_time = check_out_half_charge_to_time;
	}

	public String getCheck_out_full_charge_from_time() {
		return check_out_full_charge_from_time;
	}

	public void setCheck_out_full_charge_from_time(String check_out_full_charge_from_time) {
		this.check_out_full_charge_from_time = check_out_full_charge_from_time;
	}

	public String getCheck_out_full_charge_to_time() {
		return check_out_full_charge_to_time;
	}

	public void setCheck_out_full_charge_to_time(String check_out_full_charge_to_time) {
		this.check_out_full_charge_to_time = check_out_full_charge_to_time;
	}

	public Double getExtra_bed_price_type_value() {
		return extra_bed_price_type_value;
	}

	public void setExtra_bed_price_type_value(Double extra_bed_price_type_value) {
		this.extra_bed_price_type_value = extra_bed_price_type_value;
	}

	public Byte getExceed_type() {
		return exceed_type;
	}

	public void setExceed_type(Byte exceed_type) {
		this.exceed_type = exceed_type;
	}

	public Byte getGuide_escort_type() {
		return guide_escort_type;
	}

	public void setGuide_escort_type(Byte guide_escort_type) {
		this.guide_escort_type = guide_escort_type;
	}

	public Byte getGuide_escort_price_type() {
		return guide_escort_price_type;
	}

	public void setGuide_escort_price_type(Byte guide_escort_price_type) {
		this.guide_escort_price_type = guide_escort_price_type;
	}
}
