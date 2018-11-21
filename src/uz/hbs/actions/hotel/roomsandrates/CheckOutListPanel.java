package uz.hbs.actions.hotel.roomsandrates;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.utils.FormatUtil;

public class CheckOutListPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public CheckOutListPanel(String id, final List<ReservationDetail> reservelist) {
		super(id);
		add(new ListView<ReservationDetail>("list", new LoadableDetachableModel<List<ReservationDetail>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ReservationDetail> load() {
				return reservelist;
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ReservationDetail> item) {
				ReservationDetail reserve = (ReservationDetail) item.getDefaultModelObject();
				item.add(new Label("reserve_id", reserve.getId()));
				item.add(new Label("check_in", FormatUtil.toString(reserve.getCheck_in(), MyWebApplication.DATE_TIME_FORMAT)));
				item.add(new Label("check_out", FormatUtil.toString(reserve.getCheck_out(), MyWebApplication.TIME_FORMAT)));
			}
		});
	}

}
