package uz.hbs.actions.touragent.newbooking.individual;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.value.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.touragent.TourAgentPage;
import uz.hbs.actions.touragent.newbooking.SearchForBookingPanel;
import uz.hbs.actions.touragent.newbooking.panel.TAAdditionalServicePanel;
import uz.hbs.actions.touragent.newbooking.panel.TAReserveSummaryPanel;
import uz.hbs.beans.Booking;
import uz.hbs.beans.ChildAge;
import uz.hbs.beans.Guest;
import uz.hbs.beans.IndividualReservation;
import uz.hbs.beans.MealOption;
import uz.hbs.beans.Nationality;
import uz.hbs.beans.PersonTitle;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.RoomType;
import uz.hbs.components.ajax.AjaxCheckPeriodCallBack;
import uz.hbs.components.ajax.AjaxMealOptionCallBackOther;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.DateTimeTextField;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.ReserveUtil;
import uz.hbs.utils.models.MyAjaxCallListener;
import uz.hbs.utils.models.PersonTitleModel;

public class TAIndividualReservationPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger _log = LoggerFactory.getLogger(TAIndividualReservationPanel.class);
	private MyFeedbackPanel feedback;
	private ModalWindow dialog;
	private Label totalPrice;
	private Label amountOfGuestsLabel;
	private BigDecimal totalrate;

	public TAIndividualReservationPanel(String id, IBreadCrumbModel breadCrumbModel, IModel<IndividualReservation> model, final Booking filter) {
		this(id, breadCrumbModel, model, filter, true);
	}

	public TAIndividualReservationPanel(String id, IBreadCrumbModel breadCrumbModel, IModel<IndividualReservation> model, final Booking filter,
			final boolean isEditable) {
		super(id, breadCrumbModel, model);

		add(dialog = new ModalWindow("dialog"));

		final IndividualReservation reserve = model.getObject();
		if (filter != null)
			reserve.setResident(filter.isResident());

		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);

		add(new Label("hotel_name", new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new MyBatisHelper().selectOne("selectHotelAndCityName", reserve.getHotelsusers_id());
			}
		}).setRenderBodyOnly(true));

		final ReservationRuleType rule = new MyBatisHelper().selectOne("selectTAReservationRule",
				new ReservationRuleType(reserve.getHotelsusers_id(), reserve.isIs_group()));

		if (rule == null)
			feedback.error(new StringResourceModel("hotels.reservation.reservation_rule_undefined", null).getString());

		if (totalrate == null)
			totalrate = CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), reserve.getTotal());

		add(totalPrice = new Label("totalPrice", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new StringResourceModel("touragents.newbooking.total_price_label", null,
						new Object[] { ((MySession) getSession()).getCurrencyName(),
								CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), totalrate) }).getString();
			}
		}));
		totalPrice.setOutputMarkupId(true);

		Link<Void> newBookingLink = new Link<Void>("newBookingLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TourAgentPage(TourAgentPage.NEW_BOOKING, false, null, null));
			}

			@Override
			public boolean isVisible() {
				return isEditable;
			}
		};
		add(newBookingLink);

		Link<Void> backToSearchResultsLink = new Link<Void>("backToSearchResultsLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						activate(breadCrumbModel.allBreadCrumbParticipants().get(0));
						return new SearchForBookingPanel(componentId, breadCrumbModel, filter);
					}
				});
			}

			@Override
			public boolean isVisible() {
				return filter != null && isEditable;
			}
		};
		add(backToSearchResultsLink);

		ReservationRoom reserveroom = (ReservationRoom) reserve.getReserverooms().get(0);

		if (reserveroom.getRoomtype() == null) {
			if (reserve.getId() != null)
				reserveroom.setRoomtype((RoomType) new MyBatisHelper().selectOne("selectReservationRoomType", reserve.getId()));
			else
				reserveroom.setRoomtype(new RoomType("Unkhown"));
		}

		add(new Label("amountOfNightsLabel",
				new StringResourceModel("touragents.newbooking.price_of_nights", null, new Object[] { reserve.getNumber_of_nights() })));
		add(amountOfGuestsLabel = new Label("amountOfGuestsLabel", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				short adults = 0;
				for (ReservationRoom reserveroom : reserve.getReserverooms()) {
					adults += reserveroom.getHolding_capacity();
				}
				return new StringResourceModel("touragents.newbooking.for_number_of_guests", null, new Object[] { adults }).getString();
			}
		}));
		amountOfGuestsLabel.setOutputMarkupId(true);

		add(new Label("amountOfChildrenLabel", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				short childs = 0;
				for (ReservationRoom reserveroom : reserve.getReserverooms()) {
					childs += (short) reserveroom.getChildAgeList().size();
				}
				return new StringResourceModel("touragents.newbooking.for_number_of_childs", null, new Object[] { childs }).getString();
			}
		}));

		add(new Label("checkInLabel", DateUtil.toString(reserve.getCheck_in(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
		add(new Label("checkOutLabel", DateUtil.toString(reserve.getCheck_out(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
		add(new Label("nightsLabel", reserve.getNumber_of_nights()));
		add(new Label("nightsSuffixLabel", new StringResourceModel(
				"touragents.newbooking.nights." + (reserve.getNumber_of_nights() > 5 ? 5 : reserve.getNumber_of_nights()), null)));
		add(new Label("ageLabel", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				short adults = 0;
				short childs = 0;
				String childAgesForDisplay = "";
				for (ReservationRoom reserveroom : reserve.getReserverooms()) {
					adults += reserveroom.getHolding_capacity();
					childs += (short) reserveroom.getChildAgeList().size();
					for (ChildAge age : reserveroom.getChildAgeList()) {
						childAgesForDisplay += childAgesForDisplay.isEmpty() ? age.getAge() : ", " + age.getAge();
					}
				}

				return new StringResourceModel("touragents.newbooking.adults", null).getString() + ": " + adults + ", "
						+ new StringResourceModel("touragents.newbooking.children", null).getString() + ": " + childs
						+ (childs > 0 ? " (" + childAgesForDisplay + ")" : "");
			}
		}));

		add(new ListView<RoomType>("roomtypelist", new LoadableDetachableModel<List<RoomType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<RoomType> load() {
				return ReserveUtil.getRoomTypesByReserve(reserve);
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<RoomType> item) {
				RoomType roomtype = (RoomType) item.getDefaultModelObject();
				item.add(new Label("room_count", roomtype.getNumber_of_rooms()));
				item.add(new Label("roomtype", roomtype.getName()));
			}
		});

		final Form<IndividualReservation> reserveform;
		final Button done;

		add(reserveform = new ReserveForm("form", reserve, rule) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onAfterRender() {
				super.onAfterRender();
				if (!isEditable)
					JavaScriptUtils.writeJavaScript(getResponse(), "$(document).ready(function(){ $form = $('form#" + this.getMarkupId()
							+ "'); $('input,select,textarea,a', $form).each(function(){ $(this).attr('disabled','disabled'); }); });");
			}
		});

		reserveform.add(new Link<Void>("cancel") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						BreadCrumbPanel parent = (BreadCrumbPanel) breadCrumbModel.allBreadCrumbParticipants().get(breadCrumbModel.allBreadCrumbParticipants().size() - 2);
						activate(parent);
						return parent;
					}
				});
			}

			@Override
			public boolean isVisible() {
				return isEditable;
			}
		});

		reserveform.add(done = new Button("ready") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				final IndividualReservation reserve = (IndividualReservation) reserveform.getDefaultModelObject();
				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						return new TAReserveSummaryPanel(componentId, breadCrumbModel, reserve, rule);
					}
				});
			}

			@Override
			public boolean isVisible() {
				return isEditable;
			}
		});
		final TAAdditionalServicePanel additional_option;
		reserveform.add(additional_option = new TAAdditionalServicePanel("additional_option", reserve));
		additional_option.setOutputMarkupPlaceholderTag(true);
		additional_option
				.setVisible((Boolean) new MyBatisHelper().selectOne("selectAdditionalServiceTashkent", model.getObject().getHotelsusers_id()));
		additional_option.setEnabled(!reserve.isPayment_owner());
		done.setOutputMarkupId(true);

		final WebMarkupContainer payment_owner_info;
		reserveform.add(payment_owner_info = new WebMarkupContainer("payment_owner_info") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return !additional_option.isEnabled();
			}
		});
		payment_owner_info.setOutputMarkupPlaceholderTag(true);

		reserveform.add(new AjaxCheckBox("payment_owner") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				additional_option.setEnabled(!Strings.isTrue(getValue()));
				target.add(additional_option);
				target.add(payment_owner_info);
			}
			
			@Override
			public boolean isEnabled() {
				return new MyBatisHelper().selectOne("selectHotelSelfPaymentPossibility", reserve.getHotelsusers_id());
			}
		});

		CommonUtil.setFormComponentRequired(reserveform);
	}

	private class ReserveForm extends Form<IndividualReservation> {
		private static final long serialVersionUID = 1L;
		private String javascript2;
		private DateTimeTextField check_in;
		private DateTimeTextField check_out;
		private String javascript = "";

		public ReserveForm(String id, final IndividualReservation reserve, final ReservationRuleType rule) {
			super(id, new CompoundPropertyModel<IndividualReservation>(reserve));

			final WebMarkupContainer container;
			add(container = new WebMarkupContainer("container"));
			container.setOutputMarkupId(true);

			container.add(new ListView<ReservationRoom>("reserveroomlist", new LoadableDetachableModel<List<ReservationRoom>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<ReservationRoom> load() {
					return reserve.getReserverooms();
				}
			}) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final ListItem<ReservationRoom> item) {
					boolean extra_bed = false;
					_log.debug("Extra bed: " + item.getModelObject().isExtra_bed_needed());
					final HashMap<String, Object> param = new HashMap<String, Object>();
					param.put("hotel_id", reserve.getHotelsusers_id());

					item.getModelObject().setIndex((short) (item.getIndex() + 1));

					item.add(new Label("reserveOrder", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return String.valueOf(item.getModelObject().getIndex());
						}
					}));

					final DropDownChoice<RoomType> roomtype;

					WebMarkupContainer reserve_edit;
					item.add(reserve_edit = new WebMarkupContainer("reserve_edit"));
					reserve_edit.setVisible(reserve.getId() != null);

					reserve_edit.add(roomtype = new DropDownChoice<RoomType>("roomtype", new PropertyModel<RoomType>(item.getModel(), "roomtype"),
							new LoadableDetachableModel<List<RoomType>>() {
								private static final long serialVersionUID = 1L;

								@Override
								protected List<RoomType> load() {
									return new MyBatisHelper().selectList("selectReservationRoomTypeByHotel", param);
								}
							}, new ChoiceRenderer<RoomType>("name", "id")) {
						private static final long serialVersionUID = 1L;

						protected boolean isDisabled(RoomType object, int index, String selected) {
							if (item.getModelObject().getId() == null && !selected.equals(String.valueOf(object.getId())))
								return true;
							return super.isDisabled(object, index, selected);
						};
					});
					if (reserve.getId() == null)
						roomtype.add(new AttributeModifier("readonly", true));

					WebMarkupContainer reserve_add;
					item.add(reserve_add = new WebMarkupContainer("reserve_add"));
					reserve_add.setVisible(reserve.getId() == null);
					reserve_add.add(new Label("roomtype_label", item.getModelObject().getRoomtype().getName()));

					item.add(new AjaxLink<Void>("delete") {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target) {
							ReservationRoom temp = reserve.getReserverooms().get(item.getIndex());
							short guests = (short) item.getModelObject().getGuestlist().size();

							if (temp.getId() != null) {
								SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
								try {
									sql.delete("deleteGuestByReserveRoom", temp.getId());
									sql.delete("deleteReservationRoomById", temp.getId());
									reserve.getReserverooms().remove(item.getIndex());
									sql.commit();
								} catch (Exception e) {
									sql.rollback();
									logger.error("Exception", e);
								} finally {
									sql.close();
								}
							} else
								reserve.getReserverooms().remove(item.getIndex());

							reserve.setAdults((short) (reserve.getAdults() - guests));
							target.add(amountOfGuestsLabel);
							ReserveUtil.recalcTAIndividualTotalAmount(reserve, rule);
							target.add(totalPrice);
							target.add(container);
							target.appendJavaScript("return changeMealOption(); ");
						}

						@Override
						public boolean isVisible() {
							return reserve.getReserverooms().size() > 1;
						};

						@Override
						public boolean isEnabled() {
							return reserve.getId() != null;
						};

						@Override
						protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
							super.updateAjaxAttributes(attributes);
							attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
							IAjaxCallListener listener = new AjaxCallListener() {
								private static final long serialVersionUID = 1L;

								//								@Override
								//								public CharSequence getBeforeSendHandler(Component component) {
								//									return "return setMealOtions();";
								//								};

								@Override
								public CharSequence getCompleteHandler(Component component) {
									return "return getMealOptions();";
								};
							};
							attributes.getAjaxCallListeners().add(listener);
						}
					});

					if (item.getModelObject().getGuestlist().isEmpty()) {
						param.put("roomtype_id", item.getModelObject().getRoomtype().getId());
						short holding_capacity = new MyBatisHelper().selectOne("selectHoldingCapacityRoomType",
								item.getModelObject().getRoomtype().getId());
						_log.debug("Holding capacity: " + item.getModelObject().getHolding_capacity());
						if (holding_capacity < item.getModelObject().getHolding_capacity()) {
							holding_capacity = item.getModelObject().getHolding_capacity();
							extra_bed = true;
						} else if (holding_capacity > item.getModelObject().getHolding_capacity()) {
							holding_capacity = item.getModelObject().getHolding_capacity();
						}

						item.getModelObject().setGuest_count(holding_capacity);

						List<Guest> guestlist = getGuestDetail(holding_capacity);
						item.getModelObject().setHolding_capacity(holding_capacity);
						item.getModelObject().setGuestlist(guestlist);
					}

					final ListView<Guest> listview;

					item.add(listview = new ListView<Guest>("guests", new LoadableDetachableModel<List<Guest>>() {
						private static final long serialVersionUID = 1L;

						@Override
						protected List<Guest> load() {
							return item.getModelObject().getGuestlist();
						}
					}) {
						private static final long serialVersionUID = 1L;

						@Override
						protected void populateItem(final ListItem<Guest> guestItem) {
							guestItem.add(new GuestDataPanel("guestdetails", new Model<Guest>(guestItem.getModelObject()), item.getModelObject(),
									guestItem.getIndex(), rule.getMinimum_free_age(), item.getIndex(), reserve.isResident()) {
								private static final long serialVersionUID = 1L;

								@Override
								protected void onDelete(AjaxRequestTarget target) {
									Guest temp = item.getModelObject().getGuestlist().get(guestItem.getIndex());
									if (temp.getId() != null) {
										new MyBatisHelper().delete("deleteGuestById", temp.getId());
										item.getModelObject().getGuestlist().remove(guestItem.getIndex());
									} else
										item.getModelObject().getGuestlist().remove(guestItem.getIndex());
									target.add(amountOfGuestsLabel);
									ReserveUtil.recalcTAIndividualTotalAmount(reserve, rule);
									item.getModelObject().setHolding_capacity((short) (item.getModelObject().getHolding_capacity() - 1));
									target.add(totalPrice);
									target.add(container);
								}

								@Override
								protected boolean isVisibleDelete() {
									return reserve.getId() != null && guestItem.getIndex() >= 1;
								}

								@Override
								protected void onAppend(AjaxRequestTarget target) {
									item.getModelObject().getGuestlist().add(new Guest((short) (item.getModelObject().getGuestlist().size() + 1)));
									target.add(amountOfGuestsLabel);
									ReserveUtil.recalcTAIndividualTotalAmount(reserve, rule);
									item.getModelObject().setHolding_capacity((short) (item.getModelObject().getHolding_capacity() + 1));
									target.add(totalPrice);
									target.add(container);
								}

								@Override
								protected boolean isVisibleAppend() {
									short roomtype_holding_capacity = new MyBatisHelper().selectOne("selectHoldingCapacityRoomType",
											item.getModelObject().getRoomtype().getId());
									return reserve.getId() != null && roomtype_holding_capacity > (short) (item.getModelObject().getAdults_count()
											- (item.getModelObject().isExtra_bed_needed() ? 1 : 0));
								}

								@Override
								protected void onSelectAge(AjaxRequestTarget target) {
									target.add(container);
								}

								@Override
								protected boolean isActionEnabled() {
									return reserve.getId() != null;
								}
							});
						}
					});
					listview.setOutputMarkupId(true);
					roomtype.add(new AjaxFormComponentUpdatingBehavior("onchange") {
						private static final long serialVersionUID = 1L;
						private Integer roomtype_id;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							try {
								param.put("roomtype_id", item.getModelObject().getRoomtype().getId());
								param.put("check_in", reserve.getCheck_in());
								param.put("check_out", reserve.getCheck_out());
								param.put("reserve_id", reserve.getId());
								short count = new MyBatisHelper().selectOne("selectTAReserveAvailableRoomsByRoomType", param);
								if (count == 0) {
									feedback.error(new StringResourceModel("hotels.reservation.room.vacant.not.found", null,
											new Object[] { item.getModelObject().getRoomtype().getName() }).getString());
									item.getModelObject().getRoomtype().setId(roomtype_id);
									target.add(feedback);
									return;
								} else if (count > 0) {
									short holding_capacity = new MyBatisHelper().selectOne("selectHoldingCapacityRoomType",
											item.getModelObject().getRoomtype().getId());
									if (item.getModelObject().getGuestlist().size() < holding_capacity) {
										List<Guest> guestlist = new ArrayList<Guest>();
										for (short guest = (short) (item.getModelObject().getGuestlist().size()
												+ 1); guest <= holding_capacity; guest++) {
											guestlist.add(new Guest(guest));
										}
										item.getModelObject().getGuestlist().addAll(guestlist);
										item.getModelObject().setHolding_capacity(holding_capacity);
										ReserveUtil.recalcTAIndividualTotalAmount(reserve, rule);
										target.add(totalPrice);
									} else if (item.getModelObject().getGuestlist().size() > holding_capacity) {
										item.getModelObject().getRoomtype().setId(roomtype_id);
										target.appendJavaScript("alert('Please, first need to remove needless person(s)');");
									} else {
										ReserveUtil.recalcTAIndividualTotalAmount(reserve, rule);
										target.add(totalPrice);
									}
								}
							} finally {
								target.add(container);
							}
						}

						@Override
						protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
							super.updateAjaxAttributes(attributes);
							IAjaxCallListener listener = new AjaxCallListener() {
								private static final long serialVersionUID = 1L;

								@Override
								public CharSequence getBeforeSendHandler(Component component) {
									roomtype_id = Integer.parseInt(roomtype.getValue());
									return super.getBeforeSendHandler(component);
								}

								@Override
								public CharSequence getCompleteHandler(Component component) {
									return "return getMealOptions();";
								};
							};
							attributes.getAjaxCallListeners().add(listener);
						}
					});

					final HiddenField<Byte> meal_options;
					item.add(meal_options = new HiddenField<Byte>("meal_options", new PropertyModel<Byte>(item.getModel(), "meal_options")));
					meal_options.setLabel(new StringResourceModel("hotels.reservation.details.meal_options", null));
					meal_options.setRequired(true);
					meal_options.setOutputMarkupId(true);
					meal_options.add(new AttributeModifier("data-guest", item.getModelObject().getHolding_capacity()));

					if (reserve.getId() == null && !item.getModelObject().isExtra_bed_needed())
						item.getModelObject().setExtra_bed_needed(extra_bed);

					final Label extra_bed_cost_label;
					item.add(extra_bed_cost_label = new Label("extra_bed_cost_label", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							//if (reserve.isResident()) {
							return new StringResourceModel("touragents.newbooking.total_price_label", null,
									new Object[] { ((MySession) getSession()).getCurrencyName(), CommonUtil.currencyConverted(
											((MySession) getSession()).getCurrency(), new BigDecimal(rule.getExtra_bed_price_type_value())) })
													.getString();
							//							} else {
							//								   return new StringResourceModel("touragents.newbooking.total_price_label", null, new Object[] {
							//											((MySession) getSession()).getCurrencyName(), rule.getExtra_bed_price_type_value()}).getString();
							//							}
						}
					}));
					extra_bed_cost_label.setOutputMarkupPlaceholderTag(true);
					extra_bed_cost_label.setVisible(item.getModelObject().isExtra_bed_needed());

					_log.debug("Extra bed: " + item.getModelObject().isExtra_bed_needed());

					item.add(new AjaxCheckBox("extra_bed_needed", new PropertyModel<Boolean>(item.getModel(), "extra_bed_needed")) {
						private static final long serialVersionUID = 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							extra_bed_cost_label.setVisible(Strings.isTrue(getValue()));
							Double d = CommonUtil.nvl(rule.getExtra_bed_price_type_value());
							if (extra_bed_cost_label.isVisible()) {
								totalrate = reserve.getTotal().add(BigDecimal.valueOf(d * reserve.getNumber_of_nights()));
								//								item.getModelObject().setRate(item.getModelObject().getRate().add(BigDecimal.valueOf(d * reserve.getNumber_of_nights())));
							} else {
								totalrate = reserve.getTotal().subtract(BigDecimal.valueOf(d * reserve.getNumber_of_nights()));
								//								item.getModelObject().setRate(item.getModelObject().getRate().subtract(BigDecimal.valueOf(d * reserve.getNumber_of_nights())));
							}
							item.getModelObject().setExtra_bed_cost(BigDecimal.valueOf(d * reserve.getNumber_of_nights()));
							reserve.setTotal(totalrate);
							target.add(totalPrice);
							target.add(container);
							target.add(extra_bed_cost_label);
						}

						@Override
						public boolean isEnabled() {
							return (Boolean) new MyBatisHelper().selectOne("selectExtraBedNeed", item.getModelObject().getRoomtype().getId());
						}

						@Override
						protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
							super.updateAjaxAttributes(attributes);
							IAjaxCallListener listener = new AjaxCallListener() {
								private static final long serialVersionUID = 1L;

								@Override
								public CharSequence getSuccessHandler(Component component) {
									return "return getMealOptions();";
								};
							};
							attributes.getAjaxCallListeners().add(listener);
						}
					});
					item.add(new AjaxCheckBox("non_smokers", new PropertyModel<Boolean>(item.getModel(), "non_smokers")) {
						private static final long serialVersionUID = 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
						}
					});
					item.add(new AjaxCheckBox("city_view", new PropertyModel<Boolean>(item.getModel(), "city_view")) {
						private static final long serialVersionUID = 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
						}
					});
					item.add(new Label("meal_options_cost_label", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return new StringResourceModel("hotels.reservation.details.meal_options_cost_label", null,
									new Object[] { ((MySession) getSession()).getCurrencyName() }).getString();
						}
					}));
				}
			});

			add(new AjaxMealOptionCallBackOther() {
				private static final long serialVersionUID = 1L;

				@Override
				protected void getJavaScript(String jscript) {
					javascript = jscript;
				}

				@Override
				protected void onUpdate(AjaxRequestTarget target, String objId, byte objVal, short guest_count) {
					Component component = ReserveForm.this.get(objId);
					byte oldVal = (byte) component.getDefaultModelObject();

					_log.debug("Meal Option { new = " + objVal + ", old = " + oldVal + ", guest = " + guest_count + " }");

					component.setDefaultModelObject(objVal);
					target.add(component);
					Double d = 0d;

					byte temp = (byte) (objVal - oldVal);

					if (temp > 0) {
						if (temp == 3) {
							Double d1 = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
									new MealOption(MealOption.LUNCH, reserve.getHotelsusers_id())));
							Double d2 = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
									new MealOption(MealOption.DINNER, reserve.getHotelsusers_id())));
							d = 1d * ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.LUNCH, d1)
									+ 1d * ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.DINNER, d2);
						} else {
							d = 1d * ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), temp,
									CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
											new MealOption(temp, reserve.getHotelsusers_id()))));
						}
						totalrate = reserve.getTotal().add(BigDecimal.valueOf(d * guest_count));
					} else {
						temp = (byte) (-1 * temp);
						if (temp == 3) {
							Double d1 = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
									new MealOption(MealOption.LUNCH, reserve.getHotelsusers_id())));
							Double d2 = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
									new MealOption(MealOption.DINNER, reserve.getHotelsusers_id())));
							d = 1d * ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.LUNCH, d1)
									+ 1d * ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.DINNER, d2);
						} else {
							d = 1d * ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), temp,
									CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
											new MealOption(temp, reserve.getHotelsusers_id()))));
						}
						totalrate = reserve.getTotal().subtract(BigDecimal.valueOf(d * guest_count));
					}
					reserve.setTotal(totalrate);
					target.add(totalPrice);
				}
			});

			add(new Label("jscript", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return javascript;
				}
			}).setEscapeModelStrings(false));

			container.add(new AjaxLink<Void>("add") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					dialog.setTitle(new StringResourceModel("hotels.reservation.details.reservation.roomtype.add", null));
					dialog.setInitialHeight(100);
					dialog.setInitialWidth(300);
					dialog.setMinimalHeight(100);
					dialog.setMinimalWidth(300);
					dialog.setCookieName("roomtype");
					dialog.setContent(new AddRoomTypePanel(dialog.getContentId(), reserve) {
						private static final long serialVersionUID = 1L;

						@Override
						protected void onSelect(AjaxRequestTarget target, Form<?> form) {
							ValueMap model = (ValueMap) form.getDefaultModelObject();
							RoomType roomtype = (RoomType) model.get("roomtype");
							HashMap<String, Serializable> param = new HashMap<String, Serializable>();
							param.put("roomtype_id", roomtype.getId());
							param.put("hotel_id", reserve.getHotelsusers_id());
							short holding_capacity = new MyBatisHelper().selectOne("selectHoldingCapacityRoomType", roomtype.getId());
							List<Guest> guestlist = getGuestDetail(holding_capacity);
							ReservationRoom reserveroom = new ReservationRoom();
							reserveroom.setRoomtype(roomtype);
							reserveroom.setRoom_count((short) 1);
							reserveroom.setHolding_capacity(holding_capacity);
							reserveroom.setGuestlist(guestlist);
							reserve.getReserverooms().add(reserveroom);
							reserve.setAdults((short) (reserve.getAdults() + holding_capacity));
							target.add(amountOfGuestsLabel);
							target.add(container);
							target.appendJavaScript("return changeMealOption(); ");
							ReserveUtil.recalcTAIndividualTotalAmount(reserve, rule);
							target.add(totalPrice);
							dialog.close(target);
						}
					});
					dialog.show(target);
				}

				@Override
				public boolean isVisible() {
					return reserve.getId() != null;
				}
			});

			add(new HiddenField<String>("breakfast_cost", new Model<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					Double d = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
							new MealOption(MealOption.BREAKFAST, reserve.getHotelsusers_id())));
					if (reserve.isResident())
						return String.valueOf(CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), BigDecimal.valueOf(d)));
					else
						return String.valueOf(d);

				}
			}).add(new AttributeModifier("data-currency", getMySession().getCurrencyName())));
			add(new HiddenField<String>("lunch_cost", new Model<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					Double d = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
							new MealOption(MealOption.LUNCH, reserve.getHotelsusers_id())));
					if (reserve.isResident())
						return String.valueOf(CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), BigDecimal.valueOf(d)));
					else
						return String.valueOf(d);
				}
			}).add(new AttributeModifier("data-currency", getMySession().getCurrencyName())).setEscapeModelStrings(false));
			add(new HiddenField<String>("dinner_cost", new Model<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					Double d = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
							new MealOption(MealOption.DINNER, reserve.getHotelsusers_id())));
					if (reserve.isResident())
						return String.valueOf(CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), BigDecimal.valueOf(d)));
					else
						return String.valueOf(d);
				}
			}).add(new AttributeModifier("data-currency", getMySession().getCurrencyName())));

			RadioGroup<Byte> reservation_type;
			add(reservation_type = new RadioGroup<Byte>("reservation_type"));
			reservation_type.add(new Radio<Byte>("definite", new Model<Byte>(ReservationType.DEFINITE)));
			reservation_type.add(new Radio<Byte>("tentative", new Model<Byte>(ReservationType.TENTATIVE)) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible() {
					return rule.getReservationcancellationpolicy().isSupport_tentative_reservation();
				}

				@Override
				public boolean isEnabled() {
					return !(reserve.getId() != null && reserve.getReservation_type() == ReservationType.TENTATIVE);
				}
			});

			Label tentativeLabel = new Label("tentativeLabel",
					new StringResourceModel("hotels.reservation.details.reservation.type.tentative", null));
			tentativeLabel.setVisible(rule.getReservationcancellationpolicy().isSupport_tentative_reservation());
			reservation_type.add(tentativeLabel);

			add(new TextArea<String>("ta_comments"));

			WebMarkupContainer reserve_details_container;
			add(reserve_details_container = new WebMarkupContainer("reserve_details_container"));
			reserve_details_container.setOutputMarkupPlaceholderTag(true);
			reserve_details_container.setVisible(false); //TODO 

			reserve_details_container.add(check_in = new DateTimeTextField("check_in") {
				private static final long serialVersionUID = 1L;

				@Override
				protected Date getDateFrom() {
					return (reserve.getId() == null ? reserve.getCheck_in() : null); //CommonUtil.now()
				}

				@Override
				protected Date getDateTo() {
					return null;
				}
			});
			check_in.setOutputMarkupId(true);
			check_in.getDateField().add(new AttributeModifier("data-bind", "check_in"));
			check_in.getDateField().setLabel(new StringResourceModel("hotels.reservation.details.check_in", null));

			reserve_details_container.add(check_out = new DateTimeTextField("check_out") {
				private static final long serialVersionUID = 1L;

				@Override
				protected Date getDateFrom() {
					return null;
				}

				@Override
				protected Date getDateTo() {
					return (reserve.getId() == null ? reserve.getCheck_out() : null);
				}
			});
			check_out.setOutputMarkupId(true);
			check_out.getDateField().add(new AttributeModifier("data-bind", "check_out"));
			check_out.getDateField().setLabel(new StringResourceModel("hotels.reservation.details.check_in", null));

			add(new AjaxCheckPeriodCallBack() {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, String check_in, String check_out, Short number_of_nights) {
					Date date_i = reserve.getCheck_in();
					Date date_o = reserve.getCheck_out();
					try {
						if (check_in != null) {
							Date date = DateUtil.parseDate(check_in, MyWebApplication.DATE_TIME_FORMAT);
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(date);
							ReserveForm.this.check_in.setTime((byte) calendar.get(Calendar.HOUR_OF_DAY));
							ReserveForm.this.check_in.setDate(calendar.getTime());
							reserve.setCheck_in(calendar.getTime());
						}
						if (check_out != null) {
							Date date = DateUtil.parseDate(check_out, MyWebApplication.DATE_TIME_FORMAT);
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(date);
							ReserveForm.this.check_out.setTime((byte) calendar.get(Calendar.HOUR_OF_DAY));
							reserve.setCheck_out(calendar.getTime());
						}
						if (number_of_nights != null)
							reserve.setNumber_of_nights(number_of_nights);
						//						reserve.setTotal(ReserveUtil.getTAIndividualTotalAmount(reserve, rule));
						target.add(totalPrice);
						if (reserve.getId() != null) {
							if (date_i.after(reserve.getCheck_in()) || date_o.before(reserve.getCheck_out())) {
								reserve.newlyReserve = true;
							} else
								reserve.newlyReserve = false;
						}
						if (reserve.newlyReserve) {
							HashMap<String, Serializable> param = new HashMap<String, Serializable>();
							param.put("check_in", reserve.getCheck_in());
							param.put("check_out", reserve.getCheck_out());
							param.put("reserve_id", reserve.getId());
							for (ReservationRoom reserveroom : reserve.getReserverooms()) {
								param.put("roomtype_id", reserveroom.getRoomtype().getId());
								short count = new MyBatisHelper().selectOne("selectTAReserveAvailableRoomsByRoomType", param);
								if (count != 0) {
									feedback.error(new StringResourceModel("hotels.reservation.room.vacant.not.found", null,
											new Object[] { reserveroom.getRoomtype().getName() }).getString());
									reserve.newlyReserve = false;
								}
							}
							if (!reserve.newlyReserve) {
								reserve.setCheck_in(date_i);
								reserve.setCheck_out(date_o);
								target.add(ReserveForm.this.check_in);
								target.add(ReserveForm.this.check_out);
							}
						}
					} catch (ParseException e) {
						logger.error("ParseException", e);
					} finally {
						target.add(feedback);
					}
				}

				@Override
				protected void getJavaScript(String jscript) {
					javascript2 = jscript;
				}
			});

			add(new Label("jscript2", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return javascript2;
				}
			}).setEscapeModelStrings(false));
		}
	}

	private abstract class GuestDataPanel extends Panel {
		private static final long serialVersionUID = 1L;
		private List<Short> selectedAgesList = new ArrayList<Short>();

		public GuestDataPanel(String id, final Model<Guest> model, final ReservationRoom reserveroom, final int guest_index, final Short free_age,
				final int reserveroom_index, final boolean resident) {
			super(id, model);

			boolean require = guest_index == 0 && reserveroom_index == 0;

			if (model.getObject().getChildAge() == null)
				model.getObject().setChildAge(new ChildAge(ChildAge.UNKNOWN, ChildAge.UNKNOWN));

			if (resident) {
				if (model.getObject().getNationality() == null) {
					model.getObject().setNationality(new Nationality("uz"));
				}
			}
			final DropDownChoice<PersonTitle> person_title;
			add(person_title = new DropDownChoice<PersonTitle>("person_title", new PropertyModel<PersonTitle>(model, "person_title"),
					PersonTitleModel.getPersonTitle(), new ChoiceRenderer<PersonTitle>("name", "title")) {
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean isDisabled(PersonTitle object, int index, String selected) {
					if (guest_index == 0 && object.getTitle().equals(PersonTitle.CHILD))
						return true;
					if (reserveroom.getChildAgeList().isEmpty() && object.getTitle().equals(PersonTitle.CHILD))
						return true;
					return super.isDisabled(object, index, selected);
				}
			});
			person_title.setNullValid(true);
			person_title.setRequired(require);
			if (require)
				person_title.add(new AttributeModifier("required", require));

			TextField<String> first_name, last_name;

			add(first_name = new TextField<String>("first_name", new PropertyModel<String>(model, "first_name")));
			first_name.setLabel(new StringResourceModel("touragents.accountant_details.first_name", null));
			first_name.setRequired(require);
			if (require)
				first_name.add(new AttributeModifier("required", require));
			first_name.setOutputMarkupId(true);
			first_name.add(new AjaxOnBlurEvent());

			add(last_name = new TextField<String>("last_name", new PropertyModel<String>(model, "last_name")));
			last_name.setLabel(new StringResourceModel("touragents.accountant_details.last_name", null));
			last_name.setRequired(require);
			if (require)
				last_name.add(new AttributeModifier("required", require));
			last_name.setOutputMarkupId(true);
			last_name.add(new AjaxOnBlurEvent());

			DropDownChoice<Nationality> nationality;
			add(nationality = new DropDownChoice<Nationality>("nationality", new PropertyModel<Nationality>(model, "nationality"),
					new LoadableDetachableModel<List<Nationality>>() {
						private static final long serialVersionUID = 1L;

						@Override
						protected List<Nationality> load() {
							return new MyBatisHelper().selectList("selectNationalityList", (resident ? "uz" : null));
						}
					}, new ChoiceRenderer<Nationality>("name", "code")));
			nationality.setNullValid(true);
			nationality.setLabel(new StringResourceModel("hotels.guest.details.nationality", null));
			nationality.add(new AjaxOnBlurEvent());
			nationality.setRequired(require);
			if (require)
				nationality.add(new AttributeModifier("required", require));

			add(new HiddenField<Short>("guest_index", new PropertyModel<Short>(model, "guest_index")));
			add(new AjaxLink<Void>("delete") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					onDelete(target);
				}

				@Override
				public boolean isVisible() {
					return isVisibleDelete();
				}

				@Override
				public boolean isEnabled() {
					return isActionEnabled();
				};

				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
					IAjaxCallListener listener = new AjaxCallListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public CharSequence getSuccessHandler(Component component) {
							return "return getMealOptions();";
						};
					};
					attributes.getAjaxCallListeners().add(listener);
				}
			});
			add(new AjaxLink<Void>("append") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					onAppend(target);
				}

				@Override
				public boolean isVisible() {
					return isVisibleAppend();
				}

				@Override
				public boolean isEnabled() {
					return isActionEnabled();
				};

				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
					IAjaxCallListener listener = new AjaxCallListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public CharSequence getCompleteHandler(Component component) {
							return "return getMealOptions();";
						};
					};
					attributes.getAjaxCallListeners().add(listener);
				}
			});
			final DropDownChoice<ChildAge> guestAge;
			add(guestAge = new DropDownChoice<ChildAge>("childAge", new PropertyModel<ChildAge>(model, "childAge"),
					new LoadableDetachableModel<List<ChildAge>>() {
						private static final long serialVersionUID = 1L;

						@Override
						protected List<ChildAge> load() {
							List<ChildAge> list = new ArrayList<ChildAge>();
							list.add(new ChildAge(ChildAge.UNKNOWN, ChildAge.UNKNOWN));
							list.addAll(reserveroom.getChildAgeList());
							return list;
						}
					}, new ChoiceRenderer<ChildAge>() {
						private static final long serialVersionUID = 1L;

						@Override
						public Object getDisplayValue(ChildAge object) {
							if (object.getChild_index() == ChildAge.UNKNOWN)
								return new StringResourceModel("hotels.guest.details.child.age", null).getString();
							return String.valueOf(object.getAge());
						}

						@Override
						public String getIdValue(ChildAge object, int index) {
							return String.valueOf(object.getChild_index());
						}
					}) {
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean isDisabled(ChildAge object, int index, String selected) {
					if (selectedAgesList.contains(object.getChild_index())) {
						if (!"".equals(CommonUtil.nvl(selected)) && object.getChild_index() == Short.valueOf(selected))
							return false;
						return true;
					}
					if (selected != null && !"".equals(selected) && !selectedAgesList.contains(Short.valueOf(selected))) {
						selectedAgesList.add(Short.valueOf(selected));
					}
					if (object.getAge() <= free_age && object.getAge() >= 0)
						return true;
					return super.isDisabled(object, index, selected);
				}

				@Override
				protected void onModelChanged() {
					if (this.getValue() != null && !"".equals(this.getValue()) && !selectedAgesList.contains(Short.valueOf(this.getValue()))) {
						selectedAgesList.add(Short.valueOf(this.getValue()));
					}
					super.onModelChanged();
				}

				@Override
				protected void onModelChanging() {
					if (this.getValue() != null && !"".equals(this.getValue()))
						selectedAgesList.remove(Short.valueOf(this.getValue()));
					super.onModelChanging();
				}
			});
			guestAge.setOutputMarkupPlaceholderTag(true);
			guestAge.setVisible(model.getObject().getChildAge().getChild_index() != ChildAge.UNKNOWN
					|| (model.getObject().getPerson_title() != null && model.getObject().getPerson_title().getTitle().equals(PersonTitle.CHILD)));
			guestAge.setRequired(guestAge.isVisible());
			guestAge.setNullValid(false);
			guestAge.setLabel(new StringResourceModel("hotels.guest.details.child.age", null));

			person_title.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (person_title.getValue() != null) {
						if (person_title.getValue().equals(PersonTitle.CHILD)) {
							guestAge.setVisible(true);
							target.appendJavaScript(" $('#" + guestAge.getMarkupId() + "').attr('required', true);");
						} else {
							guestAge.setVisible(false);
							model.getObject().setChildAge(null);
							target.appendJavaScript(" $('#" + guestAge.getMarkupId() + "').removeAttr('required');");
						}
					} else {
						guestAge.setVisible(false);
						model.getObject().setChildAge(null);
					}

					guestAge.setRequired(guestAge.isVisible());
					target.add(guestAge);
				}
			});

			guestAge.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					onSelectAge(target);
				}
			});
		}

		protected abstract void onDelete(AjaxRequestTarget target);

		protected abstract boolean isVisibleDelete();

		protected abstract void onAppend(AjaxRequestTarget target);

		protected abstract void onSelectAge(AjaxRequestTarget target);

		protected abstract boolean isVisibleAppend();

		protected abstract boolean isActionEnabled();
	}

	private abstract class AddRoomTypePanel extends Panel {
		private FeedbackPanel feedback;
		private short count = 0;

		public AddRoomTypePanel(String id, final IndividualReservation reserve) {
			super(id);
			add(feedback = new MyFeedbackPanel("feedback"));
			feedback.setOutputMarkupId(true);

			final HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("hotel_id", reserve.getHotelsusers_id()); //selectTAReserveAvailableRoomsByRoomType

			final Form<ValueMap> form;
			add(form = new Form<ValueMap>("form", new CompoundPropertyModel<ValueMap>(new ValueMap())));
			final DropDownChoice<RoomType> roomtype;
			form.add(roomtype = new DropDownChoice<RoomType>("roomtype", new LoadableDetachableModel<List<RoomType>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<RoomType> load() {
					return new MyBatisHelper().selectList("selectReservationRoomTypeByHotel", param);
				}
			}, new ChoiceRenderer<RoomType>("name", "id")));
			roomtype.setNullValid(true).setRequired(true).setLabel(new StringResourceModel("hotels.reservation.details.reservation.roomtype", null));
			final AjaxButton select;
			form.add(select = new AjaxButton("select") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					onSelect(target, form);
				}

				@Override
				public boolean isEnabled() {
					return count > 0;
				}

				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					IAjaxCallListener listener = new AjaxCallListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public CharSequence getCompleteHandler(Component component) {
							return "return getMealOptions();";
						};
					};
					attributes.getAjaxCallListeners().add(listener);
				}
			});
			roomtype.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					ValueMap model = (ValueMap) form.getDefaultModelObject();
					try {
						count = 0;
						RoomType roomtype = (RoomType) model.get("roomtype");
						if (roomtype.getId() != null) {
							HashMap<String, Serializable> param = new HashMap<String, Serializable>();
							param.put("roomtype_id", roomtype.getId());
							param.put("check_in", reserve.getCheck_in());
							param.put("check_out", reserve.getCheck_out());
							param.put("reserve_id", reserve.getId());
							count = new MyBatisHelper().selectOne("selectTAReserveAvailableRoomsByRoomType", param);
							if (count == 0)
								feedback.error(
										new StringResourceModel("hotels.reservation.room.vacant.not.found", null, new Object[] { roomtype.getName() })
												.getString());
						}
						target.add(select);
					} finally {
						target.add(feedback);
					}
				}
			});
		}

		protected abstract void onSelect(AjaxRequestTarget target, Form<?> form);

		private static final long serialVersionUID = 1L;
	}

	private List<Guest> getGuestDetail(short adults) {
		List<Guest> guestlist = new ArrayList<Guest>();
		for (short guest = 1; guest <= adults; guest++) {
			guestlist.add(new Guest(guest));
		}
		return guestlist;
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.newbooking.reservation_details", null);
	}
}
