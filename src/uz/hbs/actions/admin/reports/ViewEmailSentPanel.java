package uz.hbs.actions.admin.reports;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.ReservationEmail;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.FormatUtil;

public class ViewEmailSentPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ViewEmailSentPanel(String id, long reservationsId) {
		super(id);

		add(new ListView<ReservationEmail>("list", new LoadableDetachableModel<List<ReservationEmail>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ReservationEmail> load() {
				return new MyBatisHelper().selectList("selectReservationEmails", reservationsId);
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ReservationEmail> item) {
				ReservationEmail email = item.getModelObject();
				item.add(new Label("datetime", FormatUtil.toString(email.getUpdate_date(), MyWebApplication.DATE_TIME_FORMAT)));
				item.add(new Label("type", new StringResourceModel("reservation.email.type." + email.getType().name(), null)));
				item.add(new Label("status", new StringResourceModel("reservation.email.status." + email.getStatus().name(), null)));
			}
		});
	}
}
