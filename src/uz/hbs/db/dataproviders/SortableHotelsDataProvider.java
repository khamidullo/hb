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
import uz.hbs.db.MyBatisHelper;

public class SortableHotelsDataProvider extends SortableDataProvider<Hotel, String> implements IFilterStateLocator<Hotel> {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SortableHotelsDataProvider.class);
	private Hotel filter;

	public SortableHotelsDataProvider() {
		setSort("u.name", SortOrder.DESCENDING);
	}

	@Override
	public Iterator<? extends Hotel> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<Hotel> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("users_id", filter.getUsers_id());
			param.put("countries", filter.getCountries());
			param.put("name", filter.getName());
			param.put("city", filter.getCity());
			param.put("display_name", filter.getDisplay_name());
			param.put("primary_phone", filter.getPrimary_phone());
			param.put("corporate_email", filter.getCorporate_email());
			param.put("status", filter.getHotelstatus());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectHotelsList", param);
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
			param.put("countries", filter.getCountries());
			param.put("name", filter.getName());
			param.put("city", filter.getCity());
			param.put("display_name", filter.getDisplay_name());
			param.put("status", filter.getHotelstatus());
			param.put("primary_phone", filter.getPrimary_phone());
			param.put("corporate_email", filter.getCorporate_email());
			Long count = (Long) new MyBatisHelper().selectOne("selectHotelsCount", param);
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
