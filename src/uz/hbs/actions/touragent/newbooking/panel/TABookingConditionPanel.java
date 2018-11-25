package uz.hbs.actions.touragent.newbooking.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.PriceType;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.CommonUtil;

public class TABookingConditionPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public TABookingConditionPanel(String id, long hotel_id, boolean is_group) {
		this(id, (ReservationRuleType) new MyBatisHelper().selectOne("selectTAReservationRule", new ReservationRuleType(hotel_id, is_group)));
	}
	
	public TABookingConditionPanel(String id, ReservationRuleType rule) {
		super(id);
		add(new Label("check_in_from", CommonUtil.getTimeWithOutSec(rule.getCheck_in_from_time())));
		add(new Label("check_in_to", CommonUtil.getTimeWithOutSec(rule.getCheck_in_to_time())));
		
		add(new Label("early_check_in_to_half", CommonUtil.getTimeWithOutSec(rule.getCheck_in_half_charge_to_time())));
		add(new Label("early_check_in_to_value_half", rule.getCheck_in_half_charge_service_charge()  +  " " + (rule.getCheck_in_half_charge_service_charge_type() == PriceType.IN_PERCENT?"%":"")));
		
		add(new Label("early_check_in_to_full", CommonUtil.getTimeWithOutSec(rule.getCheck_in_full_charge_to_time())));
		add(new Label("early_check_in_to_value_full", rule.getCheck_in_full_charge_service_charge()  +  " " + (rule.getCheck_in_full_charge_service_charge_type() == PriceType.IN_PERCENT?"%":"")));
		
		add(new Label("check_out_from", CommonUtil.getTimeWithOutSec(rule.getCheck_out_from_time())));
		add(new Label("check_out_to", CommonUtil.getTimeWithOutSec(rule.getCheck_out_to_time())));
		
		add(new Label("late_check_out_from_half", CommonUtil.getTimeWithOutSec(rule.getCheck_out_half_charge_from_time())));
		add(new Label("late_check_out_from_value_half", rule.getCheck_out_half_charge_service_charge()  +  " " + (rule.getCheck_out_half_charge_service_charge_type() == PriceType.IN_PERCENT?"%":"")));
		
		add(new Label("late_check_out_from_full", CommonUtil.getTimeWithOutSec(rule.getCheck_out_full_charge_from_time())));
		add(new Label("late_check_out_from_value_full", rule.getCheck_out_full_charge_service_charge()  +  " " + (rule.getCheck_out_full_charge_service_charge_type() == PriceType.IN_PERCENT?"%":"")));
		
		add(new Label("child_free_age", rule.getMinimum_free_age()));
		add(new Label("extra_bed_cost", rule.getExtra_bed_price_type_value()));
		
		add(new Label("cancelation_day", rule.getReservationcancellationpolicy().getNo_penalty_before_days().getId()));
		
		StringResourceModel late_cancel_penalty = null;
		
		if (rule.getReservationcancellationpolicy().isLate_cancel_penalty_first_night()) {
			late_cancel_penalty = new StringResourceModel("touragents.newbooking.cancelation_charge_value.first_night", null,
					new Object[] { rule.getReservationcancellationpolicy().getNo_penalty_before_days().getId() });
		} else {
			late_cancel_penalty = new StringResourceModel("touragents.newbooking.cancelation_charge_value", null,
					new Object[] { rule.getReservationcancellationpolicy().getNo_penalty_before_days().getId(), rule.getReservationcancellationpolicy().getLate_cancel_penalty().getId() });
		}
		add(new Label("cancelation_policy", late_cancel_penalty));
		
		StringResourceModel no_show_penalty = null;
		
		if (rule.getReservationcancellationpolicy().isLate_cancel_penalty_first_night()) {
			no_show_penalty = new StringResourceModel("touragents.newbooking.no_show_policy.first_night", null);
		} else if (rule.getReservationcancellationpolicy().getNo_show_penalty() != null) {
			no_show_penalty = new StringResourceModel("touragents.newbooking.no_show_policy", null,
					new Object[] { rule.getReservationcancellationpolicy().getNo_show_penalty().getId() });
		}
		
		add(new Label("noshow_policy", no_show_penalty));
	}

}
