package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.enums.ActionRight;

public class Action implements IClusterable {
	private static final long serialVersionUID = 1L;

	public static final byte TYPE_FOR_ROLE_OR_LOG = 0;
	public static final byte TYPE_FOR_ONLY_LOG = 1;

	
	private ActionRight id;
	private String value;
	private Byte type;

	public Action() {
	}

	public ActionRight getId() {
		return id;
	}

	public void setId(ActionRight id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}
}
