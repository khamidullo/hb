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

import uz.hbs.beans.Commission;
import uz.hbs.db.MyBatisHelper;

public class SortableTouragentCommissionsDataProvider extends SortableDataProvider<Commission, String> implements IFilterStateLocator<Commission> {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SortableTouragentCommissionsDataProvider.class);
	private Commission filter;

	public SortableTouragentCommissionsDataProvider() {
		setSort("c.hotel_name", SortOrder.DESCENDING);
	}

	@Override
	public Iterator<? extends Commission> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<Commission> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("creator_user_id", filter.getTouragent_id());
			param.put("display_name", filter.getHotel_name());
			param.put("date_from", filter.getFromDate());
			param.put("date_to", filter.getToDate());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectTACommissionsList", param);
			
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
			param.put("creator_user_id", filter.getTouragent_id());
			param.put("display_name", filter.getHotel_name());
			param.put("date_from", filter.getFromDate());
			param.put("date_to", filter.getToDate());
		
			Long count = (Long) new MyBatisHelper().selectOne("selectTACommissionsCount", param);

			return count;
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return 0;
	}

	@Override
	public IModel<Commission> model(Commission object) {
		return Model.of(object);
	}

	@Override
	public Commission getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(Commission filter) {
		this.filter = filter;
	}
}
