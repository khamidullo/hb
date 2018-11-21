package uz.hbs.components.panels.address;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.PageCreator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Address;
import uz.hbs.beans.City;
import uz.hbs.beans.Country;
import uz.hbs.beans.Region;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.models.HotelModels;

public class AddressPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private ModalWindow dialog;
	private static HashMap<String, Double> point = new HashMap<String, Double>();

	public AddressPanel(String id, final Address address) {
		super(id, new CompoundPropertyModel<Address>(address));
		add(dialog = new ModalWindow("dialog"));
		dialog.setInitialWidth(1100);
		dialog.setInitialHeight(600);
		dialog.setMinimalWidth(900);
		dialog.setMinimalHeight(500);
		dialog.setAutoSize(true);
		
		LoadableDetachableModel<List<? extends Country>> countriesList = HotelModels.getCountriesList();

		final DropDownChoice<Country> countries = new DropDownChoice<Country>("countries", countriesList, new ChoiceRenderer<Country>("name", "id"));
		countries.setLabel(new StringResourceModel("hotels.details.country", null));
		countries.setRequired(true);
		add(countries);

		LoadableDetachableModel<List<? extends Region>> regionsList = new LoadableDetachableModel<List<? extends Region>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Region> load() {
				Country country = address.getCountries();
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("countries_id", country != null ? country.getId() : -1);
				params.put("sortField", "r.name");
				params.put("sortOrder", "ASC");
				return new MyBatisHelper().selectList("selectRegionsList", params);
			}
		};
		
		final DropDownChoice<Region> regions = new DropDownChoice<Region>("regions", regionsList, new ChoiceRenderer<Region>("name", "id"));
		regions.setLabel(new StringResourceModel("hotels.details.region", null));
		regions.setRequired(true);
		regions.setOutputMarkupId(true);
		add(regions);

		LoadableDetachableModel<List<? extends City>> citiesList = new LoadableDetachableModel<List<? extends City>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends City> load() {
				Region region = address.getRegions();
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("regions_id", region != null ? region.getId() : -1);
				params.put("sortField", "ct.name");
				params.put("sortOrder", "ASC");

				return new MyBatisHelper().selectList("selectCitiesList", params);
			}
		};
		
		final DropDownChoice<City> cities = new DropDownChoice<City>("cities", citiesList, new ChoiceRenderer<City>("name", "id"));
		cities.setLabel(new StringResourceModel("hotels.details.city", null));
		cities.setRequired(true);
		cities.setOutputMarkupId(true);
		add(cities);
		
		countries.add(new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				address.setRegions(null);
				address.setCities(null);
				target.add(regions);
				target.add(cities);
			}
		});
		
		regions.add(new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				address.setCities(null);
				target.add(cities);
			}
		});

		TextField<String> postal_index = new TextField<String>("postal_index");
		postal_index.setLabel(new StringResourceModel("hotels.details.postal_index", null));
		add(postal_index);

		TextField<String> addressField = new TextField<String>("address");
		addressField.setLabel(Model.of(new StringResourceModel("hotels.details.address", null).getString() + "(" + new StringResourceModel("language.ru", null).getString() + ")"));
		addressField.add(new AttributeModifier("placeholder", Model.of(new StringResourceModel("hotels.details.address", null).getString() + "(" + new StringResourceModel("language.ru", null).getString() + ")")));
		addressField.setRequired(true);
		add(addressField);
		
		TextField<String> addressFieldEn = new TextField<String>("address_en");
		addressFieldEn.setLabel(Model.of(new StringResourceModel("hotels.details.address", null).getString() + "(" + new StringResourceModel("language.en", null).getString() + ")"));
		addressFieldEn.add(new AttributeModifier("placeholder", Model.of(new StringResourceModel("hotels.details.address", null).getString() + "(" + new StringResourceModel("language.en", null).getString() + ")")));
		addressFieldEn.setRequired(true);
		add(addressFieldEn);
		
		TextField<String> addressFieldUz = new TextField<String>("address_uz");
		addressFieldUz.setLabel(Model.of(new StringResourceModel("hotels.details.address", null).getString() + "(" + new StringResourceModel("language.uz", null).getString() + ")"));
		addressFieldUz.add(new AttributeModifier("placeholder", Model.of(new StringResourceModel("hotels.details.address", null).getString() + "(" + new StringResourceModel("language.uz", null).getString() + ")")));
		addressFieldUz.setRequired(true);
		add(addressFieldUz);
		
		final WebMarkupContainer longlat;
		add(longlat = new WebMarkupContainer("longlat"));
		longlat.setOutputMarkupId(true);
		
		final NumberTextField<Double> latitude;
		longlat.add(latitude = new NumberTextField<Double>("latitude"));
		latitude.setLabel(new StringResourceModel("latitude", null));
		latitude.setMarkupId("latitude");
		latitude.setOutputMarkupId(true);

		final NumberTextField<Double> longitude;
		longlat.add(longitude = new NumberTextField<Double>("longitude"));
		longitude.setLabel(new StringResourceModel("longitude", null));
		longitude.setMarkupId("longitude");
		longitude.setOutputMarkupId(true);
		
		add(new AjaxLink<Void>("indicate_google_map"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				dialog.setPageCreator(new PageCreator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Page createPage() {
						return new GoogleMapDialog(address){
							private static final long serialVersionUID = 1L;

							@Override
							protected void onIndicate(AjaxRequestTarget target, Form<?> form) {
								Address temp = (Address) form.getDefaultModelObject();
								point.put("latitude", temp.getLatitude());
								point.put("longitude", temp.getLongitude());
								dialog.close(target);
							}
						};
					}
				});
				dialog.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClose(AjaxRequestTarget target) {
						if (point.get("latitude") != null && point.get("longitude") != null) {
							address.setLatitude(point.get("latitude"));
							address.setLongitude(point.get("longitude"));
							target.appendJavaScript("$('#latitude').val(" + point.get("latitude") + "); $('#longitude').val(" + point.get("longitude") + ");");
						}	
					}
				});
				dialog.show(target);
			}
		});
	}
}
