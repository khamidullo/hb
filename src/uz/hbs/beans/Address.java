package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class Address implements IClusterable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Country countries;
	private Region regions;
	private City cities;
	private String postal_index;
	private String address;
	private String address_en;
	private String address_uz;
	private Double latitude;
	private Double longitude;
	private Long initiator_user_id;

	public Address(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Address() {
	}

	public Address(Long id) {
		this.id = id;
	}

	public Address(Country countries, Region regions, City cities, String postal_index, String address) {
		this.countries = countries;
		this.regions = regions;
		this.cities = cities;
		this.postal_index = postal_index;
		this.address = address;
	}

	@Override
	public String toString() {
		return "{id=" + id + ", countries=" + (countries != null ? countries.getId() : null) + ", regions="
				+ (regions != null ? regions.getId() : null) + ", city=" + (cities != null ? cities.getId() : null) + ", postal_index=" + postal_index
				+ ", address=" + address + ", latitude=" + latitude + ", longitude=" + longitude + ", initiator_user_id=" + initiator_user_id + "}";
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

	public Country getCountries() {
		return countries;
	}

	public void setCountries(Country countries) {
		this.countries = countries;
	}

	public Region getRegions() {
		return regions;
	}

	public void setRegions(Region regions) {
		this.regions = regions;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public String getPostal_index() {
		return postal_index;
	}

	public void setPostal_index(String postal_index) {
		this.postal_index = postal_index;
	}

	public City getCities() {
		return cities;
	}

	public void setCities(City cities) {
		this.cities = cities;
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
}
