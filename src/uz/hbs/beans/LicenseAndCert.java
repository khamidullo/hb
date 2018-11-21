package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class LicenseAndCert implements IClusterable {
	private static final long serialVersionUID = 1L;


	public static final int TYPE_LICENSE = 1;
	public static final int TYPE_CERT = 2;
	
	private Long users_id;
	private String doc_number;
	private String file_name;
	private byte[] content;
	private String mime_type;
	private IdAndValue type;
	private Date create_date;
	private Date update_date;

	public LicenseAndCert() {
	}

	@Override
	public String toString() {
		return "{users_id=" + users_id + ", doc_number=" + doc_number + ", file_name=" + file_name + ", content="
				+ (content != null ? content.length : 0) + ", mime_type=" + mime_type + ", type=" + (type != null ? type.getId() : null) + "}";
	}

	public Long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Long users_id) {
		this.users_id = users_id;
	}

	public String getDoc_number() {
		return doc_number;
	}

	public void setDoc_number(String doc_number) {
		this.doc_number = doc_number;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getMime_type() {
		return mime_type;
	}

	public void setMime_type(String mime_type) {
		this.mime_type = mime_type;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public IdAndValue getType() {
		return type;
	}

	public void setType(IdAndValue type) {
		this.type = type;
	}

}
