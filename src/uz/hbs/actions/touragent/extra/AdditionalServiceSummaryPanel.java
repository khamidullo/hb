package uz.hbs.actions.touragent.extra;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.AdditionalServiceDetail;
import uz.hbs.beans.AdditionalServiceOrder;
import uz.hbs.beans.AdditionalServicePrice;
import uz.hbs.beans.Guest;
import uz.hbs.beans.Insurance;
import uz.hbs.beans.MyBean;
import uz.hbs.beans.PersonTitle;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.ReserveUtil;
import uz.hbs.utils.email.CIPHallEmailNotifier;
import uz.hbs.utils.email.GreenHallEmailNotifier;
import uz.hbs.utils.email.InsuranceEmailNotifier;
import uz.hbs.utils.email.TaxiEmailNotifier;

public class AdditionalServiceSummaryPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private BigDecimal total = new BigDecimal("0");

	public AdditionalServiceSummaryPanel(String id, IBreadCrumbModel breadCrumbModel, final AdditionalServiceOrder order) {
		super(id, breadCrumbModel);
		add(new Label("guest_count", order.getPerson()));
		add(new Label("arrival_time", FormatUtil.toString(order.getArrival_date(), MyWebApplication.DATE_FORMAT)));
		add(new Label("departure_time", FormatUtil.toString(order.getDeparture_date(), MyWebApplication.DATE_FORMAT)));
		add(new ListView<Insurance>("guestlist", new LoadableDetachableModel<List<Insurance>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Insurance> load() {
				if (order.isInsurance()) return order.getInsuranceList();
				return Collections.emptyList();
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Insurance> item) {
				item.add(new Label("guest_index", item.getIndex() + 1));
				item.add(new Label("fullname", item.getModelObject().getFirst_name() + " " + item.getModelObject().getLast_name()));
				item.add(new Label("nationality", item.getModelObject().getNationality().getName()));
			}
		});
		final List<MyBean> otherAdditionalServiceList = ReserveUtil.getOtherAdditionalServicesByReserve(order); 
		WebMarkupContainer other_additional_service_container;
		add(other_additional_service_container = new WebMarkupContainer("other_additional_service_container"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return otherAdditionalServiceList.size() > 0;
			}
		});
		other_additional_service_container.add(new ListView<MyBean>("list", new LoadableDetachableModel<List<MyBean>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected List<MyBean> load() {
				return otherAdditionalServiceList;
			}
		}) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<MyBean> item) {
				final MyBean obj = (MyBean) item.getDefaultModelObject();
				item.add(new Label("name", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						if (obj.getType().equals(AdditionalServicePrice.Insurance)) {
							return getString("touragents.reservation.guest.service.type.insurance");
						} else if (obj.getType().equals(AdditionalServicePrice.ArrivalTransfer)) {
							return getString("touragents.reservation.guest.service.type.arrival");
						} else if (obj.getType().equals(AdditionalServicePrice.ArrivalAirServiceTypeGreenHall)) {
							return getString("touragents.reservation.guest.service.type.air.green_hall");
						} else if (obj.getType().equals(AdditionalServicePrice.ArrivalAirServiceTypeVipHall)) {
							return getString("touragents.reservation.guest.service.type.air.vip_hall");
						} else if (obj.getType().equals(AdditionalServicePrice.DepartureTransfer)) {
							return getString("touragents.reservation.guest.service.type.departure");
						} else if (obj.getType().equals(AdditionalServicePrice.DepartureAirServiceTypeGreenHall)) {
							return getString("touragents.reservation.guest.service.type.air.green_hall");
						} else if (obj.getType().equals(AdditionalServicePrice.DepartureAirServiceTypeVipHall)) {
							return getString("touragents.reservation.guest.service.type.air.vip_hall");
						}
						return null;
					}
				}).setEscapeModelStrings(false));
				item.add(new Label("quantity", new AbstractReadOnlyModel<Integer>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Integer getObject() {
						return obj.getCount();
					}
				}));
				item.add(new Label("days", new AbstractReadOnlyModel<Short>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Short getObject() {
						if (obj.getType().equals(AdditionalServicePrice.Insurance)) return order.getDays();
						return null;
					}
				}));
				item.add(new Label("amount", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						BigDecimal val = BigDecimal.valueOf(obj.getCost());
						total = total.add(val);
						
						return new StringResourceModel("touragents.newbooking.total_price_label", null, new Object[] {
								((MySession) getSession()).getCurrencyName(), CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), val)}).getString();
					}
				}));
			}
		});
		other_additional_service_container.add(new Label("total", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new StringResourceModel("touragents.newbooking.total_price_label", null, new Object[] {
						((MySession) getSession()).getCurrencyName(), CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), total)}).getString();
			}
		}));
		
		add(new Label("grand_total", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new StringResourceModel("touragents.newbooking.total_price_label", null, new Object[] {
						((MySession) getSession()).getCurrencyName(), CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), total)}).getString();
			}
		}));
		add(new AgreeForm("agreeform", order).add(new Button("back"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						breadCrumbModel.allBreadCrumbParticipants().clear();
						return new AdditionalServiceOrderPanel(componentId, breadCrumbModel, order, true);
					}
				});
			}
		}));
	}
	
	public class AgreeForm extends Form<Void>{
		private FeedbackPanel feedback;
		private boolean agree;
		private boolean adding = true;
		
		public AgreeForm(String id, final AdditionalServiceOrder order) {
			super(id);
			add(feedback = new MyFeedbackPanel("feedback"));
			feedback.setOutputMarkupId(true);
			final AjaxButton confirm;
			add(confirm = new AjaxButton("confirm") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if (order.getId() != null) adding = false;
					SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
					try {
						if (order.getArrival_date().after(order.getDeparture_date())) {
							feedback.error(getString("date.validator.before.check_out_in"));
							return;
						}
						order.setCreator_user_id(((MySession) getSession()).getUser().getId());
						order.setInitiator_user_id(((MySession) getSession()).getUser().getId());
						order.setTotal(total);
						if (order.isInsurance() || (CommonUtil.nvl(order.getArrival().getTransport_type(), AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN || 
	        					  					  CommonUtil.nvl(order.getDeparture().getTransport_type(), AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN)) {
							if (sql.update("updateAdditionalServiceOrderById", order) == 0) sql.insert("insertAdditionalServiceOrder", order);
							if (order.isInsurance() && ! order.getInsuranceList().isEmpty()) {
								for (Insurance insurance : order.getInsuranceList()){
									insurance.setAdditionalserviceorders_id(order.getId());
									insurance.setCreator_user_id(((MySession) getSession()).getUser().getId());
									insurance.setInitiator_user_id(((MySession) getSession()).getUser().getId());
									insurance.setPeriod_from_date(order.getArrival_date());
									insurance.setPeriod_to_date(order.getDeparture_date());
									insurance.setWith_hotel(true);
									if (sql.update("updateInsurance", insurance) == 0) sql.insert("insertInsurance", insurance);
								}
							}
							order.getArrival().setAdditionalserviceorders_id(order.getId());
							if (order.getArrival().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) {
								order.getArrival().setCreator_user_id(((MySession) getSession()).getUser().getId());
								order.getArrival().setInitiator_user_id(((MySession) getSession()).getUser().getId());
								order.getArrival().setWith_hotel(true);
								if (sql.update("updateAdditionalServiceDetail", order.getArrival()) == 0)
								sql.insert("insertAdditionalServiceDetail", order.getArrival());
								
								if (order.getArrival().getAir_service_type() != AdditionalServiceDetail.AIR_SERVICE_TYPE_UNKNOWN) {
									for (Guest guest : order.getArrival().getGuestlist()){
										guest.setAdditionalservicedetails_id(order.getArrival().getId());
										guest.setInitiator_user_id(((MySession) getSession()).getUser().getId());
										if (guest.getPerson_title() == null) guest.setPerson_title(new PersonTitle(PersonTitle.UNKNOWN));
										if (sql.update("updateGuestForAdditionalService", guest) == 0) sql.insert("insertGuestForAdditionalService", guest);
									}
								}
							} else sql.delete("deleteAdditionalServiceDetail", order.getArrival());

							order.getDeparture().setAdditionalserviceorders_id(order.getId());
							if (order.getDeparture().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) {
								order.getDeparture().setCreator_user_id(((MySession) getSession()).getUser().getId());
								order.getDeparture().setInitiator_user_id(((MySession) getSession()).getUser().getId());
								order.getDeparture().setWith_hotel(true);
								if (sql.update("updateAdditionalServiceDetail", order.getDeparture()) == 0)
								sql.insert("insertAdditionalServiceDetail", order.getDeparture());
								
								if (order.getDeparture().getAir_service_type() != AdditionalServiceDetail.AIR_SERVICE_TYPE_UNKNOWN) {
									for (Guest guest : order.getDeparture().getGuestlist()){
										guest.setAdditionalservicedetails_id(order.getDeparture().getId());
										guest.setInitiator_user_id(((MySession) getSession()).getUser().getId());
										if (guest.getPerson_title() == null) guest.setPerson_title(new PersonTitle(PersonTitle.UNKNOWN));
										if (sql.update("updateGuestForAdditionalService", guest) == 0) sql.insert("insertGuestForAdditionalService", guest);
									}
								}
							} else sql.delete("deleteAdditionalServiceDetail", order.getDeparture());
						} else sql.delete("deleteInsuranceByReserveId", order.getId());
						sql.commit();
						if (adding) { 
							feedback.success(getString("additional.service.insert.success"));
							CIPHallEmailNotifier.send(order.getId());
							GreenHallEmailNotifier.send(order.getId());
							TaxiEmailNotifier.send(order.getId());
							InsuranceEmailNotifier.send(order.getId());
						} else {
							CIPHallEmailNotifier.send(order.getId());
							GreenHallEmailNotifier.send(order.getId());
							TaxiEmailNotifier.send(order.getId());
							InsuranceEmailNotifier.send(order.getId());
							feedback.success(getString("additional.service.update.success"));
						}
						setResponsePage(new AdditionalServicePage(AdditionalServicePage.LIST));
					} catch (Exception e) {
						logger.error("Exception", e);
						if (adding) feedback.error(getString("additional.service.insert.fail"));
						else feedback.error(getString("additional.service.update.fail"));
						sql.rollback();
					} finally {
						sql.close();
						target.add(feedback);
					}
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				public boolean isVisible() {
					return getMySession().getUser().isWorkable();
				}
			});
			
			confirm.setEnabled(false);
			confirm.setOutputMarkupId(true);
			
			final WebMarkupContainer cancellation_policy_container;
			add(cancellation_policy_container = new WebMarkupContainer("cancellation_policy_container"));
			cancellation_policy_container.setVisible(false);
			cancellation_policy_container.setOutputMarkupPlaceholderTag(true);
			
			add(new AjaxCheckBox("agree", new PropertyModel<Boolean>(this, "agree")) {
				private static final long serialVersionUID = 1L;
	
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					confirm.setEnabled(Strings.isTrue(getValue()));
					cancellation_policy_container.setVisible(confirm.isEnabled());
					target.add(confirm);
					target.add(cancellation_policy_container);
				}
			});
		}
		

		public boolean isAgree() {
			return agree;
		}

		public void setAgree(boolean agree) {
			this.agree = agree;
		}
		
		private static final long serialVersionUID = 1L;
	}	
	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.reservation.guest.service.summary.confirm", null);
	}
	
	@Override
	protected void onBeforeRender() {
		total = new BigDecimal("0");
		super.onBeforeRender();
	}
}
