package uz.hbs.markup.navigation.repeater.data.table;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * A toolbar that displays a "no records found" message when the data table contains no rows.
 * <p>
 * The message can be overridden by providing a resource with key <code>datatable.no-records-found</code>
 * 
 * @see DefaultDataTable
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public class MyNoRecordsToolbar extends AbstractToolbar {
	private static final long serialVersionUID = 1L;

	private static final IModel<String> DEFAULT_MESSAGE_MODEL = new ResourceModel("datatable.no-records-found");

	/**
	 * Constructor
	 * 
	 * @param table
	 *            data table this toolbar will be attached to
	 */
	public MyNoRecordsToolbar(final DataTable<?, ?> table) {
		this(table, DEFAULT_MESSAGE_MODEL);
	}

	/**
	 * @param table
	 *            data table this toolbar will be attached to
	 * @param messageModel
	 *            model that will be used to display the "no records found" message
	 */
	public MyNoRecordsToolbar(final DataTable<?, ?> table, final IModel<String> messageModel) {
		super(table);

		WebMarkupContainer td = new WebMarkupContainer("td");
		add(td);

		td.add(AttributeModifier.replace("colspan", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return String.valueOf(table.getColumns().size());
			}
		}));
		td.add(new Label("msg", messageModel));
	}

	/**
	 * Only shows this toolbar when there are no rows
	 * 
	 * @see org.apache.wicket.Component#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return getTable().getRowCount() == 0;
	}
}
