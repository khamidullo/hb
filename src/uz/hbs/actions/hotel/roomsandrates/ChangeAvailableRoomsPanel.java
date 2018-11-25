package uz.hbs.actions.hotel.roomsandrates;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.RoomType;
import uz.hbs.beans.TourAgentAvailableRooms;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.models.MyChoiceRenderer;

public abstract class ChangeAvailableRoomsPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ChangeAvailableRoomsPanel.class);
	private Date date;
	private DropDownChoice<Integer> number_of_rooms;

	public ChangeAvailableRoomsPanel(String id, final FeedbackPanel feedback, long hotel_id, final Date dateValue, final RoomType roomType) {
		super(id);

		date = dateValue;

		final ValueMap valueMap = new ValueMap();

		final boolean isSingleDate = date == null ? false : true;
		if (date == null) {
			date = new MyBatisHelper().selectOne("selectCurrentDate");
		}

		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("roomtype_id", roomType.getId());
		param.put("hotel_id", hotel_id);

		valueMap.put("roomtype_id", roomType.getId());
		valueMap.put("date_from", date);
		valueMap.put("date_to", date);
		valueMap.put("number_of_rooms", 0);

		if (isSingleDate) {
			param.put("available_date", date);
			valueMap.put("number_of_rooms", CommonUtil.nvl((Short) new MyBatisHelper().selectOne("selectTAAvailableRoomBySingleDate", param)));
		}

		final Form<ValueMap> form = new Form<ValueMap>("form", new CompoundPropertyModel<ValueMap>(valueMap));
		add(form);

		DateTextField fromDate;
		add(fromDate = new DateTextField("date_from", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
		fromDate.setLabel(new StringResourceModel("from", null));
		if (!isSingleDate)
			fromDate.add(new MyDatePicker(date, null));
		fromDate.setRequired(true);
		fromDate.setEnabled(!isSingleDate);
		fromDate.add(new IValidator<Date>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(IValidatable<Date> validatable) {
				Date date = validatable.getValue();
				Date fromDate = null;
				try {
					fromDate = DateUtil.parseDate(DateUtil.toString((Date) form.getModelObject().get("date_from"), MyWebApplication.DATE_FORMAT),
							MyWebApplication.DATE_FORMAT);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (date.before(fromDate)) {
					ValidationError error = new ValidationError(this);

					error.setMessage(
							new StringResourceModel("date.validator.before", null, new Object[] { new StringResourceModel("from", null).getString() })
									.getString());

					validatable.error(error);
				}
			}
		});
		form.add(fromDate);

		DateTextField toDate;
		add(toDate = new DateTextField("date_to", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
		toDate.setLabel(new StringResourceModel("to", null));
		if (!isSingleDate)
			toDate.add(new MyDatePicker(date, null));
		toDate.setRequired(true);
		toDate.setEnabled(!isSingleDate);
		toDate.add(new IValidator<Date>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(IValidatable<Date> validatable) {
				Date date = validatable.getValue();
				Date fromDate = null;
				try {
					fromDate = DateUtil.parseDate(DateUtil.toString((Date) form.getModelObject().get("date_from"), MyWebApplication.DATE_FORMAT),
							MyWebApplication.DATE_FORMAT);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (date.before(fromDate)) {
					ValidationError error = new ValidationError(this);

					error.setMessage(
							new StringResourceModel("date.validator.before", null, new Object[] { new StringResourceModel("to", null).getString() })
									.getString());

					validatable.error(error);
				}
			}
		});
		form.add(toDate);
		CommonUtil.setFormComponentRequired(form);

		Label maxNumberOfRoomsLabel = new Label("max_number_of_rooms");
		form.add(maxNumberOfRoomsLabel);

		form.add(number_of_rooms = new DropDownChoice<Integer>("number_of_rooms", new LoadableDetachableModel<List<Integer>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Integer> load() {
				short count = new MyBatisHelper().selectOne("selectNumberOfRooms", roomType.getId());
				return CommonUtil.getIntegerList((int) count, true);
			}
		}, new MyChoiceRenderer<Integer>()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isDisabled(Integer object, int index, String selected) {
				short max_busy = CommonUtil.nvl((Short) new MyBatisHelper().selectOne("selectTAAvailableRoomCount", valueMap));
				if (max_busy != 0 && object < max_busy)
					return true;
				return super.isDisabled(object, index, selected);
			}
		});
		number_of_rooms.setLabel(new StringResourceModel("hotels.for_sale", null));
		number_of_rooms.setOutputMarkupId(true);
		number_of_rooms.setRequired(true);

		toDate.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(number_of_rooms);
			}
		});

		fromDate.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(number_of_rooms);
			}
		});

		AjaxButton submit = new AjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				ValueMap model = (ValueMap) form.getModelObject();

				if (isSingleDate) {
					TourAgentAvailableRooms availableRooms = new TourAgentAvailableRooms();
					availableRooms.setAvailable_date(date);
					availableRooms.setAvailable_count((Integer) model.get("number_of_rooms"));
					availableRooms.setRoomtypes_id(roomType.getId());
					availableRooms.setInitiator_user_id(((MySession) getSession()).getUser().getId());
					if (new MyBatisHelper().update("updateTourAgentAvailableRooms", availableRooms) == 0) {
						new MyBatisHelper().insert("insertTourAgentAvailableRooms", availableRooms);
						logger.info("Available rooms inserted(single): HotelId=" + hotel_id + ", Date="
								+ DateUtil.toString(availableRooms.getAvailable_date(), "dd/MM/yyyy") + ", Count="
								+ availableRooms.getAvailable_count() + ", RoomType=" + availableRooms.getRoomtypes_id() + ", InitiatorUserId="
								+ availableRooms.getInitiator_user_id());
					} else {
						logger.info("Available rooms updated(single): HotelId=" + hotel_id + ", Date="
								+ DateUtil.toString(availableRooms.getAvailable_date(), "dd/MM/yyyy") + ", Count="
								+ availableRooms.getAvailable_count() + ", RoomType=" + availableRooms.getRoomtypes_id() + ", InitiatorUserId="
								+ availableRooms.getInitiator_user_id());
					}
					feedback.success(new StringResourceModel("update.successfully", null).getString());
				} else {
					SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
					try {
						List<TourAgentAvailableRooms> selectList = sql.selectList("selectTourAgentAvailableRoomsByDates", model);
						for (TourAgentAvailableRooms room : selectList) {
							room.setAvailable_date(room.getAvailable_date());
							room.setAvailable_count((Integer) model.get("number_of_rooms"));
							room.setRoomtypes_id(roomType.getId());
							room.setInitiator_user_id(((MySession) getSession()).getUser().getId());
							if (sql.update("updateTourAgentAvailableRooms", room) == 0) {
								sql.insert("insertTourAgentAvailableRooms", room);
								logger.info("Available rooms inserted: HotelId=" + hotel_id + ", Date="
										+ DateUtil.toString(room.getAvailable_date(), "dd/MM/yyyy") + ", Count=" + room.getAvailable_count()
										+ ", RoomType=" + room.getRoomtypes_id() + ", InitiatorUserId=" + room.getInitiator_user_id());
							} else {
								logger.info("Available rooms updated: HotelId=" + hotel_id + ", Date="
										+ DateUtil.toString(room.getAvailable_date(), "dd/MM/yyyy") + ", Count=" + room.getAvailable_count()
										+ ", RoomType=" + room.getRoomtypes_id() + ", InitiatorUserId=" + room.getInitiator_user_id());

							}
						}
						sql.commit();
						feedback.success(new StringResourceModel("update.successfully", null).getString());
					} finally {
						sql.close();
					}
				}

				target.add(feedback);
				onOk(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
		};
		form.add(submit);

		AjaxButton cancel = new AjaxButton("cancel") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				onCancel(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				onCancel(target);
			}
		};
		form.add(cancel);
	}

	protected abstract void onOk(AjaxRequestTarget target);

	protected abstract void onCancel(AjaxRequestTarget target);
}
