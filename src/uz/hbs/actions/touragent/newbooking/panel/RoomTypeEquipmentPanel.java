package uz.hbs.actions.touragent.newbooking.panel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;

import uz.hbs.beans.Equipment;
import uz.hbs.db.MyBatisHelper;

public class RoomTypeEquipmentPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public RoomTypeEquipmentPanel(String id, Long hotelsusers_id, Integer roomtypes_id, boolean isFromFilterForm) {
		super(id);
		
		LoadableDetachableModel<List<Equipment>> listModel = new LoadableDetachableModel<List<Equipment>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Equipment> load() {
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("hotelsusers_id", hotelsusers_id);
				params.put("roomtypes_id", roomtypes_id);
				if (isFromFilterForm)
					params.put("filter", true);
				return new MyBatisHelper().selectList("selectEquipmentListByRoomTypeHotel", params);

			}
		};
		
		ListView<Equipment> list = new ListView<Equipment>("list", listModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Equipment> item) {
				item.add(new Label("name", item.getModelObject().getName()));
			}
		};

		add(list);
	}
}