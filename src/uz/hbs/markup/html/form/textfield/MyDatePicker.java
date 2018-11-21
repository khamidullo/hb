package uz.hbs.markup.html.form.textfield;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.Response;

import uz.hbs.MyWebApplication;

public class MyDatePicker extends DatePicker {
	private static final long serialVersionUID = 1L;
	private boolean afterRenderIcon;
	private Date minDate;
	private Date maxDate;

	public MyDatePicker() {
		this(true, MyWebApplication.DATE_FORMAT);
	}

	public MyDatePicker(String datePattern) {
		this(true, datePattern);
	}

	public MyDatePicker(boolean afterRenderIcon) {
		this(afterRenderIcon, MyWebApplication.DATE_FORMAT);
	}

	public MyDatePicker(boolean afterRenderIcon, String datePattern) {
		this.afterRenderIcon = afterRenderIcon;
		FORMAT_DATE = datePattern;
		setAutoHide(true);
		setShowOnFieldClick(true);
	}

	public MyDatePicker(boolean afterRenderIcon, String datePattern, Date minDate, Date maxDate) {
		this(afterRenderIcon, datePattern);
		this.minDate = minDate;
		this.maxDate = maxDate;
	}

	public MyDatePicker(Date minDate, Date maxDate) {
		this();
		this.minDate = minDate;
		this.maxDate = maxDate;
	}

	@Override
	protected boolean enableMonthYearSelection() {
		return true;
	}

	@Override
	public void afterRender(Component component) {
		Response response = component.getResponse();
		if (afterRenderIcon) {
			writeIcon(response);
		} else {
			response.write("");
		}
	}

	@Override
	public void beforeRender(Component component) {
		Response response = component.getResponse();
		if (afterRenderIcon) {
			response.write("");
		} else {
			writeIcon(response);
		}
	}

	private void writeIcon(Response response) {
		response.write("\n<span class=\"input-group-addon\">");
		response.write("<i class=\"fa fa-calendar\" id=\"" + getIconId() + "\" style=\"cursor: pointer;\"></i>");
		response.write("<span class=\"yui-skin-sam\"><span style=\"");

		if (renderOnLoad()) {
			response.write("display:block;");
		} else {
			response.write("display:none;");
			response.write("position:fixed;");
		}

		response.write("z-index: 99999;\" id=\"");
		response.write(getEscapedComponentMarkupId());
		response.write("Dp\"></span>");

		if (renderOnLoad()) {
			response.write("<br style=\"clear:left;\"/>");
		}
		response.write("</span></span>");
	}

	@Override
	protected void configure(Map<String, Object> widgetProperties, IHeaderResponse response, Map<String, Object> initVariables) {
		super.configure(widgetProperties, response, initVariables);

		/*
		 * var navConfig = {
		 * strings: {
		 * month:"Calendar Month",
		 * year:"Calendar Year",
		 * submit: "Submit",
		 * cancel: "Cancel",
		 * invalidYear: "Please enter a valid year"
		 * },
		 * monthFormat: YAHOO.widget.Calendar.SHORT,
		 * initialFocus: "month"
		 * }
		 */

		Map<String, Object> strings = new HashMap<String, Object>();
		strings.put("month", new StringResourceModel("calendar.month", null).getString());
		strings.put("year", new StringResourceModel("calendar.year", null).getString());
		strings.put("submit", new StringResourceModel("calendar.submit", null).getString()); // put label for 'Okay' button
		strings.put("cancel", new StringResourceModel("calendar.cancel", null).getString()); // put label for 'Cancel' button
		strings.put("invalidYear", new StringResourceModel("calendar.invalid_year", null).getString());

		Map<String, Object> props = new HashMap<String, Object>();
		props.put("strings", strings); // pass localization related parameters
		props.put("monthFormat", "YAHOO.widget.Calendar.SHORT");
		props.put("initialFocus", "year");

		widgetProperties.put("navigator", props);

		if (minDate != null) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			widgetProperties.put("mindate", format.format(minDate));
		}

		if (maxDate != null) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			widgetProperties.put("maxdate", format.format(maxDate));
		}
	}
	//
	// @Override
	// protected void configure(Map<String, Object> widgetProperties, IHeaderResponse response, Map<String, Object> initVariables) {
	// super.configure(widgetProperties, response, initVariables);
	// SimpleDateFormat format = new SimpleDateFormat(MyWebApplication.DATE_FORMAT);
	// widgetProperties.put("mindate", "01/02/2015");
	// }
}
