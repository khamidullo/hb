package uz.hbs.components.panels.address;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Address;
import uz.hbs.utils.HeaderItemUtil;

public class ViewAddressPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private boolean hasMap;

	public ViewAddressPanel(String id, final Address addresses, final boolean hasMap) {
		super(id, new CompoundPropertyModel<Address>(addresses));
		this.hasMap = hasMap;
		
		Label countries = new Label("countries", addresses.getCountries().getName());
		add(countries);

		Label regions = new Label("regions", addresses.getRegions() != null ? addresses.getRegions().getName() : "");
		add(regions);

		Label city = new Label("cities", addresses.getCities() != null ? addresses.getCities().getName() : "");
		add(city);

		Label address = new Label("address");
		add(address);

		WebMarkupContainer mapContainer = new WebMarkupContainer("mapContainer") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return hasMap;
			}
		};

		NumberTextField<Float> latitude = new NumberTextField<Float>("latitude") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return hasMap;
			}

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("id", "latitude");
			}
		};
		latitude.setLabel(new StringResourceModel("latitude", null));
		mapContainer.add(latitude);

		NumberTextField<Float> longitude = new NumberTextField<Float>("longitude") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return hasMap;
			}

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("id", "longitude");
			}
		};
		longitude.setLabel(new StringResourceModel("longitude", null));
		mapContainer.add(longitude);

		add(mapContainer);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		if (hasMap) {
			HeaderItemUtil.setGoogleMapHeaderItem(response, false);
		}
		super.renderHead(response);
	}

	public boolean isHasMap() {
		return hasMap;
	}

	public void setHasMap(boolean hasMap) {
		this.hasMap = hasMap;
	}
}
