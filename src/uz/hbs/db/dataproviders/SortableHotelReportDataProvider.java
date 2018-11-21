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

import uz.hbs.beans.reports.HotelReport;
import uz.hbs.db.MyBatisHelper;

public class SortableHotelReportDataProvider extends SortableDataProvider<HotelReport, String> {
	private static final long serialVersionUID = 1L;
	private static Logger _log = LoggerFactory.getLogger(SortableHotelReportDataProvider.class);
	
	public SortableHotelReportDataProvider() {
		setSort("hotel", SortOrder.ASCENDING);
	}

	@Override
	public Iterator<HotelReport> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<HotelReport> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectHotelReportList", param);
			if (list != null)
				return list.iterator();
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		
		return null;
	}

	@Override
	public IModel<HotelReport> model(final HotelReport report) {
		return new LoadableDetachableModel<HotelReport>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected HotelReport load() {
				return report;
			}
		};
	}

	@Override
	public long size() {
		try {
			Long count = (Long) new MyBatisHelper().selectOne("selectHotelReportListCount");
			return count;
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return 0;
	}

}
