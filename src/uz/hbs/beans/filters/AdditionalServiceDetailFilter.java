package uz.hbs.beans.filters;

import org.apache.wicket.util.io.IClusterable;

public class AdditionalServiceDetailFilter implements IClusterable {
	private static final long serialVersionUID = 1L;
	private long creator_user_id;
	private long additionalserviceorders_id;
	
	public AdditionalServiceDetailFilter(long creator_user_id, long additionalserviceorders_id) {
		this.creator_user_id = creator_user_id;
		this.setAdditionalserviceorders_id(additionalserviceorders_id); 
	}

	public long getCreator_user_id() {
		return creator_user_id;
	}

	public void setCreator_user_id(long creator_user_id) {
		this.creator_user_id = creator_user_id;
	}

	public long getAdditionalserviceorders_id() {
		return additionalserviceorders_id;
	}

	public void setAdditionalserviceorders_id(long additionalserviceorders_id) {
		this.additionalserviceorders_id = additionalserviceorders_id;
	}
}
