package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class ReservationRule implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final boolean WARN = true;
	public static final boolean NO_WARN = false;
	
	private ReservationRuleType individual; 
	private ReservationRuleType group;
    private long hotelsusers_id;
	
	
	public ReservationRule() {
		individual = new ReservationRuleType(ReservationRuleType.INDIVIDUAL);
		group = new ReservationRuleType(ReservationRuleType.GROUP);
	}
	
	public long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public ReservationRuleType getIndividual() {
		return individual;
	}

	public void setIndividual(ReservationRuleType individual) {
		this.individual = individual;
	}

	public ReservationRuleType getGroup() {
		return group;
	}

	public void setGroup(ReservationRuleType group) {
		this.group = group;
	}
}
