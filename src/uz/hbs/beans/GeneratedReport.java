package uz.hbs.beans;

import java.io.Serializable;
import java.util.Date;

public class GeneratedReport implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer cities_id;
	private String name;
	private String link;
	private Long initiator_user_id;
	private Date create_date;
	private String author;
	private String city_name;

	public GeneratedReport() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getCities_id() {
		return cities_id;
	}

	public void setCities_id(Integer cities_id) {
		this.cities_id = cities_id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
}