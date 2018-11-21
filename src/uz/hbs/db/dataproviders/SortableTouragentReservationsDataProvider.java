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

import uz.hbs.beans.Reservation;
import uz.hbs.db.MyBatisHelper;

public class SortableTouragentReservationsDataProvider extends SortableDataProvider<Reservation, String> implements IFilterStateLocator<Reservation> {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SortableTouragentReservationsDataProvider.class);
	private Reservation filter;

	public SortableTouragentReservationsDataProvider() {
		setSort("r.id", SortOrder.DESCENDING);
	}

	@Override
	public Iterator<? extends Reservation> iterator(long offset, long limit) {
		final SortParam<String> sp = getSort();

		List<Reservation> list;
		try {
			Map<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("creator_user_id", filter.getTouragent_id());
			param.put("hotelsusers_id", filter.getHotel_id());
			param.put("display_name", filter.getHotel_name());
			param.put("date_from", filter.getFromDate());
			param.put("date_to", filter.getToDate());
			param.put("type", filter.getType());
			param.put("status", filter.getStatus());
			param.put("payment_method", filter.getPayment_method());
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("sortField", sp.getProperty());
			param.put("sortOrder", sp.isAscending() ? "ASC" : "DESC");

			list = new MyBatisHelper().selectList("selectTAReservationList", param);
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
			param.put("creator_user_id", filter.getTouragent_id());
			param.put("hotelsusers_id", filter.getHotel_id());
			param.put("display_name", filter.getHotel_name());
			param.put("date_from", filter.getFromDate());
			param.put("date_to", filter.getToDate());
			param.put("type", filter.getType());
			param.put("status", filter.getStatus());
			param.put("payment_method", filter.getPayment_method());
 		    Long count = (Long) new MyBatisHelper().selectOne("selectTAReservationListCount", param);
			return count;
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return 0;
	}

	@Override
	public IModel<Reservation> model(Reservation object) {
		return Model.of(object);
	}

	@Override
	public Reservation getFilterState() {
		return filter;
	}

	@Override
	public void setFilterState(Reservation filter) {
		this.filter = filter;
	}
}
