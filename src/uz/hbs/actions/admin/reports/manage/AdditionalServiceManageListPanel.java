package uz.hbs.actions.admin.reports.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
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
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.hotel.reservations.panels.ReservationActionPanel;
import uz.hbs.actions.touragent.extra.AdditionalServiceOrderLogPanel;
import uz.hbs.actions.touragent.extra.AdditionalServiceOrderPanel;
import uz.hbs.beans.AdditionalServiceDetail;
import uz.hbs.beans.AdditionalServiceManage;
import uz.hbs.beans.AdditionalServiceOrder;
import uz.hbs.beans.Guest;
import uz.hbs.beans.Insurance;
import uz.hbs.beans.ServiceType;
import uz.hbs.beans.filters.AdditionalServiceManageFilter;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableAdditionalServiceManageDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.label.AjaxLinkLabel;
import uz.hbs.markup.html.form.textfield.DateFilteredPropertyColumn;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyChoiceFilteredPropertyColumn;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.email.CIPHallEmailNotifier;
import uz.hbs.utils.email.GreenHallEmailNotifier;
import uz.hbs.utils.email.TaxiEmailNotifier;

public class AdditionalServiceManageListPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private FeedbackPanel feedback;
	private HashMap<String, Integer> total = new HashMap<String, Integer>();
	private ModalWindow dialog;
	private AdditionalServiceManageFilter filter;
	private Date now = CommonUtil.now();

	public AdditionalServiceManageListPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		filter = new AdditionalServiceManageFilter();
		filter.setDatefrom(new Date());
		filter.setDateto(CommonUtil.getLastDateOfMonth(filter.getDatefrom()));
		
		add(dialog = new ModalWindow("dialog"));
		dialog.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				return true;
			}
		});

		ShowForm showForm;
		add(showForm = new ShowForm("form", filter));
		
		final SortableAdditionalServiceManageDataProvider provider = new SortableAdditionalServiceManageDataProvider();
		provider.setFilterState(filter);

		
		WebMarkupContainer containerInfo;
		add(containerInfo = new WebMarkupContainer("container_info"));
		containerInfo.setOutputMarkupId(true);
		containerInfo.add(new Label("departure_taxi", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("datefrom", filter.getDatefrom());
				map.put("dateto", filter.getDateto());
				map.put("service_type", AdditionalServiceManage.DEPARTURE_AIR_TAXI);
				total.put("DEPARTURE_AIR_TAXI", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAdditionalServiceManageCount", map)));
				map.put("service_type", AdditionalServiceManage.DEPARTURE_TRAIN_TAXI);
				total.put("DEPARTURE_TRAIN_TAXI", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAdditionalServiceManageCount", map)));
				return total.get("DEPARTURE_AIR_TAXI") + total.get("DEPARTURE_TRAIN_TAXI");
			}
		}));
		containerInfo.add(new Label("arrival_taxi", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("datefrom", filter.getDatefrom());
				map.put("dateto", filter.getDateto());
				map.put("service_type", AdditionalServiceManage.ARRIVAL_AIR_TAXI);
				total.put("ARRIVAL_AIR_TAXI", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAdditionalServiceManageCount", map)));
				map.put("service_type", AdditionalServiceManage.ARRIVAL_TRAIN_TAXI);
				total.put("ARRIVAL_TRAIN_TAXI", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAdditionalServiceManageCount", map)));
				return total.get("ARRIVAL_AIR_TAXI") + total.get("ARRIVAL_TRAIN_TAXI");
			}
		}));
		containerInfo.add(new Label("cip_hall", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("datefrom", filter.getDatefrom());
				map.put("dateto", filter.getDateto());
				map.put("service_type", AdditionalServiceManage.ARRIVAL_AIR_CIP_HALL);
				total.put("ARRIVAL_AIR_CIP_HALL", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAdditionalServiceManageCount", map)));
				map.put("service_type", AdditionalServiceManage.DEPARTURE_AIR_CIP_HALL);
				total.put("DEPARTURE_AIR_CIP_HALL", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAdditionalServiceManageCount", map)));
				return total.get("ARRIVAL_AIR_CIP_HALL") + total.get("DEPARTURE_AIR_CIP_HALL");
			}
		}));
		containerInfo.add(new Label("green_hall", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("datefrom", filter.getDatefrom());
				map.put("dateto", filter.getDateto());
				map.put("service_type", AdditionalServiceManage.ARRIVAL_AIR_GREEN_HALL);
				total.put("ARRIVAL_AIR_GREEN_HALL", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAdditionalServiceManageCount", map)));
				map.put("service_type", AdditionalServiceManage.DEPARTURE_AIR_GREEN_HALL);
				total.put("DEPARTURE_AIR_GREEN_HALL", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAdditionalServiceManageCount", map)));
				return total.get("ARRIVAL_AIR_GREEN_HALL") + total.get("DEPARTURE_AIR_GREEN_HALL");
			}
		}));
		containerInfo.add(new Label("insurance", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("datefrom", filter.getDatefrom());
				map.put("dateto", filter.getDateto());
				map.put("service_type", AdditionalServiceManage.INSURANCE);
				total.put("INSURANCE", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAdditionalServiceManageCount", map)));
				return total.get("INSURANCE");
			}
		}));
		containerInfo.add(new Label("total", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Integer getObject() {
				Iterator<Integer> it = total.values().iterator();
				int totalValue = 0;
				while (it.hasNext()) totalValue += CommonUtil.nvl(it.next());  
				
				return totalValue;
			}
		}));
		
		WebMarkupContainer container;
		add(container = new WebMarkupContainer("container"));
		container.setOutputMarkupId(true);
		
		ArrayList<IColumn<AdditionalServiceManage, String>> columns = new ArrayList<IColumn<AdditionalServiceManage, String>>();
		columns.add(new PropertyColumn<AdditionalServiceManage, String>(new StringResourceModel("hotels.reservation.details.reservation.id", null), "id", "id") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceManage>> item, String componentId, final IModel<AdditionalServiceManage> rowModel) {
				item.add(new AjaxLinkLabel(componentId, String.valueOf(rowModel.getObject().getId())) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onLinkClick(AjaxRequestTarget target) {
						dialog.setTitle("INFO");
						dialog.setContent(new ViewDetail(dialog.getContentId(), rowModel.getObject()));
						dialog.setMinimalWidth(800);
						dialog.setMinimalHeight(600);
						dialog.setAutoSize(true);
						dialog.show(target);
					}

					@Override
					protected boolean isLinkEnabled() {
						return true;
					}
				});
				item.add(new AttributeModifier("style", "white-space: nowrap"));
			}
		});
		columns.add(new MyChoiceFilteredPropertyColumn<AdditionalServiceManage, ServiceType, String>(new StringResourceModel("additional.service.manage.service_type", null), "servicetype", "service_type", new LoadableDetachableModel<List<? extends ServiceType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ServiceType> load() {
				List<ServiceType> list = new ArrayList<ServiceType>();
				list.add(new ServiceType(AdditionalServiceManage.ARRIVAL_AIR_TAXI, getString("additional.service.manage.taxi.arrival.air")));
				list.add(new ServiceType(AdditionalServiceManage.ARRIVAL_TRAIN_TAXI, getString("additional.service.manage.taxi.arrival.train")));
				list.add(new ServiceType(AdditionalServiceManage.ARRIVAL_AIR_GREEN_HALL, getString("additional.service.manage.arrival.hall.green")));
				list.add(new ServiceType(AdditionalServiceManage.ARRIVAL_AIR_CIP_HALL, getString("additional.service.manage.arrival.hall.vip")));
				list.add(new ServiceType(AdditionalServiceManage.DEPARTURE_AIR_TAXI, getString("additional.service.manage.taxi.departure.air")));
				list.add(new ServiceType(AdditionalServiceManage.DEPARTURE_TRAIN_TAXI, getString("additional.service.manage.taxi.departure.train")));
				list.add(new ServiceType(AdditionalServiceManage.DEPARTURE_AIR_GREEN_HALL, getString("additional.service.manage.departure.hall.green")));
				list.add(new ServiceType(AdditionalServiceManage.DEPARTURE_AIR_CIP_HALL, getString("additional.service.manage.departure.hall.vip")));
				list.add(new ServiceType(AdditionalServiceManage.INSURANCE, getString("additional.service.manage.insurance")));
				return list;
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceManage>> item, String componentId,IModel<AdditionalServiceManage> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return getServiceType(rowModel.getObject().getServicetype());
					}
				}).setEscapeModelStrings(false));
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<AdditionalServiceManage, AdditionalServiceManage, String>(new StringResourceModel("additional.service.manage.touragent", null), "touragent", "touragent"));
		columns.add(new DateFilteredPropertyColumn<AdditionalServiceManage, String>(new StringResourceModel("additional.service.manage.create_date", null),"create_date", "create_date", MyWebApplication.DATE_FORMAT) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceManage>> item, String componentId, IModel<AdditionalServiceManage> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getCreate_date(), MyWebApplication.DATE_FORMAT))).add(new AttributeModifier("style", "white-space: nowrap"));
			}
		});
		columns.add(new DateFilteredPropertyColumn<AdditionalServiceManage, String>(new StringResourceModel("additional.service.manage.arrival_date", null),"arrival_date", "arrival_date", MyWebApplication.DATE_FORMAT) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceManage>> item, String componentId, IModel<AdditionalServiceManage> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getArrival_date(), MyWebApplication.DATE_FORMAT))).add(new AttributeModifier("style", "white-space: nowrap"));
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<AdditionalServiceManage, AdditionalServiceManage, String>(new StringResourceModel("additional.service.manage.guest", null), "guest", "guest"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceManage>> item, String componentId, IModel<AdditionalServiceManage> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return rowModel.getObject().getGuest();
					}
				}).setEscapeModelStrings(false));
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<AdditionalServiceManage, AdditionalServiceManage, String>(new StringResourceModel("additional.service.manage.amount", null), "total", "total"));
		columns.add(new FilteredAbstractColumn<AdditionalServiceManage, String>(new StringResourceModel("users.operation", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, final FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onClearSubmit(Button button) {
						super.onClearSubmit(button);
						provider.getFilterState().setDatefrom(filter.getDatefrom());
						provider.getFilterState().setDateto(filter.getDateto());
						provider.getFilterState().setFullname(filter.getFullname());
					}

					@Override
					protected void onGoSubmit(Button button) {
						super.onGoSubmit(button);
						provider.getFilterState().setDatefrom(filter.getDatefrom());
						provider.getFilterState().setDateto(filter.getDateto());
						provider.getFilterState().setFullname(filter.getFullname());
					}
				};
			}

			@Override
			public void populateItem(Item<ICellPopulator<AdditionalServiceManage>> cellItem, String componentId, final IModel<AdditionalServiceManage> rowModel) {
				cellItem.add(new ReservationActionPanel<AdditionalServiceManage>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onViewDetails() {
						activate(new IBreadCrumbPanelFactory() {
							private static final long serialVersionUID = 1L;

							@Override
							public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
								AdditionalServiceOrder reserve = new MyBatisHelper().selectOne("selectAdditionalServiceOrderById", rowModel.getObject().getId());
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
					protected void onChangeRoom() {
					}

					@Override
					protected void onIssueBill() {
					}

					@Override
					protected void onRegisterGuest() {
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
					protected boolean isVisibleViewDetails() {
						return true;
					}

					@Override
					protected boolean isVisibleChangeRoom() {
						return false;
					}

					@Override
					protected boolean isVisibleIssueBill() {
						return false;
					}

					@Override
					protected boolean isVisibleRegisterGuest() {
						return false;
					}

					@Override
					protected boolean isVisibleCancel() {
						long arrival_diff = ((rowModel.getObject().getArrival_date().getTime() - now.getTime()) / 1000 / 60 / 60 / 24);
						long departure_diff = ((rowModel.getObject().getDeparture_date().getTime() - now.getTime()) / 1000 / 60 / 60 / 24);
						return rowModel.getObject().getStatus() == AdditionalServiceOrder.STATUS_NEW && (arrival_diff >= 1 || departure_diff >= 1);
					}

					@Override
					protected void onNoShow(AjaxRequestTarget target) {
					}

					@Override
					protected boolean isVisibleNoShow() {
						return false;
					}

					@Override
					protected void onViewReserveLogs() {
						activate(new IBreadCrumbPanelFactory() {
							private static final long serialVersionUID = 1L;

							@Override
							public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
								return new AdditionalServiceOrderLogPanel(componentId, breadCrumbModel, rowModel.getObject().getId());
							}
						});
					}

					@Override
					protected boolean isVisibleViewReserveLogs() {
						return true;
					}
				});
			}
		});
		final DataTable<AdditionalServiceManage, String> dataTable = new DataTable<AdditionalServiceManage, String>("table", columns, provider, getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<AdditionalServiceManageFilter> form = new FilterForm<AdditionalServiceManageFilter>("filterForm", provider) {
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

		showForm.add(new IndicatingAjaxButton("show"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				total.clear();
				target.add(containerInfo);
				target.add(container);
			}
		});
	}
	
	
	private class ShowForm extends Form<AdditionalServiceManageFilter> {
		private static final long serialVersionUID = 1L;

		public ShowForm(String id, AdditionalServiceManageFilter filter) {
			super(id, new CompoundPropertyModel<AdditionalServiceManageFilter>(filter));
			DateTextField dateFromField;
			add(dateFromField = new DateTextField("datefrom", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			dateFromField.setRequired(true);
			dateFromField.setLabel(new StringResourceModel("additional.service.manage.show.from", null));
			dateFromField.add(new MyDatePicker());
			
			DateTextField dateToField;
			add(dateToField = new DateTextField("dateto", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			dateToField.setRequired(true);
			dateToField.setLabel(new StringResourceModel("additional.service.manage.show.to", null));
			dateToField.add(new MyDatePicker());
			
			add(new TextField<String>("fullname"));
		}
	}
	
	private class ViewDetail extends Panel {
		private static final long serialVersionUID = 1L;

		public ViewDetail(String id, final AdditionalServiceManage manage) {
			super(id);
			add(new Label("touragent", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return new MyBatisHelper().selectOne("selectTourAgentNameAndLoginByOrderId", manage.getId());
				}
			}));
			add(new Label("contact", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return new MyBatisHelper().selectOne("selectTourAgentOperatorNameAndPhoneByOrderId", manage.getId());
				}
			}));
			add(new Label("guest_info", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return manage.getGuest();
				}
			}).setEscapeModelStrings(false));
			add(new Label("service_type", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return getServiceType(manage.getServicetype());
				}
			}).setEscapeModelStrings(false));
			add(new Label("arrival_date", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return DateUtil.toString(manage.getArrival_date(), MyWebApplication.DATE_FORMAT);
				}
			}));
			add(new Label("guest_count", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return String.valueOf(getCount(manage.getGuest()));
				}
			}));
			add(new Label("service_count", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return String.valueOf(getCount(getServiceType(manage.getServicetype())));
				}
			}));
			add(new Label("total", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return String.valueOf(manage.getTotal());
				}
			}));
		}
		
	}
	
	private String getServiceType(String s){
		if (s == null) return "";
		s = s.replace("ARRIVAL_AIR_TAXI", getString("additional.service.manage.taxi.arrival.air"));
		s = s.replace("ARRIVAL_TRAIN_TAXI", getString("additional.service.manage.taxi.arrival.train"));
		s = s.replace("ARRIVAL_AIR_GREEN_HALL", getString("additional.service.manage.arrival.hall.green"));
		s = s.replace("ARRIVAL_AIR_VIP_HALL", getString("additional.service.manage.arrival.hall.vip"));
		s = s.replace("DEPARTURE_AIR_TAXI", getString("additional.service.manage.taxi.departure.air"));
		s = s.replace("DEPARTURE_TRAIN_TAXI", getString("additional.service.manage.taxi.departure.train"));
		s = s.replace("DEPARTURE_AIR_GREEN_HALL", getString("additional.service.manage.departure.hall.green"));
		s = s.replace("DEPARTURE_AIR_VIP_HALL", getString("additional.service.manage.departure.hall.vip"));
		s = s.replace("INSURANCE", getString("additional.service.manage.insurance"));
		return s.replace(" ,", ",");
	}
	
	private Short getCount(String s){
		if (s == null || "".equals(s.trim())) return 0;
		String s1 [] = s.split(",");  
		return (short)(s1.length);		
	}
	
	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("additional.service.manage.title", null);
	}
}
