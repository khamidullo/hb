package uz.hbs.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.util.io.IClusterable;

public class HotelDetail implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long hotel_id;
	private IdAndValue hotelstars_id;
	private String display_name;
	private String display_name_en;
	private String display_name_uz;
	private Long addresses_id;
	private String contact_number;
	private String contact_number2;
	private String fax;
	private String fax2;
	private String contact_email;
	private String contact_email2;
	private String description;
	private String description_en;
	private String description_uz;
	private Long recommended_sort;

	private HotelStar hotelStars;
	private HotelCategory hotelCategory;
	private List<Service> service;
	private Address address;
	private List<Facility> facilities; 
	private List<HotelNearByPlace> hotelsnearbyplaces;
	private FileUploadField hotelImagesfield;
	private String hotel_images;
	private boolean support_resident_rate;
	
	public HotelDetail() {
		service = new ArrayList<Service>();
		address = new Address();
		facilities = new ArrayList<Facility>();
		hotelsnearbyplaces = new ArrayList<HotelNearByPlace>();
	}

	public Long getHotelsusers_id() {
		return hotel_id;
	}

	public void setHotelsusers_id(Long hotel_id) {
		this.hotel_id = hotel_id;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public Long getAddresses_id() {
		return addresses_id;
	}

	public void setAddresses_id(Long addresses_id) {
		this.addresses_id = addresses_id;
	}
	
	public String getContact_number() {
		return contact_number;
	}
	
	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	public String getContact_email() {
		return contact_email;
	}

	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}

	public IdAndValue getHotelstars_id() {
		return hotelstars_id;
	}

	public void setHotelstars_id(IdAndValue hotelstars_id) {
		this.hotelstars_id = hotelstars_id;
	}

	public HotelStar getHotelStars() {
		return hotelStars;
	}

	public void setHotelStars(HotelStar hotelStars) {
		this.hotelStars = hotelStars;
	}
	
	public List<Service> getService() {
		return service;
	}

	public void setService(List<Service> service) {
		this.service = service;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Facility> getFacilities() {
		return facilities;
	}

	public void setFacilities(List<Facility> facilities) {
		this.facilities = facilities;
	}

	public List<HotelNearByPlace> getHotelsnearbyplaces() {
		return hotelsnearbyplaces;
	}

	public void setHotelsnearbyplaces(List<HotelNearByPlace> hotelsnearbyplaces) {
		this.hotelsnearbyplaces = hotelsnearbyplaces;
	}

	public FileUploadField getHotelImagesfield() {
		return hotelImagesfield;
	}

	public void setHotelImagesfield(FileUploadField hotelImagesfield) {
		this.hotelImagesfield = hotelImagesfield;
	}

	public String getHotel_images() {
		return hotel_images;
	}

	public void setHotel_images(String hotel_images) {
		this.hotel_images = hotel_images;
	}

	public boolean isSupport_resident_rate() {
		return support_resident_rate;
	}

	public void setSupport_resident_rate(boolean support_resident_rate) {
		this.support_resident_rate = support_resident_rate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContact_number2() {
		return contact_number2;
	}

	public void setContact_number2(String contact_number2) {
		this.contact_number2 = contact_number2;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getContact_email2() {
		return contact_email2;
	}

	public void setContact_email2(String contact_email2) {
		this.contact_email2 = contact_email2;
	}

	public String getFax2() {
		return fax2;
	}

	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}

	public HotelCategory getHotelCategory() {
		return hotelCategory;
	}

	public void setHotelCategory(HotelCategory hotelCategory) {
		this.hotelCategory = hotelCategory;
	}

	public Long getRecommended_sort() {
		return recommended_sort;
	}

	public void setRecommended_sort(Long recommended_sort) {
		this.recommended_sort = recommended_sort;
	}

	public String getDisplay_name_en() {
		return display_name_en;
	}

	public void setDisplay_name_en(String display_name_en) {
		this.display_name_en = display_name_en;
	}

	public String getDisplay_name_uz() {
		return display_name_uz;
	}

	public void setDisplay_name_uz(String display_name_uz) {
		this.display_name_uz = display_name_uz;
	}

	public String getDescription_en() {
		return description_en;
	}

	public void setDescription_en(String description_en) {
		this.description_en = description_en;
	}

	public String getDescription_uz() {
		return description_uz;
	}

	public void setDescription_uz(String description_uz) {
		this.description_uz = description_uz;
	}
}
