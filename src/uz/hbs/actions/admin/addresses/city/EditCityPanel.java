package uz.hbs.actions.admin.addresses.city;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.City;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class EditCityPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(EditCityPanel.class);

	public EditCityPanel(String id, IBreadCrumbModel breadCrumbModel, City model) {
		super(id, breadCrumbModel);
		add(new CityForm("form", new CompoundPropertyModel<City>(model)));
	}

	private class CityForm extends Form<City> {
		private static final long serialVersionUID = 1L;

		public CityForm(String id, IModel<City> model) {
			super(id, model);

			final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			feedback.setEscapeModelStrings(false);
			add(feedback);

			final City city = model.getObject();

			add(new Label("country", city.getCountry_name()));
			
			add(new Label("region", city.getRegion_name()));

			add(new Label("labelName", new StringResourceModel("address.city", null, new Object[] { " (Ru)" })));
			
			RequiredTextField<String> name = new RequiredTextField<String>("name");
			name.setLabel(new StringResourceModel("address.city", null, new Object[] { " (Ru)" }));
			add(name);

			add(new Label("labelNameUz", new StringResourceModel("address.city", null, new Object[] { " (Uz)" })));

			RequiredTextField<String> nameUz = new RequiredTextField<String>("name_uz");
			nameUz.setLabel(new StringResourceModel("address.city", null, new Object[] { " (Uz)" }));
			add(nameUz);

			add(new Label("labelNameEn", new StringResourceModel("address.city", null, new Object[] { " (En)" })));

			RequiredTextField<String> nameEn = new RequiredTextField<String>("name_en");
			nameEn.setLabel(new StringResourceModel("address.city", null, new Object[] { " (En)" }));
			add(nameEn);

			IndicatingAjaxButton ajaxButton = new IndicatingAjaxButton("submit") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					int result = 0;
					City modelObject = (City) form.getModelObject();
					try {
						result = new MyBatisHelper().update("updateCity", modelObject);
					} catch (Exception e) {
						logger.error("Exception", e);
					}
					
					if (result > 0) {
						feedback.success(new StringResourceModel("address.city.edit.success", null, new Object[] { modelObject.getName() }).getString());
						logger.debug("City {} successfully edited", modelObject.getName());
					} else {
						feedback.error(new StringResourceModel("address.city.edit.unsuccess", null, new Object[] { modelObject.getName() }).getString());
						logger.error("Error, City {} is not edited", modelObject.getName());
					}
					
					target.add(feedback);
				}
			};
			add(ajaxButton);
		}

	}

	@Override
	public Class<?> implementedClass() {
		return getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("address.city.edit", null);
	}
}
