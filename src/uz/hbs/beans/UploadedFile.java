package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class UploadedFile implements IClusterable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String link;
	private Date create_date;
	private String mime_type;
	private String comment;
	private boolean is_default; 
	
	public UploadedFile() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getMime_type() {
		return mime_type;
	}

	public void setMime_type(String mime_type) {
		this.mime_type = mime_type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isIs_default() {
		return is_default;
	}

	public void setIs_default(boolean is_default) {
		this.is_default = is_default;
	}
}
