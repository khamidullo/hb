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

import uz.hbs.beans.GeneratedReport;
import uz.hbs.db.MyBatisHelper;

public class SortableGeneratedReportDataProvider extends SortableDataProvider<GeneratedReport, String>
		implements IFilterStateLocator<GeneratedReport> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(SortableGeneratedReportDataProvider.class);
	private GeneratedReport filter;

	public SortableGeneratedReportDataProvider() {
		setSort("rt.create_date", SortOrder.DESCENDING);
	}

	@Override
	public Iterator<? extends GeneratedReport> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<GeneratedReport> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectGeneratedReportList", param);
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
			Long count = (Long) new MyBatisHelper().selectOne("selectGeneratedReportCount", param);
			return count;
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return 0;
	}

	@Override
	public IModel<GeneratedReport> model(GeneratedReport object) {
		return Model.of(object);
	}

	@Override
	public GeneratedReport getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(GeneratedReport filter) {
		this.filter = filter;
	}
}