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

import uz.hbs.beans.Region;
import uz.hbs.db.MyBatisHelper;

public class SortableRegionDataProvider  extends SortableDataProvider<Region, String> implements IFilterStateLocator<Region> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(SortableRegionDataProvider.class);
	private Region filter;

	public SortableRegionDataProvider() {
		setSort("c.name", SortOrder.ASCENDING);
	}

	@Override
	public Iterator<? extends Region> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<Region> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("country_name", filter.getCountry_name());
			param.put("id", filter.getId());
			param.put("name", filter.getName());
			param.put("name_uz", filter.getName_uz());
			param.put("name_en", filter.getName_en());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectCountryRegionList", param);
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
			param.put("country_name", filter.getCountry_name());
			param.put("id", filter.getId());
			param.put("name", filter.getName());
			param.put("name_uz", filter.getName_uz());
			param.put("name_en", filter.getName_en());

			Long count = (Long) new MyBatisHelper().selectOne("selectCountryRegionCount", param);
			return count;
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return 0;
	}

	@Override
	public IModel<Region> model(Region object) {
		return Model.of(object);
	}

	@Override
	public Region getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(Region filter) {
		this.filter = filter;
	}
}
