package uz.hbs.actions.hotel.roomsandrates.rate.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.rate.RateDetails;
import uz.hbs.beans.rate.SalePlane;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.models.HotelModels;

public class ViewSaleDialogPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	public ViewSaleDialogPanel(String id, final SalePlane plane) {
		super(id);
		
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("hotel_id", plane.getHotelsusers_id());
		param.put("roomtype_id", plane.getRoomtypes_id());
				
		final short max_person = new MyBatisHelper().selectOne("selectHoldingCapacityRoomType", plane.getRoomtypes_id());
		
		final HashMap<Short,List<SalePlane>> salemap = getSaleMap(plane.getHotelsusers_id(), plane.getRoomtypes_id(), max_person, plane.getSale_date());
		
		add(new Label("roomtype", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new MyBatisHelper().selectOne("selectRoomTypeNameById", plane.getRoomtypes_id());
			}
		}));
		
		add(new ListView<Short>("personlist", HotelModels.getShortListModel((short) 1, max_person)) {
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
					protected void populateItem(final ListItem<SalePlane> childItem) {
						childItem.add(new Label("sale", new PropertyModel<BigDecimal>(childItem.getModel(), "sale")));
						childItem.add(new Label("sale_uz", new PropertyModel<BigDecimal>(childItem.getModel(), "sale_uz")));
						childItem.add(new Label("status", new StringResourceModel("status." + childItem.getModel().getObject().isStatus(), null)));
					}
				}.setReuseItems(true));
			}
		}.setReuseItems(true));
	}
	
	private HashMap<Short,List<SalePlane>> getSaleMap(long hotel_id, int roomtype_id, short max_person, Date sale_date){
		HashMap<Short,List<SalePlane>> salemap = new HashMap<Short,List<SalePlane>>();
		for (short person = 1; person <= max_person; person ++){
			List<SalePlane> list = new ArrayList<SalePlane>();
			list.add((SalePlane) new MyBatisHelper().selectOne("selectSalePlane", new SalePlane(hotel_id, roomtype_id, person, sale_date, RateDetails.INDIVIDUAL)));
			list.add((SalePlane) new MyBatisHelper().selectOne("selectSalePlane", new SalePlane(hotel_id, roomtype_id, person, sale_date, RateDetails.GROUP)));
			salemap.put(person, list);
		}
		return salemap;
	}
}
