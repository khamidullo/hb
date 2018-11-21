package uz.hbs.actions.admin.addresses.region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Country;
import uz.hbs.beans.Region;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class AddRegionPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public AddRegionPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		add(new AddForm("form", new CompoundPropertyModel<Region>(new Region())));
	}

	private class AddForm extends Form<Region> {
		private static final long serialVersionUID = 1L;

		public AddForm(String id, IModel<Region> model) {
			super(id, model);
			
			this.setOutputMarkupId(true);
			
			final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			feedback.setEscapeModelStrings(false);
			add(feedback);

			final ModalWindow dialog = new ModalWindow("dialog");
			dialog.setAutoSize(true);
			dialog.setMinimalHeight(400);
			dialog.setMinimalWidth(500);
			dialog.setCookieName("addRegion");
			dialog.setInitialHeight(400);
			dialog.setInitialWidth(500);
			add(dialog);

			final WebMarkupContainer container = new WebMarkupContainer("container");
			container.setOutputMarkupId(true);
			add(container);

			final Region region = model.getObject();

			container.add(new Label("labelName", new StringResourceModel("address.region", null, new Object[] { " (Ru)" })));

			RequiredTextField<String> name = new RequiredTextField<String>("name") {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled() {
					return region.getCountry() != null;
				}
			};
			name.setLabel(new StringResourceModel("address.region", null, new Object[] { " (Ru)" }));
			name.setEnabled(false);
			name.setOutputMarkupId(true);
			container.add(name);

			container.add(new Label("labelNameUz", new StringResourceModel("address.region", null, new Object[] { " (Uz)" })));

			RequiredTextField<String> nameUz = new RequiredTextField<String>("name_uz") {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled() {
					return region.getCountry() != null;
				}
			};
			nameUz.setLabel(new StringResourceModel("address.region", null, new Object[] { " (Uz)" }));
			nameUz.setEnabled(false);
			nameUz.setOutputMarkupId(true);
			container.add(nameUz);

			container.add(new Label("labelNameEn", new StringResourceModel("address.region", null, new Object[] { " (En)" })));

			RequiredTextField<String> nameEn = new RequiredTextField<String>("name_en") {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled() {
					return region.getCountry() != null;
				}
			};
			nameEn.setLabel(new StringResourceModel("address.region", null, new Object[] { " (En)" }));
			nameEn.setEnabled(false);
			nameEn.setOutputMarkupId(true);
			container.add(nameEn);

			IndicatingAjaxButton submit = new IndicatingAjaxButton("submit") {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled() {
					return region.getCountry() != null;
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					Region modelObject = (Region) form.getModelObject();
					int result = 0;

					try {
						result = new MyBatisHelper().insert("insertRegion", modelObject);
					} catch (Exception e) {
						logger.error("Exception", e);
					}

					if (result > 0) {
						feedback.success(
								new StringResourceModel("address.region.add.success", null, new Object[] { modelObject.getName() }).getString());
						logger.debug("Region {} successfully added", modelObject.getName());
					} else {
						feedback.error(
								new StringResourceModel("address.region.add.unsuccess", null, new Object[] { modelObject.getName() }).getString());
						logger.error("Error, Region {} is not added", modelObject.getName());
					}
					AddForm.this.setEnabled(false);
					target.add(AddForm.this);
					target.add(feedback);
				}
			};
			submit.setOutputMarkupId(true);
			container.add(submit);

			LoadableDetachableModel<List<Country>> countryListModel = new LoadableDetachableModel<List<Country>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Country> load() {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("sortField", "name");
					params.put("sortOrder", "ASC");
					return new MyBatisHelper().selectList("selectCountriesList", params);
				}
			};

			DropDownChoice<Country> country = new DropDownChoice<Country>("country", countryListModel, new ChoiceRenderer<Country>("name", "id"));
			country.add(new OnChangeAjaxBehavior() {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (model.getObject().getCountry() != null) {
						model.getObject().setCountries_id(region.getCountry().getId());
					} else {
						model.getObject().setCountries_id(null);
					}
					target.add(name);
					target.add(nameUz);
					target.add(nameEn);
					target.add(submit);
				}
			});
			container.add(country);
		}
	}

	@Override
	public Class<?> implementedClass() {
		return getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("address.region.add", null);
	}
}
