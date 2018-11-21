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

import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.filters.ReservationFilter;
import uz.hbs.db.MyBatisHelper;

public class SortableGroupReservationDataProvider extends SortableDataProvider<ReservationDetail, String> implements IFilterStateLocator<ReservationFilter> {
	private static final long serialVersionUID = 1L;
	private static Logger _log = LoggerFactory.getLogger(SortableGroupReservationDataProvider.class);
	private ReservationFilter filter;
	
	public SortableGroupReservationDataProvider() {
		setSort("first_name, last_name", SortOrder.ASCENDING);
	}

	@Override
	public Iterator<ReservationDetail> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<ReservationDetail> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("hotelsusers_id", filter.getHotelsusers_id());
			param.put("group_name", filter.getGroup_name());
			param.put("last_name", filter.getLast_name());
			param.put("first_name", filter.getFirst_name());
			param.put("check_in", filter.getCheck_in());
			param.put("check_out", filter.getCheck_out());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");
			param.put("is_group", ReservationDetail.GROUP);
			list = new MyBatisHelper().selectList("selectRegisteredGuestList", param);
			if (list != null)
				return list.iterator();
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return null;
	}

	@Override
	public IModel<ReservationDetail> model(final ReservationDetail reserv) {
		return new LoadableDetachableModel<ReservationDetail>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected ReservationDetail load() {
				return reserv;
			}
		};
	}

	@Override
	public long size() {
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("hotelsusers_id", filter.getHotelsusers_id());
			param.put("group_name", filter.getGroup_name());
			param.put("last_name", filter.getLast_name());
			param.put("first_name", filter.getFirst_name());
			param.put("check_in", filter.getCheck_in());
			param.put("check_out", filter.getCheck_out());
			param.put("is_group", ReservationDetail.GROUP);
			Long count = (Long) new MyBatisHelper().selectOne("selectRegisteredGuestListCount", param);
			return count;
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return 0;
	}

	@Override
	public ReservationFilter getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(ReservationFilter filter) {
		this.filter = filter;
	}
}
