package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class City implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer countries_id;
	private Integer regions_id;
	private String name;
	private String name_uz;
	private String name_en;
	private String region_name;
	private String country_name;
	
	private Country country;
	private Region region;
	
	public City() {
	}
	
	public City(int id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCountries_id() {
		return countries_id;
	}

	public void setCountries_id(Integer countries_id) {
		this.countries_id = countries_id;
	}

	public Integer getRegions_id() {
		return regions_id;
	}

	public void setRegions_id(Integer regions_id) {
		this.regions_id = regions_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getName_uz() {
		return name_uz;
	}

	public void setName_uz(String name_uz) {
		this.name_uz = name_uz;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
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
}
