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

import uz.hbs.beans.AdditionalServiceOrder;
import uz.hbs.beans.filters.AdditionalServiceOrderFilter;
import uz.hbs.db.MyBatisHelper;

public class SortableAdditionalServiceOrderDataProvider extends SortableDataProvider<AdditionalServiceOrder, String> implements IFilterStateLocator<AdditionalServiceOrderFilter> {
	private static final long serialVersionUID = 1L;
	private static Logger _log = LoggerFactory.getLogger(SortableAdditionalServiceOrderDataProvider.class);
	private AdditionalServiceOrderFilter filter;
	
	public SortableAdditionalServiceOrderDataProvider() {
		setSort("a.create_date", SortOrder.DESCENDING);
	}

	@Override
	public Iterator<AdditionalServiceOrder> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<AdditionalServiceOrder> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("creator_user_id", filter.getCreator_user_id());
			param.put("touragent_id", filter.getTouragent_id());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("id", filter.getId());
			param.put("reservations_id", filter.getReservations_id());
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");
			list = new MyBatisHelper().selectList("selectAdditionalServiceOrderList", param);
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
			param.put("touragent_id", filter.getTouragent_id());
			param.put("id", filter.getId());
			param.put("reservations_id", filter.getReservations_id());
			Long count = (Long) new MyBatisHelper().selectOne("selectAdditionalServiceOrderListCount", param);
			return count;
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return 0;
	}

	@Override
	public IModel<AdditionalServiceOrder> model(final AdditionalServiceOrder order) {
		return new LoadableDetachableModel<AdditionalServiceOrder>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected AdditionalServiceOrder load() {
				return order;
			}
		};
	}

	@Override
	public AdditionalServiceOrderFilter getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(AdditionalServiceOrderFilter filter) {
		this.filter = filter;
	}
}
