package uz.hbs.actions.api.additionalservices;

import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.AdditionalServiceOrder;
import uz.hbs.beans.Insurance;
import uz.hbs.beans.Nationality;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.models.MyAjaxCallListener;

public class InsuranceDataPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public InsuranceDataPanel(String id, final AdditionalServiceOrder reserve, final boolean editable) {
		super(id, new CompoundPropertyModel<AdditionalServiceOrder>(reserve));
		
		final WebMarkupContainer container_insurance;
		add(container_insurance = new WebMarkupContainer("container_insurance"));
		container_insurance.setOutputMarkupId(true);
		
		container_insurance.add(new ListView<Insurance>("list", new LoadableDetachableModel<List<Insurance>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Insurance> load() {
				reserve.setInsuranceList(CommonUtil.getInsuranceList(reserve.getInsuranceList(), reserve.getPerson(), ((MySession) getSession()).getUser().getId()));
				return reserve.getInsuranceList();
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Insurance> item) {
				item.add(new Label("order", item.getIndex() + 1).setRenderBodyOnly(true));
				item.add(new RequiredTextField<String>("first_name", new PropertyModel<String>(item.getModel(), "first_name")).setLabel(new StringResourceModel("hotels.guest.details.guest.name.first", null)).add(new AttributeModifier("data-item-index", item.getIndex())).add(new AjaxOnBlurEvent()));
				item.add(new RequiredTextField<String>("last_name", new PropertyModel<String>(item.getModel(), "last_name")).setLabel(new StringResourceModel("hotels.guest.details.guest.name.last", null)).add(new AttributeModifier("data-item-index", item.getIndex())).add(new AjaxOnBlurEvent()));
				DropDownChoice<Nationality> nationality;
				item.add(nationality = new DropDownChoice<Nationality>("nationality", new PropertyModel<Nationality>(item.getModel(), "nationality"), new LoadableDetachableModel<List<Nationality>>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<Nationality> load() {
						return new MyBatisHelper().selectList("selectNationalityList");
					}
				}, new ChoiceRenderer<Nationality>("name", "code")));
				nationality.setNullValid(true);
				nationality.add(new AjaxOnBlurEvent());
				nationality.add(new AttributeModifier("data-item-index", item.getIndex()));
				nationality.setLabel(new StringResourceModel("hotels.guest.details.nationality", null));
				
				DateTextField birth_date;
				item.add(birth_date = new DateTextField("birth_date", new PropertyModel<Date>(item.getModel(), "birth_date"), new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
				birth_date.add(new MyDatePicker());
				birth_date.add(new AttributeModifier("data-date-format", MyWebApplication.DATE_FORMAT));
				birth_date.setLabel(new StringResourceModel("hotels.guest.details.passport.date_of_issue", null));
				
				TextField<String> passport_number;
				item.add(passport_number = new TextField<String>("passport_number", new PropertyModel<String>(item.getModel(), "passport_number")));
				passport_number.setRequired(true);
				passport_number.add(new AjaxOnBlurEvent());
				passport_number.setLabel(new StringResourceModel("hotels.guest.details.passport.number", null));
				
				DateTextField passport_issue_date;
				item.add(passport_issue_date = new DateTextField("passport_issue_date", new PropertyModel<Date>(item.getModel(), "passport_issue_date"), new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
				passport_issue_date.add(new MyDatePicker());
				passport_issue_date.add(new AjaxOnBlurEvent());
				passport_issue_date.add(new AttributeModifier("data-date-format", MyWebApplication.DATE_FORMAT));
				passport_issue_date.setLabel(new StringResourceModel("hotels.guest.details.passport.date_of_issue", null));
				
				item.add(new AjaxLink<Void>("delete"){
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						new MyBatisHelper().delete("deleteInsuranceById", item.getModelObject().getId());
						reserve.getInsuranceList().remove(item.getIndex());
						target.add(container_insurance);
					}
					
					@Override
					public boolean isVisible() {
						return item.getIndex() > 0;
					}
					
					@Override
					public boolean isEnabled() {
						return editable;
					}
					
					@Override
					protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
						super.updateAjaxAttributes(attributes);
						attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
						IAjaxCallListener listener = new AjaxCallListener(){
							private static final long serialVersionUID = 1L;

							@Override
							public CharSequence getSuccessHandler(Component component) {
								return "doneDuplicate();";
							}
						};
						attributes.getAjaxCallListeners().add(listener);
					}
				});

			}
		}.setReuseItems(true));
	}
}
