package uz.hbs.actions.hotel.reservations.panels;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationLog;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.FormatUtil;

public class ViewReservationLogPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ViewReservationLogPanel(String id, final IModel<ReservationDetail> model) {
		super(id, model);
		add(new ListView<ReservationLog>("loglist", new LoadableDetachableModel<List<ReservationLog>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ReservationLog> load() {
				return new MyBatisHelper().selectList("selectReservationLogs", model.getObject().getId());
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ReservationLog> item) {
				ReservationLog log = (ReservationLog) item.getDefaultModelObject();
				item.add(new Label("datetime", FormatUtil.toString(log.getCreate_date(), MyWebApplication.DATE_TIME_FORMAT)));
				item.add(new Label("description", log.getDescription()));
				item.add(new Label("author", log.getAuthor()));
			}
		});
	}

}
