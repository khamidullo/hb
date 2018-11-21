package uz.hbs.actions.hotel.roomsandrates.rate.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.rate.RateDetails;
import uz.hbs.beans.rate.SalePlane;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.models.HotelModels;

public class SalePlanePanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private FeedbackPanel feedback;
	private GridView<Short> calendarGrid;
	private IDataProvider<Short> calendarDataProvider;
	private HashMap<Short, HashMap<String, Serializable>> datemap = new HashMap<Short, HashMap<String,Serializable>>();
	private HashMap<Short,List<SalePlane>> salemap = new HashMap<Short,List<SalePlane>>();
	private ModalWindow dialog;
	private DateRange range;
	private boolean resident;
	int diff_month = 0;
	
	List<SalePlane> saleList = new ArrayList<SalePlane>();

	public SalePlanePanel(String id, IBreadCrumbModel breadCrumbModel, final long hotelsusers_id, final int roomtype_id) {
		super(id, breadCrumbModel);
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		add(dialog = new ModalWindow("dialog"));
		dialog.setInitialHeight(300);
		dialog.setInitialWidth(800);
		dialog.setMinimalHeight(300);
		dialog.setMinimalWidth(800);
		dialog.setCookieName("salePlan");
		
		final Form<ValueMap> form;
		add(form = new Form<ValueMap>("form", new CompoundPropertyModel<ValueMap>(new ValueMap())));
		form.setOutputMarkupId(true);
		
		form.add(new Label("roomtype", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new MyBatisHelper().selectOne("selectRoomTypeNameById", roomtype_id);
			}
		}));
		
		final short max_person = new MyBatisHelper().selectOne("selectHoldingCapacityRoomType", roomtype_id);
		
		resident = (Boolean) new MyBatisHelper().selectOne("selectSupportResidentRate", hotelsusers_id);
		
		salemap = getSaleMap(hotelsusers_id, roomtype_id, max_person);
		
		form.add(new ListView<Short>("personlist", HotelModels.getShortListModel((short) 1, max_person)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Short> item) {
				item.add(new Label("person_number", new StringResourceModel("hotels.rate.plane.season.person", null, new Object[]{ item.getModelObject() })));
				item.add(new ListView<SalePlane>("salelist", new LoadableDetachableModel<List<SalePlane>>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<SalePlane> load() {
						return salemap.get(item.getModelObject());
					}
				}) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<SalePlane> childItem) {
						childItem.add(new RequiredTextField<BigDecimal>("sale", new PropertyModel<BigDecimal>(childItem.getModel(), "sale")));
						childItem.add(new TextField<BigDecimal>("sale_uz", new PropertyModel<BigDecimal>(childItem.getModel(), "sale_uz")).setEnabled(resident));
					}
				}.setReuseItems(true));
			}
		}.setReuseItems(true));
		
		range = new DateRange(getDate(false), getDate(true));
		
		final WebMarkupContainer calendar;
		form.add(calendar = new WebMarkupContainer("calendar"));
		calendar.setOutputMarkupId(true);
		
		datemap = getDateMap(range.getDatefrom(), range.getDateto());
		
		saleList = new ArrayList<SalePlane>();
		
		calendarDataProvider = new ListDataProvider<Short>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Short> getData() {
				List<Short> list = new ArrayList<Short>();
				for (short mm = 0; mm <= diff_month; mm++){
					list.add(mm);
				}
				return list;
			}
		};
		
		calendarGrid = new GridView<Short>("rows", calendarDataProvider) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<Short> item) {
				item.add(new CalendarGridPanel("container", datemap.get(item.getModelObject()), hotelsusers_id, roomtype_id, saleList, dialog));
			}

			@Override
			protected void populateEmptyItem(Item<Short> item) {
				item.add(new WebMarkupContainer("container").setVisible(false));
			}
		};
		calendarGrid.setColumns(3);
		calendarGrid.setRows(roundUP((float)(diff_month + 1) / 2));
		calendarGrid.setOutputMarkupId(true);
		calendar.add(calendarGrid);
		form.add(new AjaxButton("apply") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				short checked = 0;
				boolean onlyNew = false;
				SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
				try {
					for (SalePlane sale : saleList){
						if (sale.isChecked()) {
							for (short person = 1; person <= max_person; person ++){
								for (SalePlane plane : salemap.get(person)){
									if (plane.getId() == null) {
										onlyNew = true;
										break;
									}
								}
								if (onlyNew) break;
							}
						}
						if (onlyNew) break;
					}
					for (SalePlane sale : saleList){
						if (sale.isChecked()) {
							for (short person = 1; person <= max_person; person ++){
								for (SalePlane plane : salemap.get(person)){
									SalePlane temp = new SalePlane(plane, sale.getSale_date(), ((MySession) getSession()).getUser().getId());
									if (onlyNew) {
										boolean exists = new MyBatisHelper().selectOne("selectIsExistsSalePlane", temp);
										if (! exists) sql.insert("insertSalePlane", temp);
									}
									else {	
										if (sql.update("updateSalePlane2", temp) == 0) {
											sql.insert("insertSalePlane", temp);
										}
									}
								}
							}
							checked ++;
						}
					}
					if (checked > 0) {
						sql.commit();
						feedback.success(getString("hotels.sale.plane.update.success"));
						target.add(calendar);
					} else feedback.warn(getString("hotels.sale.plane.update.warn"));
				} catch (Exception e) {
					logger.error("Exception", e);
					sql.rollback();
					feedback.error(getString("hotels.sale.plane.update.fail"));
				} finally {
					sql.close();
					target.add(feedback);
				}
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(form.getMarkupId(), getSession().getLocale().getLanguage()));
			}
		});
		
		final SearchForm searchform;
		add(searchform = new SearchForm("searchform", range));
		searchform.add(new AjaxButton("search") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				range = (DateRange) form.getDefaultModelObject();
				try {
					if (range.getDatefrom().after(range.getDateto())) {
						feedback.error("Date Range Error");
						return;
					}
					datemap = getDateMap(range.getDatefrom(), range.getDateto());
					
					saleList = new ArrayList<SalePlane>();
					
					calendarDataProvider = new ListDataProvider<Short>() {
						private static final long serialVersionUID = 1L;

						@Override
						protected List<Short> getData() {
							List<Short> list = new ArrayList<Short>();
							for (short mm = 0; mm <= diff_month; mm++){
								list.add(mm);
							}
							return list;
						}
					};
					
					calendarGrid.setRows(roundUP((float)(diff_month + 1) / 2));
					
					target.add(calendar);
				} finally {
					target.add(feedback);
				}
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(searchform.getMarkupId(), getSession().getLocale().getLanguage()));
			}
		});
	}
	
	private class SearchForm extends Form<DateRange>{
		private static final long serialVersionUID = 1L;

		public SearchForm(String id, DateRange range) {
			super(id, new CompoundPropertyModel<DateRange>(range));
			DateTextField date_from, date_to;
			add(date_from = new DateTextField("datefrom", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			date_from.add(new MyDatePicker());
			date_from.setLabel(new StringResourceModel("from", null));
			date_from.setRequired(true);
			
			add(date_to = new DateTextField("dateto", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			date_to.add(new MyDatePicker());
			date_to.setLabel(new StringResourceModel("to", null));
			date_to.setRequired(true);
			
			CommonUtil.setFormComponentRequired(this);
		}
		
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.rate.sale", null);
	}
	
	public Date getDate(boolean isLastDay){
		Calendar calendar = Calendar.getInstance();
		if (isLastDay) calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		else calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		return calendar.getTime();
	}
	
	public Date getDate(Date date, boolean isLastDay){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (isLastDay) calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		else calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		return calendar.getTime();
	}
	
	public Date getDateFrom(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 4);
		return calendar.getTime();
	}
	
	public Date getDateTo(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR) + 1, Calendar.JANUARY, 18);
		return calendar.getTime();
	}
	
	public HashMap<Short, HashMap<String,Serializable>> getDateMap(Date datefrom, Date dateto){
		HashMap<Short, HashMap<String, Serializable>> datemap = new HashMap<Short, HashMap<String,Serializable>>();
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("date_from", getDate(datefrom, false));
		param.put("date_to", getDate(dateto, true));
		diff_month = new MyBatisHelper().selectOne("selectDiffMonth", param);
		for (short mm = 0; mm <= diff_month; mm++){
			if (diff_month == 0) {
				datemap.put((short) mm, param);
			} else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(datefrom);
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + mm);
				param = new HashMap<String, Serializable>();
				param.put("date_from", getDate(calendar.getTime(), false));
				param.put("date_to", getDate(calendar.getTime(), true));
				datemap.put(mm, param);
			}
		}
		return datemap;
	}
	
	private HashMap<Short,List<SalePlane>> getSaleMap(long hotelsusers_id, int roomtype_id, short max_person){
		HashMap<Short,List<SalePlane>> salemap = new HashMap<Short,List<SalePlane>>();
		for (short person = 1; person <= max_person; person ++){
			List<SalePlane> list = new ArrayList<SalePlane>();
			list.add(new SalePlane(hotelsusers_id, roomtype_id, person, RateDetails.INDIVIDUAL));
			list.add(new SalePlane(hotelsusers_id, roomtype_id, person, RateDetails.GROUP));
			salemap.put(person, list);
		}
		return salemap;
	}
	
	private int roundUP(double d){
	    double dAbs = Math.abs(d);
	    int i = (int) dAbs;
	    double result = dAbs - (double) i;
	    if(result == 0.0){ 
	        return (int) d;
	    } else {
	        return (int) d<0 ? -(i+1) : i+1;          
	    }
	}
	
	@SuppressWarnings("unused")
	private class DateRange implements IClusterable {
		private static final long serialVersionUID = 1L;
		private Date datefrom;
		private Date dateto;
		
		public DateRange() {
		}
		
		public DateRange(Date datefrom, Date dateto) {
			this.datefrom = datefrom;
			this.dateto = dateto;
		}

		public Date getDatefrom() {
			return datefrom;
		}

		public void setDatefrom(Date datefrom) {
			this.datefrom = datefrom;
		}

		public Date getDateto() {
			return dateto;
		}

		public void setDateto(Date dateto) {
			this.dateto = dateto;
		}
	}
}
