package uz.hbs.markup.navigation;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;

import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;

public class BootstrapPagingNavigatorToolbar extends AbstractToolbar {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param table
	 *            data table this toolbar will be attached to
	 */
	public BootstrapPagingNavigatorToolbar(final DataTable<?, ?> table, Size size) {
		super(table);

		WebMarkupContainer span = new WebMarkupContainer("span");
		add(span);
		span.add(AttributeModifier.replace("colspan", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return String.valueOf(table.getColumns().size());
			}
		}));

		span.add(newPagingNavigator("navigator", table, size));
		span.add(newNavigatorLabel("navigatorLabel", table));
	}

	/**
	 * Factory method used to create the paging navigator that will be used by the datatable
	 * 
	 * @param navigatorId
	 *            component id the navigator should be created with
	 * @param table
	 *            dataview used by datatable
	 * @return paging navigator that will be used to navigate the data table
	 */
	protected BootstrapPagingNavigator newPagingNavigator(final String navigatorId, final DataTable<?, ?> table, Size size) {
		return new BootstrapPagingNavigator(navigatorId, table, size);
	}

	/**
	 * Factory method used to create the navigator label that will be used by the datatable
	 * 
	 * @param navigatorId
	 *            component id navigator label should be created with
	 * @param table
	 *            dataview used by datatable
	 * @return navigator label that will be used to navigate the data table
	 */
	protected WebComponent newNavigatorLabel(final String navigatorId, final DataTable<?, ?> table) {
		return new NavigatorLabel(navigatorId, table);
	}

	/** {@inheritDoc} */
	@Override
	protected void onConfigure() {
		super.onConfigure();
		setVisible(getTable().getPageCount() > 1);
	}
}
