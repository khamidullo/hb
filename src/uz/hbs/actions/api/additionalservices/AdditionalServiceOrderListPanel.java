package uz.hbs.actions.api.additionalservices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.touragent.newbooking.panel.InsuranceListPanel;
import uz.hbs.beans.AdditionalServiceDetail;
import uz.hbs.beans.AdditionalServiceOrder;
import uz.hbs.beans.Guest;
import uz.hbs.beans.Insurance;
import uz.hbs.beans.User;
import uz.hbs.beans.filters.AdditionalServiceOrderFilter;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableAdditionalServiceOrderDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.email.CIPHallEmailNotifier;
import uz.hbs.utils.email.GreenHallEmailNotifier;
import uz.hbs.utils.email.TaxiEmailNotifier;

public class AdditionalServiceOrderListPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private String name;
	private FeedbackPanel feedback;
	private Date now = CommonUtil.now();

	public AdditionalServiceOrderListPanel(String id, IBreadCrumbModel breadCrumbModel, long user_id) {
		this(id, breadCrumbModel, new AdditionalServiceOrderFilter(user_id));
	}
	
	public AdditionalServiceOrderListPanel(String id, IBreadCrumbModel breadCrumbModel, AdditionalServiceOrderFilter filter) {
		super(id, breadCrumbModel);
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		name = getMySession().getUser().getName();
		
		WebMarkupContainer container;
		add(container = new WebMarkupContainer("container"));
		container.setOutputMarkupId(true);
		
		ArrayList<IColumn<AdditionalServiceOrder, String>> columns = new ArrayList<IColumn<AdditionalServiceOrder, String>>();

		columns.add(new MyTextFilteredPropertyColumn<AdditionalServiceOrder, AdditionalServiceOrder, String>(new StringResourceModel("id", null), "id"));
		columns.add(new MyTextFilteredPropertyColumn<AdditionalServiceOrder, AdditionalServiceOrder, String>(new StringResourceModel("touragents.reservation.id", null), "reservation_id", "reservation_id"));
		columns.add(new PropertyColumn<AdditionalServiceOrder, String>(new StringResourceModel("hotels.reservation.details.create_date", null), "create_date", "create_date"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceOrder>> item, String componentId, final IModel<AdditionalServiceOrder> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return FormatUtil.toString(rowModel.getObject().getCreate_date(), MyWebApplication.DATE_TIME_FORMAT);
					}
				}));
			}
		});
		columns.add(new PropertyColumn<AdditionalServiceOrder, String>(new StringResourceModel("hotels.guest.details.guest.name.full", null), "fullname", "fullname"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceOrder>> item, String componentId, final IModel<AdditionalServiceOrder> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						return rowModel.getObject().getFullname();
					}
				}).setEscapeModelStrings(false));
			}
		});
		columns.add(new PropertyColumn<AdditionalServiceOrder, String>(new StringResourceModel("hotels.reservation.details.update_date", null), "update_date", "update_date"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceOrder>> item, String componentId, final IModel<AdditionalServiceOrder> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						return FormatUtil.toString(rowModel.getObject().getUpdate_date(), MyWebApplication.DATE_TIME_FORMAT);
					}
				}));
			}
		});
		columns.add(new PropertyColumn<AdditionalServiceOrder, String>(new StringResourceModel("touragents.reservation.guest.service.arrival.time", null), "arrival_date", "arrival_date"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceOrder>> item, String componentId, final IModel<AdditionalServiceOrder> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						return FormatUtil.toString(rowModel.getObject().getArrival_date(), MyWebApplication.DATE_FORMAT);
					}
				}));
			}
		});
		columns.add(new PropertyColumn<AdditionalServiceOrder, String>(new StringResourceModel("touragents.reservation.guest.service.departure.time", null), "departure_date", "departure_date"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceOrder>> item, String componentId, final IModel<AdditionalServiceOrder> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						return FormatUtil.toString(rowModel.getObject().getDeparture_date(), MyWebApplication.DATE_FORMAT);
					}
				}));
			}
		});
		columns.add(new PropertyColumn<AdditionalServiceOrder, String>(new StringResourceModel("touragents.reservation.guest.service.total", null), "total", "total"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceOrder>> item, String componentId, final IModel<AdditionalServiceOrder> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						return FormatUtil.toString(rowModel.getObject().getTotal().doubleValue());
					}
				}));
			}
		});
		columns.add(new FilteredAbstractColumn<AdditionalServiceOrder, String>(new StringResourceModel("users.operation", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form);
			}

			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceOrder>> cellItem, String componentId, final IModel<AdditionalServiceOrder> rowModel) {
				cellItem.add(new AdditionalServiceActionPanel<AdditionalServiceOrder>(componentId) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onView() {
						activate(new IBreadCrumbPanelFactory() {
							private static final long serialVersionUID = 1L;

							@Override
							public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
								AdditionalServiceOrder reserve = rowModel.getObject();
								List<Insurance> insurancelist = new MyBatisHelper().selectList("selectInsuranceByOrderId", reserve.getId());
								reserve.setInsuranceList(insurancelist);
								if (reserve.getPerson() == null) reserve.setPerson((short) insurancelist.size());
								
								reserve.setInsurance(insurancelist.size() > 0);
								
								HashMap<String, Serializable> param = new HashMap<String, Serializable>();
								param.put("additionalserviceorders_id", reserve.getId());
								param.put("service_type", AdditionalServiceDetail.SERVICE_TYPE_ARRIVED);
								
								AdditionalServiceDetail detail = null;
								List<Guest> arrivalGuestlist = Collections.emptyList(); 
								detail = new MyBatisHelper().selectOne("selectAdditionalServiceDetailByOrderId", param);
								if (detail != null) {
									reserve.setArrival(detail);
									
									arrivalGuestlist = new MyBatisHelper().selectList("selectGuestForAdditionalService", reserve.getArrival().getId());
									if (detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT && arrivalGuestlist.isEmpty()) reserve.getArrival().getGuestlist().add(new Guest(Guest.MAIN_GUEST)); else reserve.getArrival().setGuestlist(arrivalGuestlist);
									if (reserve.getPerson() == null ||(reserve.getPerson() == 0 && arrivalGuestlist.size() > 0)) reserve.setPerson((short) arrivalGuestlist.size());
								}
								
								param.put("service_type", AdditionalServiceDetail.SERVICE_TYPE_DEPARTED);
								
								detail = new MyBatisHelper().selectOne("selectAdditionalServiceDetailByOrderId", param);
								if (detail != null) {
									reserve.setDeparture(detail);
									
									List<Guest> guestlist = new MyBatisHelper().selectList("selectGuestForAdditionalService", reserve.getDeparture().getId());
									if (detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT && guestlist.isEmpty()) reserve.getDeparture().getGuestlist().add(new Guest(Guest.MAIN_GUEST)); else reserve.getDeparture().setGuestlist(guestlist);
									if (reserve.getReservations_id() != null) {
										if (reserve.getDeparture().getPerson() == 0) reserve.getDeparture().setPerson(reserve.getArrival().getPerson());
										if (detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT && guestlist.isEmpty()) reserve.getDeparture().getGuestlist().add(new Guest(Guest.MAIN_GUEST)); else reserve.getDeparture().setGuestlist(arrivalGuestlist);
									}
								}
								return new AdditionalServiceOrderPanel(componentId, breadCrumbModel, reserve, false);
							}
						});
					}
					
					@Override
					protected void onTaxiOrder() {
						activate(new IBreadCrumbPanelFactory() {
							private static final long serialVersionUID = 1L;

							@Override
							public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
								return new AdditionalServiceDetailListPanel(componentId, breadCrumbModel, rowModel.getObject().getId());
							}
						});
					}
					
					@Override
					protected void onInsurance() {
						activate(new IBreadCrumbPanelFactory() {
							private static final long serialVersionUID = 1L;

							@Override
							public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
								return new InsuranceListPanel(componentId, breadCrumbModel, rowModel.getObject().getId());
							}
						});
					}
					
					@Override
					protected void onEdit() {
						activate(new IBreadCrumbPanelFactory() {
							private static final long serialVersionUID = 1L;

							@Override
							public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
								AdditionalServiceOrder reserve = rowModel.getObject();
								List<Insurance> insurancelist = new MyBatisHelper().selectList("selectInsuranceByOrderId", reserve.getId());
								reserve.setInsuranceList(insurancelist);
								
								reserve.setInsurance(insurancelist.size() > 0);
								
								HashMap<String, Serializable> param = new HashMap<String, Serializable>();
								param.put("additionalserviceorders_id", reserve.getId());
								param.put("service_type", AdditionalServiceDetail.SERVICE_TYPE_ARRIVED);
								
								AdditionalServiceDetail detail = null;
								detail = new MyBatisHelper().selectOne("selectAdditionalServiceDetailByOrderId", param);
								List<Guest> arrivalGuestlist = Collections.emptyList(); 
								if (detail != null) {
									reserve.setArrival(detail);
									
									arrivalGuestlist = new MyBatisHelper().selectList("selectGuestForAdditionalService", reserve.getArrival().getId());
									if (detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT && arrivalGuestlist.isEmpty()) reserve.getArrival().getGuestlist().add(new Guest(Guest.MAIN_GUEST)); else reserve.getArrival().setGuestlist(arrivalGuestlist);
									if (reserve.getPerson() == null ||(reserve.getPerson() == 0 && arrivalGuestlist.size() > 0)) reserve.setPerson((short) arrivalGuestlist.size());
								}
								
								param.put("service_type", AdditionalServiceDetail.SERVICE_TYPE_DEPARTED);
								
								detail = new MyBatisHelper().selectOne("selectAdditionalServiceDetailByOrderId", param);
								if (detail != null) {
									reserve.setDeparture(detail);

									List<Guest> guestlist = new MyBatisHelper().selectList("selectGuestForAdditionalService", reserve.getDeparture().getId());
									if (detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT && guestlist.isEmpty()) reserve.getDeparture().getGuestlist().add(new Guest(Guest.MAIN_GUEST)); else reserve.getDeparture().setGuestlist(guestlist);
									if (reserve.getReservations_id() != null) {
										if (reserve.getDeparture().getPerson() == 0) reserve.getDeparture().setPerson(reserve.getArrival().getPerson());
										if (detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT && guestlist.isEmpty()) reserve.getDeparture().getGuestlist().add(new Guest(Guest.MAIN_GUEST)); else reserve.getDeparture().setGuestlist(arrivalGuestlist);
									}
								}
								return new AdditionalServiceOrderPanel(componentId, breadCrumbModel, reserve, true);
							}
						});
					}
					
					@Override
					protected void onCancel(AjaxRequestTarget target) {
						try {
							HashMap<String, Object> param = new HashMap<>();
							param.put("new_status", AdditionalServiceOrder.STATUS_CANCEL);
							param.put("status", AdditionalServiceOrder.STATUS_NEW);
							param.put("initiator_user_id", getMySession().getUser().getId());
							param.put("id", rowModel.getObject().getId());
							if (new MyBatisHelper().update("updateStatusOfAdditionalServiceOrderById", param) > 0) {
								feedback.info(getString("additional.service.cancel.success"));
								TaxiEmailNotifier.sendCancel(rowModel.getObject().getId());
								CIPHallEmailNotifier.sendCancel(rowModel.getObject().getId());
								GreenHallEmailNotifier.sendCancel(rowModel.getObject().getId());
							}
						} catch (Exception e) {
							feedback.error("additional.service.cancel.fail");
						} finally {
							target.add(container);
							target.add(feedback);
						}
					}

					@Override
					protected boolean isEditVisible() {
						if (getMySession().getUser().getType().getId().intValue() == User.TYPE_API) {
							return false;
						}
						long arrival_diff = ((rowModel.getObject().getArrival_date().getTime() - now.getTime()) / 1000 / 60 / 60 / 24);
						long departure_diff = ((rowModel.getObject().getDeparture_date().getTime() - now.getTime()) / 1000 / 60 / 60 / 24);
						return rowModel.getObject().getStatus() == AdditionalServiceOrder.STATUS_NEW && (arrival_diff >= 1 || departure_diff >= 1);
					}

					@Override
					protected boolean isCancelVisible() {
						if (getMySession().getUser().getType().getId().intValue() == User.TYPE_API) {
							return false;
						}
						long arrival_diff = ((rowModel.getObject().getArrival_date().getTime() - now.getTime()) / 1000 / 60 / 60 / 24);
						long departure_diff = ((rowModel.getObject().getDeparture_date().getTime() - now.getTime()) / 1000 / 60 / 60 / 24);
						return rowModel.getObject().getStatus() == AdditionalServiceOrder.STATUS_NEW && (arrival_diff >= 1 || departure_diff >= 1);
					}
				});
			}
		});

		SortableAdditionalServiceOrderDataProvider provider = new SortableAdditionalServiceOrderDataProvider();
		provider.setFilterState(filter);

		final DataTable<AdditionalServiceOrder, String> dataTable = new DataTable<AdditionalServiceOrder, String>("table", columns, provider, getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<AdditionalServiceOrderFilter> form = new FilterForm<AdditionalServiceOrderFilter>("filterForm", provider) {
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

		container.add(form);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.reservation.guest.service.order.list", null, new Object[] {name});
	}

}
