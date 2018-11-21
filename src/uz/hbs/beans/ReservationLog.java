package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class ReservationLog implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Date create_date;
	private String description;
	private String author;
	
	public ReservationLog() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
}
