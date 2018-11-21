package uz.hbs.actions.touragent.reports;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;

import uz.hbs.beans.Hotel;
import uz.hbs.beans.Reservation;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;

public class ReservationsByHotelsReportPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	public ReservationsByHotelsReportPanel(String id) {
		super(id);
		
		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		add(feedback);
		feedback.setOutputMarkupId(true);

		WebMarkupContainer wrapper = new WebMarkupContainer("wrapper");
		wrapper.setOutputMarkupId(true);
		add(wrapper);

		final Reservation filter = new Reservation();
		filter.setTouragent_id(((MySession) getSession()).getUser().getId());
		add(new ReportFilterPanel("searchFilterForm", filter, wrapper, feedback, false));
		
		LoadableDetachableModel<List<Hotel>> hotelListModel = new LoadableDetachableModel<List<Hotel>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<Hotel> load() {
				Map<String, Serializable> param = new HashMap<String, Serializable>();
				param.put("creator_user_id", filter.getTouragent_id());
				param.put("date_from", filter.getFromDate());
				param.put("date_to", filter.getToDate());
				return new MyBatisHelper().selectList("selectTAReservationHotelsCount", param);
			}
		};
		
		ListView<Hotel> listView = new ListView<Hotel>("rows", hotelListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Hotel> item) {
				Reservation filter2 = new Reservation(filter.getTouragent_id(), filter.getFromDate(), filter.getToDate(), item.getModelObject().getUsers_id());
				item.add(new Label("name", item.getModelObject().getName()));
				item.add(new ReservationsResultPanel("resultPanel", filter2));
			}
		};
		wrapper.add(listView);
	}
}
