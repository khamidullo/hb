package uz.hbs.markup.navigation.repeater.data.table.filter;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * A filtered property column that creates a textfield filter component. The default model of the created textfield is a property model with the same
 * property expression as the one used to display data. This works well when the filter state object is of the same type as the objects in the data
 * table.
 * 
 * @author Igor Vaynberg (ivaynberg)
 * @param <T>
 *            The column's model object type
 * @param <F>
 *            Filter's model object type
 * @param <S>
 *            the type of the sort property
 */
public class MyTextFilteredPropertyColumn<T, F, S> extends FilteredPropertyColumn<T, S> {
	private static final long serialVersionUID = 1L;

	/**
	 * @param displayModel
	 * @param sortProperty
	 * @param propertyExpression
	 */
	public MyTextFilteredPropertyColumn(final IModel<String> displayModel, final S sortProperty, final String propertyExpression) {
		super(displayModel, sortProperty, propertyExpression);
	}

	/**
	 * @param displayModel
	 * @param propertyExpression
	 */
	public MyTextFilteredPropertyColumn(final IModel<String> displayModel, final String propertyExpression) {
		super(displayModel, propertyExpression);
	}

	/**
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn#getFilter(java.lang.String,
	 *      org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm)
	 */
	@Override
	public Component getFilter(final String componentId, final FilterForm<?> form) {
		TextFilter<F> filter = new TextFilter<F>(componentId, getFilterModel(form), form);
		filter.getFilter().add(new AttributeModifier("class", "form-control"));
		filter.getFilter().add(new AttributeModifier("style", "margin:0;"));
		return filter;
	}

	/**
	 * Returns the model that will be passed on to the text filter. Users can override this method to change the model.
	 * 
	 * @param form
	 *            filter form
	 * @return model passed on to the text filter
	 */
	protected IModel<F> getFilterModel(final FilterForm<?> form) {
		return new PropertyModel<F>(form.getDefaultModel(), getPropertyExpression());
	}
}