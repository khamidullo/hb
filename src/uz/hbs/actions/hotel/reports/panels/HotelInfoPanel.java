package uz.hbs.actions.hotel.reports.panels;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.time.Duration;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.RoomType;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.FormatUtil;

public class HotelInfoPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public HotelInfoPanel(String id, final Hotel hotel) {
		super(id);
		add(new Label("operday", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return FormatUtil.toString(hotel.getOperday(), MyWebApplication.DATE_FORMAT);
			}
		}));
		add(new ListView<RoomType>("list", new LoadableDetachableModel<List<RoomType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<RoomType> load() {
				return new MyBatisHelper().selectList("selectRoomTypesByHotel", hotel.getUsers_id());
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<RoomType> item) {
				final RoomType roomtype = (RoomType) item.getDefaultModelObject();
				item.add(new Label("roomtypeName", roomtype.getName()));
				item.add(new Label("numberOfRooms", FormatUtil.toString(roomtype.getNumber_of_rooms())));
				Label availabletoom;
				item.add(availabletoom = new Label("availableroom", new LoadableDetachableModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected String load() {
						HashMap<String, Serializable> param = new HashMap<String, Serializable>();
						param.put("date", hotel.getOperday());
						param.put("roomtype_id", roomtype.getId());
						param.put("type", 1);//-1
						return new MyBatisHelper().selectOne("selectCountAvailableRoomsForReport", param);
					}
				}));
				availabletoom.add(new AjaxSelfUpdatingTimerBehavior(Duration.ONE_MINUTE));
			}
		});
	}
}
