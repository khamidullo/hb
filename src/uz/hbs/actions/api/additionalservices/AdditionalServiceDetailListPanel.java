package uz.hbs.actions.api.additionalservices;

import java.util.ArrayList;

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
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.AdditionalServiceDetail;
import uz.hbs.beans.filters.AdditionalServiceDetailFilter;
import uz.hbs.db.dataproviders.SortableAdditionalServiceDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.session.MySession;
import uz.hbs.utils.FormatUtil;

public class AdditionalServiceDetailListPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private long additionalserviceorders_id;

	public AdditionalServiceDetailListPanel(String id, IBreadCrumbModel breadCrumbModel, final long additionalserviceorders_id) {
		super(id, breadCrumbModel);
		this.additionalserviceorders_id = additionalserviceorders_id;
		ArrayList<IColumn<AdditionalServiceDetail, String>> columns = new ArrayList<IColumn<AdditionalServiceDetail, String>>();

		columns.add(new PropertyColumn<AdditionalServiceDetail, String>(new StringResourceModel("id", null), "id", "id"));
		columns.add(new PropertyColumn<AdditionalServiceDetail, String>(new StringResourceModel("touragents.reservation.guest.service.additional.order.id", null), "additionalserviceorders_id", "additionalserviceorders_id"));
		columns.add(new PropertyColumn<AdditionalServiceDetail, String>(new StringResourceModel("hotels.reservation.details.create_date", null), "create_date", "create_date"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceDetail>> item, String componentId, final IModel<AdditionalServiceDetail> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return FormatUtil.toString(rowModel.getObject().getCreate_date(), MyWebApplication.DATE_FORMAT);
					}
				}));
			}
		});
		columns.add(new PropertyColumn<AdditionalServiceDetail, String>(new StringResourceModel("touragents.reservation.guest.service", null), "service_type", "service_type"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceDetail>> item, String componentId, final IModel<AdditionalServiceDetail> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						if (rowModel.getObject().getService_type() == AdditionalServiceDetail.SERVICE_TYPE_ARRIVED) {
							return getString("touragents.reservation.guest.service.arrive");
						} else if (rowModel.getObject().getService_type() == AdditionalServiceDetail.SERVICE_TYPE_DEPARTED) {
							return getString("touragents.reservation.guest.service.depart");
						}
						return null;
					}
				}));
			}
		});
		columns.add(new PropertyColumn<AdditionalServiceDetail, String>(new StringResourceModel("touragents.reservation.guest.service.transport_type", null), "transport_type", "transport_type"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceDetail>> item, String componentId, final IModel<AdditionalServiceDetail> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						if (rowModel.getObject().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT) {
							return getString((rowModel.getObject().getService_type() == AdditionalServiceDetail.SERVICE_TYPE_ARRIVED)?"touragents.reservation.guest.service.arrive.airport":"touragents.reservation.guest.service.depart.airport");
						} else if (rowModel.getObject().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN) {
							return getString((rowModel.getObject().getService_type() == AdditionalServiceDetail.SERVICE_TYPE_ARRIVED)?"touragents.reservation.guest.service.arrive.train_station":"touragents.reservation.guest.service.depart.train_station");
						}
						return null;
					}
				}));
			}
		});
		columns.add(new PropertyColumn<AdditionalServiceDetail, String>(new StringResourceModel("touragents.reservation.guest.service.location_type", null), "location_type", "location_type"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceDetail>> item, String componentId, final IModel<AdditionalServiceDetail> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						if (rowModel.getObject().getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_ADDRESS) {
							StringBuffer sb = new StringBuffer(getString((rowModel.getObject().getService_type() == AdditionalServiceDetail.SERVICE_TYPE_ARRIVED)?"touragents.reservation.guest.service.taxi_order.deliver.location_type.address":"touragents.reservation.guest.service.taxi_order.pickup.location_type.address"));
							sb.append(": ").append("<br>");
							sb.append(rowModel.getObject().getLocation_value());
							return sb.toString();
						} else if (rowModel.getObject().getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_RESERVED_HOTEL) {
							return getString((rowModel.getObject().getService_type() == AdditionalServiceDetail.SERVICE_TYPE_ARRIVED)?"touragents.reservation.guest.service.taxi_order.deliver.location_type.reserved_hotel":"touragents.reservation.guest.service.taxi_order.pickup.location_type.reserved_hotel");
						}
						return null;
					}
				}).setEscapeModelStrings(false));
			}
		});
		columns.add(new PropertyColumn<AdditionalServiceDetail, String>(new StringResourceModel("touragents.reservation.guest.service.transport_type.info", null), "transport_type"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceDetail>> item, String componentId, final IModel<AdditionalServiceDetail> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						StringBuffer sb = new StringBuffer();
						if (rowModel.getObject().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT) {
							sb.append(getString("touragents.reservation.guest.service")).append(": ");
							if (rowModel.getObject().getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_GREEN_HALL) {
								sb.append(getString("touragents.reservation.guest.service.airport.green_corridor"));
							} else if (rowModel.getObject().getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_VIP_HALL) {
								sb.append(getString("touragents.reservation.guest.service.airport.vip_hall"));
							}
							sb.append("<br>");
							sb.append(getString("touragents.reservation.guest.service.airport.terminal")).append(": ").append(rowModel.getObject().getAir_terminal()).append("<br>");
							sb.append(getString("touragents.reservation.guest.service.airport.flight")).append(": ").append(rowModel.getObject().getAir_numb()).append("<br>");
							sb.append(getString("touragents.reservation.guest.service.airport.flight.time")).append(": ").append(rowModel.getObject().getAir_time()).append("<br>");
							sb.append(getString("touragents.reservation.guest.service.airport.class")).append(": ");
							if (rowModel.getObject().getAir_class() == AdditionalServiceDetail.AIR_CLASS_ECONOM) {
								sb.append(getString("touragents.reservation.guest.service.airport.class.econom")).append("<br>");
							} else if (rowModel.getObject().getAir_class() == AdditionalServiceDetail.AIR_CLASS_BUSINESS) {
								sb.append(getString("touragents.reservation.guest.service.airport.class.business")).append("<br>");
							}
							if (rowModel.getObject().getDestination() != null) sb.append(getString("touragents.reservation.guest.service.airport.destination")).append(": ").append(rowModel.getObject().getDestination()).append("<br>");
						} else if (rowModel.getObject().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN) {
							sb.append(getString("touragents.reservation.guest.service.train.number")).append(": ").append(rowModel.getObject().getTrain_numb()).append("<br>");
							sb.append(getString("touragents.reservation.guest.service.train.time")).append(": ").append(rowModel.getObject().getTrain_time()).append("<br>");
						}
						return sb.toString();
					}
				}).setEscapeModelStrings(false));
			}
		});
		
		AdditionalServiceDetailFilter filter = new AdditionalServiceDetailFilter(((MySession) getSession()).getUser().getId(), additionalserviceorders_id);

		SortableAdditionalServiceDataProvider provider = new SortableAdditionalServiceDataProvider();
		provider.setFilterState(filter);

		final DataTable<AdditionalServiceDetail, String> dataTable = new DataTable<AdditionalServiceDetail, String>("table", columns, provider, getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<AdditionalServiceDetailFilter> form = new FilterForm<AdditionalServiceDetailFilter>("filterForm", provider) {
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
		return new StringResourceModel("touragents.reservation.guest.service.order.detail", null, new Object[]{additionalserviceorders_id});
	}

}
