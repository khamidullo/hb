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

import uz.hbs.beans.Insurance;
import uz.hbs.beans.filters.InsuranceFilter;
import uz.hbs.db.MyBatisHelper;

public class SortableInsuranceDataProvider extends SortableDataProvider<Insurance, String> implements IFilterStateLocator<InsuranceFilter> {
	private static final long serialVersionUID = 1L;
	private static Logger _log = LoggerFactory.getLogger(SortableInsuranceDataProvider.class);
	private InsuranceFilter filter;
	
	public SortableInsuranceDataProvider() {
		setSort("first_name, last_name", SortOrder.ASCENDING);
	}
	

	@Override
	public Iterator<? extends Insurance> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<Insurance> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("creator_user_id", filter.getCreator_user_id());
			param.put("additionalserviceorders_id", filter.getAdditionalserviceorders_id());
			param.put("last_name", filter.getLast_name());
			param.put("first_name", filter.getFirst_name());
			param.put("passport_number", filter.getPassport_number());
			param.put("nationality", filter.getNationality());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectInsuranceList", param);
			if (list != null)
				return list.iterator();
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return null;
	}

	@Override
	public IModel<Insurance> model(final Insurance insurance) {
		return new LoadableDetachableModel<Insurance>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Insurance load() {
				return insurance;
			}
		};
	}

	@Override
	public long size() {
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("creator_user_id", filter.getCreator_user_id());
			param.put("additionalserviceorders_id", filter.getAdditionalserviceorders_id());
			param.put("last_name", filter.getLast_name());
			param.put("first_name", filter.getFirst_name());
			param.put("passport_number", filter.getPassport_number());
			param.put("nationality", filter.getNationality());
			Long count = (Long) new MyBatisHelper().selectOne("selectInsuranceListCount", param);
			return count;
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return 0;
	}

	@Override
	public InsuranceFilter getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(InsuranceFilter filter) {
		this.filter = filter;
	}

}
