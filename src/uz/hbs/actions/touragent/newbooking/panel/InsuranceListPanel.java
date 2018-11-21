package uz.hbs.actions.touragent.newbooking.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Insurance;
import uz.hbs.beans.Nationality;
import uz.hbs.beans.filters.InsuranceFilter;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableInsuranceDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyChoiceFilteredPropertyColumn;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;
import uz.hbs.session.MySession;
import uz.hbs.utils.FormatUtil;

public class InsuranceListPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private long additionalserviceorders_id;

	public InsuranceListPanel(String id, IBreadCrumbModel breadCrumbModel, final long additionalserviceorders_id) {
		super(id, breadCrumbModel);
		this.additionalserviceorders_id = additionalserviceorders_id;
		
		ArrayList<IColumn<Insurance, String>> columns = new ArrayList<IColumn<Insurance, String>>();

		columns.add(new PropertyColumn<Insurance, String>(new StringResourceModel("id", null), "u.id", "id"));
		columns.add(new PropertyColumn<Insurance, String>(new StringResourceModel("touragents.reservation.guest.service.additional.order.id", null), "additionalserviceorders_id", "additionalserviceorders_id"));
		columns.add(new PropertyColumn<Insurance, String>(new StringResourceModel("hotels.reservation.details.create_date", null), "create_date", "create_date"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Insurance>> item, String componentId, final IModel<Insurance> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return FormatUtil.toString(rowModel.getObject().getCreate_date(), MyWebApplication.DATE_FORMAT);
					}
				}));
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<Insurance, Insurance, String>(new StringResourceModel("hotels.guest.details.guest.name.first", null), "first_name", "first_name"));
		columns.add(new MyTextFilteredPropertyColumn<Insurance, Insurance, String>(new StringResourceModel("hotels.guest.details.guest.name.last", null), "last_name", "last_name"));
		columns.add(new PropertyColumn<Insurance, String>(new StringResourceModel("hotels.guest.details.birth_date", null), "birth_date", "birth_date"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Insurance>> item, String componentId, final IModel<Insurance> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return FormatUtil.toString(rowModel.getObject().getBirth_date(), MyWebApplication.DATE_FORMAT);
					}
				}));
			}
		});
		columns.add(new MyChoiceFilteredPropertyColumn<Insurance, Nationality, String>(new StringResourceModel("hotels.guest.details.nationality", null), "nationality", "nationality", new LoadableDetachableModel<List<? extends Nationality>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Nationality> load() {
				return new MyBatisHelper().selectList("selectNationalityList");
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Insurance>> cellItem, String componentId, final IModel<Insurance> rowModel) {
				cellItem.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return rowModel.getObject().getNationality().getName();
					}
				}));
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<Insurance, Insurance, String>(new StringResourceModel("hotels.guest.details.passport.number", null), "passport_number", "passport_number"));
		columns.add(new PropertyColumn<Insurance, String>(new StringResourceModel("hotels.guest.details.passport.date_of_issue", null), "passport_issue_date", "passport_issue_date"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Insurance>> item, String componentId, final IModel<Insurance> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return FormatUtil.toString(rowModel.getObject().getPassport_issue_date(), MyWebApplication.DATE_FORMAT);
					}
				}));
			}
		});
		columns.add(new PropertyColumn<Insurance, String>(new StringResourceModel("hotels.guest.details.insurance.period_from_date", null), "period_from_date", "period_from_date"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Insurance>> item, String componentId, final IModel<Insurance> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return FormatUtil.toString(rowModel.getObject().getPeriod_from_date(), MyWebApplication.DATE_FORMAT);
					}
				}));
			}
		});
		columns.add(new PropertyColumn<Insurance, String>(new StringResourceModel("hotels.guest.details.insurance.period_to_date", null), "period_to_date", "period_to_date"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Insurance>> item, String componentId, final IModel<Insurance> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return FormatUtil.toString(rowModel.getObject().getPeriod_to_date(), MyWebApplication.DATE_FORMAT);
					}
				}));
			}
		});
		
		InsuranceFilter filter = new InsuranceFilter(((MySession) getSession()).getUser().getId(), additionalserviceorders_id);

		SortableInsuranceDataProvider provider = new SortableInsuranceDataProvider();
		provider.setFilterState(filter);

		final DataTable<Insurance, String> dataTable = new DataTable<Insurance, String>("table", columns, provider, getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<InsuranceFilter> form = new FilterForm<InsuranceFilter>("filterForm", provider) {
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

		form.add(dataTable);
		dataTable.addTopToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addTopToolbar(new HeadersToolbar<String>(dataTable, provider));
		dataTable.addTopToolbar(new FilterToolbar(dataTable, form, provider));
		dataTable.addBottomToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addBottomToolbar(new MyNoRecordsToolbar(dataTable));

		add(form);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.reservation.guest.service.insurance.list", null, new Object[]{additionalserviceorders_id});
	}
}
