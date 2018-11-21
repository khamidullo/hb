package uz.hbs.actions.touragent.newbooking.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.BookingSearchResult;
import uz.hbs.beans.HotelNearByPlace;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.models.HotelModels;

public class NearbyPlacesPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public NearbyPlacesPanel(String id, BookingSearchResult model) {
		super(id);

		RepeatingView repeatingView = new RepeatingView("repeater");

		List<HotelNearByPlace> nearbyPlacesList = new MyBatisHelper().selectList("selectSearchHotelNearByPlacesByHotelId", model.getHotelsusers_id());
		for (HotelNearByPlace nearbyPlace : nearbyPlacesList) {
			repeatingView.add(new Label(repeatingView.newChildId(), HotelModels.getNearByPlacesType(nearbyPlace.getType()) + " - " + FormatUtil.format2(nearbyPlace.getValue()) + " " + new StringResourceModel("touragents.newbooking.near_by_places.unit", null).getString()));
		}
		add(repeatingView);
	}
}