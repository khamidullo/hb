package uz.hbs.actions.admin.preferences.sortable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.beans.HotelDetail;
import uz.hbs.beans.Region;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class HotelSortablePanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private FeedbackPanel feedback;

	public HotelSortablePanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		add(new MyForm("form"));
	}
	
	private class MyForm extends Form<ValueMap>{
		private static final long serialVersionUID = 1L;

		public MyForm(String id) {
			super(id, new CompoundPropertyModel<ValueMap>(new ValueMap()));
			DropDownChoice<Region> region;
			add(region = new DropDownChoice<Region>("region", new LoadableDetachableModel<List<Region>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Region> load() {
					return new MyBatisHelper().selectList("selectRegionsList");
				}
			}, new ChoiceRenderer<Region>("name", "id")));
			region.setRequired(true);
			region.setLabel(new StringResourceModel("hotels.details.region", null));
			
			final WebMarkupContainer container;
			add(container = new WebMarkupContainer("container"));
			container.setOutputMarkupId(true);
			container.add(new ListView<HotelDetail>("hotellist", new LoadableDetachableModel<List<HotelDetail>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<HotelDetail> load() {
					ValueMap model = (ValueMap) getDefaultModelObject();
					if (model.get("region") != null) return new MyBatisHelper().selectList("selectHotelDetailRecommendedSort", ((Region) model.get("region")).getId());
					return Collections.emptyList();
				}
			}){
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<HotelDetail> item) {
					HotelDetail detail = (HotelDetail) item.getDefaultModelObject();
					item.add(new Label("name", detail.getDisplay_name()));
					item.add(new AttributeModifier("data-item", detail.getHotelsusers_id()));
				}
				
				@Override
				protected void onAfterRender() {
					super.onAfterRender();
					JavaScriptUtils.writeJavaScript(getResponse(), "$(function() { $('ol.hotel-sortable').sortable(); });");
				}
			});
			add(new HiddenField<String>("sortlist").setMarkupId("sortlist"));
			
			region.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(container);
				}
//				
//				@Override
//				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
//					super.updateAjaxAttributes(attributes);
//					IAjaxCallListener listener = new AjaxCallListener() {
//						private static final long serialVersionUID = 1L;
//						
//						@Override
//						public CharSequence getCompleteHandler(Component component) {
//							return "hotelSortable();";
//						}
//					};
//					attributes.getAjaxCallListeners().add(listener);
//				}
			});
			add(new IndicatingAjaxButton("submit") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					IAjaxCallListener listener = new AjaxCallListener() {
						private static final long serialVersionUID = 1L;
						
						@Override
						public CharSequence getBeforeHandler(Component component) {
							return "genSortList();";
						}
					};
					attributes.getAjaxCallListeners().add(listener);
				}
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					ValueMap model = (ValueMap) form.getDefaultModelObject();
					String sortlist = model.getString("sortlist");
					if (sortlist != null && ! sortlist.isEmpty()) {
						SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
						try {
							String [] s = sortlist.split(",");
							HashMap<String, Long> param = new HashMap<String, Long>();
							for (int i = 0; i < s.length; i++){
								param.put("recommended_sort", (long) i);
								param.put("hotel_id", Long.parseLong(s[i]));
								sql.update("updateHotelDetailsRecommendedSort", param);
							}
							sql.commit();
							feedback.success("Hotel Sortable List is successfull updated");
						} catch (NumberFormatException e) {
							logger.error("NumberFormatException", e);
							sql.rollback();
							feedback.error("Hotel Sortable List is not updated");
						} catch (Exception e){
							logger.error("Exception", e);
							sql.rollback();
							feedback.error("Hotel Sortable List is not updated");
						} finally {
							sql.close();
							target.add(feedback);
						}
					}
				}
			});
		}
		
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.hotel.sortable", null);
	}
}
