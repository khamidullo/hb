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

import uz.hbs.beans.AdditionalServiceDetail;
import uz.hbs.beans.filters.AdditionalServiceDetailFilter;
import uz.hbs.db.MyBatisHelper;

public class SortableAdditionalServiceDataProvider extends SortableDataProvider<AdditionalServiceDetail, String> implements IFilterStateLocator<AdditionalServiceDetailFilter> {
	private static final long serialVersionUID = 1L;
	private static Logger _log = LoggerFactory.getLogger(SortableAdditionalServiceDataProvider.class);
	private AdditionalServiceDetailFilter filter;
	
	public SortableAdditionalServiceDataProvider() {
		setSort("additionalserviceorders_id, create_date", SortOrder.ASCENDING);
	}

	@Override
	public Iterator<AdditionalServiceDetail> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<AdditionalServiceDetail> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("creator_user_id", filter.getCreator_user_id());
			param.put("additionalserviceorders_id", filter.getAdditionalserviceorders_id());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");
			list = new MyBatisHelper().selectList("selectAdditionalServiceDetailList", param);
			if (list != null)
				return list.iterator();
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		
		return null;
	}

	@Override
	public long size() {
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("creator_user_id", filter.getCreator_user_id());
			param.put("additionalserviceorders_id", filter.getAdditionalserviceorders_id());
			Long count = (Long) new MyBatisHelper().selectOne("selectAdditionalServiceDetailListCount", param);
			return count;
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return 0;
	}

	@Override
	public IModel<AdditionalServiceDetail> model(final AdditionalServiceDetail object) {
		return new LoadableDetachableModel<AdditionalServiceDetail>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected AdditionalServiceDetail load() {
				return object;
			}
		};
	}

	@Override
	public AdditionalServiceDetailFilter getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(AdditionalServiceDetailFilter filter) {
		this.filter = filter;
	}

}
