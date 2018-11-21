package uz.hbs.actions.touragent.newbooking;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Booking;
import uz.hbs.beans.Currencies;
import uz.hbs.beans.Equipment;
import uz.hbs.beans.Facility;
import uz.hbs.beans.HotelNearByPlace;
import uz.hbs.beans.HotelStar;
import uz.hbs.beans.IdAndName;
import uz.hbs.beans.PricesRange;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.SelectCityField;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.HotelUtil;
import uz.hbs.utils.models.HotelModels;
import uz.hbs.utils.models.MyChoiceRenderer;

public class SearchForBookingPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private SearchForBookingResultPanel resultPanel;

	public SearchForBookingPanel(String id, IBreadCrumbModel breadCrumbModel, final Booking booking) {
		super(id, breadCrumbModel);

		booking.setSortResults(new IdAndName(Booking.SORT_BY_RECOMMENDED, new StringResourceModel("touragents.newbooking.sort_by.recommended", null).getString()));
		booking.setChangeCurrency(getMySession().getCurrency());

		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		final Form<Booking> form = new Form<Booking>("form", new CompoundPropertyModel<Booking>(booking));
		add(form);

		form.add(new SelectCityField("city").setRequired(true));
		
		RadioGroup<Boolean> residentRadioGroup = new RadioGroup<Boolean>("resident");
		form.add(residentRadioGroup);

		residentRadioGroup.add(new Radio<Boolean>("no_resident", new Model<Boolean>(false)));
		residentRadioGroup.add(new Radio<Boolean>("resident", new Model<Boolean>(true)));

		DateTextField fromDate = new DateTextField("fromDate", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false));
		fromDate.setLabel(new StringResourceModel("touragents.newbooking.check_in_date", null));
		fromDate.add(new MyDatePicker(new Date(), null));
		fromDate.add(new AttributeModifier("placeholder", MyWebApplication.DATE_FORMAT.toLowerCase()));
		fromDate.setRequired(true);
		fromDate.add(new IValidator<Date>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(IValidatable<Date> validatable) {
				Date date = validatable.getValue();
				Date currDate = null;
				try {
					currDate = DateUtil.parseDate(DateUtil.toString(new Date(), MyWebApplication.DATE_FORMAT), MyWebApplication.DATE_FORMAT);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (date.before(currDate)) {
					ValidationError error = new ValidationError(this);

					error.setMessage(new StringResourceModel("date.validator.before.check_in", null).getString());

					validatable.error(error);
				}
			}
		});
		form.add(fromDate);

		DropDownChoice<String> fromTime = new DropDownChoice<String>("fromTime", HotelUtil.getTimeList());
		form.add(fromTime);

		DateTextField toDate = new DateTextField("toDate", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false));
		toDate.setLabel(new StringResourceModel("touragents.newbooking.check_out_date", null));
		toDate.add(new MyDatePicker(new Date(), null));
		toDate.add(new AttributeModifier("placeholder", MyWebApplication.DATE_FORMAT.toLowerCase()));
		toDate.setRequired(true);
		toDate.add(new IValidator<Date>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(IValidatable<Date> validatable) {
				Date date = validatable.getValue();
				Date currDate = null;
				try {
					currDate = DateUtil.parseDate(DateUtil.toString(new Date(), MyWebApplication.DATE_FORMAT), MyWebApplication.DATE_FORMAT);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (date.before(currDate)) {
					ValidationError error = new ValidationError(this);

					error.setMessage(new StringResourceModel("date.validator.before.check_out", null).getString());

					validatable.error(error);
				}
			}
		});
		form.add(toDate);

		DropDownChoice<String> toTime = new DropDownChoice<String>("toTime", HotelUtil.getTimeList());
		form.add(toTime);

		HiddenField<Integer> nights = new HiddenField<Integer>("nights");
		form.add(nights);

		DropDownChoice<Integer> rooms = new DropDownChoice<Integer>("rooms", CommonUtil.getIntegerList(Booking.COUNT_ROOMS, false));
		form.add(rooms);

		final WebMarkupContainer roomsListContainer = new WebMarkupContainer("roomsListContainer");
		roomsListContainer.setOutputMarkupId(true);
		form.add(roomsListContainer);

		roomsListContainer.add(new RoomsCountPanel("roomsCountPanel", form.getModelObject(), true));

		rooms.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(roomsListContainer);
			}
		});

		final LoadableDetachableModel<PricesRange> rangeModel = new LoadableDetachableModel<PricesRange>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected PricesRange load() {
				return new MyBatisHelper().selectOne("selectPricesRange");
			}
		};

		final WebMarkupContainer priceRangeContainer = new WebMarkupContainer("priceRangeContainer");
		priceRangeContainer.setOutputMarkupId(true);
		form.add(priceRangeContainer);

		// if (booking.isResident()) {
		booking.setRange_from_value((int) CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), rangeModel.getObject().getStart_price()));
		booking.setRange_to_value((int) CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), rangeModel.getObject().getEnd_price()));
		booking.setRange_min_value((int) CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), rangeModel.getObject().getStart_price()));
		booking.setRange_max_value((int) CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), rangeModel.getObject().getEnd_price()));
		booking.setRange_step_value((int) CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), 5d));
		booking.setRange_prefix_value(getMySession().getCurrencyName());
		// } else {
		// booking.setRange_from_value((int) (100 * rangeModel.getObject().getStart_price())/100);
		// booking.setRange_to_value((int) (100 * rangeModel.getObject().getEnd_price())/100);
		// booking.setRange_min_value((int) (100 * rangeModel.getObject().getStart_price())/100);
		// booking.setRange_max_value((int) (100 * rangeModel.getObject().getEnd_price().intValue())/100);
		// booking.setRange_step_value(5);
		// booking.setRange_prefix_value("$");
		// }

		priceRangeContainer.add(new HiddenField<Integer>("range_from_value"));
		priceRangeContainer.add(new HiddenField<Integer>("range_to_value"));
		priceRangeContainer.add(new HiddenField<Integer>("range_min_value"));
		priceRangeContainer.add(new HiddenField<Integer>("range_max_value"));
		priceRangeContainer.add(new HiddenField<Integer>("range_step_value"));
		priceRangeContainer.add(new HiddenField<String>("range_prefix_value"));

		ListView<HotelStar> checksList = new ListView<HotelStar>("stars", HotelModels.getHotelStarsList(true)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<HotelStar> item) {
				Check<HotelStar> check = new Check<HotelStar>("star", item.getModel());
				check.setLabel(Model.of(item.getModel().getObject().getName()));
				item.add(check);
				// item.add(new Label("starLabel", new StringResourceModel("touragents.newbooking.hotel_class.stars" +
				// item.getModel().getObject().getId().toString(), null)));
				item.add(new Label("starLabel", HotelUtil.starDecorator(item.getModel().getObject().getId(), HotelStar.MAX_STARS, true))
						.setEscapeModelStrings(false));
			}
		};

		final CheckGroup<HotelStar> checks = new CheckGroup<HotelStar>("starsCheckGroup", form.getModelObject().getStars());
		checks.add(checksList);
		form.add(checks);

		LoadableDetachableModel<List<Facility>> hotelFacilitiesListModel = new LoadableDetachableModel<List<Facility>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Facility> load() {
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("filter_flag", true);
				return new MyBatisHelper().selectList("selectFacilitiesList", params);
			}
		};

		ListView<Facility> hotelFacilitiesList = new ListView<Facility>("hotelFacilities", hotelFacilitiesListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Facility> item) {
				Check<Facility> check = new Check<Facility>("hotelFacility", item.getModel());
				item.add(check);
				item.add(new Label("hotelFacilityLabel", item.getModel().getObject().getName()));
			}
		};
		final CheckGroup<Facility> hotelFacilitiesCheckGroup = new CheckGroup<Facility>("hotelFacilitiesCheckGroup", form.getModelObject()
				.getHotelFacilities());
		hotelFacilitiesCheckGroup.add(hotelFacilitiesList);
		form.add(hotelFacilitiesCheckGroup);

		LoadableDetachableModel<List<Equipment>> roomFacilitiesListModel = new LoadableDetachableModel<List<Equipment>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Equipment> load() {
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("filter_flag", true);
				return new MyBatisHelper().selectList("selectEquipments", params);
			}
		};

		ListView<Equipment> roomFacilitiesList = new ListView<Equipment>("roomFacilities", roomFacilitiesListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Equipment> item) {
				Check<Equipment> check = new Check<Equipment>("roomFacility", item.getModel());
				item.add(check);
				item.add(new Label("roomFacilityLabel", item.getModel().getObject().getName()));
			}
		};
		final CheckGroup<Equipment> roomFacilitiesCheckGroup = new CheckGroup<Equipment>("roomFacilitiesCheckGroup", form.getModelObject().getRoomFacilities());
		roomFacilitiesCheckGroup.add(roomFacilitiesList);
		form.add(roomFacilitiesCheckGroup);

		CheckBox cancelation = new CheckBox("cancelation");
		form.add(cancelation);

		Label scriptNightsSuffix = new Label("scriptNightsSuffix", new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected String load() {
				String s = "var nightsSuffix = {";
				for (int i = 1; i <= 5; i++) {
					s +=   ( i == 1 ? "'" : ", '") + i +"'" + ": '" + new StringResourceModel("touragents.newbooking.nights." + i, null).getString() + "'";
				}
				s += "};";
				return s;
			}
		});
		scriptNightsSuffix.setEscapeModelStrings(false);
		form.add(scriptNightsSuffix);
		
		final CheckGroup<HotelNearByPlace> roomNearByPlaceCheckGroup;
		add (roomNearByPlaceCheckGroup = new CheckGroup<HotelNearByPlace>("roomNearByPlaceCheckGroup", form.getModelObject().getNearByPlaces()));
		form.add(roomNearByPlaceCheckGroup);

		ListView<HotelNearByPlace> nearByPlaces = new ListView<HotelNearByPlace>("nearByPlaces", new LoadableDetachableModel<List<HotelNearByPlace>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<HotelNearByPlace> load() {
				List<HotelNearByPlace> list = new ArrayList<HotelNearByPlace>();
				list.add(new HotelNearByPlace(getString("hotels.details.nearbyplaces.city_center"), (float) 0, HotelNearByPlace.CITY_CENTER));
				list.add(new HotelNearByPlace(getString("hotels.details.nearbyplaces.airport"), (float) 0, HotelNearByPlace.AIROPORT));
				list.add(new HotelNearByPlace(getString("hotels.details.nearbyplaces.train"), (float) 0, HotelNearByPlace.TRAIN));
				list.add(new HotelNearByPlace(getString("hotels.details.nearbyplaces.metro"), (float) 0, HotelNearByPlace.METRO));
				return list;
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<HotelNearByPlace> item) {
				final Check<HotelNearByPlace> check;
				final DropDownChoice<Float> nearByPlaceValue;
				item.add(check = new Check<HotelNearByPlace>("nearByPlace", item.getModel()));
				item.add(new Label("nearByPlaceLabel", item.getModel().getObject().getName()));
				final String check_markup_id = check.getMarkupId();
				item.add(nearByPlaceValue = new DropDownChoice<Float>("nearByPlaceValue", new PropertyModel<Float>(check.getModel(), "value"), CommonUtil.getNearByPlaceList(), new MyChoiceRenderer<Float>()){
					private static final long serialVersionUID = 1L;

					@Override
					protected void onAfterRender() {
						super.onAfterRender();
						JavaScriptUtils.writeJavaScript(getResponse(), "$('#" + this.getMarkupId() + "').prop('disabled',! $('#" + check_markup_id +  "').prop('checked'))");
					}
				});
				check.add(new AjaxEventBehavior("onchange") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onEvent(AjaxRequestTarget target) {
						target.appendJavaScript("$('#" + nearByPlaceValue.getMarkupId() + "').prop('disabled',! $('#" + check_markup_id +  "').prop('checked'))");
					}
				});
				nearByPlaceValue.setOutputMarkupId(true);
				nearByPlaceValue.add(new AttributeModifier("data-item", check_markup_id));
			}
		};
		roomNearByPlaceCheckGroup.add(nearByPlaces);

		
		final WebMarkupContainer resultContainer = new WebMarkupContainer("resultContainer");
		resultContainer.setOutputMarkupId(true);
		form.add(resultContainer);
		resultPanel = new SearchForBookingResultPanel("resultPanel", SearchForBookingPanel.this, form.getModel());
		resultPanel.setOutputMarkupId(true);
		resultContainer.add(resultPanel);

		final IndicatingAjaxButton submit = new IndicatingAjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				submit(target, form, feedback, resultContainer);
			}
		};
		form.add(submit);

		IndicatingAjaxButton filter = new IndicatingAjaxButton("filter") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				submit(target, form, feedback, resultContainer);
			}
		};
		form.add(filter);

		DropDownChoice<Currencies> changeCurrency = new DropDownChoice<Currencies>("changeCurrency", new LoadableDetachableModel<List<Currencies>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Currencies> load() {
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("status", Currencies.STATUS_ACTIVE);
				return new MyBatisHelper().selectList("selectCurrencies");
			}
		}, new IChoiceRenderer<Currencies>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Currencies object) {
				String s = null;
				try {
					s = new StringResourceModel("currency." + object.getName().toLowerCase(), null).getString();
				} catch (Exception e) {
					logger.error("Exception", e);
				}
				return s == null ? object.getName() : s;
			}

			@Override
			public String getIdValue(Currencies object, int index) {
				return object.getId().toString();
			}
		});
		
		changeCurrency.setVisible(false);
		
		changeCurrency.add(new AjaxFormSubmitBehavior(form, "onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				Currencies currency = form.getModelObject().getChangeCurrency();
				getMySession().setCurrency(currency);
				form.getModelObject().setRange_min_value((int) CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), rangeModel.getObject().getStart_price()));
				form.getModelObject().setRange_max_value((int) CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), rangeModel.getObject().getEnd_price()));
				form.getModelObject().setRange_step_value((int) CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), 5d));
				form.getModelObject().setRange_prefix_value(getMySession().getCurrencyName() + " ");
				submit(target, form, feedback, resultContainer);
				target.add(priceRangeContainer);
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public CharSequence getSuccessHandler(Component component) {
						return " rangeCreator();";
					}
				};

				attributes.getAjaxCallListeners().add(listener);
			}
		});
		form.add(changeCurrency);

		DropDownChoice<IdAndName> sortResults = new DropDownChoice<IdAndName>("sortResults", new LoadableDetachableModel<List<IdAndName>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<IdAndName> load() {
				List<IdAndName> list = new ArrayList<IdAndName>();
				list.add(new IdAndName(Booking.SORT_BY_RECOMMENDED, new StringResourceModel("touragents.newbooking.sort_by.recommended", null).getString()));
				list.add(new IdAndName(Booking.SORT_BY_PRICE, new StringResourceModel("touragents.newbooking.sort_by.price", null).getString()));
				list.add(new IdAndName(Booking.SORT_BY_HOTEL_NAME, new StringResourceModel("touragents.newbooking.sort_by.hotel_name", null).getString()));
				list.add(new IdAndName(Booking.SORT_BY_STAR_RATING, new StringResourceModel("touragents.newbooking.sort_by.star_rating", null).getString()));
//				list.add(new IdAndName(Booking.SORT_BY_TA_RATING, new StringResourceModel("touragents.newbooking.sort_by.ta_rating", null).getString()));
				return list;
			}
		}, new IChoiceRenderer<IdAndName>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(IdAndName object) {
				return object.getName();
			}

			@Override
			public String getIdValue(IdAndName object, int index) {
				return object.getId().toString();
			}
		});
		sortResults.add(new AjaxFormSubmitBehavior(form, "onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				submit(target, form, feedback, resultContainer);
			}
		});
		form.add(sortResults);
		form.add(new AjaxCheckBox("sortOrder") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				submit(target, form, feedback, resultContainer);
			}
		});
	}

	private void submit(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback, WebMarkupContainer resultContainer) {
		resultPanel = new SearchForBookingResultPanel("resultPanel", SearchForBookingPanel.this, new Model<Booking>((Booking) form.getModelObject()));
		resultContainer.addOrReplace(resultPanel);

		target.add(resultContainer);
		target.add(feedback);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forUrl("js/ion.rangeSlider.js", "rangeslider_id"));
		response.render(CssHeaderItem.forUrl("css/ion.rangeSlider.css"));
		response.render(CssHeaderItem.forUrl("css/ion.rangeSlider.skinHTML5.css"));
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.booking_search_results", null);
	}
}
