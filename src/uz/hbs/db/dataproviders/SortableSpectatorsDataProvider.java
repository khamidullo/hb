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

import uz.hbs.beans.Spectator;
import uz.hbs.db.MyBatisHelper;

public class SortableSpectatorsDataProvider extends SortableDataProvider<Spectator, String> implements IFilterStateLocator<Spectator> {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SortableSpectatorsDataProvider.class);
	private Spectator filter;

	public SortableSpectatorsDataProvider() {
		setSort("u.name", SortOrder.DESCENDING);
	}

	@Override
	public Iterator<? extends Spectator> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<Spectator> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("users_id", filter.getUsers_id());
			param.put("name", filter.getName());
			param.put("contact_number", filter.getContact_number());
			param.put("contact_email", filter.getContact_email());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectSpectatorsList", param);
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
			param.put("last_name", filter.getLast_name());
			param.put("contact_number", filter.getContact_number());
			param.put("contact_email", filter.getContact_email());
			Long count = (Long) new MyBatisHelper().selectOne("selectSpectatorsCount", param);
			return count;
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return 0;
	}

	@Override
	public IModel<Spectator> model(Spectator object) {
		return Model.of(object);
	}

	@Override
	public Spectator getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(Spectator filter) {
		this.filter = filter;
	}
}
