package uz.hbs.db.dataproviders;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.Room;
import uz.hbs.beans.filters.RoomFilter;
import uz.hbs.db.MyBatisHelper;

public class SortableRoomDataProvider extends SortableDataProvider<Room, String> implements IFilterStateLocator<RoomFilter>  {
	private static final long serialVersionUID = 1L;
	private static Logger _log = LoggerFactory.getLogger(SortableRoomDataProvider.class);
	private RoomFilter filter;
	
	public SortableRoomDataProvider() {
		setSort("room_number", SortOrder.ASCENDING);
	}

	@Override
	public Iterator<Room> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<Room> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("room_number", filter.getRoom_number());
			param.put("clean_state", filter.getClean_state());
			param.put("onsale_state", filter.getOnsale_state());
			param.put("room_state", filter.getRoom_state());
			param.put("hotel_id", filter.getHotelsusers_id());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectRoomManagementList", param);
			if (list != null)
				return list.iterator();
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return null;
	}

	@Override
	public IModel<Room> model(final Room room) {
		return new LoadableDetachableModel<Room>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Room load() {
				return room;
			}
		};
	}

	@Override
	public long size() {
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("room_number", filter.getRoom_number());
			param.put("clean_state", filter.getClean_state());
			param.put("onsale_state", filter.getOnsale_state());
			param.put("room_state", filter.getRoom_state());
			param.put("hotel_id", filter.getHotelsusers_id());
			Long count = (Long) new MyBatisHelper().selectOne("selectRoomManagementListCount", param);
			return count;
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return 0;
	}

	@Override
	public RoomFilter getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(RoomFilter filter) {
		this.filter = filter;
	}
}
