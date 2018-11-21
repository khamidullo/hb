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

import uz.hbs.beans.ReserveManage;
import uz.hbs.beans.filters.ReserveManageFilter;
import uz.hbs.db.MyBatisHelper;

public class SortableReserveManageDataProvider extends SortableDataProvider<ReserveManage, String> implements IFilterStateLocator<ReserveManageFilter>{
	private static final long serialVersionUID = 1L;
	private static Logger _log = LoggerFactory.getLogger(SortableReserveManageDataProvider.class);
	private ReserveManageFilter filter;
	
	
	public SortableReserveManageDataProvider() {
		setSort("id", SortOrder.ASCENDING);
	}

	@Override
	public Iterator<? extends ReserveManage> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<ReserveManage> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("datefrom", filter.getDatefrom());
			param.put("dateto", filter.getDateto());
			param.put("fullname", filter.getFullname());
			param.put("id", filter.getId());
			param.put("touragent", filter.getTouragent());
			param.put("hotel", filter.getHotel());
			param.put("create_date", filter.getCreate_date());
			param.put("check_in", filter.getCheck_in());
			param.put("check_out", filter.getCheck_out());
			param.put("type", filter.getType());
			param.put("status", filter.getStatus());
			param.put("total", filter.getTotal());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");
			list = new MyBatisHelper().selectList("", param);
			if (list != null)
				return list.iterator();
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		
		
		return null;
	}

	@Override
	public IModel<ReserveManage> model(final ReserveManage manage) {
		return new LoadableDetachableModel<ReserveManage>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected ReserveManage load() {
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
			param.put("touragent", filter.getTouragent());
			param.put("hotel", filter.getHotel());
			param.put("create_date", filter.getCreate_date());
			param.put("check_in", filter.getCheck_in());
			param.put("check_out", filter.getCheck_out());
			param.put("type", filter.getType());
			param.put("status", filter.getStatus());
			param.put("total", filter.getTotal());
			Long count = (Long) new MyBatisHelper().selectOne("", param);
			return count;
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return 0;
	}

	@Override
	public ReserveManageFilter getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(ReserveManageFilter filter) {
		this.filter = filter;
	}
}
