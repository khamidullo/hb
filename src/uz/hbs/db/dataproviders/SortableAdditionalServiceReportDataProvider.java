package uz.hbs.db.dataproviders;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.reports.AdditionalServiceReport;
import uz.hbs.db.MyBatisHelper;

public class SortableAdditionalServiceReportDataProvider extends SortableDataProvider<AdditionalServiceReport, String> {
	private static final long serialVersionUID = 1L;
	private static Logger _log = LoggerFactory.getLogger(SortableAdditionalServiceReportDataProvider.class);
	
	public SortableAdditionalServiceReportDataProvider() {
		setSort("name", SortOrder.ASCENDING);
	}

	@Override
    public Iterator<AdditionalServiceReport> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<AdditionalServiceReport> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectAdditionalServiceReportList", param);
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
			Long count = (Long) new MyBatisHelper().selectOne("selectAdditionalServiceReportListCount");
			return count;
		} catch (Exception e) {
			_log.error("Exception", e);
		}
        return 0;
    }

	@Override
	public IModel<AdditionalServiceReport> model(final AdditionalServiceReport report) {
		return new LoadableDetachableModel<AdditionalServiceReport>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected AdditionalServiceReport load() {
				return report;
			}
		};
	}
}
