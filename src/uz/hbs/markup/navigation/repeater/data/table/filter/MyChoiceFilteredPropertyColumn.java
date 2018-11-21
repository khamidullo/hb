package uz.hbs.markup.navigation.repeater.data.table.filter;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredPropertyColumn;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.KeyAndValue;
/**
 * A filtered property column that creates a textfield filter component. The default model of the
 * created textfield is a property model with the same property expression as the one used to
 * display data. This works well when the filter state object is of the same type as the objects in
 * the data table.
 * 
 * @author Igor Vaynberg (ivaynberg)
 * @param <T>
 *            The model object type
 * @param <Y>
 *            The column model object type
 * @param <S>
 *            the type of the sort property
 */
public class MyChoiceFilteredPropertyColumn<T, Y, S> extends FilteredPropertyColumn<T, S> {
	private static final long serialVersionUID = 1L;
	private final IModel<List<? extends Y>> filterChoices;

	/**
	 * @param displayModel
	 * @param sortProperty
	 * @param propertyExpression
	 * @param filterChoices
	 *            collection choices used in the choice filter
	 */
	public MyChoiceFilteredPropertyColumn(final IModel<String> displayModel, final S sortProperty, final String propertyExpression,
			final IModel<List<? extends Y>> filterChoices) {
		super(displayModel, sortProperty, propertyExpression);
		this.filterChoices = filterChoices;
	}

	/**
	 * @param displayModel
	 * @param propertyExpression
	 * @param filterChoices
	 *            collection of choices used in the choice filter
	 */
	public MyChoiceFilteredPropertyColumn(final IModel<String> displayModel, final String propertyExpression,
			final IModel<List<? extends Y>> filterChoices) {
		super(displayModel, propertyExpression);
		this.filterChoices = filterChoices;
	}

	/**
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
		super.detach();
		if (filterChoices != null) {
			filterChoices.detach();
		}
	}

	/**
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn#getFilter(java.lang.String,
	 *      org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm)
	 */
	@Override
	public Component getFilter(final String componentId, final FilterForm<?> form) {
		ChoiceFilter<Y> filter = new ChoiceFilter<Y>(componentId, getFilterModel(form), form, filterChoices, enableAutoSubmit());

		IChoiceRenderer<Y> renderer = getChoiceRenderer();
		if (renderer != null) {
			filter.getChoice().setChoiceRenderer(renderer);
		}
		filter.getChoice().add(new AttributeModifier("class", "form-control"));
		filter.getChoice().add(new AttributeModifier("style", "margin:0;"));
		return filter;
	}

	/**
	 * Returns the model that will be passed on to the text filter. Users can override this method to change the model.
	 * 
	 * @param form
	 *            filter form
	 * @return model passed on to the text filter
	 */
	protected IModel<Y> getFilterModel(final FilterForm<?> form) {
		return new PropertyModel<Y>(form.getDefaultModel(), getPropertyExpression());
	}

	/**
	 * Returns true if the constructed choice filter should autosubmit the form when its value is changed.
	 * 
	 * @return true to make choice filter autosubmit, false otherwise
	 */
	protected boolean enableAutoSubmit() {
		return true;
	}

	/**
	 * Returns choice renderer that will be used to create the choice filter
	 * 
	 * @return choice renderer that will be used to create the choice filter
	 */
	protected IChoiceRenderer<Y> getChoiceRenderer() {
		return new IChoiceRenderer<Y>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getDisplayValue(Y object) {
				if (object instanceof IdAndValue) {
					return ((IdAndValue) object) == null ? null : ((IdAndValue) object).getValue();
				} else if (object instanceof KeyAndValue) {
					return ((KeyAndValue) object) == null ? null : ((KeyAndValue) object).getValue();
				} else {
					return object == null ? null : String.valueOf(object);
				}
			}

			@Override
			public String getIdValue(Y object, int index) {
				if (object instanceof IdAndValue) {
					return ((IdAndValue) object) == null ? null : String.valueOf(((IdAndValue) object).getId());
				} else if (object instanceof KeyAndValue) {
					return ((KeyAndValue) object) == null ? null : ((KeyAndValue) object).getKey();
				} else {
					return object == null ? null : String.valueOf(object);
				}
			}
		};
	}
}
