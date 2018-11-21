package uz.hbs.actions.admin.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.beans.IdLongAndName;
import uz.hbs.beans.reports.FinanceReport;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.utils.BundleUtil;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.models.MyChoiceRenderer;

public class FinanceTotalReportPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private ValueMap model;
	private FeedbackPanel feedback;
	List<FinanceReport> list = new ArrayList<FinanceReport>();
	private FinanceReport totalReport = new FinanceReport();
	private double annual_volume = new Double(new BundleUtil().configValue("touragent_annual_volume"));
	private HashMap<Integer, Double> commission = new HashMap<Integer, Double>();


	public FinanceTotalReportPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		Form<ValueMap> form;
		model = new ValueMap();
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		Calendar calendar = Calendar.getInstance();
		final int current_year = calendar.get(Calendar.YEAR);
		model.put("year", current_year);
		add(form = new Form<ValueMap>("form", new CompoundPropertyModel<ValueMap>(model)));
		form.add(new DropDownChoice<Integer>("year", new LoadableDetachableModel<List<Integer>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Integer> load() {
				List<Integer> list = new ArrayList<Integer>();
				for (int i = current_year; i >= 2015; i--) list.add(i);
				return list;
			}
		}, new MyChoiceRenderer<Integer>()));
		
		WebMarkupContainer container;
		add(container = new WebMarkupContainer("container"));
		container.setOutputMarkupId(true);
		container.add(new ListView<FinanceReport>("list", new LoadableDetachableModel<List<FinanceReport>>(){
			private static final long serialVersionUID = 1L;

			@Override
			protected List<FinanceReport> load(){
				list.clear();
				List<IdLongAndName> list2 = new MyBatisHelper().selectList("selectTAAssignedManagers");
				for (IdLongAndName obj : list2) list.add(new FinanceReport(obj));
				return list;
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<FinanceReport> item) {
				FinanceReport report = (FinanceReport) item.getDefaultModelObject();
				item.add(new Label("manager", report.getManager()));
				
				HashMap<String,Object> param = new HashMap<String, Object>();
				param.put("year", model.get("year"));
				FinanceReport temp;
				param.put("id", 0);
				if (commission.get(1) == null) {
					param.put("month", 1);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(1, CommonUtil.nvl(temp.getHotel()));
				}
				if (commission.get(2) == null) {
					param.put("month", 2);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(2, CommonUtil.nvl(temp.getHotel()));
				}
				if (commission.get(3) == null) {
					param.put("month", 3);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(3, CommonUtil.nvl(temp.getHotel()));
				}
				if (commission.get(4) == null) {
					param.put("month", 4);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(4, CommonUtil.nvl(temp.getHotel()));
				}
				if (commission.get(5) == null) {
					param.put("month", 5);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(5, CommonUtil.nvl(temp.getHotel()));
				}
				if (commission.get(6) == null) {
					param.put("month", 6);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(6, CommonUtil.nvl(temp.getHotel()));
				}
				if (commission.get(7) == null) {
					param.put("month", 7);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(7, CommonUtil.nvl(temp.getHotel()));
					if (temp != null) logger.debug("Hotel = " + temp.getHotel());
				}
				if (commission.get(8) == null) {
					param.put("month", 8);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(8, CommonUtil.nvl(temp.getHotel()));
				}
				if (commission.get(9) == null) {
					param.put("month", 9);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(9, CommonUtil.nvl(temp.getHotel()));
				}
				if (commission.get(10) == null) {
					param.put("month", 10);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(10, CommonUtil.nvl(temp.getHotel()));
				}
				if (commission.get(11) == null) {
					param.put("month", 11);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(11, CommonUtil.nvl(temp.getHotel()));
				}
				if (commission.get(12) == null) {
					param.put("month", 12);
					temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
					if (temp != null) commission.put(12, CommonUtil.nvl(temp.getHotel()));
				}
				param.put("month", 1);
				
				param.put("id", report.getUsers_id());
				
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_1(temp.getHotel());
					report.setService_1(temp.getService());
					report.setCommission_1(FinanceReport.calcCommission(report.getHotel_1(), commission.get(1), list.size()));
				}
				
				item.add(new Label("hotel_1", FormatUtil.toString(report.getHotel_1())));
				item.add(new Label("service_1", FormatUtil.toString(report.getService_1())));
				item.add(new Label("commission_1", FormatUtil.toString(report.getCommission_1())));
				
				param.put("month", 2);
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_2(temp.getHotel());
					report.setService_2(temp.getService());
					report.setCommission_2(FinanceReport.calcCommission(report.getHotel_2(), commission.get(2), list.size()));
				}
				
				item.add(new Label("hotel_2", FormatUtil.toString(report.getHotel_2())));
				item.add(new Label("service_2", FormatUtil.toString(report.getService_2())));
				item.add(new Label("commission_2", FormatUtil.toString(report.getCommission_2())));

				param.put("month", 3);
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_3(temp.getHotel());
					report.setService_3(temp.getService());
					report.setCommission_3(FinanceReport.calcCommission(report.getHotel_3(), commission.get(3), list.size()));
				}
				item.add(new Label("hotel_3", FormatUtil.toString(report.getHotel_3())));
				item.add(new Label("service_3", FormatUtil.toString(report.getService_3())));
				item.add(new Label("commission_3", FormatUtil.toString(report.getCommission_3())));

				param.put("month", 4);
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_4(temp.getHotel());
					report.setService_4(temp.getService());
					report.setCommission_4(FinanceReport.calcCommission(report.getHotel_4(), commission.get(4), list.size()));
				}
				
				item.add(new Label("hotel_4", FormatUtil.toString(report.getHotel_4())));
				item.add(new Label("service_4", FormatUtil.toString(report.getService_4())));
				item.add(new Label("commission_4", FormatUtil.toString(report.getCommission_4())));

				param.put("month", 5);
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_5(temp.getHotel());
					report.setService_5(temp.getService());
					report.setCommission_5(FinanceReport.calcCommission(report.getHotel_5(), commission.get(5), list.size()));
				}
				item.add(new Label("hotel_5", FormatUtil.toString(report.getHotel_5())));
				item.add(new Label("service_5", FormatUtil.toString(report.getService_5())));
				item.add(new Label("commission_5", FormatUtil.toString(report.getCommission_5())));

				param.put("month", 6);
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_6(temp.getHotel());
					report.setService_6(temp.getService());
					report.setCommission_6(FinanceReport.calcCommission(report.getHotel_6(), commission.get(6), list.size()));
				}
				
				item.add(new Label("hotel_6", FormatUtil.toString(report.getHotel_6())));
				item.add(new Label("service_6", FormatUtil.toString(report.getService_6())));
				item.add(new Label("commission_6", FormatUtil.toString(report.getCommission_6())));

				param.put("month", 7);
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_7(temp.getHotel());
					report.setService_7(temp.getService());
					report.setCommission_7(FinanceReport.calcCommission(report.getHotel_7(), commission.get(7), list.size()));
				}	
				item.add(new Label("hotel_7", FormatUtil.toString(report.getHotel_7())));
				item.add(new Label("service_7", FormatUtil.toString(report.getService_7())));
				item.add(new Label("commission_7", FormatUtil.toString(report.getCommission_7())));

				param.put("month", 8);
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_8(temp.getHotel());
					report.setService_8(temp.getService());
					report.setCommission_8(FinanceReport.calcCommission(report.getHotel_8(), commission.get(8), list.size()));
				}
				item.add(new Label("hotel_8", FormatUtil.toString(report.getHotel_8())));
				item.add(new Label("service_8", FormatUtil.toString(report.getService_8())));
				item.add(new Label("commission_8", FormatUtil.toString(report.getCommission_8())));

				param.put("month", 9);
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_9(temp.getHotel());
					report.setService_9(temp.getService());
					report.setCommission_9(FinanceReport.calcCommission(report.getHotel_9(), commission.get(9), list.size()));
				}	
				item.add(new Label("hotel_9", FormatUtil.toString(report.getHotel_9())));
				item.add(new Label("service_9", FormatUtil.toString(report.getService_9())));
				item.add(new Label("commission_9", FormatUtil.toString(report.getCommission_9())));

				param.put("month", 10);
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_10(temp.getHotel());
					report.setService_10(temp.getService());
					report.setCommission_10(FinanceReport.calcCommission(report.getHotel_10(), commission.get(10), list.size()));
				}	
				item.add(new Label("hotel_10", FormatUtil.toString(report.getHotel_10())));
				item.add(new Label("service_10", FormatUtil.toString(report.getService_10())));
				item.add(new Label("commission_10", FormatUtil.toString(report.getCommission_10())));

				param.put("month", 11);
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_11(temp.getHotel());
					report.setService_11(temp.getService());
					report.setCommission_11(FinanceReport.calcCommission(report.getHotel_11(), commission.get(11), list.size()));
				}	
				item.add(new Label("hotel_11", FormatUtil.toString(report.getHotel_11())));
				item.add(new Label("service_11", FormatUtil.toString(report.getService_11())));
				item.add(new Label("commission_11", FormatUtil.toString(report.getCommission_11())));

				param.put("month", 12);
				temp = new MyBatisHelper().selectOne("selectFinanceTotalVolume", param);
				if (temp != null) {
					report.setHotel_12(temp.getHotel());
					report.setService_12(temp.getService());
					report.setCommission_12(FinanceReport.calcCommission(report.getHotel_12(), commission.get(12), list.size()));
				}	
				item.add(new Label("hotel_12", FormatUtil.toString(report.getHotel_12())));
				item.add(new Label("service_12", FormatUtil.toString(report.getService_12())));
				item.add(new Label("commission_12", FormatUtil.toString(report.getCommission_12())));
				
				report.calcTotal();
				item.add(new Label("hotel", FormatUtil.toString(report.getHotel())));
				item.add(new Label("service", FormatUtil.toString(report.getService())));
				
				totalReport.setHotel_1(CommonUtil.nvl(totalReport.getHotel_1()) + CommonUtil.nvl(report.getHotel_1()));
				totalReport.setHotel_2(CommonUtil.nvl(totalReport.getHotel_2()) + CommonUtil.nvl(report.getHotel_2()));
				totalReport.setHotel_3(CommonUtil.nvl(totalReport.getHotel_3()) + CommonUtil.nvl(report.getHotel_3()));
				totalReport.setHotel_4(CommonUtil.nvl(totalReport.getHotel_4()) + CommonUtil.nvl(report.getHotel_4()));
				totalReport.setHotel_5(CommonUtil.nvl(totalReport.getHotel_5()) + CommonUtil.nvl(report.getHotel_5()));
				totalReport.setHotel_6(CommonUtil.nvl(totalReport.getHotel_6()) + CommonUtil.nvl(report.getHotel_6()));
				totalReport.setHotel_7(CommonUtil.nvl(totalReport.getHotel_7()) + CommonUtil.nvl(report.getHotel_7()));
				totalReport.setHotel_8(CommonUtil.nvl(totalReport.getHotel_8()) + CommonUtil.nvl(report.getHotel_8()));
				totalReport.setHotel_9(CommonUtil.nvl(totalReport.getHotel_9()) + CommonUtil.nvl(report.getHotel_9()));
				totalReport.setHotel_10(CommonUtil.nvl(totalReport.getHotel_10()) + CommonUtil.nvl(report.getHotel_10()));
				totalReport.setHotel_11(CommonUtil.nvl(totalReport.getHotel_11()) + CommonUtil.nvl(report.getHotel_11()));
				totalReport.setHotel_12(CommonUtil.nvl(totalReport.getHotel_12()) + CommonUtil.nvl(report.getHotel_12()));
				totalReport.setService_1(CommonUtil.nvl(totalReport.getService_1()) + CommonUtil.nvl(report.getService_1()));
				totalReport.setService_2(CommonUtil.nvl(totalReport.getService_2()) + CommonUtil.nvl(report.getService_2()));
				totalReport.setService_3(CommonUtil.nvl(totalReport.getService_3()) + CommonUtil.nvl(report.getService_3()));
				totalReport.setService_4(CommonUtil.nvl(totalReport.getService_4()) + CommonUtil.nvl(report.getService_4()));
				totalReport.setService_5(CommonUtil.nvl(totalReport.getService_5()) + CommonUtil.nvl(report.getService_5()));
				totalReport.setService_6(CommonUtil.nvl(totalReport.getService_6()) + CommonUtil.nvl(report.getService_6()));
				totalReport.setService_7(CommonUtil.nvl(totalReport.getService_7()) + CommonUtil.nvl(report.getService_7()));
				totalReport.setService_8(CommonUtil.nvl(totalReport.getService_8()) + CommonUtil.nvl(report.getService_8()));
				totalReport.setService_9(CommonUtil.nvl(totalReport.getService_9()) + CommonUtil.nvl(report.getService_9()));
				totalReport.setService_10(CommonUtil.nvl(totalReport.getService_10()) + CommonUtil.nvl(report.getService_10()));
				totalReport.setService_11(CommonUtil.nvl(totalReport.getService_11()) + CommonUtil.nvl(report.getService_11()));
				totalReport.setService_12(CommonUtil.nvl(totalReport.getService_12()) + CommonUtil.nvl(report.getService_12()));
				totalReport.setCommission_1(CommonUtil.nvl(totalReport.getCommission_1()) + CommonUtil.nvl(report.getCommission_1()));
				totalReport.setCommission_2(CommonUtil.nvl(totalReport.getCommission_2()) + CommonUtil.nvl(report.getCommission_2()));
				totalReport.setCommission_3(CommonUtil.nvl(totalReport.getCommission_3()) + CommonUtil.nvl(report.getCommission_3()));
				totalReport.setCommission_4(CommonUtil.nvl(totalReport.getCommission_4()) + CommonUtil.nvl(report.getCommission_4()));
				totalReport.setCommission_5(CommonUtil.nvl(totalReport.getCommission_5()) + CommonUtil.nvl(report.getCommission_5()));
				totalReport.setCommission_6(CommonUtil.nvl(totalReport.getCommission_6()) + CommonUtil.nvl(report.getCommission_6()));
				totalReport.setCommission_7(CommonUtil.nvl(totalReport.getCommission_7()) + CommonUtil.nvl(report.getCommission_7()));
				totalReport.setCommission_8(CommonUtil.nvl(totalReport.getCommission_8()) + CommonUtil.nvl(report.getCommission_8()));
				totalReport.setCommission_9(CommonUtil.nvl(totalReport.getCommission_9()) + CommonUtil.nvl(report.getCommission_9()));
				totalReport.setCommission_10(CommonUtil.nvl(totalReport.getCommission_10()) + CommonUtil.nvl(report.getCommission_10()));
				totalReport.setCommission_11(CommonUtil.nvl(totalReport.getCommission_11()) + CommonUtil.nvl(report.getCommission_11()));
				totalReport.setCommission_12(CommonUtil.nvl(totalReport.getCommission_12()) + CommonUtil.nvl(report.getCommission_12()));				
			}
		});
		WebMarkupContainer totalSeparately;
		container.add(totalSeparately = new WebMarkupContainer("totalSeparately"));
		totalSeparately.setOutputMarkupId(true);
		totalSeparately.add(new Label("hotel_1", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_1());
			}
		}));
		totalSeparately.add(new Label("service_1", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_1());
			}
		}));
		totalSeparately.add(new Label("commission_1", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_1());
			}
		}));
		totalSeparately.add(new Label("hotel_2", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_2());
			}
		}));
		totalSeparately.add(new Label("service_2", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_2());
			}
		}));
		totalSeparately.add(new Label("commission_2", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_2());
			}
		}));
		totalSeparately.add(new Label("hotel_3", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_3());
			}
		}));
		totalSeparately.add(new Label("service_3", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_3());
			}
		}));
		totalSeparately.add(new Label("commission_3", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_3());
			}
		}));
		totalSeparately.add(new Label("hotel_4", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_4());
			}
		}));
		totalSeparately.add(new Label("service_4", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_4());
			}
		}));
		totalSeparately.add(new Label("commission_4", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_4());
			}
		}));
		totalSeparately.add(new Label("hotel_5", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_5());
			}
		}));
		totalSeparately.add(new Label("service_5", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_5());
			}
		}));
		totalSeparately.add(new Label("commission_5", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_5());
			}
		}));
		totalSeparately.add(new Label("hotel_6", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_6());
			}
		}));
		totalSeparately.add(new Label("service_6", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_6());
			}
		}));
		totalSeparately.add(new Label("commission_6", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_6());
			}
		}));
		totalSeparately.add(new Label("hotel_7", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_7());
			}
		}));
		totalSeparately.add(new Label("service_7", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_7());
			}
		}));
		totalSeparately.add(new Label("commission_7", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_7());
			}
		}));
		totalSeparately.add(new Label("hotel_8", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_8());
			}
		}));
		totalSeparately.add(new Label("service_8", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_8());
			}
		}));
		totalSeparately.add(new Label("commission_8", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_8());
			}
		}));
		totalSeparately.add(new Label("hotel_9", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_9());
			}
		}));
		totalSeparately.add(new Label("service_9", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_9());
			}
		}));
		totalSeparately.add(new Label("commission_9", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_9());
			}
		}));
		totalSeparately.add(new Label("hotel_10", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_10());
			}
		}));
		totalSeparately.add(new Label("service_10", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_10());
			}
		}));
		totalSeparately.add(new Label("commission_10", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_10());
			}
		}));
		totalSeparately.add(new Label("hotel_11", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_11());
			}
		}));
		totalSeparately.add(new Label("service_11", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_11());
			}
		}));
		totalSeparately.add(new Label("commission_11", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_11());
			}
		}));
		totalSeparately.add(new Label("hotel_12", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getHotel_12());
			}
		}));
		totalSeparately.add(new Label("service_12", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService_12());
			}
		}));
		totalSeparately.add(new Label("commission_12", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getCommission_12());
			}
		}));
		totalSeparately.add(new Label("hotel", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				totalReport.calcTotal();
				return FormatUtil.toString(totalReport.getHotel());
			}
		}));
		totalSeparately.add(new Label("service", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(totalReport.getService());
			}
		}));		
		
		WebMarkupContainer total;
		container.add(total = new WebMarkupContainer("total"));
		total.setOutputMarkupId(true);
		total.add(new Label("total_1", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_1()) + CommonUtil.nvl(totalReport.getService_1()));
			}
		}));
		total.add(new Label("total_2", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_2()) + CommonUtil.nvl(totalReport.getService_2()));
			}
		}));
		total.add(new Label("total_3", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_3()) + CommonUtil.nvl(totalReport.getService_3()));
			}
		}));
		total.add(new Label("total_4", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_4()) + CommonUtil.nvl(totalReport.getService_4()));
			}
		}));
		total.add(new Label("total_5", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_5()) + CommonUtil.nvl(totalReport.getService_5()));
			}
		}));
		total.add(new Label("total_6", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_6()) + CommonUtil.nvl(totalReport.getService_6()));
			}
		}));
		total.add(new Label("total_7", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_7()) + CommonUtil.nvl(totalReport.getService_7()));
			}
		}));
		total.add(new Label("total_8", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_8()) + CommonUtil.nvl(totalReport.getService_8()));
			}
		}));
		total.add(new Label("total_9", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_9()) + CommonUtil.nvl(totalReport.getService_9()));
			}
		}));
		total.add(new Label("total_10", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_10()) + CommonUtil.nvl(totalReport.getService_10()));
			}
		}));
		total.add(new Label("total_11", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_11()) + CommonUtil.nvl(totalReport.getService_11()));
			}
		}));
		total.add(new Label("total_12", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel_12()) + CommonUtil.nvl(totalReport.getService_12()));
			}
		}));
		total.add(new Label("total", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString(CommonUtil.nvl(totalReport.getHotel()) + CommonUtil.nvl(totalReport.getService()));
			}
		}));
		WebMarkupContainer percent;
		container.add(percent = new WebMarkupContainer("percent"));
		percent.setOutputMarkupId(true);
		percent.add(new Label("percent_1", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_1()) + CommonUtil.nvl(totalReport.getService_1()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent_2", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_2()) + CommonUtil.nvl(totalReport.getService_2()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent_3", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_3()) + CommonUtil.nvl(totalReport.getService_3()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent_4", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_4()) + CommonUtil.nvl(totalReport.getService_4()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent_5", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_5()) + CommonUtil.nvl(totalReport.getService_5()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent_6", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_6()) + CommonUtil.nvl(totalReport.getService_6()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent_7", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_7()) + CommonUtil.nvl(totalReport.getService_7()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent_8", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_8()) + CommonUtil.nvl(totalReport.getService_8()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent_9", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_9()) + CommonUtil.nvl(totalReport.getService_9()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent_10", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_10()) + CommonUtil.nvl(totalReport.getService_10()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent_11", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_11()) + CommonUtil.nvl(totalReport.getService_11()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent_12", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel_12()) + CommonUtil.nvl(totalReport.getService_12()) * 100) / annual_volume);
			}
		}));
		percent.add(new Label("percent", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return FormatUtil.toString((CommonUtil.nvl(totalReport.getHotel()) + CommonUtil.nvl(totalReport.getService()) * 100) / annual_volume);
			}
		}));
		
		form.add(new AjaxButton("search"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
			}
		});
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("admin.reports.finance_total", null);
	}
	
	@Override
	protected void onBeforeRender() {
		totalReport = new FinanceReport();
		super.onBeforeRender();
	}
}
