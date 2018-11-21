package uz.hbs.actions.touragent.newbooking.individual.print;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
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
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.RoomType;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.DateTimeTextField;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.ReserveUtil;

public class TAIndividualReservePrintPanel extends MyBreadCrumbPanel {
	private static final Logger _log = LoggerFactory.getLogger(TAIndividualReservePrintPanel.class);
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;
	private Label totalPrice;
	private Label amountOfGuestsLabel;
	private BigDecimal totalrate;

	
	public TAIndividualReservePrintPanel(String id, IBreadCrumbModel breadCrumbModel, IModel<IndividualReservation> model, final Booking filter) {
		this(id, breadCrumbModel, model, filter, true);
	}
		
	public TAIndividualReservePrintPanel(String id, IBreadCrumbModel breadCrumbModel, IModel<IndividualReservation> model, final Booking filter, final boolean isEditable) {		
		super(id, breadCrumbModel, model);
		
		final IndividualReservation reserve = model.getObject();
		if (filter != null) reserve.setResident(filter.isResident());
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);

		add(new Label("hotelName", new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new MyBatisHelper().selectOne("selectTAHotelName", reserve.getHotelsusers_id());
			}
		}));

		final ReservationRuleType rule = new MyBatisHelper().selectOne("selectTAReservationRule", new ReservationRuleType(reserve.getHotelsusers_id(), reserve.isIs_group()));

		if (rule == null) feedback.error("Reservation Rule is undefined.");
		
		if (totalrate == null) totalrate = CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), reserve.getTotal());

		add(totalPrice = new Label("totalPrice", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				if (reserve.isResident()) {
					return new StringResourceModel("touragents.newbooking.total_price_label", null, new Object[] { ((MySession) getSession()).getCurrencyName(), CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), totalrate) }).getString();
				} else {
					return new StringResourceModel("touragents.newbooking.total_price_label", null, new Object[] { ((MySession) getSession()).getCurrencyName(), totalrate }).getString();
				}
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

		add(new Label("amountOfNightsLabel", new StringResourceModel("touragents.newbooking.price_of_nights", null, new Object[] { reserve.getNumber_of_nights() })));
		add(amountOfGuestsLabel = new Label("amountOfGuestsLabel", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				short adults = 0;
				for (ReservationRoom reserveroom : reserve.getReserverooms()){
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
				for (ReservationRoom reserveroom : reserve.getReserverooms()){
					childs += (short) reserveroom.getChildAgeList().size();
				}				
				return new StringResourceModel("touragents.newbooking.for_number_of_childs", null, new Object[] { childs }).getString();
			}
		}));
		
		add(new Label("checkInLabel", DateUtil.toString(reserve.getCheck_in(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
		add(new Label("checkOutLabel", DateUtil.toString(reserve.getCheck_out(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
		add(new Label("nightsLabel", reserve.getNumber_of_nights()));
		add(new Label("nightsSuffixLabel", new StringResourceModel("touragents.newbooking.nights." + (reserve.getNumber_of_nights() > 5 ? 5 : reserve.getNumber_of_nights()), null)));
		add(new Label("ageLabel", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				short adults = 0;
				short childs = 0;
				String childAgesForDisplay = "";
				for (ReservationRoom reserveroom : reserve.getReserverooms()){
					adults += reserveroom.getHolding_capacity();
					childs += (short) reserveroom.getChildAgeList().size();
					for (ChildAge age : reserveroom.getChildAgeList()){
						childAgesForDisplay += childAgesForDisplay.isEmpty() ? age.getAge() : ", " + age.getAge();
					}
				}
				
				return new StringResourceModel("touragents.newbooking.adults", null).getString() + ": " + adults + ", " + new StringResourceModel("touragents.newbooking.children", null).getString() + ": " + childs + (childs > 0 ? " (" + childAgesForDisplay + ")" : "");
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

		add(reserveform = new ReserveForm("form", reserve, rule){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onAfterRender() {
				super.onAfterRender();
				if (!isEditable) JavaScriptUtils.writeJavaScript(getResponse(), "$(document).ready(function(){ $form = $('form#" + this.getMarkupId() + "'); $('input,select,textarea,a', $form).each(function(){ $(this).attr('disabled','disabled'); }); });");
			}
		});
		
		reserveform.add(new Link<Void>("cancel"){
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
				return isEditable;
			}
		});
		
		reserveform.add(done = new Button("ready") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit() {
				final IndividualReservation reserve = (IndividualReservation) reserveform.getDefaultModelObject();
				activate(new IBreadCrumbPanelFactory(){
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						return new TAReserveSummaryPanel(componentId, breadCrumbModel, reserve, rule);
					}
				});
			}
			
			@Override
			protected String getOnClickScript() {
				return "return confirm('Are you sure?');";
			}
			
			@Override
			public boolean isVisible() {
				return isEditable;
			}
		});
		final TAAdditionalServicePanel additional_option;
		reserveform.add(additional_option = new TAAdditionalServicePanel("additional_option", reserve));
		additional_option.setOutputMarkupPlaceholderTag(true);
		done.setOutputMarkupId(true);
		CommonUtil.setFormComponentRequired(reserveform);
	}
	
	private class ReserveForm extends Form<IndividualReservation> {
		private static final long serialVersionUID = 1L;
		private DateTimeTextField check_in;
		private DateTimeTextField check_out;

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
					_log.debug("Extrabed: "+item.getModelObject().isExtra_bed_needed());
					final HashMap<String, Object> param = new HashMap<String, Object>();
					param.put("hotelsusers_id", reserve.getHotelsusers_id());
					
					item.getModelObject().setIndex((short) (item.getIndex() + 1));
					
					item.add(new Label("reserveOrder", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return String.valueOf(item.getModelObject().getIndex());
						}
					}));
					
					item.add(new Label("roomtype_label", item.getModelObject().getRoomtype().getName()));
					
					if (item.getModelObject().getGuestlist().isEmpty()) {
						param.put("roomtypes_id", item.getModelObject().getRoomtype().getId());
						short holding_capacity = new MyBatisHelper().selectOne("selectHoldingCapacityRoomType", item.getModelObject().getRoomtype().getId());
						_log.debug("Holding capacity: "+item.getModelObject().getHolding_capacity());
						if (holding_capacity < item.getModelObject().getHolding_capacity()) {
							holding_capacity = item.getModelObject().getHolding_capacity();
							extra_bed = true;
						} else if (holding_capacity > item.getModelObject().getHolding_capacity()){
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
					}){
						private static final long serialVersionUID = 1L;

						@Override
						protected void populateItem(final ListItem<Guest> guestItem) {
							guestItem.add(new GuestDataPanel("guestdetails", new Model<Guest>(guestItem.getModelObject())));
						}	
					});
					listview.setOutputMarkupId(true);
					
					final HiddenField<Byte> meal_options;
					item.add(meal_options = new HiddenField<Byte>("meal_options", new PropertyModel<Byte>(item.getModel(), "meal_options")));
					meal_options.setLabel(new StringResourceModel("hotels.reservation.details.meal_options", null));
					meal_options.setRequired(true);
					meal_options.setOutputMarkupId(true);
					meal_options.add(new AttributeModifier("data-guest", item.getModelObject().getHolding_capacity()));
					
					if (reserve.getId() == null && ! item.getModelObject().isExtra_bed_needed()) item.getModelObject().setExtra_bed_needed(extra_bed);
					
					final Label extra_bed_cost_label;
					item.add(extra_bed_cost_label = new Label("extra_bed_cost_label", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							if (reserve.isResident()) {
								   return new StringResourceModel("touragents.newbooking.total_price_label", null, new Object[] {
											((MySession) getSession()).getCurrencyName(), CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), new BigDecimal(rule.getExtra_bed_price_type_value()))}).getString();
							} else {
								   return new StringResourceModel("touragents.newbooking.total_price_label", null, new Object[] {
											((MySession) getSession()).getCurrencyName(), rule.getExtra_bed_price_type_value()}).getString();
							}
						}
					}));
					extra_bed_cost_label.setOutputMarkupPlaceholderTag(true);
					extra_bed_cost_label.setVisible(item.getModelObject().isExtra_bed_needed());
					
					_log.debug("Extra bed: "+item.getModelObject().isExtra_bed_needed());
					
					item.add(new CheckBox("extra_bed_needed", new PropertyModel<Boolean>(item.getModel(), "extra_bed_needed")));
					item.add(new CheckBox("non_smokers", new PropertyModel<Boolean>(item.getModel(), "non_smokers")));
					item.add(new CheckBox("city_view", new PropertyModel<Boolean>(item.getModel(), "city_view")));
					item.add(new Label("meal_options_cost_label", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return new StringResourceModel("hotels.reservation.details.meal_options_cost_label", null, new Object[] { ((MySession) getSession()).getCurrencyName()}).getString();
						}
					}));
				}
			});
			
			
			add(new HiddenField<String>("breakfast_cost", new Model<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					Double d = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.BREAKFAST, reserve.getHotelsusers_id())));
					if (reserve.isResident())
						return String.valueOf(CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), d));
					else
						return String.valueOf(d);
					
				}
			}).add(new AttributeModifier("data-currency", getMySession().getCurrencyName())));
			add(new HiddenField<String>("lunch_cost", new Model<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					Double d = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.LUNCH, reserve.getHotelsusers_id())));
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
					Double d = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.DINNER, reserve.getHotelsusers_id())));
					if (reserve.isResident())
						return String.valueOf(CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), BigDecimal.valueOf(d)));
					else
						return String.valueOf(d);
				}
			}).add(new AttributeModifier("data-currency", getMySession().getCurrencyName())));

			RadioGroup<Byte> reservation_type;
			add(reservation_type = new RadioGroup<Byte>("reservation_type"));
			reservation_type.add(new Radio<Byte>("definite", new Model<Byte>(ReservationType.DEFINITE)));
			reservation_type.add(new Radio<Byte>("tentative", new Model<Byte>(ReservationType.TENTATIVE)){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible() {
					return rule.getReservationcancellationpolicy().isSupport_tentative_reservation();
				}
				
				@Override
				public boolean isEnabled() {
					return ! (reserve.getId() != null && reserve.getReservation_type() == ReservationType.TENTATIVE);
				}
			});

			Label tentativeLabel = new Label("tentativeLabel", new StringResourceModel("hotels.reservation.details.reservation.type.tentative", null));
			tentativeLabel.setVisible(rule.getReservationcancellationpolicy().isSupport_tentative_reservation());
			reservation_type.add(tentativeLabel);

			add(new TextArea<String>("ta_comments"));
			
			WebMarkupContainer reserve_details_container;
			add(reserve_details_container = new WebMarkupContainer("reserve_details_container"));
			reserve_details_container.setOutputMarkupPlaceholderTag(true);
			reserve_details_container.setVisible(false); //TODO 

			reserve_details_container.add(check_in = new DateTimeTextField("check_in"){
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

			reserve_details_container.add(check_out = new DateTimeTextField("check_out"){
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
		}
	}
	
	private class GuestDataPanel extends Panel {
		private static final long serialVersionUID = 1L;
		
		public GuestDataPanel(String id, final Model<Guest> model) {
			super(id, model);
			if (model.getObject().getChildAge() == null) model.getObject().setChildAge(new ChildAge(ChildAge.UNKNOWN, ChildAge.UNKNOWN));
			add(new Label("person_title", model.getObject().getPerson_title().getName()));
			add(new Label("first_name", model.getObject().getFirst_name()));
			add(new Label("last_name", model.getObject().getLast_name()));
			add(new Label("nationality", model.getObject().getNationality().getName()));
			add(new HiddenField<Short>("guest_index", new PropertyModel<Short>(model, "guest_index")));
			add(new Label("childAge", model.getObject().getChildAge().getAge()));
		}
	}
	
	
	private List<Guest> getGuestDetail(short adults){
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
