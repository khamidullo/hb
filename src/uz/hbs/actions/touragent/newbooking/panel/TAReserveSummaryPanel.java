package uz.hbs.actions.touragent.newbooking.panel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.touragent.TourAgentPage;
import uz.hbs.beans.AdditionalServiceDetail;
import uz.hbs.beans.AdditionalServiceOrder;
import uz.hbs.beans.AdditionalServicePrice;
import uz.hbs.beans.Child;
import uz.hbs.beans.ChildAge;
import uz.hbs.beans.GroupReservation;
import uz.hbs.beans.Guest;
import uz.hbs.beans.IdAndName;
import uz.hbs.beans.IndividualReservation;
import uz.hbs.beans.Insurance;
import uz.hbs.beans.MealOption;
import uz.hbs.beans.MyBean;
import uz.hbs.beans.PersonTitle;
import uz.hbs.beans.PreviouslyRoom;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.beans.ReservationSale;
import uz.hbs.beans.RoomType;
import uz.hbs.beans.TourAgent;
import uz.hbs.beans.rate.RatePlane;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.ReserveUtil;
import uz.hbs.utils.email.CIPHallEmailNotifier;
import uz.hbs.utils.email.GreenHallEmailNotifier;
import uz.hbs.utils.email.InsuranceEmailNotifier;
import uz.hbs.utils.email.ReservationEmailNotifier;
import uz.hbs.utils.email.TaxiEmailNotifier;

public class TAReserveSummaryPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private HashMap<String, BigDecimal> totals = new HashMap<String, BigDecimal>();
	private BigDecimal total = new BigDecimal("0");
	private BigDecimal rate = BigDecimal.valueOf(0);
	private ReservationRuleType rule;

	public TAReserveSummaryPanel(String id, IBreadCrumbModel breadCrumbModel, final ReservationDetail reserve, final ReservationRuleType rule) {
		super(id, breadCrumbModel);
		this.rule = rule;
		List<ReservationRoom> reserveroomlist = reserve.getReserverooms();
		for (ReservationRoom reserveroom : reserveroomlist) {
			rate = rate.add(reserveroom.getRate());
		}

		add(new Label("hotel_name", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new MyBatisHelper().selectOne("selectHotelAndCityName", reserve.getHotelsusers_id());
			}
		}).setRenderBodyOnly(true));

		add(new Label("check_in", DateUtil.toString(reserve.getCheck_in(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
		add(new Label("check_out", DateUtil.toString(reserve.getCheck_out(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
		add(new Label("nights", reserve.getNumber_of_nights()));
		add(new Label("nightsSuffixLabel", new StringResourceModel(
				"touragents.newbooking.nights." + (reserve.getNumber_of_nights() > 5 ? 5 : reserve.getNumber_of_nights()), null)));
		add(new ListView<RoomType>("roomtypelist", new LoadableDetachableModel<List<RoomType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<RoomType> load() {
				if (reserve.isIs_group())
					return ReserveUtil.getRoomTypesByReserve((GroupReservation) reserve);
				return ReserveUtil.getRoomTypesByReserve((IndividualReservation) reserve);
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
		add(new Label("check_in_early", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				BigDecimal charge = ReserveUtil.getCheckInCharge(reserve, rate);
				return new StringResourceModel("touragents.reservation.check_in.early." + (charge != null && charge.doubleValue() > 0 ? "yes" : "no"),
						null).getString();
			}
		}));
		add(new Label("check_out_late", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				BigDecimal charge = ReserveUtil.getCheckOutCharge(reserve, rate);
				return new StringResourceModel("touragents.reservation.check_in.early." + (charge != null && charge.doubleValue() > 0 ? "yes" : "no"),
						null).getString();
			}
		}));
		add(new Label("adults", new AbstractReadOnlyModel<Short>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Short getObject() {
				short adults = 0;
				for (ReservationRoom reserveroom : reserve.getReserverooms()) {
					adults += reserveroom.getHolding_capacity();
				}
				return adults;
			}
		}));
		add(new Label("children", new AbstractReadOnlyModel<Short>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Short getObject() {
				short childs = 0;
				for (ReservationRoom reserveroom : reserve.getReserverooms()) {
					childs += (short) reserveroom.getChildAgeList().size();
				}
				return childs;
			}
		}));
		add(new ListView<String>("preferencelist", new LoadableDetachableModel<List<String>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<String> load() {
				if (reserve.isIs_group())
					return ReserveUtil.getPreferencesByReserve((GroupReservation) reserve);
				return ReserveUtil.getPreferencesByReserve((IndividualReservation) reserve);
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<String> item) {
				item.add(new Label("name", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						String s = item.getModelObject();
						if (s.equals(ReserveUtil.EXTRA_BED))
							return getString("touragents.newbooking.bed_needed");
						else if (s.equals(ReserveUtil.CITY_VIEW))
							return getString("touragents.newbooking.city_view_availability");
						else if (s.equals(ReserveUtil.NO_SMOKER))
							return getString("touragents.newbooking.non_smokers_availability");
						return null;
					}
				}));
			}

			@Override
			public boolean isVisible() {
				return getModelObject().size() > 0;
			}
		});

		WebMarkupContainer roomtype_container;
		add(roomtype_container = new WebMarkupContainer("roomtype_container"));
		roomtype_container.add(new Label("nightsSuffixLabel", new StringResourceModel(
				"touragents.newbooking.nights." + (reserve.getNumber_of_nights() > 5 ? 5 : reserve.getNumber_of_nights()), null)));
		roomtype_container.add(new ListView<RoomType>("list", new LoadableDetachableModel<List<RoomType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<RoomType> load() {
				if (reserve.isIs_group())
					return ReserveUtil.getRoomTypesByReserve((GroupReservation) reserve);
				return ReserveUtil.getRoomTypesByReserve((IndividualReservation) reserve);
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<RoomType> item) {
				final RoomType roomtype = (RoomType) item.getDefaultModelObject();
				item.add(new Label("roomtype", roomtype.getName()));
				item.add(new Label("quantity", roomtype.getNumber_of_rooms()));
				item.add(new Label("nights", reserve.getNumber_of_nights()));
				item.add(new Label("amount", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						BigDecimal val = roomtype.getRoom_rate();
						if (!totals.containsKey("roomtype"))
							totals.put("roomtype", BigDecimal.valueOf(0));
						totals.put("roomtype", totals.get("roomtype").add(val));
						return new StringResourceModel("touragents.newbooking.total_price_label", null,
								new Object[] { ((MySession) getSession()).getCurrencyName(),
										CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), val) }).getString();
					}
				}));
			}
		});
		roomtype_container.add(new Label("total", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new StringResourceModel("touragents.newbooking.total_price_label", null,
						new Object[] { ((MySession) getSession()).getCurrencyName(),
								CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), totals.get("roomtype")) }).getString();
			}
		}));

		final WebMarkupContainer children_container;
		add(children_container = new WebMarkupContainer("children_container"));
		children_container.setOutputMarkupPlaceholderTag(true);
		children_container.add(new ListView<ReservationRoom>("reserveroomlist", new LoadableDetachableModel<List<ReservationRoom>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ReservationRoom> load() {
				return reserve.getReserverooms();
			}

		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<ReservationRoom> reserveroomItem) {
				reserveroomItem.add(new Label("room_index",
						new StringResourceModel("hotels.reservation.details.room.index", null, new Object[] { reserveroomItem.getIndex() + 1 })));
				reserveroomItem.add(new ListView<ChildAge>("list", new LoadableDetachableModel<List<ChildAge>>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<ChildAge> load() {
						return reserveroomItem.getModelObject().getChildAgeList();
					}
				}) {
					private static final long serialVersionUID = 1L;
					private BigDecimal childrate = null;

					@Override
					protected void populateItem(ListItem<ChildAge> item) {
						final ChildAge childAge = (ChildAge) item.getDefaultModelObject();
						item.add(new Label("children", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								return new StringResourceModel("hotels.guest.details.child", null).getString() + " " + childAge.getChild_index();
							}
						}));
						item.add(new Label("age", childAge.getAge()));
						item.add(new Label("nights", reserve.getNumber_of_nights()));

						childrate = null;

						String info = "";

						if (childAge.getAge() > rule.getMinimum_free_age()) {
							for (Guest guest : reserveroomItem.getModelObject().getGuestlist()) {
								if (guest.getPerson_title() != null && guest.getPerson_title().getTitle().equals(PersonTitle.CHILD)
										&& guest.getChildAge().equals(childAge)) {
									childrate = ReserveUtil.getRateWithDiscount(reserve, reserveroomItem.getModelObject(), rule);
									if (childrate != null)
										info = "со скидкой";
								}
							}
						} else {
							info = "бесплатный";
							childrate = new BigDecimal("0.00");
						}

						final BigDecimal temp = childrate;

						item.add(new Label("amount", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								if (temp != null) {
									if (childrate != null && childrate.doubleValue() == 0.00) {
										if (reserveroomItem.getModelObject().isExtra_bed_needed())
											return getString("hotels.reservation.details.additional_bed.extra_bed.short");
										else {
											return new StringResourceModel("touragents.newbooking.total_price_label", null,
													new Object[] { ((MySession) getSession()).getCurrencyName(),
															CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), temp) })
																	.getString();
										}
									}
									if (!totals.containsKey("children"))
										totals.put("children", BigDecimal.valueOf(0));
									totals.put("children", totals.get("children").add(temp));

									return new StringResourceModel("touragents.newbooking.total_price_label", null,
											new Object[] { ((MySession) getSession()).getCurrencyName(),
													CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), temp) }).getString();
								}
								return null;
							}
						}));
						item.add(new Label("info", info) {
							private static final long serialVersionUID = 1L;

							@Override
							public boolean isVisible() {
								return childrate != null;
							}
						});
					}
				});
			}
		});
		children_container.add(new Label("total", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new StringResourceModel("touragents.newbooking.total_price_label", null,
						new Object[] { ((MySession) getSession()).getCurrencyName(),
								CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(totals.get("children"))) })
										.getString();
			}
		}));

		final WebMarkupContainer early_check_in_container;
		add(early_check_in_container = new WebMarkupContainer("early_check_in_container"));
		early_check_in_container.setOutputMarkupPlaceholderTag(true);
		early_check_in_container.add(new ListView<RoomType>("list", new LoadableDetachableModel<List<RoomType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<RoomType> load() {
				if (reserve.isIs_group())
					return ReserveUtil.getRoomTypesByReserve((GroupReservation) reserve);
				return ReserveUtil.getRoomTypesByReserve((IndividualReservation) reserve);
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<RoomType> item) {
				final RoomType roomtype = (RoomType) item.getDefaultModelObject();
				item.add(new Label("roomtype", roomtype.getName()));
				item.add(new Label("quantity", roomtype.getNumber_of_rooms()));
				item.add(new Label("amount", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						BigDecimal val = CommonUtil.nvl(roomtype.getCheck_in_rate());//ReserveUtil.getCheckInCharge(reserve, roomtype.getCheck_in_rate()));
						if (!totals.containsKey("early"))
							totals.put("early", BigDecimal.valueOf(0));
						totals.put("early", totals.get("early").add(val));
						return new StringResourceModel("touragents.newbooking.total_price_label", null,
								new Object[] { ((MySession) getSession()).getCurrencyName(),
										CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), val) }).getString();
					}
				}));
			}
		});
		early_check_in_container.add(new Label("total", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new StringResourceModel("touragents.newbooking.total_price_label", null,
						new Object[] { ((MySession) getSession()).getCurrencyName(),
								CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(totals.get("early"))) })
										.getString();
			}
		}));

		final WebMarkupContainer late_check_out_container;
		add(late_check_out_container = new WebMarkupContainer("late_check_out_container"));
		late_check_out_container.setOutputMarkupPlaceholderTag(true);
		late_check_out_container.add(new ListView<RoomType>("list", new LoadableDetachableModel<List<RoomType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<RoomType> load() {
				if (reserve.isIs_group())
					return ReserveUtil.getRoomTypesByReserve((GroupReservation) reserve);
				return ReserveUtil.getRoomTypesByReserve((IndividualReservation) reserve);
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<RoomType> item) {
				final RoomType roomtype = (RoomType) item.getDefaultModelObject();
				item.add(new Label("roomtype", roomtype.getName()));
				item.add(new Label("quantity", roomtype.getNumber_of_rooms()));
				item.add(new Label("amount", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						BigDecimal val = CommonUtil.nvl(roomtype.getCheck_out_rate());
						if (!totals.containsKey("late"))
							totals.put("late", BigDecimal.valueOf(0));
						totals.put("late", totals.get("late").add(val));
						return new StringResourceModel("touragents.newbooking.total_price_label", null,
								new Object[] { ((MySession) getSession()).getCurrencyName(),
										CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), val) }).getString();
					}
				}));
			}
		});
		late_check_out_container.add(new Label("total", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new StringResourceModel("touragents.newbooking.total_price_label", null,
						new Object[] { ((MySession) getSession()).getCurrencyName(),
								CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(totals.get("late"))) })
										.getString();
			}
		}));

		final WebMarkupContainer additional_service_container;
		add(additional_service_container = new WebMarkupContainer("additional_service_container"));
		additional_service_container.setOutputMarkupPlaceholderTag(true);
		additional_service_container.add(new ListView<IdAndName>("list", new LoadableDetachableModel<List<IdAndName>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<IdAndName> load() {
				if (reserve.isIs_group())
					return ReserveUtil.getAdditionalServicesByReserve((GroupReservation) reserve);
				return ReserveUtil.getAdditionalServicesByReserve((IndividualReservation) reserve);
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<IdAndName> item) {
				final IdAndName obj = (IdAndName) item.getDefaultModelObject();
				item.add(new Label("service", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						if (obj.getName().equals(ReserveUtil.EXTRA_BED)) {
							return getString("hotels.reservation.details.additional_bed.extra_bed");
						} else if (obj.getName().equals(ReserveUtil.MEAL_LUNCH)) {
							return getString("hotels.reference_info.meal_options.lunch");
						} else if (obj.getName().equals(ReserveUtil.MEAL_DINNER)) {
							return getString("hotels.reference_info.meal_options.dinner");
						} else if (obj.getName().equals(ReserveUtil.MEAL_LUNCH_DINNER)) {
							return getString("hotels.reference_info.meal_options.fb");
						}
						return null;
					}
				}));
				item.add(new Label("quantity", new AbstractReadOnlyModel<Integer>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Integer getObject() {
						if (obj.getName().equals(ReserveUtil.EXTRA_BED))
							return obj.getId();
						return obj.getId() * reserve.getAdults();
					}
				}));
				item.add(new Label("nights", reserve.getNumber_of_nights()));
				item.add(new Label("amount", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						BigDecimal val;
						if (reserve.isIs_group())
							val = CommonUtil.nvl(ReserveUtil.getAdditionalServicesAmountByReserve((GroupReservation) reserve, rule, obj.getName(),
									getMySession()));
						else
							val = CommonUtil.nvl(ReserveUtil.getAdditionalServicesAmountByReserve((IndividualReservation) reserve, rule,
									obj.getName(), getMySession()));

						if (obj.getName().equals(ReserveUtil.EXTRA_BED)) {
							val = val.multiply(BigDecimal.valueOf(obj.getId())).multiply(BigDecimal.valueOf(reserve.getNumber_of_nights()));
						} else {
							val = val.multiply(BigDecimal.valueOf(reserve.getAdults()));
						}
						if (!totals.containsKey("additional"))
							totals.put("additional", BigDecimal.valueOf(0));

						totals.put("additional", totals.get("additional").add(val));

						return new StringResourceModel("touragents.newbooking.total_price_label", null,
								new Object[] { ((MySession) getSession()).getCurrencyName(),
										CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), val) }).getString();
					}
				}));
			}
		});
		additional_service_container.add(new Label("total", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new StringResourceModel("touragents.newbooking.total_price_label", null,
						new Object[] { ((MySession) getSession()).getCurrencyName(),
								CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(totals.get("additional"))) })
										.getString();
			}
		}));
		final List<MyBean> otherAdditionalServiceList = ReserveUtil.getOtherAdditionalServicesByReserve(reserve);
		WebMarkupContainer other_additional_service_container;
		add(other_additional_service_container = new WebMarkupContainer("other_additional_service_container") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return (!reserve.isPayment_owner()) && otherAdditionalServiceList.size() > 0;
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
						if (obj.getType().equals(AdditionalServicePrice.Insurance))
							return (short) (reserve.getNumber_of_nights() + 1);
						return null;
					}
				}));
				item.add(new Label("amount", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						BigDecimal val = BigDecimal.valueOf(obj.getCost());
						if (!totals.containsKey("additional_other"))
							totals.put("additional_other", BigDecimal.valueOf(0));

						totals.put("additional_other", totals.get("additional_other").add(val));

						return new StringResourceModel("touragents.newbooking.total_price_label", null,
								new Object[] { ((MySession) getSession()).getCurrencyName(),
										CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), val) }).getString();
					}
				}));
			}
		});
		other_additional_service_container.add(new Label("total", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new StringResourceModel("touragents.newbooking.total_price_label", null,
						new Object[] { ((MySession) getSession()).getCurrencyName(), CommonUtil
								.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(totals.get("additional_other"))) })
										.getString();
			}
		}));

		add(new Label("grand_total", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				Iterator<BigDecimal> iterator = totals.values().iterator();
				while (iterator.hasNext())
					total = total.add(iterator.next());

				return new StringResourceModel("touragents.newbooking.total_price_label", null, new Object[] {
						((MySession) getSession()).getCurrencyName(), CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), total) })
								.getString();
			}
		}));
		add(new AgreeForm("agreeform", reserve));
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		if (totals.get("children") == null || totals.get("children").doubleValue() == 0d) {
			JavaScriptUtils.writeJavaScript(getResponse(), "$('table[data-item=children]').css('display','none');");
		}
		if (totals.get("additional") == null || totals.get("additional").doubleValue() == 0d) {
			JavaScriptUtils.writeJavaScript(getResponse(), "$('table[data-item=additional]').css('display','none');");
		}
		if (totals.get("late") == null || totals.get("late").doubleValue() == 0d) {
			JavaScriptUtils.writeJavaScript(getResponse(), "$('table[data-item=late-check]').css('display','none');");
		}
		if (totals.get("early") == null || totals.get("early").doubleValue() == 0d) {
			JavaScriptUtils.writeJavaScript(getResponse(), "$('table[data-item=early-check]').css('display','none');");
		}
	}

	public class AgreeForm extends Form<Void> {
		private FeedbackPanel feedback;
		private boolean agree;

		public AgreeForm(String id, final ReservationDetail reserve) {
			super(id);
			add(feedback = new MyFeedbackPanel("feedback"));
			feedback.setOutputMarkupId(true);
			final AjaxButton confirm;
			add(confirm = new AjaxButton("confirm") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					boolean vacant = true;
					SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
					if (!reserve.isIs_group()) { //INDIVIDUAL
						boolean isNewReservation = reserve.getId() == null;
						try {
							reserve.setInitiator_user_id(((MySession) getSession()).getUser().getId());
							reserve.setCreator_user_id(((MySession) getSession()).getUser().getId());
							reserve.setTour_agent(new TourAgent(((MySession) getSession()).getUser().getTouragency_user_id()));
							reserve.setRateplane(new RatePlane((Integer) sql.selectOne("selectCurrentRoomRatePlanId", reserve)));
							if (totals.get("additional_other") != null) {
								reserve.setAddition_service_cost(totals.get("additional_other").doubleValue());
								total = total.subtract(totals.get("additional_other"));
							}
							reserve.setTotal(total);

							if (reserve.getId() == null) {
								sql.insert("insertReservation", (ReservationDetail) reserve);
							} else {
								sql.update("updateReservation", (ReservationDetail) reserve);
							}

							boolean isNewAdditionalService = false;
							AdditionalServiceOrder addtservorder = new AdditionalServiceOrder(reserve.getId());

							if (!reserve.isPayment_owner()) {
								if (reserve.isInsurance() || (CommonUtil.nvl(reserve.getArrival().getTransport_type(),
										AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN
										&& CommonUtil.nvl(reserve.getDeparture().getTransport_type(),
												AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN)) {
									addtservorder.setArrival_date(reserve.getCheck_in());
									addtservorder.setDeparture_date(reserve.getCheck_out());
									addtservorder.setCreator_user_id(((MySession) getSession()).getUser().getId());
									addtservorder.setInitiator_user_id(((MySession) getSession()).getUser().getId());
									addtservorder.setTotal(totals.get("additional_other"));
									addtservorder
											.setId((Long) new MyBatisHelper().selectOne("selectAdditionalServiceIdByReserveId", reserve.getId()));
									if (sql.update("updateAdditionalServiceOrder", addtservorder) == 0)
										sql.insert("insertAdditionalServiceOrder", addtservorder);
									if (!reserve.getInsuranceList().isEmpty()) {
										for (Insurance insurance : reserve.getInsuranceList()) {
											insurance.setAdditionalserviceorders_id(addtservorder.getId());
											insurance.setCreator_user_id(((MySession) getSession()).getUser().getId());
											insurance.setInitiator_user_id(((MySession) getSession()).getUser().getId());
											insurance.setPeriod_from_date(reserve.getCheck_in());
											insurance.setPeriod_to_date(reserve.getCheck_out());
											insurance.setWith_hotel(true);
											if (sql.update("updateInsurance", insurance) == 0) {
												sql.insert("insertInsurance", insurance);
												isNewAdditionalService = true;
											}
										}
									}
									reserve.getArrival().setAdditionalserviceorders_id(addtservorder.getId());
									if (reserve.getArrival().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) {
										reserve.getArrival().setCreator_user_id(((MySession) getSession()).getUser().getId());
										reserve.getArrival().setInitiator_user_id(((MySession) getSession()).getUser().getId());
										reserve.getArrival().setWith_hotel(true);
										if (sql.update("updateAdditionalServiceDetail", reserve.getArrival()) == 0) {
											sql.insert("insertAdditionalServiceDetail", reserve.getArrival());
											isNewAdditionalService = true;
										}
									} else
										sql.delete("deleteAdditionalServiceDetail", reserve.getArrival());

									reserve.getDeparture().setAdditionalserviceorders_id(addtservorder.getId());
									if (reserve.getDeparture().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) {
										reserve.getDeparture().setCreator_user_id(((MySession) getSession()).getUser().getId());
										reserve.getDeparture().setInitiator_user_id(((MySession) getSession()).getUser().getId());
										reserve.getDeparture().setWith_hotel(true);
										if (sql.update("updateAdditionalServiceDetail", reserve.getDeparture()) == 0) {
											sql.insert("insertAdditionalServiceDetail", reserve.getDeparture());
											isNewAdditionalService = true;
										}
									} else
										sql.delete("deleteAdditionalServiceDetail", reserve.getDeparture());
								} else
									sql.delete("deleteInsuranceByReserveId", reserve.getId());
							}
							HashMap<String, Serializable> param = new HashMap<String, Serializable>();
							param.put("check_in", reserve.getCheck_in());
							param.put("check_out", reserve.getCheck_out());
							param.put("reserve_id", reserve.getId());

							HashMap<Integer, Short> roomtype_map = new HashMap<Integer, Short>();

							for (ReservationRoom reserveroom : reserve.getReserverooms()) {//
								param.put("roomtypes_id", reserveroom.getRoomtype().getId());

								short available = new MyBatisHelper().selectOne("selectTAReserveAvailableRoomsByRoomType", param);
								short available2 = 0;

								if (!roomtype_map.containsKey(reserveroom.getRoomtype().getId()))
									roomtype_map.put(reserveroom.getRoomtype().getId(), available);
								available2 = roomtype_map.get(reserveroom.getRoomtype().getId());

								if (available > 0 && available2 > 0) {
									Long prev_room_id = reserveroom.getPreviously_rooms_id();
									if (prev_room_id == null) {
										if (reserve.isIs_group()) {
											prev_room_id = (Long) new MyBatisHelper().selectOne("selectPreviouslyRoomId",
													new PreviouslyRoom((GroupReservation) reserve, reserveroom.getRoomtype().getId(),
															(short) reserveroom.getAdults_count()));
										} else {
											prev_room_id = (Long) new MyBatisHelper().selectOne("selectPreviouslyRoomId",
													new PreviouslyRoom((IndividualReservation) reserve, reserveroom.getRoomtype().getId(),
															(short) reserveroom.getAdults_count()));
										}
									}
									if (prev_room_id != null) {
										reserveroom.setHotelsusers_id(reserve.getHotelsusers_id());
										reserveroom.setCheck_in(reserve.getCheck_in());
										reserve.setRateplane(new RatePlane((Integer) sql.selectOne("selectRoomRatePlanId", reserveroom)));

										Double d = 0d;
										if (reserveroom.getMeal_options() == 3) {
											Double d1 = CommonUtil
													.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
															new MealOption(MealOption.LUNCH, reserve.getHotelsusers_id())))
													* ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.LUNCH, 1d);
											Double d2 = CommonUtil
													.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
															new MealOption(MealOption.DINNER, reserve.getHotelsusers_id())))
													* ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.DINNER,
															1d);
											d = d1 + d2;
										} else {
											d = CommonUtil
													.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
															new MealOption(reserveroom.getMeal_options(), reserve.getHotelsusers_id())))
													* ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(),
															reserveroom.getMeal_options(), 1d);
										}
										reserveroom.setMeal_cost(BigDecimal.valueOf(d * reserveroom.getHolding_capacity()));

										reserveroom.setTotal(reserveroom.getRate().add(reserveroom.getMeal_cost()));

										if (reserveroom.isExtra_bed_needed()) {
											reserveroom.setExtra_bed_cost(
													BigDecimal.valueOf(rule.getExtra_bed_price_type_value() * reserve.getNumber_of_nights()));
											reserveroom.setTotal(reserveroom.getTotal().add(reserveroom.getExtra_bed_cost()));
										}

										BigDecimal charge = reserveroom.getCheck_in_rate();
										if (charge != null) {
											reserveroom.setTotal(reserveroom.getTotal().add(charge));
											reserveroom.setEarly_check_in_cost(charge);
										}

										charge = reserveroom.getCheck_out_rate();
										if (charge != null) {
											reserveroom.setTotal(reserveroom.getTotal().add(charge));
											reserveroom.setLate_check_out_cost(charge);
										}

										reserveroom.setReservations_id(reserve.getId());
										reserveroom.setInitiator_user_id(((MySession) getSession()).getUser().getId());
										reserveroom.setPreviously_rooms_id(prev_room_id);
										reserveroom.setRooms_id(null);
										reserveroom.setRoom(null);
										if (reserveroom.getId() == null)
											sql.insert("insertReservationRoom", reserveroom);
										else
											sql.update("updateReservationRoom", reserveroom);
										for (Guest guest : reserveroom.getGuestlist()) {
											if (guest.isCorrect()) {
												if (reserve.getArrival() != null)
													guest.setAdditionalservicedetails_id(reserve.getArrival().getId());
												guest.setReservations_id(reserve.getId());
												guest.setReservationrooms_id(reserveroom.getId());
												guest.setInitiator_user_id(((MySession) getSession()).getUser().getId());
												guest.init_gender();
												if (guest.getId() == null)
													sql.insert("insertGuest", guest);
												else
													sql.update("updateGuest", guest);
											}
										}
										for (short child_index = 1; child_index <= reserveroom.getChildAgeList().size(); child_index++) {
											sql.insert("insertChildTAReserve", new Child(reserve.getId(), reserveroom.getId(), child_index,
													((ChildAge) reserveroom.getChildAgeList().get(child_index - 1)).getAge()));
										}
										//if (reserve.getId() == null)
										sql.delete("deleteTAReserveSalesByReservation", reserve.getId());
										sql.insert("insertTAReserveSalesByReservation", new ReservationSale((IndividualReservation) reserve,
												reserveroom.getRoomtype().getId(), (short) reserveroom.getHolding_capacity()));
										roomtype_map.put(reserveroom.getRoomtype().getId(), (short) (available2 - 1));
									} else {
										vacant = false;
										feedback.error(new StringResourceModel("hotels.reservation.room.vacant.not.found", null,
												new Object[] { reserveroom.getRoomtype().getName() }).getString());
									}
								} else {
									vacant = false;
									feedback.error(new StringResourceModel("hotels.reservation.room.vacant.not.found", null,
											new Object[] { reserveroom.getRoomtype().getName() }).getString());
								}
							}
							if (vacant) {
								sql.commit();
								
								new MyBatisHelper().selectOne("selectFixGuestIndex", reserve.getId());

								String feedbackMessge;
								if (isNewReservation) {
									feedbackMessge = getString("hotels.reservation.create.success");
									logger.debug("Reservation created, Id=" + reserve.getId());
									ReservationEmailNotifier.newReservation(reserve.getId());
								} else {
									feedbackMessge = getString("hotels.reservation.update.success");
									logger.debug("Reservation updated, Id=" + reserve.getId());
									ReservationEmailNotifier.changeReservation(reserve.getId());
								}
								if (isNewAdditionalService) {
									try {
										CIPHallEmailNotifier.send(addtservorder.getId());
										GreenHallEmailNotifier.send(addtservorder.getId());
										TaxiEmailNotifier.send(addtservorder.getId());
										InsuranceEmailNotifier.send(addtservorder.getId());
									} catch (Exception e) {
										logger.error("Exception", e);
									}
								}
								((MySession) getSession()).setFeedbackMessage(feedbackMessge); //Keyingi pagega feedback messageni berish uchun kerak
								feedback.success(feedbackMessge);
								setResponsePage(new TourAgentPage(TourAgentPage.RESERVATIONS, false, null, null));
							} else {
								sql.rollback();
							}
						} catch (Exception e) {
							//reserve.setId(null);
							logger.error("Exception", e);
							sql.rollback();
							String feedbackMessage;
							if (isNewReservation) {
								feedbackMessage = getString("touragents.reservation.create.error");
								logger.debug("Reservation create is failed, Id=" + reserve.getId());
							} else {
								feedbackMessage = getString("touragents.reservation.update.error");
								logger.debug("Reservation update is failed, Id=" + reserve.getId());
							}
							feedback.error(feedbackMessage);
						} finally {
							sql.close();
							target.add(feedback);
						}
					} else {
						Byte meal_option_all = null;
						boolean isNewReservation = reserve.getId() == null;
						try {
							reserve.setInitiator_user_id(((MySession) getSession()).getUser().getId());
							reserve.setCreator_user_id(((MySession) getSession()).getUser().getId());
							reserve.setTour_agent(new TourAgent(((MySession) getSession()).getUser().getTouragency_user_id()));
							reserve.setRateplane(new RatePlane((Integer) sql.selectOne("selectCurrentRoomRatePlanId", reserve)));
							if (totals.get("additional_other") != null) {
								reserve.setAddition_service_cost(totals.get("additional_other").doubleValue());
								total = total.subtract(totals.get("additional_other"));
							}
							reserve.setTotal(total);

							if (reserve.getId() == null) {
								sql.insert("insertReservation", (ReservationDetail) reserve);
							} else {
								sql.update("updateReservation", (ReservationDetail) reserve);
							}

							AdditionalServiceOrder addtservorder = new AdditionalServiceOrder(reserve.getId());
							boolean isNewAdditionalService = false;

							if (!reserve.isPayment_owner()) {
								if (reserve.isInsurance() || (CommonUtil.nvl(reserve.getArrival().getTransport_type(),
										AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN
										&& CommonUtil.nvl(reserve.getDeparture().getTransport_type(),
												AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN)) {
									addtservorder.setArrival_date(reserve.getCheck_in());
									addtservorder.setDeparture_date(reserve.getCheck_out());
									addtservorder.setCreator_user_id(((MySession) getSession()).getUser().getId());
									addtservorder.setInitiator_user_id(((MySession) getSession()).getUser().getId());
									addtservorder.setTotal(BigDecimal.valueOf(reserve.getAdditional_service_cost()));
									if (sql.update("updateAdditionalServiceOrder", addtservorder) == 0)
										sql.insert("insertAdditionalServiceOrder", addtservorder);
									if (!reserve.getInsuranceList().isEmpty()) {
										for (Insurance insurance : reserve.getInsuranceList()) {
											insurance.setAdditionalserviceorders_id(addtservorder.getId());
											insurance.setInitiator_user_id(((MySession) getSession()).getUser().getId());
											insurance.setWith_hotel(true);
											if (sql.update("updateInsurance", insurance) == 0) {
												sql.insert("insertInsurance", insurance);
												isNewAdditionalService = true;
											}
										}
									}
									reserve.getArrival().setAdditionalserviceorders_id(addtservorder.getId());
									if (reserve.getArrival().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) {
										reserve.getArrival().setCreator_user_id(((MySession) getSession()).getUser().getId());
										reserve.getArrival().setInitiator_user_id(((MySession) getSession()).getUser().getId());
										reserve.getArrival().setWith_hotel(true);
										if (sql.update("updateTourAgentAdditionalService", reserve.getArrival()) == 0) {
											sql.insert("insertTourAgentAdditionalService", reserve.getArrival());
											isNewAdditionalService = true;
										}
									} else
										sql.delete("deleteAdditionalServiceDetail", reserve.getArrival());

									reserve.getDeparture().setAdditionalserviceorders_id(addtservorder.getId());
									if (reserve.getDeparture().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN) {
										reserve.getDeparture().setCreator_user_id(((MySession) getSession()).getUser().getId());
										reserve.getDeparture().setInitiator_user_id(((MySession) getSession()).getUser().getId());
										reserve.getDeparture().setWith_hotel(true);
										if (sql.update("updateTourAgentAdditionalService", reserve.getDeparture()) == 0) {
											sql.insert("insertTourAgentAdditionalService", reserve.getDeparture());
											isNewAdditionalService = true;
										}
									} else
										sql.delete("deleteAdditionalServiceDetail", reserve.getDeparture());
								} else
									sql.delete("deleteInsuranceByReserveId", reserve.getId());
							}
							HashMap<String, Serializable> param = new HashMap<String, Serializable>();
							param.put("check_in", reserve.getCheck_in());
							param.put("check_out", reserve.getCheck_out());
							param.put("reserve_id", reserve.getId());

							HashMap<Integer, Short> roomtype_map = new HashMap<Integer, Short>();

							for (ReservationRoom reserveroom : reserve.getReserverooms()) {//
								if (meal_option_all == null)
									meal_option_all = reserveroom.getMeal_options();
								else
									reserveroom.setMeal_options(meal_option_all);

								param.put("roomtypes_id", reserveroom.getRoomtype().getId());

								short available = new MyBatisHelper().selectOne("selectTAReserveAvailableRoomsByRoomType", param);
								short available2 = 0;

								if (!roomtype_map.containsKey(reserveroom.getRoomtype().getId()))
									roomtype_map.put(reserveroom.getRoomtype().getId(), available);
								available2 = roomtype_map.get(reserveroom.getRoomtype().getId());

								if (available > 0 && available2 > 0) {
									Long prev_room_id = reserveroom.getPreviously_rooms_id();
									if (prev_room_id == null)
										prev_room_id = (Long) new MyBatisHelper().selectOne("selectPreviouslyRoomId",
												new PreviouslyRoom((GroupReservation) reserve, reserveroom.getRoomtype().getId(),
														(short) reserveroom.getAdults_count()));
									if (prev_room_id != null) {
										reserveroom.setHotelsusers_id(reserve.getHotelsusers_id());
										reserveroom.setCheck_in(reserve.getCheck_in());
										reserve.setRateplane(new RatePlane((Integer) sql.selectOne("selectRoomRatePlanId", reserveroom)));

										Double d = 0d;
										if (reserveroom.getMeal_options() == 3) {
											Double d1 = CommonUtil
													.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
															new MealOption(MealOption.LUNCH, reserve.getHotelsusers_id())))
													* ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.LUNCH, 1d);
											Double d2 = CommonUtil
													.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
															new MealOption(MealOption.DINNER, reserve.getHotelsusers_id())))
													* ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.DINNER,
															1d);
											d = d1 + d2;
										} else {
											d = CommonUtil
													.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
															new MealOption(reserveroom.getMeal_options(), reserve.getHotelsusers_id())))
													* ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(),
															reserveroom.getMeal_options(), 1d);
										}
										reserveroom.setMeal_cost(BigDecimal.valueOf(d * reserveroom.getHolding_capacity()));

										reserveroom.setTotal(reserveroom.getRate().add(reserveroom.getMeal_cost()));

										if (reserveroom.isExtra_bed_needed()) {
											reserveroom.setExtra_bed_cost(
													BigDecimal.valueOf(rule.getExtra_bed_price_type_value() * reserve.getNumber_of_nights()));
											reserveroom.setTotal(reserveroom.getTotal().add(reserveroom.getExtra_bed_cost()));
										}

										BigDecimal charge = reserveroom.getCheck_in_rate();
										if (charge != null) {
											reserveroom.setTotal(reserveroom.getTotal().add(charge));
											reserveroom.setEarly_check_in_cost(charge);
										}

										charge = reserveroom.getCheck_out_rate();
										if (charge != null) {
											reserveroom.setTotal(reserveroom.getTotal().add(charge));
											reserveroom.setLate_check_out_cost(charge);
										}

										reserveroom.setReservations_id(reserve.getId());
										reserveroom.setInitiator_user_id(((MySession) getSession()).getUser().getId());
										reserveroom.setPreviously_rooms_id(prev_room_id);
										reserveroom.setRooms_id(null);
										reserveroom.setRoom(null);
										if (reserveroom.getId() == null)
											sql.insert("insertReservationRoom", reserveroom);
										else
											sql.update("updateReservationRoom", reserveroom);
										for (Guest guest : reserveroom.getGuestlist()) {
											if (guest.isCorrect()) {
												guest.setReservations_id(reserve.getId());
												guest.setReservationrooms_id(reserveroom.getId());
												guest.setInitiator_user_id(((MySession) getSession()).getUser().getId());
												guest.init_gender();
												if (guest.getId() == null)
													sql.insert("insertGuest", guest);
												else
													sql.update("updateGuest", guest);
											}
										}
										for (short child_index = 1; child_index < reserveroom.getChildAgeList().size(); child_index++) {
											if (sql.update("insertChildTAReserve", new Child(reserve.getId(), reserveroom.getId(), child_index,
													((ChildAge) reserveroom.getChildAgeList().get(child_index - 1)).getAge())) == 0) {
												sql.insert("insertChildTAReserve", new Child(reserve.getId(), reserveroom.getId(), child_index,
														((ChildAge) reserveroom.getChildAgeList().get(child_index - 1)).getAge()));
											}
										}
										//if (reserve.getId() == null)
										sql.delete("deleteTAReserveSalesByReservation", reserve.getId());
										sql.insert("insertTAReserveSalesByReservation", new ReservationSale((GroupReservation) reserve,
												reserveroom.getRoomtype().getId(), (short) reserveroom.getHolding_capacity()));
										roomtype_map.put(reserveroom.getRoomtype().getId(), (short) (available2 - 1));
									} else {
										vacant = false;
										feedback.error(new StringResourceModel("hotels.reservation.room.vacant.not.found", null,
												new Object[] { reserveroom.getRoomtype().getName() }).getString());
									}
								} else {
									vacant = false;
									feedback.error(new StringResourceModel("hotels.reservation.room.vacant.not.found", null,
											new Object[] { reserveroom.getRoomtype().getName() }).getString());
								}
							}
							if (vacant) {
								sql.commit();
								
								new MyBatisHelper().selectOne("selectFixGuestIndex", reserve.getId());
								
								String feedbackMessage;
								if (isNewReservation) {
									feedbackMessage = getString("hotels.reservation.create.success");
									logger.debug("Group Reservation created, Id=" + reserve.getId());
									ReservationEmailNotifier.newReservation(reserve.getId());
								} else {
									feedbackMessage = getString("hotels.reservation.update.success");
									logger.debug("Group Reservation updated, Id=" + reserve.getId());
									ReservationEmailNotifier.changeReservation(reserve.getId());
								}
								if (isNewAdditionalService) {
									try {
										CIPHallEmailNotifier.send(addtservorder.getId());
										GreenHallEmailNotifier.send(addtservorder.getId());
										TaxiEmailNotifier.send(addtservorder.getId());
										InsuranceEmailNotifier.send(addtservorder.getId());
									} catch (Exception e) {
										logger.error("Exception", e);
									}
								}
								((MySession) getSession()).setFeedbackMessage(feedbackMessage); //Keyingi pagega feedback messageni berish uchun kerak
								feedback.success(feedbackMessage);
								setResponsePage(new TourAgentPage(TourAgentPage.RESERVATIONS, false, null, null));
							} else {
								sql.rollback();
							}
						} catch (Exception e) {
							//reserve.setId(null);
							logger.error("Exception", e);
							sql.rollback();
							String feedbackMessage;
							if (isNewReservation) {
								feedbackMessage = getString("touragents.reservation.create.error");
								logger.debug("Group Reservation create is failed, Id=" + reserve.getId());
							} else {
								feedbackMessage = getString("touragents.reservation.update.error");
								logger.debug("Group Reservation update is failed, Id=" + reserve.getId());
							}
							feedback.error(feedbackMessage);
						} finally {
							sql.close();
							target.add(feedback);
						}
					}
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
				}

				@Override
				public boolean isVisible() {
					return getMySession().getUser().isWorkable();
				}
			});
			confirm.setEnabled(false);
			confirm.setOutputMarkupId(true);

			final Panel cancellation_policy_container;
			add(cancellation_policy_container = new TABookingConditionPanel("cancellation_policy_container", rule));
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
	protected void onBeforeRender() {
		super.onBeforeRender();
		totals = new HashMap<String, BigDecimal>();
		total = new BigDecimal("0");
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.reservation.summary.confirm", null);
	}
}
