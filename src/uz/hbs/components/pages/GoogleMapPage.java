package uz.hbs.components.pages;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;

import uz.hbs.beans.Address;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.HeaderItemUtil;

public class GoogleMapPage extends WebPage {
	private static final long serialVersionUID = 1L;
	private final Address address;

	public GoogleMapPage(Address address) {
		if (address == null)
			address = new Address();

		this.address = address;

		WebMarkupContainer mapContainer = new WebMarkupContainer("mapContainer");

		add(mapContainer);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		HeaderItemUtil.setGoogleMapHeaderReadOnlyItem(response, "map-canvas2", false, CommonUtil.nvl(address.getLatitude()),
				CommonUtil.nvl(address.getLongitude()));
		super.renderHead(response);
	}
}
