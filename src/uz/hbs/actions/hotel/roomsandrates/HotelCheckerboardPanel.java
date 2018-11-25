package uz.hbs.actions.hotel.roomsandrates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Range;
import uz.hbs.beans.Room;
import uz.hbs.beans.RoomType;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;

public class HotelCheckerboardPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;
	private List<Date> dateList = new ArrayList<Date>();
	private ValueMap model;
	private static final int dayInterval = 30;
	private int colId = -1;

	@Override
	protected void onAfterRender() {
		colId = -1;
		super.onAfterRender();
	}

	@Override
	protected void onBeforeRender() {
		colId = -1;
		super.onBeforeRender();
	}

	public HotelCheckerboardPanel(String id, IBreadCrumbModel breadCrumbModel, final long hotel_id) {
		super(id, breadCrumbModel);
		model = new ValueMap();
		final Date fromDate = new MyBatisHelper().selectOne("selectCurrentDate");
		final Date toDate = new MyBatisHelper().selectOne("selectCustomDate", dayInterval - 1);

		model.put("date_from", fromDate);
		model.put("date_to", toDate);

		updateDateList(fromDate, toDate);

		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		final ModalWindow dialog = new ModalWindow("dialog");
		dialog.setInitialHeight(300);
		dialog.setInitialWidth(800);
		dialog.setMinimalHeight(300);
		dialog.setMinimalWidth(800);
		dialog.setCookieName("hotelCheckerBoard");
		add(dialog);

		final WebMarkupContainer container1;
		add(container1 = new WebMarkupContainer("container"));
		container1.setOutputMarkupId(true);

		final LoadableDetachableModel<List<Date>> dateListModel = new LoadableDetachableModel<List<Date>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Date> load() {
				return dateList;
			}
		};

		ListView<Date> months = new ListView<Date>("months", dateListModel) {
			private static final long serialVersionUID = 1L;
			private WebMarkupContainer wmc;
			private int colspan = 1;

			@Override
			protected void populateItem(ListItem<Date> item) {
				int monthId = Integer.parseInt(DateUtil.toString(item.getModelObject(), "M"));

				try {
					WebMarkupContainer container = new WebMarkupContainer("month");
					if (colId == -1 || colId != monthId) {
						colId = monthId;
						colspan = 1;
						wmc = container;
					} else {
						item.setVisible(false);
						colspan++;
					}
					container.add(new Label("monthLabel", new StringResourceModel("month." + DateUtil.toString(item.getModelObject(), "M"), null)
							.getString() + ", " + DateUtil.toString(item.getModelObject(), "yyyy")));
					item.add(container);
				} finally {
					if (colspan > 1) {
						wmc.add(new AttributeModifier("colspan", colspan));
					}
				}
			}
		};
		container1.add(months);

		ListView<Date> dates = new ListView<Date>("dates", dateListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Date> item) {
				Date date = item.getModelObject();
				String s = "";
				s += DateUtil.toString(date, "dd") + ", " + DateUtil.getWeekDayName(date, true);
				Label label = new Label("date", s);
				s = DateUtil.isLastDayOfMonth(date) || Integer.parseInt(DateUtil.toString(date, "dd")) == 1 ? "td-success" : "";
				s += DateUtil.isHolidayAndWeekEnd(date) ? " text-danger" : "";
				label.add(new AttributeModifier("class", s));
				label.setEscapeModelStrings(false);
				item.add(label);
			}
		};
		container1.add(dates);

		final LoadableDetachableModel<List<RoomType>> roomTypeListModel = new LoadableDetachableModel<List<RoomType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<RoomType> load() {
				if (!dateList.isEmpty())
					return new MyBatisHelper().selectList("selectRoomTypesByHotel", hotel_id);
				else
					return Collections.emptyList();
			}
		};

		ListView<RoomType> roomTypes = new ListView<RoomType>("roomTypes", roomTypeListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<RoomType> item) {
				Label roomTypeName;
				item.add(roomTypeName = new Label("roomTypeName", item.getModelObject().getName()));
				roomTypeName.add(new AttributeModifier("data-item", item.getModelObject().getId()));

				final HashMap<String, Serializable> param = new HashMap<String, Serializable>();
				param.put("date_from", (Date) model.get("date_from"));
				param.put("date_to", (Date) model.get("date_to"));
				param.put("roomtype_id", item.getModelObject().getId());
				param.put("hotel_id", hotel_id);
				
				final List<Room> roomlist = new MyBatisHelper().selectList("selectHotelRoomTypesRoomsByHotels", param);
				
				roomTypeName.add(new AttributeModifier("rowspan", roomlist.size() + 1));
				
				item.add(new ListView<Room>("roomlist", new LoadableDetachableModel<List<Room>>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<Room> load() {
						return roomlist;
					}
					
				}){
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem<Room> roomItem) {
						roomItem.add(new Label("room_number", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
//								return roomItem.getModelObject().getRoom_number();
								return String.valueOf(roomItem.getModelObject().getId());
							}
						}).add(new AttributeModifier("data-item", roomItem.getModelObject().getId())));
						roomItem.add(new ListView<Date>("cells", dateListModel) {
							private static final long serialVersionUID = 1L;

							@Override
							protected void populateItem(ListItem<Date> cellItem) {
								RepeatingView listItems = new RepeatingView("listItems");
								param.put("rooms_id", roomItem.getModelObject().getId());
								param.put("date", cellItem.getModelObject());
								List<Range> rangelist = new MyBatisHelper().selectList("selectHotelCheckerboard", param);
								for (Range range : rangelist) {
									HiddenField<Byte> hiddenfield;
									listItems.add(hiddenfield = new HiddenField<Byte>(listItems.newChildId(), new Model<Byte>(range.getStatus())));
									hiddenfield.add(new AttributeModifier("data-date-from", FormatUtil.toString(range.getDate_from(), "HH")));
									hiddenfield.add(new AttributeModifier("data-date-to", FormatUtil.toString(range.getDate_to(), "HH")));
								}
								cellItem.add(listItems);
							}
						});
						
					}
				});
				item.add(new Label("totalRooms", new StringResourceModel("hotels.total_rooms_count", null, new Object[] { roomlist.size() })));
				item.add(new Label("splitter", "<br>").setEscapeModelStrings(false).add(new AttributeModifier("colspan", dateListModel.getObject().size() + 2)));
			}
		};
		container1.add(roomTypes);

		/********************** Filter form ************************/

		final Form<ValueMap> form;
		add(form = new SearchForm("form", model));
		form.add(new IndicatingAjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ValueMap model = (ValueMap) form.getDefaultModelObject();
				boolean error = false;
				try {
					Date from = (Date) model.get("date_from");
					Date to = (Date) model.get("date_to");

					if (from.after(to)) {
						feedback.error(getString("date.range.error"));
						error = true;
					}

					if (error)
						return;

					colId = -1;

					updateDateList(from, to);

					target.add(container1);
					target.appendJavaScript("refreshCell()");
				} finally {
					target.add(feedback);
				}
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(
						new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
			}
		});

	}

	private class SearchForm extends Form<ValueMap> {
		private static final long serialVersionUID = 1L;

		public SearchForm(String id, final ValueMap model) {
			super(id, new CompoundPropertyModel<ValueMap>(model));

			DateTextField fromDate;
			add(fromDate = new DateTextField("date_from", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			fromDate.setLabel(new StringResourceModel("from", null));
			fromDate.add(new MyDatePicker((Date) model.get("date_from"), null));
			fromDate.setRequired(true);
//			fromDate.add(new IValidator<Date>() {
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void validate(IValidatable<Date> validatable) {
//					Date date = validatable.getValue();
//					Date fromDate = null;
//					try {
//						fromDate = DateUtil.parseDate(DateUtil.toString((Date) model.get("date_from"), MyWebApplication.DATE_FORMAT),
//								MyWebApplication.DATE_FORMAT);
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//					if (date.before(fromDate)) {
//						ValidationError error = new ValidationError(this);
//
//						error.setMessage(new StringResourceModel("date.validator.before", null, new Object[] { new StringResourceModel("from", null)
//								.getString() }).getString());
//
//						validatable.error(error);
//					}
//				}
//			});
			add(fromDate);

			DateTextField toDate;
			add(toDate = new DateTextField("date_to", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			toDate.setLabel(new StringResourceModel("to", null));
			toDate.add(new MyDatePicker((Date) model.get("date_from"), null));
			toDate.setRequired(true);
//			toDate.add(new IValidator<Date>() {
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void validate(IValidatable<Date> validatable) {
//					Date date = validatable.getValue();
//					Date fromDate = null;
//					try {
//						fromDate = DateUtil.parseDate(DateUtil.toString((Date) model.get("date_from"), MyWebApplication.DATE_FORMAT),
//								MyWebApplication.DATE_FORMAT);
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//					if (date.before(fromDate)) {
//						ValidationError error = new ValidationError(this);
//
//						error.setMessage(new StringResourceModel("date.validator.before", null, new Object[] { new StringResourceModel("to", null)
//								.getString() }).getString());
//
//						validatable.error(error);
//					}
//				}
//			});
			add(toDate);
			CommonUtil.setFormComponentRequired(this);
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return Model.of("Hotel Checkerboard");
	}

	private void updateDateList(Date from, Date to) {
		Calendar calendar = Calendar.getInstance();
		dateList.clear();
		long interval = (to.getTime() - from.getTime()) / 24 / 60 / 60 / 1000;

		for (long i = 0; i <= interval; i++) {
			calendar.setTime(from);
			calendar.add(Calendar.DATE, (int) i);
			dateList.add(calendar.getTime());
		}
	}
}
