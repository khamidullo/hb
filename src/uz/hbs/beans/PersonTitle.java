package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class PersonTitle implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final String UNKNOWN = "UNKNOWN";
	public static final String MR = "MR";
	public static final String MRs = "MRs";
	public static final String MS = "MS";
	public static final String CHILD = "CHILD";
	private String title;
	private String name;
	
	public PersonTitle() {
	}

	public PersonTitle(String title) {
		this.title = title;
	}
	
	public PersonTitle(String title, String name) {
		this.title = title;
		this.name = name;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
