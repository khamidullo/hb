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
import uz.hbs.beans.filters.AllReservationFilter;
import uz.hbs.db.MyBatisHelper;

public class SortableReportByReservationForTouragentDataProvider extends SortableDataProvider<ReservationDetail, String>
		implements IFilterStateLocator<AllReservationFilter> {
	private static final long serialVersionUID = 1L;
	private static Logger _log = LoggerFactory.getLogger(SortableReportByReservationForTouragentDataProvider.class);
	private AllReservationFilter filter;

	public SortableReportByReservationForTouragentDataProvider() {
		setSort("id", SortOrder.DESCENDING);
	}

	@Override
	public Iterator<ReservationDetail> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<ReservationDetail> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("group_name", filter.getGroup_name());
			param.put("first_name", filter.getFirst_name());
			param.put("last_name", filter.getLast_name());
			param.put("created_from", filter.getCreated_from());
			param.put("created_to", filter.getCreated_to());
			param.put("checkin_from", filter.getCheckin_from());
			param.put("checkin_to", filter.getCheckin_to());
			param.put("checkout_from", filter.getCheckout_from());
			param.put("checkout_to", filter.getCheckout_to());
			param.put("check_in", filter.getCheck_in());
			param.put("check_out", filter.getCheck_out());
			param.put("touragent", filter.getTourAgent());
			param.put("hotel", filter.getHotel());
			param.put("city", filter.getCity());
			param.put("reservation_type", filter.getReservation_type());
			param.put("reservation_status", filter.getReservation_status());
			param.put("reservation_id", filter.getReservations_id());
			param.put("user_type", filter.getUser_type());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectReservationsReportListForTouragent", param);

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
			param.put("group_name", filter.getGroup_name());
			param.put("first_name", filter.getFirst_name());
			param.put("last_name", filter.getLast_name());
			param.put("created_from", filter.getCreated_from());
			param.put("created_to", filter.getCreated_to());
			param.put("checkin_from", filter.getCheckin_from());
			param.put("checkin_to", filter.getCheckin_to());
			param.put("checkout_from", filter.getCheckout_from());
			param.put("checkout_to", filter.getCheckout_to());
			param.put("check_in", filter.getCheck_in());
			param.put("check_out", filter.getCheck_out());
			param.put("touragent", filter.getTourAgent());
			param.put("hotel", filter.getHotel());
			param.put("city", filter.getCity());
			param.put("reservation_type", filter.getReservation_type());
			param.put("reservation_status", filter.getReservation_status());
			param.put("reservation_id", filter.getReservations_id());
			param.put("user_type", filter.getUser_type());

			Long count = (Long) new MyBatisHelper().selectOne("selectReservationsReportCountForTouragent", param);

			return count;
		} catch (Exception e) {
			_log.error("Exception", e);
		}
		return 0;
	}

	@Override
	public IModel<ReservationDetail> model(final ReservationDetail reserve) {
		return new LoadableDetachableModel<ReservationDetail>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected ReservationDetail load() {
				return reserve;
			}
		};
	}

	@Override
	public AllReservationFilter getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(AllReservationFilter filter) {
		this.filter = filter;
	}
}
