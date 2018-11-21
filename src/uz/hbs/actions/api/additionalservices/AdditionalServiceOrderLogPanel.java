package uz.hbs.actions.api.additionalservices;

import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.ReservationLog;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.utils.FormatUtil;

public class AdditionalServiceOrderLogPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public AdditionalServiceOrderLogPanel(String id, IBreadCrumbModel breadCrumbModel, final Long reservationId) {
		super(id, breadCrumbModel);

		add(new ListView<ReservationLog>("loglist", new LoadableDetachableModel<List<ReservationLog>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ReservationLog> load() {
				return new MyBatisHelper().selectList("selectAdditionalServiceOrdersLogs", reservationId);
			}
		}) {
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

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.reservation.details.log", null);
	}
}
