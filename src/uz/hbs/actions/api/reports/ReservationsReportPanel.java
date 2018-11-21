package uz.hbs.actions.api.reports;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

import uz.hbs.beans.Reservation;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;

public class ReservationsReportPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ReservationsReportPanel(String id) {
		super(id);

		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		add(feedback);
		feedback.setOutputMarkupId(true);

		WebMarkupContainer wrapper = new WebMarkupContainer("wrapper");
		wrapper.setOutputMarkupId(true);
		add(wrapper);

		Reservation filter = new Reservation();
		filter.setTouragent_id(((MySession) getSession()).getUser().getId());
		add(new ReportFilterPanel("searchFilterForm", filter, wrapper, feedback, false));

		wrapper.add(new ReservationsResultPanel("resultPanel", filter, true));
	}
}
