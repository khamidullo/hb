package uz.hbs.actions.admin.reports.manage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.ReserveManage;
import uz.hbs.beans.filters.ReserveManageFilter;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableReserveManageDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.label.AjaxLinkLabel;
import uz.hbs.markup.html.form.textfield.DateFilteredPropertyColumn;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyChoiceFilteredPropertyColumn;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.FormatUtil;

public class HotelReservationManageListPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private FeedbackPanel feedback;
	private HashMap<String, Integer> total = new HashMap<String, Integer>();
	private ModalWindow dialog;
	private ReserveManageFilter filter;
	

	public HotelReservationManageListPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		filter = new ReserveManageFilter();
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
		
		final SortableReserveManageDataProvider provider = new SortableReserveManageDataProvider();
		provider.setFilterState(filter);
		
		WebMarkupContainer containerInfo;
		add(containerInfo = new WebMarkupContainer("container_info"));
		containerInfo.setOutputMarkupId(true);
		containerInfo.add(new Label("reserved", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("datefrom", filter.getDatefrom());
				map.put("dateto", filter.getDateto());
//				map.put("service_type", ReserveManage.DEPARTURE_AIR_TAXI);
				total.put("RESERVED", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectReserveManageCount", map)));
				return total.get("RESERVED");
			}
		}));
		containerInfo.add(new Label("registred", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("datefrom", filter.getDatefrom());
				map.put("dateto", filter.getDateto());
//				map.put("service_type", ReserveManage.DEPARTURE_AIR_TAXI);
				total.put("REGISTRED", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectReserveManageCount", map)));
				return total.get("REGISTRED");
			}
		}));
		containerInfo.add(new Label("no_show", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("datefrom", filter.getDatefrom());
				map.put("dateto", filter.getDateto());
//				map.put("service_type", ReserveManage.DEPARTURE_AIR_TAXI);
				total.put("NO_SHOW", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectReserveManageCount", map)));
				return total.get("NO_SHOW");
			}
		}));
		containerInfo.add(new Label("cancelled", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("datefrom", filter.getDatefrom());
				map.put("dateto", filter.getDateto());
//				map.put("service_type", ReserveManage.DEPARTURE_AIR_TAXI);
				total.put("CANCELLED", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectReserveManageCount", map)));
				return total.get("CANCELLED");
			}
		}));
		containerInfo.add(new Label("definite", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("datefrom", filter.getDatefrom());
				map.put("dateto", filter.getDateto());
//				map.put("service_type", ReserveManage.DEPARTURE_AIR_TAXI);
				total.put("DEFINITE", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectReserveManageCount", map)));
				return total.get("DEFINITE");
			}
		}));
		containerInfo.add(new Label("tentative", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("datefrom", filter.getDatefrom());
				map.put("dateto", filter.getDateto());
//				map.put("service_type", ReserveManage.DEPARTURE_AIR_TAXI);
				total.put("TENTATIVE", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectReserveManageCount", map)));
				return total.get("TENTATIVE");
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
		
		ArrayList<IColumn<ReserveManage, String>> columns = new ArrayList<IColumn<ReserveManage, String>>();
		columns.add(new PropertyColumn<ReserveManage, String>(new StringResourceModel("hotels.reservation.details.reservation.id", null), "id", "id") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReserveManage>> item, String componentId, final IModel<ReserveManage> rowModel) {
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
		columns.add(new DateFilteredPropertyColumn<ReserveManage, String>(new StringResourceModel("admin.manage.created", null),"arrival_date", "check_out") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReserveManage>> item, String componentId, IModel<ReserveManage> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getCreate_date(), MyWebApplication.DATE_TIME_SHORT_FORMAT))).add(new AttributeModifier("style", "white-space: nowrap"));
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<ReserveManage, ReserveManage, String>(new StringResourceModel("admin.manage.touragent", null), "touragent", "touragent"));
		columns.add(new MyTextFilteredPropertyColumn<ReserveManage, ReserveManage, String>(new StringResourceModel("admin.manage.hotel", null), "hotel", "hotel"));
		columns.add(new DateFilteredPropertyColumn<ReserveManage, String>(new StringResourceModel("admin.manage.check.in", null),"check_in", "check_in") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReserveManage>> item, String componentId, IModel<ReserveManage> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getCheck_in(), MyWebApplication.DATE_TIME_SHORT_FORMAT))).add(new AttributeModifier("style", "white-space: nowrap"));
			}
		});
		columns.add(new DateFilteredPropertyColumn<ReserveManage, String>(new StringResourceModel("admin.manage.check.out", null),"check_out", "check_out") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(Item<ICellPopulator<ReserveManage>> item, String componentId, IModel<ReserveManage> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getCheck_out(), MyWebApplication.DATE_TIME_SHORT_FORMAT))).add(new AttributeModifier("style", "white-space: nowrap"));
			}
		});
		columns.add(new MyChoiceFilteredPropertyColumn<ReserveManage, ReservationType, String>(new StringResourceModel("admin.manage.reserve.type", null), "servicetype", "service_type", new LoadableDetachableModel<List<? extends ReservationType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ReservationType> load() {
				List<ReservationType> list = new ArrayList<ReservationType>();
				return list;
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReserveManage>> item, String componentId,IModel<ReserveManage> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return null;
					}
				}).setEscapeModelStrings(false));
			}
		});
		columns.add(new MyChoiceFilteredPropertyColumn<ReserveManage, ReservationStatus, String>(new StringResourceModel("admin.manage.reserve.status", null), "servicetype", "service_type", new LoadableDetachableModel<List<? extends ReservationStatus>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected List<ReservationStatus> load() {
				List<ReservationStatus> list = new ArrayList<ReservationStatus>();
				return list;
			}
		}){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(Item<ICellPopulator<ReserveManage>> item, String componentId,IModel<ReserveManage> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						return null;
					}
				}).setEscapeModelStrings(false));
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<ReserveManage, ReserveManage, String>(new StringResourceModel("admin.manage.total", null), "total", "total"));
		
		final DataTable<ReserveManage, String> dataTable = new DataTable<ReserveManage, String>("table", columns, provider, getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<ReserveManageFilter> form = new FilterForm<ReserveManageFilter>("filterForm", provider) {
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
	
	private class ShowForm extends Form<ReserveManageFilter> {
		private static final long serialVersionUID = 1L;

		public ShowForm(String id, ReserveManageFilter filter) {
			super(id, new CompoundPropertyModel<ReserveManageFilter>(filter));
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

		public ViewDetail(String id, final ReserveManage manage) {
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
			add(new Label("guest", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return null;
				}
			}).setEscapeModelStrings(false));
			add(new Label("hotel", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return null;
				}
			}).setEscapeModelStrings(false));
			add(new Label("check_in_out", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return DateUtil.toString(manage.getCheck_in(), MyWebApplication.DATE_TIME_SHORT_FORMAT) + " - " + DateUtil.toString(manage.getCheck_out(), MyWebApplication.DATE_TIME_SHORT_FORMAT);
				}
			}));
			add(new Label("guests", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return null;
				}
			}));
			add(new Label("nights", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return null;
				}
			}));
			add(new Label("cost", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return String.valueOf(manage.getTotal());
				}
			}));
			add(new Label("type", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return null;
				}
			}));
		}
		
	}
	


	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("admin.manage.title", null);
	}
}
