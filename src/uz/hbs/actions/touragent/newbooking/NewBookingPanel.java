package uz.hbs.actions.touragent.newbooking;

import java.text.ParseException;
import java.util.Date;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.RangeValidator;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Booking;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.SelectCityField;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.HotelUtil;

public class NewBookingPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public NewBookingPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		Booking booking = new Booking();
		booking.setRooms(1);
		booking.setFromTime("14:00");
		booking.setToTime("12:00");

		final Date currentDate = new MyBatisHelper().selectOne("selectCurrentDate");
		Date tomorrowDate = new MyBatisHelper().selectOne("selectTomorrowDate");
		booking.setFromDate(currentDate);
		booking.setToDate(tomorrowDate);

		final Form<Booking> form = new Form<Booking>("form", new CompoundPropertyModel<Booking>(booking));
		add(form);

		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		form.add(feedback);

		// final LoadableDetachableModel<List<? extends Region>> regionsList = new LoadableDetachableModel<List<? extends Region>>() {
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// protected List<? extends Region> load() {
		// Map<String, Serializable> params = new HashMap<String, Serializable>();
		// params.put("countries_id", Country.UZBEKISTAN);
		// return new MyBatisHelper().selectList("selectRegionsList", params);
		// }
		// };
		form.add(new SelectCityField("city").setRequired(true));

		/*
		 * final DropDownChoice<Region> regions = new DropDownChoice<Region>("region", regionsList, new IChoiceRenderer<Region>() {
		 * private static final long serialVersionUID = 1L;
		 * 
		 * @Override
		 * public Object getDisplayValue(Region object) {
		 * return object.getName();
		 * }
		 * 
		 * @Override
		 * public String getIdValue(Region object, int index) {
		 * return object.getId().toString();
		 * }
		 * });
		 * regions.setLabel(new StringResourceModel("hotels.details.region", null));
		 * regions.setOutputMarkupId(true);
		 * regions.setNullValid(false);
		 * regions.setRequired(true);
		 * form.add(regions);
		 */
		RadioGroup<Boolean> residentRadioGroup = new RadioGroup<Boolean>("resident");
		form.add(residentRadioGroup);

		residentRadioGroup.add(new Radio<Boolean>("no_resident", new Model<Boolean>(false)));
		residentRadioGroup.add(new Radio<Boolean>("resident", new Model<Boolean>(true)));

//		ListView<HotelStar> checksList = new ListView<HotelStar>("stars", HotelModels.getHotelStarsList(true)) {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void populateItem(ListItem<HotelStar> item) {
//				Check<HotelStar> check = new Check<HotelStar>("star", item.getModel());
//				check.setLabel(Model.of(item.getModel().getObject().getName()));
//				item.add(check);
//				item.add(new Label("starLabel", HotelUtil.starDecorator(item.getModel().getObject().getId(), HotelStar.MAX_STARS, true))
//						.setEscapeModelStrings(false));
//			}
//		};
//
//		final CheckGroup<HotelStar> checks = new CheckGroup<HotelStar>("starsCheckGroup", form.getModelObject().getStars());
//		checks.add(checksList);
//		form.add(checks);

		DateTextField fromDate = new DateTextField("fromDate", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false));
		fromDate.setLabel(new StringResourceModel("touragents.newbooking.check_in_date", null));
		fromDate.add(new MyDatePicker(currentDate, null));
		fromDate.add(new AttributeModifier("placeholder", MyWebApplication.DATE_FORMAT.toLowerCase()));
		fromDate.setRequired(true);
		fromDate.add(new IValidator<Date>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(IValidatable<Date> validatable) {
				Date date = validatable.getValue();
				Date currDate = null;
				try {
					currDate = DateUtil.parseDate(DateUtil.toString(currentDate, MyWebApplication.DATE_FORMAT), MyWebApplication.DATE_FORMAT);
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
		toDate.add(new MyDatePicker(currentDate, null));
		toDate.add(new AttributeModifier("placeholder", MyWebApplication.DATE_FORMAT.toLowerCase()));
		toDate.setRequired(true);
		toDate.add(new IValidator<Date>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(IValidatable<Date> validatable) {
				Date date = validatable.getValue();
				Date currDate = null;
				try {
					currDate = DateUtil.parseDate(DateUtil.toString(currentDate, MyWebApplication.DATE_FORMAT), MyWebApplication.DATE_FORMAT);
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
		nights.add(RangeValidator.minimum(1));
		nights.setLabel(new StringResourceModel("touragents.newbooking.nights", null));
		form.add(nights);

		DropDownChoice<Integer> rooms = new DropDownChoice<Integer>("rooms", CommonUtil.getIntegerList(Booking.COUNT_ROOMS, false));
		form.add(rooms);

		final WebMarkupContainer roomsListContainer = new WebMarkupContainer("roomsListContainer");
		roomsListContainer.setOutputMarkupId(true);
		form.add(roomsListContainer);

		roomsListContainer.add(new RoomsCountPanel("roomsCountPanel", form.getModelObject(), false));

		rooms.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(roomsListContainer);
			}
		});

		Label scriptNightsSuffix = new Label("scriptNightsSuffix", new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected String load() {
				String s = "var nightsSuffix = {";
				for (int i = 1; i <= 5; i++) {
					s += (i == 1 ? "'" : ", '") + i + "'" + ": '" + new StringResourceModel("touragents.newbooking.nights." + i, null).getString()
							+ "'";
				}
				s += "};";
				return s;
			}
		});
		scriptNightsSuffix.setEscapeModelStrings(false);
		form.add(scriptNightsSuffix);

		Button submit = new Button("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getOnClickScript() {
				return "if (! checkRequiredFields('" + form.getMarkupId() + "','" + getLocale().getLanguage() + "')) { return false; }";
			}

			@Override
			public void onSubmit() {
				final Booking model = form.getModelObject();

				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {

						// List<BookingSearchResult> searchResultList = new ArrayList<BookingSearchResult>();
						// Map<String, Serializable> params = new HashMap<String, Serializable>();
						// params.put("regions_id", regionValue.getId());
						// List<Hotel> hotelsList = new MyBatisHelper().selectList("selectHotelsByRegion", params);

						// for (Hotel hotel : hotelsList) {
						// params.put("hotel_id", hotel.getUsers_id());
						// params.put("is_group", false);
						// List<BookingSearchResult> searchResult = new MyBatisHelper().selectList("selectSearchHotels", params);
						// if (searchResult != null) {
						// searchResult.setFree_cancelation(searchResult.getNo_penalty_before_days() == null ||
						// searchResult.getNo_penalty_before_days() == -1 ? true : false);

						// searchResultList.add(searchResult);
						// logger.debug("SearchResult: " + searchResult);
						// }
						// }
						// model.setHotels(hotelsList);
						return new SearchForBookingPanel(componentId, breadCrumbModel, model);
					}
				});
			}
		};
		form.add(submit);
		CommonUtil.setFormComponentRequired(form);
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.newbooking", null);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}