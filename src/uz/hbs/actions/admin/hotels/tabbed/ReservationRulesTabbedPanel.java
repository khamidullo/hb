package uz.hbs.actions.admin.hotels.tabbed;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;

import uz.hbs.beans.Hotel;
import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.PriceType;
import uz.hbs.beans.ReservationCancellationPolicy;
import uz.hbs.beans.ReservationRule;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.TimeTextField;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.models.HotelModels;
import uz.hbs.utils.models.MyAjaxCallListener;
import uz.hbs.utils.models.MyChoiceRenderer;
import uz.hbs.utils.models.PercentModel;
import uz.hbs.utils.models.PriceTypeModel;

public abstract class ReservationRulesTabbedPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;
	private WebMarkupContainer container;

	public ReservationRulesTabbedPanel(String id, IModel<Hotel> model) {
		super(id, model);
		setOutputMarkupId(true);
		
		final Hotel hotel = model.getObject();
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		ReservationRule rules = hotel.getReservationRules();
		if (rules == null){
			rules = new ReservationRule();
			hotel.setReservationRules(rules);
			
			ReservationRuleType individual = rules.getIndividual();
			rules.setIndividual(individual);
			individual.setCheck_in_full_charge_service_charge_type(PriceType.IN_PERCENT);
			individual.setCheck_in_half_charge_service_charge_type(PriceType.IN_PERCENT);
			individual.setCheck_out_full_charge_service_charge_type(PriceType.IN_PERCENT);
			individual.setCheck_out_half_charge_service_charge_type(PriceType.IN_PERCENT);
			individual.setcity_tax((float) 0);
			individual.setSystem_commission((float) 0);
			
			ReservationRuleType group = rules.getGroup();
			rules.setGroup(group);
			group.setCheck_in_full_charge_service_charge_type(PriceType.IN_PERCENT);
			group.setCheck_in_half_charge_service_charge_type(PriceType.IN_PERCENT);
			group.setCheck_out_full_charge_service_charge_type(PriceType.IN_PERCENT);
			group.setCheck_out_half_charge_service_charge_type(PriceType.IN_PERCENT);
			group.setcity_tax((float) 0);
			group.setSystem_commission((float) 0);
		}
		
		final Form<ReservationRule> form;

		add(form = new ReservationRulesForm("form", rules));
		
		form.add(new IndicatingAjaxButton("next") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onNext(target, form, feedback);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage(), true));
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getBeforeSendHandler(Component component) {
						return "this.disabled = true;";
					}
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "this.disabled = false;";
					}
					
					@Override
					public CharSequence getFailureHandler(Component component) {
						return "this.disabled = true;";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
			
			@Override
			public boolean isEnabled() {
				return hotel.getStep() == Hotel.RESERVATION_RULES;
			}
			
			@Override
			public boolean isVisible() {
				return isVisibleNextButton();
			}
		});
		
		form.add(new IndicatingAjaxButton("save") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(target, form, feedback);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage(), true));
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getBeforeSendHandler(Component component) {
						return "this.disabled = true;";
					}
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "this.disabled = false;";
					}
					
					@Override
					public CharSequence getFailureHandler(Component component) {
						return "this.disabled = true;";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
			
			@Override
			public boolean isEnabled() {
				return hotel.getStep() == Hotel.RESERVATION_RULES;
			}
		});
	}
	
	private class ReservationRulesForm extends Form<ReservationRule>{
		private static final long serialVersionUID = 1L;

		public ReservationRulesForm(String id, final ReservationRule rules) {
			super(id, new CompoundPropertyModel<ReservationRule>(rules));
			
			add(new TimeTextField("individual.check_in_from_time"));
			add(new TimeTextField("individual.check_in_to_time"));
			add(new TimeTextField("individual.check_in_half_charge_from_time"));
			add(new TimeTextField("individual.check_in_half_charge_to_time"));
			add(new TimeTextField("individual.check_in_full_charge_from_time"));
			add(new TimeTextField("individual.check_in_full_charge_to_time"));
			add(new TimeTextField("individual.check_out_from_time"));
			add(new TimeTextField("individual.check_out_to_time"));
			add(new TimeTextField("individual.check_out_half_charge_from_time"));
			add(new TimeTextField("individual.check_out_half_charge_to_time"));
			add(new TimeTextField("individual.check_out_full_charge_from_time"));
			add(new TimeTextField("individual.check_out_full_charge_to_time"));
			
			RadioGroup<Byte> check_in_half_charge_service_charge_type = new RadioGroup<Byte>("individual.check_in_half_charge_service_charge_type");
			add(check_in_half_charge_service_charge_type);
			check_in_half_charge_service_charge_type.add(new Radio<Byte>("in_percent", new Model<Byte>(PriceType.IN_PERCENT)));
			check_in_half_charge_service_charge_type.add(new Radio<Byte>("fixed_amount", new Model<Byte>(PriceType.FIXED_AMOUNT)));
			
			RadioGroup<Byte> check_in_full_charge_service_charge_type = new RadioGroup<Byte>("individual.check_in_full_charge_service_charge_type");
			add(check_in_full_charge_service_charge_type);
			check_in_full_charge_service_charge_type.add(new Radio<Byte>("in_percent", new Model<Byte>(PriceType.IN_PERCENT)));
			check_in_full_charge_service_charge_type.add(new Radio<Byte>("fixed_amount", new Model<Byte>(PriceType.FIXED_AMOUNT)));
			
			RadioGroup<Byte> check_out_half_charge_service_charge_type = new RadioGroup<Byte>("individual.check_out_half_charge_service_charge_type");
			add(check_out_half_charge_service_charge_type);
			check_out_half_charge_service_charge_type.add(new Radio<Byte>("in_percent", new Model<Byte>(PriceType.IN_PERCENT)));
			check_out_half_charge_service_charge_type.add(new Radio<Byte>("fixed_amount", new Model<Byte>(PriceType.FIXED_AMOUNT)));
			
			RadioGroup<Byte> check_out_full_charge_service_charge_type = new RadioGroup<Byte>("individual.check_out_full_charge_service_charge_type");
			add(check_out_full_charge_service_charge_type);
			check_out_full_charge_service_charge_type.add(new Radio<Byte>("in_percent", new Model<Byte>(PriceType.IN_PERCENT)));
			check_out_full_charge_service_charge_type.add(new Radio<Byte>("fixed_amount", new Model<Byte>(PriceType.FIXED_AMOUNT)));
			
			container = new WebMarkupContainer("group_container");
			container.setOutputMarkupPlaceholderTag(true);
			add(container);
			
			RadioGroup<Byte> check_in_half_charge_service_charge_type2 = new RadioGroup<Byte>("group.check_in_half_charge_service_charge_type");
			container.add(check_in_half_charge_service_charge_type2);
			check_in_half_charge_service_charge_type2.add(new Radio<Byte>("in_percent", new Model<Byte>(PriceType.IN_PERCENT)));
			check_in_half_charge_service_charge_type2.add(new Radio<Byte>("fixed_amount", new Model<Byte>(PriceType.FIXED_AMOUNT)));
			
			RadioGroup<Byte> check_in_full_charge_service_charge_type2 = new RadioGroup<Byte>("group.check_in_full_charge_service_charge_type");
			container.add(check_in_full_charge_service_charge_type2);
			check_in_full_charge_service_charge_type2.add(new Radio<Byte>("in_percent", new Model<Byte>(PriceType.IN_PERCENT)));
			check_in_full_charge_service_charge_type2.add(new Radio<Byte>("fixed_amount", new Model<Byte>(PriceType.FIXED_AMOUNT)));
			
			RadioGroup<Byte> check_out_half_charge_service_charge_type2 = new RadioGroup<Byte>("group.check_out_half_charge_service_charge_type");
			container.add(check_out_half_charge_service_charge_type2);
			check_out_half_charge_service_charge_type2.add(new Radio<Byte>("in_percent", new Model<Byte>(PriceType.IN_PERCENT)));
			check_out_half_charge_service_charge_type2.add(new Radio<Byte>("fixed_amount", new Model<Byte>(PriceType.FIXED_AMOUNT)));
			
			RadioGroup<Byte> check_out_full_charge_service_charge_type2 = new RadioGroup<Byte>("group.check_out_full_charge_service_charge_type");
			container.add(check_out_full_charge_service_charge_type2);
			check_out_full_charge_service_charge_type2.add(new Radio<Byte>("in_percent", new Model<Byte>(PriceType.IN_PERCENT)));
			check_out_full_charge_service_charge_type2.add(new Radio<Byte>("fixed_amount", new Model<Byte>(PriceType.FIXED_AMOUNT)));
			
			NumberTextField<Double> check_in_half_charge_service_charge;
			add(check_in_half_charge_service_charge = new NumberTextField<Double>("individual.check_in_half_charge_service_charge", Double.class));
			check_in_half_charge_service_charge.setLabel(new StringResourceModel("hotels.reservation.service_charge", null));
			check_in_half_charge_service_charge.setRequired(true);
			check_in_half_charge_service_charge.add(new AjaxOnBlurEvent());
			
			NumberTextField<Double> check_in_full_charge_service_charge;
			add(check_in_full_charge_service_charge = new NumberTextField<Double>("individual.check_in_full_charge_service_charge", Double.class));
			check_in_full_charge_service_charge.setLabel(new StringResourceModel("hotels.reservation.service_charge", null));
			check_in_full_charge_service_charge.setRequired(true);
			check_in_full_charge_service_charge.add(new AjaxOnBlurEvent());
			
			NumberTextField<Double> check_out_half_charge_service_charge;
			add(check_out_half_charge_service_charge = new NumberTextField<Double>("individual.check_out_half_charge_service_charge", Double.class));
			check_out_half_charge_service_charge.setLabel(new StringResourceModel("hotels.reservation.service_charge", null));
			check_out_half_charge_service_charge.setRequired(true);
			check_out_half_charge_service_charge.add(new AjaxOnBlurEvent());
			
			NumberTextField<Double> check_out_full_charge_service_charge;
			add(check_out_full_charge_service_charge = new NumberTextField<Double>("individual.check_out_full_charge_service_charge", Double.class));
			check_out_full_charge_service_charge.setLabel(new StringResourceModel("hotels.reservation.service_charge", null));
			check_out_full_charge_service_charge.setRequired(true);
			check_out_full_charge_service_charge.add(new AjaxOnBlurEvent());
			
			ChoiceRenderer<IdAndValue> render = new ChoiceRenderer<IdAndValue>("value", "id");
			
			DropDownChoice<IdAndValue> no_penalty_before_days;
			add(no_penalty_before_days = new DropDownChoice<IdAndValue>("individual.reservationcancellationpolicy.no_penalty_before_days", HotelModels.getNoPenaltyBeforeDaysModel(), render));
			no_penalty_before_days.setLabel(new StringResourceModel("hotels.reservation.no_penalty_if_canceled_up_to", null));
			no_penalty_before_days.setRequired(true);
			
			final DropDownChoice<IdAndValue> late_cancel_penalty;
			add(late_cancel_penalty = new DropDownChoice<IdAndValue>("individual.reservationcancellationpolicy.late_cancel_penalty", PercentModel.getPercent(), render));
			late_cancel_penalty.setLabel(new StringResourceModel("hotels.reservation.if_canceled_later", null));
			if (rules.getIndividual().getReservationcancellationpolicy().getNo_penalty_before_days() != null)
				late_cancel_penalty.setEnabled(rules.getIndividual().getReservationcancellationpolicy().getNo_penalty_before_days().getId() != ReservationCancellationPolicy.NoPenaltyOnCancelation);
			late_cancel_penalty.setRequired(rules.getIndividual() != null && rules.getIndividual().getReservationcancellationpolicy() != null && ! rules.getIndividual().getReservationcancellationpolicy().isLate_cancel_penalty_first_night());
			late_cancel_penalty.add(new AjaxOnBlurEvent());
			
			final DropDownChoice<IdAndValue> no_show_penalty;
			add(no_show_penalty = new DropDownChoice<IdAndValue>("individual.reservationcancellationpolicy.no_show_penalty", PercentModel.getPercent(), render));
			no_show_penalty.setLabel(new StringResourceModel("hotels.reservation.no_show_penalty", null));
			if (rules.getIndividual().getReservationcancellationpolicy().getNo_penalty_before_days() != null)
				no_show_penalty.setEnabled(rules.getIndividual().getReservationcancellationpolicy().getNo_penalty_before_days().getId() != ReservationCancellationPolicy.NoPenaltyOnCancelation);
			no_show_penalty.setRequired(rules.getIndividual() != null && rules.getIndividual().getReservationcancellationpolicy() != null && ! rules.getIndividual().getReservationcancellationpolicy().isNo_show_penalty_first_night());
			no_show_penalty.add(new AjaxOnBlurEvent());
			
			add(new AjaxCheckBox("individual.reservationcancellationpolicy.late_cancel_penalty_first_night"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					boolean bool = Strings.isTrue(getValue());
					late_cancel_penalty.setRequired(! bool);
					target.appendJavaScript("$('#" + late_cancel_penalty.getMarkupId() + "')" + (bool ? ".removeAttr('required');" : ".attr('required', true);"));
				}
			});
			add(new AjaxCheckBox("individual.reservationcancellationpolicy.no_show_penalty_first_night"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					boolean bool = Strings.isTrue(getValue());
					no_show_penalty.setRequired(! bool);
					target.appendJavaScript("$('#" + no_show_penalty.getMarkupId() + "')" + (bool ? ".removeAttr('required');" : ".attr('required', true);"));
				}
			});
			
			no_penalty_before_days.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (rules.getIndividual().getReservationcancellationpolicy().getNo_penalty_before_days().getId() == ReservationCancellationPolicy.NoPenaltyOnCancelation){ //No penalty on cancelation
						if (rules.getIndividual().getReservationcancellationpolicy().getLate_cancel_penalty() != null) {
							rules.getIndividual().getReservationcancellationpolicy().getLate_cancel_penalty().setId(0);
						} else {
							rules.getIndividual().getReservationcancellationpolicy().setLate_cancel_penalty(new IdAndValue(0));
						}
						if (rules.getIndividual().getReservationcancellationpolicy().getNo_show_penalty() != null) {
							rules.getIndividual().getReservationcancellationpolicy().getNo_show_penalty().setId(0);
						} else {
							rules.getIndividual().getReservationcancellationpolicy().setNo_show_penalty(new IdAndValue(0));
						}
						late_cancel_penalty.setEnabled(false);
						no_show_penalty.setEnabled(false);
					} else {
						late_cancel_penalty.setEnabled(true);
						no_show_penalty.setEnabled(true);
					}
					target.add(no_show_penalty);
					target.add(late_cancel_penalty);
				}
			});

			
			final WebMarkupContainer tentative_container;
			add(tentative_container = new WebMarkupContainer("individual_tentative_container"));
			tentative_container.setOutputMarkupId(true);
			tentative_container.setEnabled(rules.getIndividual().getReservationcancellationpolicy().isSupport_tentative_reservation());
			
			final DropDownChoice<IdAndValue> support_tentative_reservation_due_day;
			tentative_container.add(support_tentative_reservation_due_day = new DropDownChoice<IdAndValue>("individual.reservationcancellationpolicy.notify_ta_before_days", HotelModels.getSupportTentativeReservationDueDayModel(), render));
			support_tentative_reservation_due_day.setLabel(new StringResourceModel("hotels.reservation.no_penalty_if_canceled_up_to", null));
			support_tentative_reservation_due_day.add(new AjaxOnBlurEvent());
			support_tentative_reservation_due_day.setEnabled(rules.getIndividual().getReservationcancellationpolicy().isSupport_tentative_reservation_warn());
			
			add(new AjaxCheckBox("individual.reservationcancellationpolicy.support_tentative_reservation"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					tentative_container.setEnabled(Strings.isTrue(getValue()));
					target.add(tentative_container);
				}
			});
			
			tentative_container.add(new AjaxCheckBox("individual.reservationcancellationpolicy.support_tentative_reservation_warn"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					support_tentative_reservation_due_day.setEnabled(Strings.isTrue(getValue()));
					target.add(support_tentative_reservation_due_day);
				}
			});
			
			DropDownChoice<Short> minimum_free_age;
			add(minimum_free_age = new DropDownChoice<Short>("individual.minimum_free_age", HotelModels.getAgeModel((short) 1), new MyChoiceRenderer<Short>()));
			minimum_free_age.setLabel(new StringResourceModel("hotels.reservation.minimum_age_of_guests", null));
			
			final DropDownChoice<Short> maximum_discount_age;
			add(maximum_discount_age = new DropDownChoice<Short>("individual.maximum_discount_age", new LoadableDetachableModel<List<Short>>() {
				private static final long serialVersionUID = 1L;
				private LoadableDetachableModel<List<? extends Short>> maximum_discount_age_model;

				@SuppressWarnings("unchecked")
				@Override
				protected List<Short> load() {
					if (rules.getIndividual().getMinimum_free_age() != null)
						maximum_discount_age_model = HotelModels.getAgeModel((short) (rules.getIndividual().getMinimum_free_age() + 1));
					else 
						maximum_discount_age_model = HotelModels.getAgeModel((short) 1);
					return (List<Short>) maximum_discount_age_model.getObject();
				}
			}, new MyChoiceRenderer<Short>()));
			maximum_discount_age.setLabel(new StringResourceModel("hotels.reservation.maximum_age_of_discount_guests", null));
//			maximum_discount_age.setRequired(true);
			maximum_discount_age.setOutputMarkupId(true);
			maximum_discount_age.add(new AjaxOnBlurEvent());
			
			DropDownChoice<Byte> maximum_discount_age_type;
			add(maximum_discount_age_type = new DropDownChoice<Byte>("individual.maximum_discount_age_type", PriceTypeModel.getPriceTypeShortListModel(), new PriceTypeModel.PriceTypeShortChoiceRenderer()));
			maximum_discount_age_type.setLabel(new StringResourceModel("hotels.reservation.minimum_age_of_guests", null));
//			maximum_discount_age_type.setRequired(true);
			maximum_discount_age_type.add(new AjaxOnBlurEvent());
			
			NumberTextField<Float> maximum_discount_age_value;
			add(maximum_discount_age_value = new NumberTextField<Float>("individual.maximum_discount_age_value", Float.class));
			maximum_discount_age_value.setOutputMarkupId(true);
			maximum_discount_age_value.add(new AjaxOnBlurEvent());
			
			minimum_free_age.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(maximum_discount_age);
				}
			});

			NumberTextField<Double> extra_bed_price_type_value;
			add(extra_bed_price_type_value = new NumberTextField<Double>("individual.extra_bed_price_type_value"));
			extra_bed_price_type_value.setLabel(new StringResourceModel("hotels.reservation.extra_bed_costs", null));
			extra_bed_price_type_value.add(new AjaxOnBlurEvent());
			extra_bed_price_type_value.setRequired(false);
			
//			NumberTextField<Float> service_charge;
//			add(service_charge = new NumberTextField<Float>("individual.system_commission"));
//			service_charge.setLabel(new StringResourceModel("hotels.reservation.system_commission", null));
//			service_charge.add(new AjaxOnBlurEvent());
//			
//			NumberTextField<Float> city_tax;
//			add(city_tax = new NumberTextField<Float>("individual.city_tax"));
//			city_tax.setLabel(new StringResourceModel("hotels.reservation.city_tax", null));
//			city_tax.add(new AjaxOnBlurEvent());
			
			final TimeTextField check_in_from_time;
			
			container.add(check_in_from_time = new TimeTextField("group.check_in_from_time"));
			check_in_from_time.setOutputMarkupId(true);
			
			container.add(new TimeTextField("group.check_in_to_time"));
			container.add(new TimeTextField("group.check_in_half_charge_from_time"));
			container.add(new TimeTextField("group.check_in_half_charge_to_time"));
			container.add(new TimeTextField("group.check_in_full_charge_from_time"));
			container.add(new TimeTextField("group.check_in_full_charge_to_time"));
			container.add(new TimeTextField("group.check_out_from_time"));
			container.add(new TimeTextField("group.check_out_to_time"));
			container.add(new TimeTextField("group.check_out_half_charge_from_time"));
			container.add(new TimeTextField("group.check_out_half_charge_to_time"));
			container.add(new TimeTextField("group.check_out_full_charge_from_time"));
			container.add(new TimeTextField("group.check_out_full_charge_to_time"));
			
			final NumberTextField<Double> check_in_half_charge_service_charge2;
			container.add(check_in_half_charge_service_charge2 = new NumberTextField<Double>("group.check_in_half_charge_service_charge", Double.class){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return true;
				}
			});
			check_in_half_charge_service_charge2.setLabel(new StringResourceModel("hotels.reservation.service_charge", null));
			check_in_half_charge_service_charge2.add(new AjaxOnBlurEvent());
			
			final NumberTextField<Double> check_in_full_charge_service_charge2;
			container.add(check_in_full_charge_service_charge2 = new NumberTextField<Double>("group.check_in_full_charge_service_charge", Double.class){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return true;
				}
			});
			check_in_full_charge_service_charge2.setLabel(new StringResourceModel("hotels.reservation.service_charge", null));
			check_in_full_charge_service_charge2.add(new AjaxOnBlurEvent());
			
			NumberTextField<Double> check_out_half_charge_service_charge2;
			container.add(check_out_half_charge_service_charge2 = new NumberTextField<Double>("group.check_out_half_charge_service_charge", Double.class){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return true;
				}
			});
			check_out_half_charge_service_charge2.setLabel(new StringResourceModel("hotels.reservation.service_charge", null));
			check_out_half_charge_service_charge2.add(new AjaxOnBlurEvent());
			
			NumberTextField<Double> check_out_full_charge_service_charge2;
			container.add(check_out_full_charge_service_charge2 = new NumberTextField<Double>("group.check_out_full_charge_service_charge", Double.class){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return true;
				}
			});
			check_out_full_charge_service_charge2.setLabel(new StringResourceModel("hotels.reservation.service_charge", null));
			check_out_full_charge_service_charge2.add(new AjaxOnBlurEvent());

			final NumberTextField<Short> exceed_type_value;
			add(exceed_type_value = new NumberTextField<Short>("group.exceed_type_value"));
			exceed_type_value.setLabel(new StringResourceModel("hotels.reservation.group.exceed.type.value", null));
			exceed_type_value.setOutputMarkupId(true);
			exceed_type_value.setRequired(true);
			exceed_type_value.add(new AjaxOnBlurEvent());
			
			DropDownChoice<Byte> exceed_type;
			add(exceed_type = new DropDownChoice<Byte>("group.exceed_type", new LoadableDetachableModel<List<Byte>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Byte> load() {
					List<Byte> list = Arrays.asList(new Byte[] {ReservationRuleType.NUMBER_OF_GUESTS, ReservationRuleType.NUMBER_OF_ROOMS});
					return list;
				}
			}, new ChoiceRenderer<Byte>(){
				private static final long serialVersionUID = 1L;
				
				@Override
				public Object getDisplayValue(Byte object) {
					if (object == ReservationRuleType.NUMBER_OF_GUESTS) return getString("hotels.reservation.group.guests");
					return getString("hotels.reservation.group.rooms");
				}
				
				@Override
				public String getIdValue(Byte object, int index) {
					return String.valueOf(object);
				}
				
			}));
			exceed_type.setRequired(true);
			exceed_type.setLabel(new StringResourceModel("hotels.reservation.group.exceed.type", null));
			
			final WebMarkupContainer guide_escort_container;
			add(guide_escort_container = new WebMarkupContainer("guide_escort_container"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled() {
					return rules.getGroup().isGuide_escort();
				}
			});
			guide_escort_container.setOutputMarkupId(true);
			
			final NumberTextField<Short> guide_escort_type_value;
			guide_escort_container.add(guide_escort_type_value = new NumberTextField<Short>("group.guide_escort_type_value"));
			guide_escort_type_value.setLabel(new StringResourceModel("hotels.reservation.group.guide.escort.type.value", null));
			guide_escort_type_value.setOutputMarkupId(true);
			guide_escort_type_value.add(new AjaxOnBlurEvent());
			guide_escort_type_value.setRequired(rules.getGroup().isGuide_escort());
			
			final DropDownChoice<Byte> guide_escort_type;
			guide_escort_container.add(guide_escort_type = new DropDownChoice<Byte>("group.guide_escort_type", new LoadableDetachableModel<List<Byte>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Byte> load() {
					List<Byte> list = Arrays.asList(new Byte[] {ReservationRuleType.NUMBER_OF_GUESTS, ReservationRuleType.NUMBER_OF_ROOMS});
					return list;
				}
			}, new ChoiceRenderer<Byte>(){
				private static final long serialVersionUID = 1L;
				
				@Override
				public Object getDisplayValue(Byte object) {
					if (object == ReservationRuleType.NUMBER_OF_GUESTS) return getString("hotels.reservation.group.guests");
					return getString("hotels.reservation.group.rooms");
				}
				
				@Override
				public String getIdValue(Byte object, int index) {
					return String.valueOf(object);
				}
				
			}));
			guide_escort_type.setRequired(rules.getGroup().isGuide_escort());
			
			final NumberTextField<Short> guide_escort_price_type_value;
			guide_escort_container.add(guide_escort_price_type_value = new NumberTextField<Short>("group.guide_escort_price_type_value"));
			guide_escort_price_type_value.setLabel(new StringResourceModel("hotels.reservation.group.guide.escort.type.value", null));
			guide_escort_price_type_value.setOutputMarkupId(true);
			guide_escort_price_type_value.add(new AjaxOnBlurEvent());
			guide_escort_price_type_value.setRequired(rules.getGroup().isGuide_escort());
			
			final DropDownChoice<Byte> guide_escort_price_type;
			guide_escort_container.add(guide_escort_price_type = new DropDownChoice<Byte>("group.guide_escort_price_type", PriceTypeModel.getPriceTypeShortListModel(), new PriceTypeModel.PriceTypeShortChoiceRenderer()));
			guide_escort_price_type.setRequired(rules.getGroup().isGuide_escort());
			guide_escort_price_type.setLabel(new StringResourceModel("hotels.reservation.group.guide.escort.type.value", null));
			
			add(new AjaxCheckBox("group.guide_escort") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					guide_escort_container.setEnabled(Strings.isTrue(getValue()));
					guide_escort_type.setRequired(guide_escort_container.isEnabled());
					guide_escort_type_value.setRequired(guide_escort_container.isEnabled());
					guide_escort_price_type_value.setRequired(guide_escort_container.isEnabled());
					guide_escort_price_type.setRequired(guide_escort_container.isEnabled());
					target.add(guide_escort_container);
				}
			});
			
			
			DropDownChoice<IdAndValue> no_penalty_before_days2;
			container.add(no_penalty_before_days2 = new DropDownChoice<IdAndValue>("group.reservationcancellationpolicy.no_penalty_before_days", HotelModels.getNoPenaltyBeforeDaysModel(), render){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return true;
				}
			});
			no_penalty_before_days2.setLabel(new StringResourceModel("hotels.reservation.no_penalty_if_canceled_up_to", null));
			
			final DropDownChoice<IdAndValue> late_cancel_penalty2;
			container.add(late_cancel_penalty2 = new DropDownChoice<IdAndValue>("group.reservationcancellationpolicy.late_cancel_penalty", PercentModel.getPercent(), render){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return rules.getGroup() != null && rules.getGroup().getReservationcancellationpolicy() != null && ! rules.getGroup().getReservationcancellationpolicy().isLate_cancel_penalty_first_night();
				}
			});
			if (rules.getGroup().getReservationcancellationpolicy().getNo_penalty_before_days() != null)
				late_cancel_penalty2.setEnabled(rules.getGroup().getReservationcancellationpolicy().getNo_penalty_before_days().getId() != ReservationCancellationPolicy.NoPenaltyOnCancelation);
			late_cancel_penalty2.setLabel(new StringResourceModel("hotels.reservation.if_canceled_later", null));
			late_cancel_penalty2.add(new AjaxOnBlurEvent());
			
			final DropDownChoice<IdAndValue> no_show_penalty2;
			container.add(no_show_penalty2 = new DropDownChoice<IdAndValue>("group.reservationcancellationpolicy.no_show_penalty", PercentModel.getPercent(), render){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return rules.getGroup() != null && rules.getGroup().getReservationcancellationpolicy() != null && ! rules.getGroup().getReservationcancellationpolicy().isNo_show_penalty_first_night();
				}
			});
			if (rules.getGroup().getReservationcancellationpolicy().getNo_penalty_before_days() != null)
				no_show_penalty2.setEnabled(rules.getGroup().getReservationcancellationpolicy().getNo_penalty_before_days().getId() != ReservationCancellationPolicy.NoPenaltyOnCancelation);
			no_show_penalty2.setLabel(new StringResourceModel("hotels.reservation.no_show_penalty", null));
			no_show_penalty2.add(new AjaxOnBlurEvent());
			
			container.add(new AjaxCheckBox("group.reservationcancellationpolicy.late_cancel_penalty_first_night"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					boolean bool = Strings.isTrue(getValue());
					late_cancel_penalty2.setRequired(! bool);
					target.appendJavaScript("$('#" + late_cancel_penalty2.getMarkupId() + "')" + (bool ? ".removeAttr('required');" : ".attr('required', true);"));
				}
			});
			container.add(new AjaxCheckBox("group.reservationcancellationpolicy.no_show_penalty_first_night"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					boolean bool = Strings.isTrue(getValue());
					no_show_penalty2.setRequired(! bool);
					target.appendJavaScript("$('#" + no_show_penalty2.getMarkupId() + "')" + (bool ? ".removeAttr('required');" : ".attr('required', true);"));
				}
			});
			
			no_penalty_before_days2.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (rules.getGroup().getReservationcancellationpolicy().getNo_penalty_before_days().getId() == ReservationCancellationPolicy.NoPenaltyOnCancelation){ //No penalty on cancelation
						if (rules.getGroup().getReservationcancellationpolicy().getLate_cancel_penalty() != null) {
							rules.getGroup().getReservationcancellationpolicy().getLate_cancel_penalty().setId(0);
						} else {
							rules.getGroup().getReservationcancellationpolicy().setLate_cancel_penalty(new IdAndValue(0));
						}
						if (rules.getGroup().getReservationcancellationpolicy().getNo_show_penalty() != null) {
							rules.getGroup().getReservationcancellationpolicy().getNo_show_penalty().setId(0);
						} else {
							rules.getGroup().getReservationcancellationpolicy().setNo_show_penalty(new IdAndValue(0));
						}
						late_cancel_penalty2.setEnabled(false);
						no_show_penalty2.setEnabled(false);
					} else {
						late_cancel_penalty2.setEnabled(true);
						no_show_penalty2.setEnabled(true);
					}
					target.add(no_show_penalty2);
					target.add(late_cancel_penalty2);
				}
			});
			
			final WebMarkupContainer tentative_container2;
			container.add(tentative_container2 = new WebMarkupContainer("group_tentative_container"));
			tentative_container2.setOutputMarkupId(true);
			tentative_container2.setEnabled(rules.getGroup().getReservationcancellationpolicy().isSupport_tentative_reservation());
			
			final DropDownChoice<IdAndValue> support_tentative_reservation_due_day2;
			tentative_container2.add(support_tentative_reservation_due_day2 = new DropDownChoice<IdAndValue>("group.reservationcancellationpolicy.notify_ta_before_days", HotelModels.getSupportTentativeReservationDueDayModel(), render));
			support_tentative_reservation_due_day2.setLabel(new StringResourceModel("hotels.reservation.no_penalty_if_canceled_up_to", null));
			support_tentative_reservation_due_day2.add(new AjaxOnBlurEvent());
			support_tentative_reservation_due_day2.setEnabled(rules.getGroup().getReservationcancellationpolicy().isSupport_tentative_reservation_warn());
			
			container.add(new AjaxCheckBox("group.reservationcancellationpolicy.support_tentative_reservation"){
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					tentative_container2.setEnabled(Strings.isTrue(getValue()));
					target.add(container);
				}
			});
			
			tentative_container2.add(new AjaxCheckBox("group.reservationcancellationpolicy.support_tentative_reservation_warn"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					support_tentative_reservation_due_day2.setEnabled(Strings.isTrue(getValue()));
					target.add(support_tentative_reservation_due_day2);
				}
			});
			
			DropDownChoice<Short> minimum_free_age2;
			container.add(minimum_free_age2 = new DropDownChoice<Short>("group.minimum_free_age", HotelModels.getAgeModel((short) 1), new MyChoiceRenderer<Short>()));
			minimum_free_age2.setLabel(new StringResourceModel("hotels.reservation.minimum_age_of_guests", null));
			
			final DropDownChoice<Short> maximum_discount_age2;
			container.add(maximum_discount_age2 = new DropDownChoice<Short>("group.maximum_discount_age", new LoadableDetachableModel<List<Short>>() {
				private static final long serialVersionUID = 1L;
				private LoadableDetachableModel<List<? extends Short>> maximum_discount_age_model;

				@SuppressWarnings("unchecked")
				@Override
				protected List<Short> load() {
					if (rules.getGroup().getMinimum_free_age() != null)
						maximum_discount_age_model = HotelModels.getAgeModel(rules.getGroup().getMinimum_free_age());
					else 
						maximum_discount_age_model = HotelModels.getAgeModel((short) 1);
					return (List<Short>) maximum_discount_age_model.getObject();
				}
			}, new MyChoiceRenderer<Short>()));
			maximum_discount_age2.setLabel(new StringResourceModel("hotels.reservation.maximum_age_of_discount_guests", null));
//			maximum_discount_age2.setRequired(true);
			maximum_discount_age2.setOutputMarkupId(true);
			maximum_discount_age2.add(new AjaxOnBlurEvent());
			
			
			DropDownChoice<Byte> maximum_discount_age_type2;
			container.add(maximum_discount_age_type2 = new DropDownChoice<Byte>("group.maximum_discount_age_type", PriceTypeModel.getPriceTypeShortListModel(), new PriceTypeModel.PriceTypeShortChoiceRenderer()));
			maximum_discount_age_type2.setLabel(new StringResourceModel("hotels.reservation.minimum_age_of_guests", null));
//			maximum_discount_age_type2.setRequired(true);
			maximum_discount_age_type2.add(new AjaxOnBlurEvent());
			
			NumberTextField<Float> maximum_discount_age_value2;
			container.add(maximum_discount_age_value2 = new NumberTextField<Float>("group.maximum_discount_age_value", Float.class));
			maximum_discount_age_value2.setOutputMarkupId(true);
			maximum_discount_age_value2.add(new AjaxOnBlurEvent());
			
			minimum_free_age2.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(maximum_discount_age2);
				}
			});
			
			NumberTextField<Double> extra_bed_price_type_value2;
			container.add(extra_bed_price_type_value2 = new NumberTextField<Double>("group.extra_bed_price_type_value"));
			extra_bed_price_type_value2.setLabel(new StringResourceModel("hotels.reservation.extra_bed_costs", null));
			extra_bed_price_type_value2.add(new AjaxOnBlurEvent());
			extra_bed_price_type_value2.setRequired(false);
			
//			NumberTextField<Float> system_commission2;
//			container.add(system_commission2 = new NumberTextField<Float>("group.system_commission"));
//			system_commission2.setLabel(new StringResourceModel("hotels.reservation.system_commission", null));
//			system_commission2.add(new AjaxOnBlurEvent());
//			
//			NumberTextField<Float> city_tax2;
//			container.add(city_tax2 = new NumberTextField<Float>("group.city_tax"));
//			city_tax2.setLabel(new StringResourceModel("hotels.reservation.city_tax", null));
//			city_tax2.add(new AjaxOnBlurEvent());
			
			add(new AjaxLink<Void>("copyfrom"){
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					try {
						if (rules.getGroup() != null && rules.getIndividual() != null) rules.getGroup().copyFrom(rules.getIndividual());
						
						if (rules.getIndividual().getReservationcancellationpolicy().isNoPenaltyOnCancelation()) { //No penalty on cancelation
							if (rules.getGroup().getReservationcancellationpolicy().getLate_cancel_penalty() != null) {
								rules.getGroup().getReservationcancellationpolicy().getLate_cancel_penalty().setId(0);
							} else {
								rules.getGroup().getReservationcancellationpolicy().setLate_cancel_penalty(new IdAndValue(0));
							}
							if (rules.getGroup().getReservationcancellationpolicy().getNo_show_penalty() != null) {
								rules.getGroup().getReservationcancellationpolicy().getNo_show_penalty().setId(0);
							} else {
								rules.getGroup().getReservationcancellationpolicy().setNo_show_penalty(new IdAndValue(0));
							}
							late_cancel_penalty2.setEnabled(false);
							no_show_penalty2.setEnabled(false);
						} else {
							late_cancel_penalty2.setEnabled(true);
							no_show_penalty2.setEnabled(true);
						}
						late_cancel_penalty2.setRequired(! rules.getIndividual().getReservationcancellationpolicy().isLate_cancel_penalty_first_night());
						no_show_penalty2.setRequired(! rules.getIndividual().getReservationcancellationpolicy().isNo_show_penalty_first_night());
						target.add(no_show_penalty2);
						target.add(late_cancel_penalty2);
						target.add(container);
						feedback.success(getString("hotels.title.reservation_rules.copy_from"));
					} finally {
						target.add(feedback);
					}
				}
				
				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
					
					IAjaxCallListener listener = new AjaxCallListener(){
						private static final long serialVersionUID = 1L;
						
						@Override
						public CharSequence getSuccessHandler(Component component) {
							return "return copyfrom();";
						}
					};
					attributes.getAjaxCallListeners().add(listener);
				}
			});
			CommonUtil.setFormComponentRequired(this);
		}
	}
	
	protected abstract void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback); 
	protected abstract void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback);
	protected abstract boolean isVisibleNextButton();
}
