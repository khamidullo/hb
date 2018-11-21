package uz.hbs.markup.html.form.textfield;

import java.util.Date;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.AbstractFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.model.IModel;

public class DateTextFilter extends AbstractFilter {
	private static final long serialVersionUID = 1L;
	private final DateTextField filter;

	public DateTextFilter(String id, IModel<Date> model, FilterForm<?> form, String datePattern) {
		super(id, form);
		filter = new DateTextField("filter", model, new PatternDateConverter(datePattern, false));
		filter.add(new AttributeModifier("placeholder", datePattern.toUpperCase()));
		filter.add(new MyDatePicker());
		enableFocusTracking(filter);
		add(filter);
	}
	
	public final DateTextField getFilter() {
		return filter;
	}
}
