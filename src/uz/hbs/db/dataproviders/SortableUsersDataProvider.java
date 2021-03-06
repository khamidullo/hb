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

import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;

public class SortableUsersDataProvider extends SortableDataProvider<User, String> implements IFilterStateLocator<User> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(SortableUsersDataProvider.class);
	private User filter;

	public SortableUsersDataProvider() {
		setSort("u.name", SortOrder.DESCENDING);
	}

	@Override
	public Iterator<? extends User> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<User> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("id", filter.getId());
			param.put("login", filter.getLogin());
			param.put("name", filter.getName());
			param.put("org_name", filter.getOrg_name());
			param.put("type", filter.getType());
			param.put("status", filter.getStatus());
			if (filter.getWork() != null)
				param.put("workusers_id", filter.getWork().getId());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectUsersList", param);
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
			param.put("id", filter.getId());
			param.put("login", filter.getLogin());
			param.put("name", filter.getName());
			param.put("org_name", filter.getOrg_name());
			param.put("type", filter.getType());
			param.put("status", filter.getStatus());
			if (filter.getWork() != null)
				param.put("workusers_id", filter.getWork().getId());
			Long count = (Long) new MyBatisHelper().selectOne("selectUsersCount", param);
			return count;
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return 0;
	}

	@Override
	public IModel<User> model(User object) {
		return Model.of(object);
	}

	@Override
	public User getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(User filter) {
		this.filter = filter;
	}
}
