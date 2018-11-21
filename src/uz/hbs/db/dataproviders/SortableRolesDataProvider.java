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

import uz.hbs.beans.Role;
import uz.hbs.db.MyBatisHelper;

public class SortableRolesDataProvider extends SortableDataProvider<Role, String> implements IFilterStateLocator<Role>  {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SortableRolesDataProvider.class);
	private Role filter;
	
	public SortableRolesDataProvider() {
		setSort("r.name", SortOrder.DESCENDING);
	}

	@Override
	public Iterator<? extends Role> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<Role> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("id", filter.getId());
			param.put("name", filter.getName());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectRolesList", param);
			if (list != null)
				return list.iterator();
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return null;
	}

	@Override
	public IModel<Role> model(final Role roles) {
		return new LoadableDetachableModel<Role>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Role load() {
				return roles;
			}
		};
	}

	@Override
	public long size() {
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("id", filter.getId());
			param.put("name", filter.getName());
			Long count = (Long) new MyBatisHelper().selectOne("selectRolesCount", param);
			return count;
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return 0;
	}

	@Override
	public Role getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(Role filter) {
		this.filter = filter;
	}

}
