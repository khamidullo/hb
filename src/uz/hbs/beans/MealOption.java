package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class MealOption implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final String LUNCHTIME_FROM = "12:00:00";
	public static final String LUNCHTIME_TO = "17:59:59";
	
	public static final String DINNERTIME_FROM = "18:00:00";
	public static final String DINNERTIME_TO = "23:59:59";
	
	public static final byte BREAKFAST = 0;
	public static final byte LUNCH = 1;
	public static final byte DINNER = 2;
	
	public static final byte BB = 0;
	public static final byte HB_LUNCH = 1;
	public static final byte HB_DINNER = 2;
	public static final byte FB = 3;
	
	private byte meal_type;
	private boolean included_to_room_rate;
	private Double cost_per_person_per_night;
	private long hotelsusers_id;
	
	private boolean breakfast;
	private boolean breakfast_included_to_room_rate;
	private Double breakfast_per_person_per_night;
	
	private boolean lunch;
	private boolean lunch_included_to_room_rate;
	private Double lunch_per_person_per_night;
	
	private boolean dinner;
	private boolean dinner_included_to_room_rate;
	private Double dinner_per_person_per_night;
	
	public MealOption() {
		
	}
	
	public MealOption(byte meal_type, boolean included_to_room_rate, Double cost_per_person_per_night, long hotelsusers_id) {
		this.meal_type = meal_type;
		this.included_to_room_rate = included_to_room_rate;
		this.cost_per_person_per_night = cost_per_person_per_night;
		this.hotelsusers_id = hotelsusers_id;
	}
	
	public MealOption(byte meal_type, long hotelsusers_id) {
		this.meal_type = meal_type;
		this.hotelsusers_id = hotelsusers_id;
	}
	
	

	@Override
	public String toString() {
		return "MealOption [meal_type=" + meal_type + ", included_to_room_rate=" + included_to_room_rate + ", cost_per_person_per_night="
				+ cost_per_person_per_night + ", hotelsusers_id=" + hotelsusers_id + ", breakfast=" + breakfast + ", breakfast_included_to_room_rate="
				+ breakfast_included_to_room_rate + ", breakfast_per_person_per_night=" + breakfast_per_person_per_night + ", lunch=" + lunch
				+ ", lunch_included_to_room_rate=" + lunch_included_to_room_rate + ", lunch_per_person_per_night=" + lunch_per_person_per_night
				+ ", dinner=" + dinner + ", dinner_included_to_room_rate=" + dinner_included_to_room_rate + ", dinner_per_person_per_night="
				+ dinner_per_person_per_night + "]";
	}

	public byte getMeal_type() {
		return meal_type;
	}

	public void setMeal_type(byte meal_type) {
		this.meal_type = meal_type;
	}

	public boolean isIncluded_to_room_rate() {
		return included_to_room_rate;
	}

	public void setIncluded_to_room_rate(boolean included_to_room_rate) {
		this.included_to_room_rate = included_to_room_rate;
	}

	public Double getCost_per_person_per_night() {
		return cost_per_person_per_night;
	}

	public void setCost_per_person_per_night(Double cost_per_person_per_night) {
		this.cost_per_person_per_night = cost_per_person_per_night;
	}

	public long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}
	
	public boolean isBreakfast() {
		return breakfast;
	}

	public void setBreakfast(boolean breakfast) {
		this.breakfast = breakfast;
	}

	public boolean isBreakfast_included_to_room_rate() {
		return breakfast_included_to_room_rate;
	}

	public void setBreakfast_included_to_room_rate(
			boolean breakfast_included_to_room_rate) {
		this.breakfast_included_to_room_rate = breakfast_included_to_room_rate;
	}

	public Double getBreakfast_per_person_per_night() {
		return breakfast_per_person_per_night;
	}

	public void setBreakfast_per_person_per_night(
			Double breakfast_per_person_per_night) {
		this.breakfast_per_person_per_night = breakfast_per_person_per_night;
	}

	public boolean isLunch() {
		return lunch;
	}

	public void setLunch(boolean lunch) {
		this.lunch = lunch;
	}

	public boolean isLunch_included_to_room_rate() {
		return lunch_included_to_room_rate;
	}

	public void setLunch_included_to_room_rate(boolean lunch_included_to_room_rate) {
		this.lunch_included_to_room_rate = lunch_included_to_room_rate;
	}

	public Double getLunch_per_person_per_night() {
		return lunch_per_person_per_night;
	}

	public void setLunch_per_person_per_night(Double lunch_per_person_per_night) {
		this.lunch_per_person_per_night = lunch_per_person_per_night;
	}

	public boolean isDinner() {
		return dinner;
	}

	public void setDinner(boolean dinner) {
		this.dinner = dinner;
	}

	public boolean isDinner_included_to_room_rate() {
		return dinner_included_to_room_rate;
	}

	public void setDinner_included_to_room_rate(boolean dinner_included_to_room_rate) {
		this.dinner_included_to_room_rate = dinner_included_to_room_rate;
	}

	public Double getDinner_per_person_per_night() {
		return dinner_per_person_per_night;
	}

	public void setDinner_per_person_per_night(Double dinner_per_person_per_night) {
		this.dinner_per_person_per_night = dinner_per_person_per_night;
	}
}
