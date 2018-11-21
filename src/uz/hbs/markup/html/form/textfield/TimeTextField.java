package uz.hbs.markup.html.form.textfield;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import uz.hbs.beans.ReservationRuleType;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.models.HotelModels;
import uz.hbs.utils.models.LPadZeroChoiceRenderer;

public class TimeTextField extends FormComponentPanel<String> {
	private static final long serialVersionUID = 1L;
	private Byte hours;
	private DropDownChoice<Byte> hoursField;
	private Byte minutes;
	private DropDownChoice<Byte> minutesField;

	public TimeTextField(String id) {
		this(id, null);
	}
	
	public TimeTextField(String id, IModel<String> model) {
		super(id, model);
		add(hoursField = new DropDownChoice<Byte>("hours", new PropertyModel<Byte>(this, "hours"), HotelModels.getTimeModel(ReservationRuleType.HOUR), new LPadZeroChoiceRenderer<Byte>()));
		hoursField.add(new AjaxOnBlurEvent());
		hoursField.setLabel(new Model<String>("hours"));
		add(minutesField = new DropDownChoice<Byte>("minutes", new PropertyModel<Byte>(this, "minutes"), HotelModels.getTimeModel(ReservationRuleType.MINUTE), new LPadZeroChoiceRenderer<Byte>()));
		minutesField.add(new AjaxOnBlurEvent());
		minutesField.setLabel(new Model<String>("minutes"));
	}
	
	@Override
	public String getInput(){
		return hoursField.getInput() + ":" + minutesField.getInput();
	}
	
	@Override
	protected void convertInput() {
		Byte hours = hoursField.getConvertedInput();
		Byte minutes = minutesField.getConvertedInput();
		setConvertedInput(FormatUtil.twoDigitFormat(hours) + ":" + FormatUtil.twoDigitFormat(minutes));
	}

	@Override
	protected void onBeforeRender() {
		String time = (String) getDefaultModelObject();
		if (time == null || "".equals(time.trim())|| ! time.contains(":")) time = "00:00";
		String [] s = time.split(":");
		this.hours 		= Byte.parseByte(s[0]);
		this.minutes 	= Byte.parseByte(s[1]);
		super.onBeforeRender();
	}
	
	public Byte getHours() {
		return hours;
	}

	public void setHours(Byte hours) {
		this.hours = hours;
		setDefaultModelObject(FormatUtil.twoDigitFormat(hours) + ":" + FormatUtil.twoDigitFormat(minutes));
	}

	public Byte getMinutes() {
		return minutes;
	}

	public void setMinutes(Byte minutes) {
		this.minutes = minutes;
		setDefaultModelObject(FormatUtil.twoDigitFormat(hours) + ":" + FormatUtil.twoDigitFormat(minutes));
	}
}
