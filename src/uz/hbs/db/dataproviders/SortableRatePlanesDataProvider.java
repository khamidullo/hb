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

import uz.hbs.beans.rate.RatePlane;
import uz.hbs.beans.rate.filters.RatePlanesFilter;
import uz.hbs.db.MyBatisHelper;

public class SortableRatePlanesDataProvider extends SortableDataProvider<RatePlane, String> implements IFilterStateLocator<RatePlanesFilter> {
	private static final long serialVersionUID = 1L;
	private static Logger _log = LoggerFactory.getLogger(SortableRatePlanesDataProvider.class);
	private RatePlanesFilter filter;
	
	public SortableRatePlanesDataProvider() {
		setSort("name", SortOrder.ASCENDING);
	}

	@Override
	public Iterator<RatePlane> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<RatePlane> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("name", filter.getName());
			param.put("description", filter.getDescription());
			param.put("hotelsusers_id", filter.getHotelsusers_id());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectRatePlaneList", param);
			if (list != null)
				return list.iterator();
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return null;
	}

	@Override
	public IModel<RatePlane> model(final RatePlane plane) {
		return new LoadableDetachableModel<RatePlane>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected RatePlane load() {
				return plane;
			}
		};
	}

	@Override
	public long size() {
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("name", filter.getName());
			param.put("description", filter.getDescription());
			param.put("hotelsusers_id", filter.getHotelsusers_id());
			Long count = (Long) new MyBatisHelper().selectOne("selectRatePlaneListCount", param);
			return count;
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return 0;
	}

	@Override
	public RatePlanesFilter getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(RatePlanesFilter filter) {
		this.filter = filter;
	}

}
