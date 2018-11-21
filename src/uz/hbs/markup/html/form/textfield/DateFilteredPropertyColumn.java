package uz.hbs.markup.html.form.textfield;

import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredPropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * A filtered property column that creates a textfield filter component. The default model of the
 * created datetextfield is a property model with the same property expression as the one used to
 * display data. This works well when the filter state object is of the same type as the objects in
 * the data table.
 * 
 * @author Maqsudjon Mamarasulov (maqsudjon)
 * @param <T>
 *            The column's model object type
 * 
 */
public class DateFilteredPropertyColumn<T,S> extends FilteredPropertyColumn<T,S> {
	private static final long serialVersionUID = 1L;
	private String datePattern; 
	/**
	 * @param displayModel
	 * @param propertyExpression
	 */
	public DateFilteredPropertyColumn(IModel<String> displayModel, String propertyExpression, String datePattern) {
		super(displayModel, propertyExpression);
		this.datePattern = datePattern;
	}

	/**
	 * @param displayModel
	 * @param sortProperty
	 * @param propertyExpression
	 */
	public DateFilteredPropertyColumn(IModel<String> displayModel, S sortProperty, String propertyExpression, String datePattern) {
		super(displayModel, sortProperty, propertyExpression);
		this.datePattern = datePattern;
	}

	/**
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn#getFilter(java.lang.String,
	 *      org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm)
	 */
	@Override
	public Component getFilter(String componentId, FilterForm<?> form) {
		return new DateTextFilter(componentId, getFilterModel(form), form, datePattern);
	}

	/**
	 * Returns the model that will be passed on to the text filter. Users can override this method
	 * to change the model.
	 * 
	 * @param form
	 *            filter form
	 * @return model passed on to the text filter
	 */
	protected IModel<Date> getFilterModel(FilterForm<?> form) {
		return new PropertyModel<Date>(form.getDefaultModel(), getPropertyExpression());
	}
}
