package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class NightAuditLog implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String description;
	
	public NightAuditLog() {
	}
	
	public NightAuditLog(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
