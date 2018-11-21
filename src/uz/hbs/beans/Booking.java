package uz.hbs.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

public class Booking implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final int SORT_BY_RECOMMENDED = 0;
	public static final int SORT_BY_PRICE = 1;
	public static final int SORT_BY_HOTEL_NAME = 2;
	public static final int SORT_BY_STAR_RATING = 3;
	public static final int SORT_BY_TA_RATING = 4;

	public static final int COUNT_ROOMS = 15;
	public static final int COUNT_ADULTS = 5;
	public static final int COUNT_CHILDREN = 3;
	public static final int COUNT_CHILD_AGE = 17;
	
	private Country country;
	private Region region;
	private City city;
	private Hotel hotel;
	private List<Hotel> hotels;
	private Date fromDate;
	private String fromTime;
	private Date toDate;
	private String toTime;
	private Integer rooms;
	private Integer nights;
	private List<HotelStar> stars = new ArrayList<HotelStar>();
	private List<Facility> hotelFacilities = new ArrayList<Facility>();
	private List<Equipment> roomFacilities = new ArrayList<Equipment>();
	private boolean cancelation;
	private IdAndName sortResults;
	private Currencies changeCurrency;
	private List<BookingSearchResult> repeater;
	private List<RoomsCount> roomsCountsList;
	private List<HotelNearByPlace> nearByPlaces = new ArrayList<HotelNearByPlace>();
	
	private Integer range_from_value;
	private Integer range_to_value;
	private Integer range_min_value;
	private Integer range_max_value;
	private Integer range_step_value;
	private String range_prefix_value;

	private boolean resident = true;
	private boolean sortOrder;
	
	private String age_label;

	public Booking() {
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
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

	public List<Hotel> getHotels() {
		return hotels;
	}

	public void setHotels(List<Hotel> hotels) {
		this.hotels = hotels;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getRooms() {
		return rooms;
	}

	public void setRooms(Integer rooms) {
		this.rooms = rooms;
	}

	public Integer getNights() {
		return nights;
	}

	public void setNights(Integer nights) {
		this.nights = nights;
	}

	public List<HotelStar> getStars() {
		return stars;
	}

	public void setStars(List<HotelStar> stars) {
		this.stars = stars;
	}

	public boolean isCancelation() {
		return cancelation;
	}

	public void setCancelation(boolean cancelation) {
		this.cancelation = cancelation;
	}

	public List<Facility> getHotelFacilities() {
		return hotelFacilities;
	}

	public void setHotelFacilities(List<Facility> hotelFacilities) {
		this.hotelFacilities = hotelFacilities;
	}

	public List<Equipment> getRoomFacilities() {
		return roomFacilities;
	}

	public void setRoomFacilities(List<Equipment> roomFacilities) {
		this.roomFacilities = roomFacilities;
	}

	public IdAndName getSortResults() {
		return sortResults;
	}

	public void setSortResults(IdAndName sortResults) {
		this.sortResults = sortResults;
	}

	public List<BookingSearchResult> getRepeater() {
		return repeater;
	}

	public void setRepeater(List<BookingSearchResult> repeater) {
		this.repeater = repeater;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public Currencies getChangeCurrency() {
		return changeCurrency;
	}

	public void setChangeCurrency(Currencies changeCurrency) {
		this.changeCurrency = changeCurrency;
	}

	public Integer getRange_from_value() {
		return range_from_value;
	}

	public void setRange_from_value(Integer range_from_value) {
		this.range_from_value = range_from_value;
	}

	public Integer getRange_to_value() {
		return range_to_value;
	}

	public void setRange_to_value(Integer range_to_value) {
		this.range_to_value = range_to_value;
	}

	public Integer getRange_min_value() {
		return range_min_value;
	}

	public void setRange_min_value(Integer range_min_value) {
		this.range_min_value = range_min_value;
	}

	public Integer getRange_max_value() {
		return range_max_value;
	}

	public void setRange_max_value(Integer range_max_value) {
		this.range_max_value = range_max_value;
	}

	public Integer getRange_step_value() {
		return range_step_value;
	}

	public void setRange_step_value(Integer range_step_value) {
		this.range_step_value = range_step_value;
	}

	public String getRange_prefix_value() {
		return range_prefix_value;
	}

	public void setRange_prefix_value(String range_prefix_value) {
		this.range_prefix_value = range_prefix_value;
	}

	public boolean isResident() {
		return resident;
	}

	public void setResident(boolean resident) {
		this.resident = resident;
	}

	public List<RoomsCount> getRoomsCountsList() {
		return roomsCountsList;
	}

	public void setRoomsCountsList(List<RoomsCount> roomCountsList) {
		this.roomsCountsList = roomCountsList;
	}

	public String getAge_label() {
		return age_label;
	}

	public void setAge_label(String age_label) {
		this.age_label = age_label;
	}

	public List<HotelNearByPlace> getNearByPlaces() {
		return nearByPlaces;
	}

	public void setNearByPlaces(List<HotelNearByPlace> nearByPlaces) {
		this.nearByPlaces = nearByPlaces;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public boolean isSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(boolean sortOrder) {
		this.sortOrder = sortOrder;
	}

	public class Age implements IClusterable {
		private static final long serialVersionUID = 1L;
		private Integer age;

		public Age() {
		}

		@Override
		public String toString() {
			return age.toString();
		}
		
		public Age(Integer age) {
			this.age = age;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}
	}

	public class RoomsCount implements IClusterable {
		private static final long serialVersionUID = 1L;
		private Integer adults;
		private Integer children;
		private List<Booking.Age> agesList = new ArrayList<Booking.Age>();

		public RoomsCount() {
		}

		public RoomsCount(Integer adults, Integer children) {
			this.adults = adults;
			this.children = children;
		}
		
		public Integer getAdults() {
			return adults;
		}

		public void setAdults(Integer adults) {
			this.adults = adults;
		}

		public Integer getChildren() {
			return children;
		}

		public void setChildren(Integer children) {
			this.children = children;
		}

		public List<Booking.Age> getAgesList() {
			return agesList;
		}

		public void setAgesList(List<Booking.Age> agesList) {
			this.agesList = agesList;
		}
	}
}
