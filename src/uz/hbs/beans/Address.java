package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class Address implements IClusterable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Country country;
	private Region region;
	private City city;
	private String postal_index;
	private String address;
	private String address_en;
	private String address_uz;
	private Double latitude;
	private Double longitude;
	private Long user_id;

	public Address(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Address() {
	}

	public Address(Long id) {
		this.id = id;
	}

	public Address(Country country, Region region, City city, String postal_index, String address) {
		this.country = country;
		this.region = region;
		this.city = city;
		this.postal_index = postal_index;
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getPostal_index() {
		return postal_index;
	}

	public void setPostal_index(String postal_index) {
		this.postal_index = postal_index;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress_en() {
		return address_en;
	}

	public void setAddress_en(String address_en) {
		this.address_en = address_en;
	}

	public String getAddress_uz() {
		return address_uz;
	}

	public void setAddress_uz(String address_uz) {
		this.address_uz = address_uz;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
}