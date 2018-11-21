package uz.hbs.actions.admin.users.roles;

import java.util.ArrayList;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.BreadCrumbLink;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Role;
import uz.hbs.db.dataproviders.SortableRolesDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.panel.ActionsPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;

public class ListRolesPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ListRolesPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		final ModalWindow dialog = new ModalWindow("dialog");
		dialog.setAutoSize(true);
		dialog.setMinimalHeight(100);
		dialog.setMinimalWidth(500);
		dialog.setCookieName("listRoles");
		dialog.setInitialHeight(100);
		dialog.setInitialWidth(500);
		add(dialog);

		final WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);
		
		BreadCrumbLink addLink = new BreadCrumbPanelLink("addLink", this, AddRolesPanel.class);
		add(addLink);

		ArrayList<IColumn<Role, String>> columns = new ArrayList<IColumn<Role, String>>();

		columns.add(new MyTextFilteredPropertyColumn<Role, Role, String>(new StringResourceModel("roles.name", null), "u.name", "name"));
		columns.add(new PropertyColumn<Role, String>(new StringResourceModel("roles.actions_count", null), "actions_count"));
		columns.add(new PropertyColumn<Role, String>(new StringResourceModel("roles.users_count", null), "users_count"));
		columns.add(new FilteredAbstractColumn<Role, String>(new StringResourceModel("operations", null)) {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form);
			}

			@Override
			public void populateItem(Item<ICellPopulator<Role>> cellItem, String componentId, final IModel<Role> rowModel) {
				cellItem.add(new ActionsPanel<Role>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;

					@Override
					public Link<Role> addEditLink(final IModel<Role> model) {
						return new Link<Role>("edit") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new EditRolesPanel("panel", breadCrumbModel, rowModel.getObject());
									}
								});
							}
						};
					}

					@Override
					public AjaxLink<Role> addDeleteLink(IModel<Role> model) {
						return new AjaxLink<Role>("delete") {
							private static final long serialVersionUID = 1L;

							@Override
							public boolean isVisible() {
								return model.getObject().getUsers_count() == 0;
							}

							@Override
							public void onClick(AjaxRequestTarget target) {
								dialog.setTitle(new StringResourceModel("roles.delete", null,
										new Object[] { rowModel.getObject().getName() }));
								dialog.setContent(new DeleteRolesPanel(dialog.getContentId(), rowModel.getObject(), dialog, feedback, container));
								dialog.show(target);
							}
						};
					}
				});
			}
		});

		Role filter = new Role();

		SortableRolesDataProvider provider = new SortableRolesDataProvider();
		provider.setFilterState(filter);

		final DataTable<Role, String> dataTable = new DataTable<Role, String>("table", columns, provider, getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<Role> form = new FilterForm<Role>("filterForm", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				dataTable.setCurrentPage(0);
			}

			@Override
			public boolean isVisible() {
				return dataTable.size() > 0;
			}
		};

		dataTable.addTopToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addTopToolbar(new HeadersToolbar<String>(dataTable, provider));
		dataTable.addTopToolbar(new FilterToolbar(dataTable, form, provider));
		dataTable.addBottomToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addBottomToolbar(new MyNoRecordsToolbar(dataTable));

		form.add(dataTable);
		container.add(form);
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("roles.list", null);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
