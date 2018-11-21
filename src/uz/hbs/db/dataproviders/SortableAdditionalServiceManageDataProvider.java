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

import uz.hbs.beans.AdditionalServiceManage;
import uz.hbs.beans.filters.AdditionalServiceManageFilter;
import uz.hbs.db.MyBatisHelper;

public class SortableAdditionalServiceManageDataProvider extends SortableDataProvider<AdditionalServiceManage, String> implements IFilterStateLocator<AdditionalServiceManageFilter> {
	private static final long serialVersionUID = 1L;
	private static Logger _log = LoggerFactory.getLogger(SortableAdditionalServiceManageDataProvider.class);
	private AdditionalServiceManageFilter filter; 
	
	public SortableAdditionalServiceManageDataProvider() {
		setSort("id", SortOrder.DESCENDING);
	}

	@Override
	public Iterator<? extends AdditionalServiceManage> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<AdditionalServiceManage> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("datefrom", filter.getDatefrom());
			param.put("dateto", filter.getDateto());
			param.put("fullname", filter.getFullname());
			param.put("id", filter.getId());
			param.put("service_type", filter.getService_type());
			param.put("touragent", filter.getTouragent());
			param.put("create_date", filter.getCreate_date());
			param.put("arrival_date", filter.getArrival_date());
			param.put("guest", filter.getGuest());
			param.put("total", filter.getTotal());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");
			list = new MyBatisHelper().selectList("selectAdditionalServiceManageList", param);
			if (list != null)
				return list.iterator();
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		
		return null;
	}

	@Override
	public IModel<AdditionalServiceManage> model(final AdditionalServiceManage manage) {
		return new LoadableDetachableModel<AdditionalServiceManage>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected AdditionalServiceManage load() {
				return manage;
			}
		};
	}

	@Override
	public long size() {
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("datefrom", filter.getDatefrom());
			param.put("dateto", filter.getDateto());
			param.put("fullname", filter.getFullname());
			param.put("id", filter.getId());
			param.put("service_type", filter.getService_type());
			param.put("touragent", filter.getTouragent());
			param.put("create_date", filter.getCreate_date());
			param.put("arrival_date", filter.getArrival_date());
			param.put("guest", filter.getGuest());
			param.put("total", filter.getTotal());
			Long count = (Long) new MyBatisHelper().selectOne("selectAdditionalServiceManageListCount", param);
			return count;
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return 0;
	}

	@Override
	public AdditionalServiceManageFilter getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(AdditionalServiceManageFilter filter) {
		this.filter = filter;
	}
}
