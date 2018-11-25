package uz.hbs.actions.hotel.roomsandrates.rate;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.IdAndName;
import uz.hbs.beans.rate.RateDetails;
import uz.hbs.beans.rate.RatePlane;
import uz.hbs.beans.rate.RateSeason;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.RatePlaneUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.models.MyAjaxCallListener;

public class RatePlanPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;
	private boolean resident;
	
	public RatePlanPanel(String id, IBreadCrumbModel breadCrumbModel, RatePlane plane) {
		this(id, breadCrumbModel, plane, true);
	}

	public RatePlanPanel(String id, IBreadCrumbModel breadCrumbModel, RatePlane plane, final boolean editable) {
		super(id, breadCrumbModel);
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		final Form<RatePlane> form;
		
		resident = (Boolean) new MyBatisHelper().selectOne("selectSupportResidentRate", plane.getHotelsusers_id());
		
		add(form = new RateForm("form", plane));
		form.add(new AjaxButton("save") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				RatePlane plane = (RatePlane) form.getDefaultModelObject();
				try {
					RatePlaneUtil.saveRatePlane(plane, plane.getId() == null, ((MySession) getSession()).getUser().getId(), feedback);
				} finally {
					target.add(feedback);
				}
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(form.getMarkupId(), getSession().getLocale().getLanguage()));
			}
			
			@Override
			public boolean isVisible() {
				return editable;
			}
		});
		form.setEnabled(editable);
	}
	
	private class RateForm extends Form<RatePlane>{
		private static final long serialVersionUID = 1L;

		public RateForm(String id, final RatePlane plane) {
			super(id, new CompoundPropertyModel<RatePlane>(plane));
			TextField<String> name;
			TextArea<String> description;
			
			final List<Short> personlist = RatePlaneUtil.personlist(plane.getHotelsusers_id());
			
			add(name = new TextField<String>("name"));
			name.setLabel(new StringResourceModel("hotels.rate.plane.name", null));
			name.setRequired(MyWebApplication.IS_REQUIRE);
			name.add(new AjaxOnBlurEvent());
			
			add(description = new TextArea<String>("description"));
			description.setLabel(new StringResourceModel("hotels.rate.plane.description", null));
			description.add(new AjaxOnBlurEvent());
			
			add(new CheckBox("internal"));
			
			final WebMarkupContainer container;
			add(container = new WebMarkupContainer("container"));
			container.setOutputMarkupId(true);
			container.add(new ListView<RateSeason>("seasondetaillist", new LoadableDetachableModel<List<RateSeason>>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected List<RateSeason> load() {
					return plane.getSeasonlist();
				}
			}) {
				
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void populateItem(final ListItem<RateSeason> item) {
					final RateSeason season = (RateSeason) item.getDefaultModelObject();
					item.add(new Label("season", new StringResourceModel("hotels.rate.plane.season", item.getModel())).add(new AttributeModifier("colspan", personlist.size() * 2)));
					WebMarkupContainer season_container;
					item.add(season_container = new WebMarkupContainer("season_container"));
					season_container.setOutputMarkupId(true);
					season_container.add(new AttributeModifier("colspan", personlist.size() * 2));
					
					WebMarkupContainer individualHeader = new WebMarkupContainer("individualHeader");
					individualHeader.add(new AttributeModifier("colspan", personlist.size()));
					item.add(individualHeader);
					
					WebMarkupContainer groupHeader = new WebMarkupContainer("groupHeader");
					groupHeader.add(new AttributeModifier("colspan", personlist.size()));
					item.add(groupHeader);
					
					DateTextField fromDate;
					season_container.add(fromDate = new DateTextField("season_from", new PropertyModel<Date>(season, "season_from"), new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
					if (item.getIndex() > 0){
						fromDate.add(new AttributeModifier("readonly", "readonly"));
						fromDate.add(new AttributeModifier("from", "date" + (item.getIndex() - 1)));
					}
					if (item.getIndex() == 0) fromDate.add(new MyDatePicker());
					fromDate.add(new AjaxOnBlurEvent());
					fromDate.setRequired(true);
					
					DateTextField toDate;
					season_container.add(toDate = new DateTextField("season_to", new PropertyModel<Date>(season, "season_to"), new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
					toDate.add(new MyDatePicker());
					toDate.add(new AjaxOnBlurEvent());
					toDate.add(new AttributeModifier("to", "date" + item.getIndex()));
					toDate.add(new AttributeModifier("data-bind", fromDate.getMarkupId()));
					toDate.setRequired(true);
					
					toDate.add(new AttributeModifier("onchange", "calcSeasonPeriod(this);"));
					season_container.add(new AjaxLink<Void>("delete"){
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target) {
							if (season.getId() != null) {
								SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
								try {
									sql.delete("deleteRateDetailsBySeason", season.getId());
									sql.delete("deleteRatePlaneSeason", season.getId());
									sql.commit();
									feedback.success(getString("hotels.rate.plane.season.delete.success"));
								} catch (Exception e) {
									logger.error("Exception", e);
									sql.rollback();
									feedback.error(getString("hotels.rate.plane.season.delete.fail"));
								} finally {
									sql.close();
									target.add(feedback);
								}
							}
							plane.getSeasonlist().remove(item.getIndex());
							target.add(container);
						}
						
						@Override
						public boolean isVisible() {
							return plane.getSeasonlist().size() > 1 && item.getIndex() == plane.getSeasonlist().size() - 1;
						}
						
						@Override
						protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
							super.updateAjaxAttributes(attributes);
							attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
							IAjaxCallListener listener = new AjaxCallListener(){
								private static final long serialVersionUID = 1L;

								@Override
								public CharSequence getBeforeSendHandler(Component component) {
									return "return getter();";
								}
								
								@Override
								public CharSequence getCompleteHandler(Component component) {
									return "return calcSeasonPeriodAll();";
								}
								
								@Override
								public CharSequence getSuccessHandler(Component component) {
									return "return setter();";
								}
							};
							attributes.getAjaxCallListeners().add(listener);
						}
					});
					season_container.add(new AjaxLink<Void>("add"){
						private static final long serialVersionUID = 1L;
						
						@Override
						public void onClick(AjaxRequestTarget target) {
							plane.getSeasonlist().add(RatePlaneUtil.createSeason(plane.getId(), plane.getHotelsusers_id(), plane.nextSeason()));
							target.add(container);
						}
						
						@Override
						public boolean isVisible() {
							return item.getIndex() == 0;
						}
						
						@Override
						protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
							super.updateAjaxAttributes(attributes);
							attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
							IAjaxCallListener listener = new AjaxCallListener(){
								private static final long serialVersionUID = 1L;

								@Override
								public CharSequence getBeforeSendHandler(Component component) {
									return "return getter();";
								}
								
								@Override
								public CharSequence getCompleteHandler(Component component) {
									return "return calcSeasonPeriodAll();";
								}
								
								@Override
								public CharSequence getSuccessHandler(Component component) {
									return "return setter();";
								}
							};
							attributes.getAjaxCallListeners().add(listener);
						}
					});
					
					item.add(new ListView<Short>("persondetaillist", new LoadableDetachableModel<List<Short>>() {
						private static final long serialVersionUID = 1L;

						@Override
						protected List<Short> load() {
							return personlist;
						}
					}) {
						private static final long serialVersionUID = 1L;

						@Override
						protected void populateItem(ListItem<Short> item) {
							Short person = (Short) item.getDefaultModelObject();
							item.add(new Label("person_number", new StringResourceModel("hotels.rate.plane.season.person", null, new Object[]{ person })));
						}
					});
					item.add(new ListView<Short>("persontypelist", new LoadableDetachableModel<List<Short>>() {
						private static final long serialVersionUID = 1L;
						
						@Override
						protected List<Short> load() {
							return personlist;
						}
					}) {
						private static final long serialVersionUID = 1L;
						
						@Override
						protected void populateItem(ListItem<Short> item) {
							RepeatingView listItems;
							item.add(listItems = new RepeatingView("persontypeitem"));
							listItems.add(new Label(listItems.newChildId(), getString("hotels.rate.plane.person.resident.no")));
							listItems.add(new Label(listItems.newChildId(), getString("hotels.rate.plane.person.resident")));
						}
					});
					final HashMap<String, Serializable> param = new HashMap<String, Serializable>();
					param.put("hotel_id", plane.getHotelsusers_id());
					item.add(new ListView<IdAndName>("roomtypelist", new LoadableDetachableModel<List<IdAndName>>() {
						private static final long serialVersionUID = 1L;
						
						@Override
						protected List<IdAndName> load() {
							return RatePlaneUtil.getRoomTypeListByHotel(plane.getHotelsusers_id());
						}
					}) {
						private static final long serialVersionUID = 1L;
						
						@Override
						protected void populateItem(ListItem<IdAndName> item) {
							final IdAndName roomtype = (IdAndName) item.getDefaultModelObject();
							
							final short holding_capacity = new MyBatisHelper().selectOne("selectHoldingCapacityRoomType", roomtype.getId());
							
							final List<RateDetails> detaillist = RatePlaneUtil.getRateDetailList(season.getDetails().get(season.getSeason_number()).get(roomtype.getId()), (short) personlist.size());
							
							item.add(new Label("roomtype", new Model<String>(roomtype.getName())));
							item.add(new ListView<RateDetails>("ratedetaillist", new LoadableDetachableModel<List<RateDetails>>() {
								private static final long serialVersionUID = 1L;
								
								@Override
								protected List<RateDetails> load() {
									return detaillist;
								}
							}) {
								private static final long serialVersionUID = 1L;
								
								@Override
								protected void populateItem(ListItem<RateDetails> item) {
									RateDetails detail = (RateDetails) item.getDefaultModelObject();
									if (detail.getPerson_number() > 0 && detail.getPerson_number() <= holding_capacity){
										item.add(new RateFieldPanel("ratedetail", item.getModel(), resident));
									} else {
										item.add(new WebMarkupContainer("ratedetail"));
									}
								}
							}.setReuseItems(true));
						}
					});
				}
			}.setReuseItems(true));
			CommonUtil.setFormComponentRequired(this);
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.rate.plane", null);
	}
}
