package uz.hbs.actions.hotel.reservations.panels;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Bill;
import uz.hbs.beans.ChildAge;
import uz.hbs.beans.IdAndName;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.HotelUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.models.MyAjaxCallListener;

public class ViewBillDetailsPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final Logger _log = LoggerFactory.getLogger(ViewBillDetailsPanel.class);
	private MyFeedbackPanel feedback;
	private ModalWindow dialog;
	private ValueMap map = new ValueMap();

	public ViewBillDetailsPanel(String id, final IModel<ReservationDetail> model) {
		super(id, model);
		
		Bill bill = new Bill(model.getObject().getId(), ((MySession) getSession()).getUser().getHotelsusers_id());
		
		add(dialog = new ModalWindow("dialog"));
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		final WebMarkupContainer container;
		add(container = new WebMarkupContainer("container"));
		container.setOutputMarkupId(true);
		
		container.add(new ConfirmForm("confirmform", model.getObject()));
		
		final Form<Bill> form;
		add(form = new BillForm("form", bill));
		form.add(new IndicatingAjaxButton("add") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Bill bill = (Bill) form.getDefaultModelObject();
				bill.setManual(Bill.MANUAL);
				try {
					if (bill.getService().getId().intValue() == Bill.ADVANCE_PAYMENT){
						bill.setDebit(bill.getCharge());
						bill.setDescription(bill.getService().getName());
					} else if (bill.getService().getId().byteValue() > 0){
						bill.setCredit(bill.getCharge());
						bill.setDescription(bill.getService().getName());
					}
					try {
						bill.setHotelsusers_id(((MySession)getSession()).getUser().getHotelsusers_id());
						bill.setInitiator_user_id(((MySession)getSession()).getUser().getId());
						new MyBatisHelper().insert("insertReservationBill", bill);
						feedback.success(getString("hotels.reservation.detail.issue_bill.add.success"));
					} catch (Exception e) {
						_log.error("Exception", e);
						feedback.error(getString("hotels.reservation.detail.issue_bill.add.fail"));
					}
					target.add(container);
				} finally {
					target.add(feedback);
				}
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
			}
			
			@Override
			public boolean isEnabled() {
				return model.getObject().getStatus().getId() == ReservationStatus.CHECKED_IN;
			}
		});
	}
	
	
	private class BillForm extends Form<Bill>{
		private static final long serialVersionUID = 1L;

		public BillForm(String id, final Bill bill) {
			super(id, new CompoundPropertyModel<Bill>(bill));
			
			DateTextField bill_date;
			add(bill_date = new DateTextField("bill_date", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			bill_date.add(new MyDatePicker());
			bill_date.setLabel(new StringResourceModel("hotels.reservation.details.bill.details.date", null));
			bill_date.setRequired(true);
			
			final DropDownChoice<IdAndName> service;
			add(service = new DropDownChoice<IdAndName>("service", new LoadableDetachableModel<List<IdAndName>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<IdAndName> load() {
					List<IdAndName> list = new ArrayList<IdAndName>();
					list.add(new IdAndName(Bill.ADVANCE_PAYMENT, getString("hotels.reservation.detail.issue_bill.payment_advance")));
					List<IdAndName> templist = new MyBatisHelper().selectList("selectIssueBillDescription", ((MySession) getSession()).getUser().getHotelsusers_id()); 
					if (templist != null) list.addAll(templist);
					list.add(new IdAndName(Bill.OTHER_BILL, getString("other")));
					return list;
				}
			}, new ChoiceRenderer<IdAndName>("name", "id")));
			service.setLabel(new StringResourceModel("hotels.reservation.details.bill.details.description", null));
			service.setRequired(true);
			
			TextField<BigDecimal> charge;
			add(charge = new TextField<BigDecimal>("charge"));
			charge.setLabel(new StringResourceModel("hotels.reservation.details.bill.details.description", null));
			charge.setRequired(true);
			
			DropDownChoice<ReservationRoom> reserveroom;
			add(reserveroom = new DropDownChoice<ReservationRoom>("reserveroom", new LoadableDetachableModel<List<ReservationRoom>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<ReservationRoom> load() {
					return new MyBatisHelper().selectList("selectReserveRoomBill", bill.getReservations_id());
				}
			}, new ChoiceRenderer<ReservationRoom>("room_number", "id")));
			reserveroom.setRequired(true);
			reserveroom.setLabel(new StringResourceModel("hotels.reservation.details.bill.details.room", null));
			
			add(new TextArea<String>("note"));
			
			final WebMarkupContainer other_container;
			add(other_container = new WebMarkupContainer("other_container"));
			other_container.setVisible(false);
			other_container.setOutputMarkupPlaceholderTag(true);
			
			final TextField<String> description;
			other_container.add(description = new TextField<String>("description"));
			
			service.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (Integer.parseInt(nvl(service.getValue())) == Bill.OTHER_BILL){
						other_container.setVisible(true);
					} else {
						other_container.setVisible(false);
					}
					description.setRequired(other_container.isVisible());
					target.add(other_container);
				}
			});
			
			CommonUtil.setFormComponentRequired(this);
		}
//		
//		@Override
//		public void renderHead(IHeaderResponse response) {
//			super.renderHead(response);
//			response.render(JavaScriptHeaderItem.forUrl("js/hb.js"));
//		}
	}
	
	private class ConfirmForm extends Form<ValueMap> {
		private static final long serialVersionUID = 1L;
		private BigDecimal total = new BigDecimal("0");
		private BigDecimal subtotal = new BigDecimal("0");
		private BigDecimal paid = new BigDecimal("0");

		public ConfirmForm(String id, final ReservationDetail reserve) {
			super(id, new CompoundPropertyModel<ValueMap>(map));
			
			final ReservationRuleType rule  = new MyBatisHelper().selectOne("selectIssueBillReservationRuleById", reserve.getId());
			
			add(new ListView<Bill>("billlist", new LoadableDetachableModel<List<Bill>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Bill> load() {
					subtotal = new BigDecimal("0");
					paid = new BigDecimal("0");
					total = new BigDecimal("0");
					return new MyBatisHelper().selectList("selectIssueBills", reserve.getId());
				}
			}) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<Bill> item) {
					final Bill bill = (Bill) item.getDefaultModelObject();
					item.add(new Label("bill_date", FormatUtil.toString(bill.getBill_date(), "dd/MM/yyyy")));
					item.add(new AjaxLink<Void>("delete") {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target) {
							if ((Integer) new MyBatisHelper().delete("deleteBill", bill.getId()) > 0){
								target.add(ConfirmForm.this);
							}
						}
						
						@Override
						public boolean isVisible() {
							return bill.isManual();
						}
						
						@Override
						protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
							super.updateAjaxAttributes(attributes);
							attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
						}
					});
					item.add(new Label("description", bill.getDescription()));
					item.add(new Label("charge", FormatUtil.toString(bill.getCharge().doubleValue())));
					item.add(new Label("debit", FormatUtil.toString(bill.getDebit().doubleValue())));
					item.add(new Label("credit", FormatUtil.toString(bill.getCredit().doubleValue())));
					item.add(new Label("note", bill.getNote()).setEscapeModelStrings(false));
					subtotal = subtotal.add(bill.getCredit());
					paid = paid.add(bill.getDebit());
				}
			});

			add(new TextArea<String>("note"));
			add(new Label("subtotal", new LoadableDetachableModel<BigDecimal>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected BigDecimal load() {
					return subtotal;
				}
			}));
//			add(new Label("service_charge", new Model<Float>(rule.getService_charge())));
			add(new Label("city_tax", new Model<Float>(rule.getcity_tax())));
			add(new Label("total", new LoadableDetachableModel<BigDecimal>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected BigDecimal load() {
//					total = total.add(subtotal).add(subtotal.multiply(BigDecimal.valueOf(rule.getService_charge()).divide(BigDecimal.valueOf(100))));
					total = total.add(BigDecimal.valueOf(rule.getcity_tax()));
					map.put("total", total);
					return total;
				}
			}));
			add(new Label("paid", new LoadableDetachableModel<BigDecimal>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected BigDecimal load() {
					return paid;
				}
			}));
			add(new Label("total_due", new LoadableDetachableModel<BigDecimal>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected BigDecimal load() {
					return total.subtract(paid);
				}
			}));
			
			add(new IndicatingAjaxButton("confirm") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					try {
						Bill bill_check = new MyBatisHelper().selectOne("selectIssueBillTotal", reserve.getId());
						if (bill_check.getCredit().doubleValue() != bill_check.getDebit().doubleValue()){
							feedback.error(getString("hotels.reservation.closed.saldo.no_equal"));
							return;
						}
						if (new MyBatisHelper().update("updateReservationChangeStatusClosed", reserve) > 0) {
							target.add(ConfirmForm.this);
							feedback.success(getString("hotels.reservation.closed.success"));
						} else {
							feedback.error(getString("hotels.reservation.closed.fail"));
						}
					} finally {
						target.add(feedback);
					}
				}
			});
			add(new IndicatingAjaxButton("check_out") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					try {
						if (HotelUtil.checkOutReserve(reserve, ((MySession) getSession()).getUser().getId(), feedback)) {
							feedback.success(getString("hotels.reservation.checkout.success"));
							target.add(ConfirmForm.this);
						} else {
							feedback.error(getString("hotels.reservation.checkout.fail"));
						}
					} finally {
						target.add(feedback);
					}
				}
			});
			add(new AjaxLink<Void>("print") {
				private static final long serialVersionUID = 1L;
				

				@Override
				public void onClick(AjaxRequestTarget target) {
					dialog.setTitle("Print");
					dialog.setContent(new IssueBillPrintPanel(dialog.getContentId(), reserve.getId()));
					dialog.show(target);
				}
			});
		}
	}
	
//	@Override
//	public void renderHead(IHeaderResponse response) {
//		HeaderItemUtil.setDatetimepickerJavaScriptContentHeaderItem(response, ((MySession) getSession()).getLocale(), new Date());
//		super.renderHead(response);
//	}
	
	private static String nvl(String s){
		if (s == null || "".equals(s)) return "0";
		return s;
	}
	
	@SuppressWarnings("unused")
	private void checkOut(ReservationDetail reserv, BigDecimal total){
		SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
		try {
			Bill bill = sql.selectOne("selectIssueBillByReservation", reserv);
			
			if (bill == null) bill = new Bill(reserv.getId());
			
			Date today = sql.selectOne("selectToday");
			bill.setDescription(new StringResourceModel("hotels.reservation.room.change", null).getString());
//			bill.setCredit(reserv.getRate());
			bill.setBill_date(today);
			sql.insert("insertReservationBill", bill);
			
			String meal_option = new StringResourceModel("meal_options.bb.description", null).getString();
//			if (reserv.getMeal_options() == MealOption.HB_LUNCH){
//				meal_option = new StringResourceModel("meal_options.hb.lunch.description", null).getString();
//			} else if (reserv.getMeal_options() == MealOption.HB_DINNER){
//				meal_option = new StringResourceModel("meal_options.hb.dinner.description", null).getString();
//			} else if (reserv.getMeal_options() == MealOption.FB){
//				meal_option = new StringResourceModel("meal_options.fb.description", null).getString();
//			}
//			
			BigDecimal meal_option_value = sql.selectOne("selectIssueBillMealOption", reserv);
			meal_option_value = CommonUtil.nvl(meal_option_value);
			
			ReservationRuleType rule = sql.selectOne("selectIssueBillReservationRule", reserv);
			
			int meal_count = reserv.getAdults();
			
			List<ChildAge> children_age_list = sql.selectList("selectIssueBillChildrenAgeList", reserv);
			
			for (ChildAge age : children_age_list){
				if (age.getAge() > rule.getMinimum_free_age()) meal_count += 1; 
			}
			
			bill.setDescription(meal_option);
			bill.setCredit(meal_option_value.multiply(new BigDecimal(meal_count)));
			bill.setBill_date(today);
			sql.insert("insertReservationBill", bill);
			
//			short extra_bed = CommonUtil.nvl(reserv.getExtra_bed());
//			
//			if (extra_bed > 0){
//				BigDecimal extra_bed_cost = new BigDecimal("0"); 
//				if (rule != null) extra_bed_cost = new BigDecimal(extra_bed).multiply(new BigDecimal(rule.getExtra_bed_price_type_value()));
//				bill.setDescription(new StringResourceModel("additional.bed", null).getString());
//				bill.setCredit(extra_bed_cost);
//				bill.setBill_date(today);
//				sql.insert("insertReservationBill", bill);
//			}
			boolean check_out_normal = sql.selectOne("selectCheckOutNormal", reserv);
			if (! check_out_normal){
				Double half_pinalty = sql.selectOne("selectCheckOutHalfPinalty", reserv);
				if (half_pinalty != null && half_pinalty > 0){
					bill.setDescription(new StringResourceModel("hotels.reservation.late_check_out_period_half", null).getString());
					bill.setCredit(new BigDecimal(half_pinalty));
					bill.setBill_date(today);
					sql.insert("insertReservationBill", bill);
				} else {
					Double full_pinalty = sql.selectOne("selectCheckOutFullPinalty", reserv);
					if (full_pinalty != null && full_pinalty > 0){
						bill.setDescription(new StringResourceModel("hotels.reservation.late_check_out_period", null).getString());
						bill.setCredit(new BigDecimal(full_pinalty));
						bill.setBill_date(today);
						sql.insert("insertReservationBill", bill);
					}
				}
			}
			HashMap<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("old_status", ReservationStatus.CHECKED_IN);
			param.put("new_status", ReservationStatus.CHECKED_OUT);
			param.put("initiator_user_id", ((MySession) getSession()).getUser().getId());
			param.put("id", reserv.getId());
			param.put("total", total);
			sql.update("updateReservationChangeStatus", param);
			sql.commit();
		} catch (Exception e) {
			sql.rollback();
		} finally {
			sql.close();
		}
	}
}
