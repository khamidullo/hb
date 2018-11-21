package uz.hbs.markup.html.form.textfield;

import java.util.Date;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.DateTimeFieldType;
import org.joda.time.MutableDateTime;

import uz.hbs.MyWebApplication;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.models.HotelModels;
import uz.hbs.utils.models.LPadZeroChoiceRenderer;

public abstract class DateTimeField extends FormComponentPanel<Date>{
	private static final long serialVersionUID = 1L;
	private MutableDateTime date;
	private DateTextField dateField;
	private Byte hours;
	private DropDownChoice<Byte> hoursField;
	private Byte minutes;
	private DropDownChoice<Byte> minutesField;

	public DateTimeField(String id){
		this(id, null);
	}

	public DateTimeField(String id, IModel<Date> model){
		super(id, model);
		setType(Date.class);
		PropertyModel<Date> dateFieldModel = new PropertyModel<Date>(this, "date");
		add(dateField = newDateTextField("date", dateFieldModel));
		dateField.add(new AttributeModifier("placeholder", MyWebApplication.DATE_FORMAT.toLowerCase()));
		dateField.add(new AttributeModifier("data-date-format", MyWebApplication.DATE_FORMAT));
		if (isFirstNow()) dateField.add(new MyDatePicker(CommonUtil.today(), null));
		else dateField.add(new MyDatePicker());
		add(hoursField = new DropDownChoice<Byte>("hours", new PropertyModel<Byte>(this, "hours"), HotelModels.getTimeModel((byte) 23), new LPadZeroChoiceRenderer<Byte>()));
		hoursField.setLabel(new Model<String>("hours"));
		hoursField.add(new AttributeModifier("data-bind", "hour"));
		add(minutesField = new DropDownChoice<Byte>("minutes", new PropertyModel<Byte>(this, "minutes"), HotelModels.getTimeModel((byte) 59), new LPadZeroChoiceRenderer<Byte>()));
		minutesField.setLabel(new Model<String>("minutes"));
		minutesField.add(new AttributeModifier("data-bind", "minute"));
	}

	public Date getDate(){
		return (date != null) ? date.toDate() : null;
	}

	public Byte getHours(){
		return hours;
	}

	protected void configure(Map<String, Object> widgetProperties){
	}

	@Override
	public String getInput(){
		return getDateField().getInput() + ", " + hoursField.getInput() + ":" + minutesField.getInput();
	}

	public Byte getMinutes(){
		return minutes;
	}

	public void setDate(Date date){
		if (date == null){
			this.date = null;
			setDefaultModelObject(null);
			setHours(null);
			setMinutes(null);
			return;
		}

		this.date = new MutableDateTime(date);
		setDefaultModelObject(date);

		Byte hours = getHours();
		Byte minutes = getMinutes();
		boolean use12HourFormat = use12HourFormat();
		if (hours != null){
			this.date.set(DateTimeFieldType.hourOfDay(), hours.intValue() % (use12HourFormat ? 12 : 24));
			this.date.setMinuteOfHour((minutes != null) ? minutes.intValue() : 0);
		}
		setDefaultModelObject(this.date.toDate());
	}

	public void setHours(Byte hours){
		this.hours = hours;
	}

	public void setMinutes(Byte minutes){
		this.minutes = minutes;
	}
	
	@Override
	protected void convertInput(){
		Object dateFieldInput = getDateField().getConvertedInput();
		if (dateFieldInput != null)	{
			MutableDateTime date = new MutableDateTime(dateFieldInput);
			Byte hours = hoursField.getConvertedInput();
			Byte minutes = minutesField.getConvertedInput();

			try {
				boolean use12HourFormat = use12HourFormat();
				if (hours != null) {
					date.set(DateTimeFieldType.hourOfDay(), hours.intValue() % getMaximumHours(use12HourFormat));
					date.setMinuteOfHour((minutes != null) ? minutes.intValue() : 0);
				}
				
				setConvertedInput(date.toDate());
			} catch (RuntimeException e) {
				DateTimeField.this.error(e.getMessage());
				invalid();
			}
		} else {
			setConvertedInput(null);
		}
	}

	protected DateTextField newDateTextField(String id, PropertyModel<Date> dateFieldModel){
		return DateTextField.withConverter(id, dateFieldModel, new PatternDateConverter(MyWebApplication.DATE_FORMAT, false));
	}

	@Override
	protected void onBeforeRender()	{
		getDateField().setRequired(isRequired());
		hoursField.setRequired(isRequired());
		minutesField.setRequired(isRequired());

		Date d = (Date)getDefaultModelObject();
		if (d != null){
			date = new MutableDateTime(d);
		} else {
			date = null;
			hours = null;
			minutes = null;
		}

		if (date != null) {
			hours 	= new Integer(date.get(DateTimeFieldType.hourOfDay())).byteValue();
			minutes = new Integer(date.getMinuteOfHour()).byteValue();
		}
		
		super.onBeforeRender();
	}

	protected boolean use12HourFormat(){
		return false;
	}

	private int getMaximumHours(boolean use12HourFormat){
		return use12HourFormat ? 12 : 24;
	}

	protected DatePicker newDatePicker(){
		return new MyDatePicker();
	}

	public DateTextField getDateField() {
		return dateField;
	}
	
	protected abstract boolean isFirstNow();
}
