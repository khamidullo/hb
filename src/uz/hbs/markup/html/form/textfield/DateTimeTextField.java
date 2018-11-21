package uz.hbs.markup.html.form.textfield;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.DateTimeFieldType;
import org.joda.time.MutableDateTime;

import uz.hbs.MyWebApplication;
import uz.hbs.utils.CommonUtil;

public abstract class DateTimeTextField extends FormComponentPanel<Date>{
	private static final long serialVersionUID = 1L;
	private MutableDateTime date;
	private DateTextField dateField;
	private Byte time;
	private DropDownChoice<Byte> timeField;

	public DateTimeTextField(String id){
		this(id, null);
	}

	public DateTimeTextField(String id, IModel<Date> model){
		super(id, model);
		setType(Date.class);
		PropertyModel<Date> dateFieldModel = new PropertyModel<Date>(this, "date");
		add(dateField = newDateTextField("date", dateFieldModel));
		dateField.add(new AttributeModifier("placeholder", MyWebApplication.DATE_FORMAT.toLowerCase()));
		dateField.add(new AttributeModifier("data-date-format", MyWebApplication.DATE_FORMAT));
		dateField.add(new MyDatePicker(getDateFrom(), getDateTo()));
		add(timeField = new DropDownChoice<Byte>("time", new PropertyModel<Byte>(this, "time"), getTimelist(), new ChoiceRenderer<Byte>(){
			private static final long serialVersionUID = 1L;

			@Override
			public String getIdValue(Byte object, int index) {
				return String.valueOf(object);
			}
			
			@Override
			public Object getDisplayValue(Byte object) {
				return CommonUtil.lpadZero((int) object) + " : 00"; 
			}
		}));
		timeField.setLabel(new Model<String>("time"));
		timeField.add(new AttributeModifier("data-bind", "time"));
	}

	public Date getDate(){
		return (date != null) ? date.toDate() : null;
	}

	protected void configure(Map<String, Object> widgetProperties){
	}

	@Override
	public String getInput(){
		return getDateField().getInput() + ", " + timeField.getInput() + ":00";
	}

	public void setDate(Date date){
		if (date == null){
			this.date = null;
			setDefaultModelObject(null);
			setTime(null);
			return;
		}

		this.date = new MutableDateTime(date);
		
		setDefaultModelObject(date);
		
		Byte hours = getTime();
		
		if (hours != null){
			this.date.set(DateTimeFieldType.hourOfDay(), hours.intValue() % 24);
			this.date.setMinuteOfHour(0);
		}
		setDefaultModelObject(this.date.toDate());
	}

	@Override
	protected void convertInput(){
		Object dateFieldInput = getDateField().getConvertedInput();
		if (dateFieldInput != null)	{
			MutableDateTime date = new MutableDateTime(dateFieldInput);
			Byte hours = timeField.getConvertedInput();
			try {
				if (hours != null) {
					date.set(DateTimeFieldType.hourOfDay(), hours.intValue() % 24);
					date.setMinuteOfHour(0);
				}
				
				setConvertedInput(date.toDate());
			} catch (RuntimeException e) {
				DateTimeTextField.this.error(e.getMessage());
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
		timeField.setRequired(isRequired());

		Date d = (Date)getDefaultModelObject();
		if (d != null){
			date = new MutableDateTime(d);
		} else {
			date = null;
		}

		if (date != null) {
			time = new Integer(date.get(DateTimeFieldType.hourOfDay())).byteValue();
		}
		
		super.onBeforeRender();
	}

	protected DatePicker newDatePicker(){
		return new MyDatePicker();
	}

	public DateTextField getDateField() {
		return dateField;
	}
	
	public Byte getTime() {
		return time;
	}

	public void setTime(Byte time) {
		this.time = time;
	}

	public DropDownChoice<Byte> getTimeField() {
		return timeField;
	}

	public void setTimeField(DropDownChoice<Byte> timeField) {
		this.timeField = timeField;
	}
	
	private List<Byte> getTimelist(){
		List<Byte> timelist = new ArrayList<Byte>();
		byte t1 = 0;
		byte t2 = 23;
		Date dt1 = getDateFrom();
		if (dt1 != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dt1);
			t1 = (byte) calendar.get(Calendar.HOUR_OF_DAY);
		}
		Date dt2 = getDateTo();
		if (dt2 != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dt2);
			t2 = (byte) calendar.get(Calendar.HOUR_OF_DAY);
		}
		for (byte t = t1; t <= t2; t++){
			timelist.add(t);
		}
		return timelist;
	}
	
	protected abstract Date getDateFrom();
	protected abstract Date getDateTo();
}
