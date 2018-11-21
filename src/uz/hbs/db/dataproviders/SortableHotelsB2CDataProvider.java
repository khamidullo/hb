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
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.Hotel;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;

public class SortableHotelsB2CDataProvider extends SortableDataProvider<Hotel, String> implements IFilterStateLocator<Hotel> {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SortableHotelsB2CDataProvider.class);
	private Hotel filter;

	public SortableHotelsB2CDataProvider() {
		setSort("u.name", SortOrder.DESCENDING);
	}

	@Override
	public Iterator<? extends Hotel> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<Hotel> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("users_id", filter.getUsers_id());
			param.put("name", filter.getName());
			param.put("city", filter.getCity());
			param.put("display_name", filter.getDisplay_name());
			param.put("status", filter.getHotelstatus());
			param.put("individual_price", filter.getB2c_individual_price());
			param.put("group_price", filter.getB2c_group_price());
			if (filter.getB2c_status() != null) {
				if (filter.getB2c_status().getId() == User.STATUS_ACTIVE) {
					param.put("is_enabled", true);
				} else {
					param.put("is_disabled", false);
				}
			}
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectHotelsB2CList", param);
			if (list != null)
				return list.iterator();
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return null;
	}

	@Override
	public long size() {
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("users_id", filter.getUsers_id());
			param.put("name", filter.getName());
			param.put("city", filter.getCity());
			param.put("display_name", filter.getDisplay_name());
			param.put("status", filter.getHotelstatus());
			param.put("individual_price", filter.getB2c_individual_price());
			param.put("group_price", filter.getB2c_group_price());
			if (filter.getB2c_status() != null) {
				if (filter.getB2c_status().getId() == User.STATUS_ACTIVE) {
					param.put("is_enabled", true);
				} else {
					param.put("is_disabled", false);
				}
			}

			Long count = (Long) new MyBatisHelper().selectOne("selectHotelsB2CCount", param);
			return count;
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return 0;
	}

	@Override
	public IModel<Hotel> model(Hotel object) {
		return Model.of(object);
	}

	@Override
	public Hotel getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(Hotel filter) {
		this.filter = filter;
	}
}
