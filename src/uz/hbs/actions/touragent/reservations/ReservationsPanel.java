package uz.hbs.actions.touragent.reservations;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.touragent.reports.ReportFilterPanel;
import uz.hbs.actions.touragent.reports.ReservationsResultWithActionPanel;
import uz.hbs.beans.Reservation;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;

public class ReservationsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ReservationsPanel(String id, IBreadCrumbModel breadCrumbModel, boolean fromHomePage, Byte reservationType, Byte reservationStatus) {
		super(id, breadCrumbModel);
		
		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		add(feedback);
		feedback.setOutputMarkupId(true);

		if (((MySession)getSession()).getFeedbackMessage() != null) {
			feedback.success(((MySession)getSession()).getFeedbackMessage());
			((MySession)getSession()).setFeedbackMessage(null);
		}
		
		WebMarkupContainer wrapper = new WebMarkupContainer("wrapper");
		wrapper.setOutputMarkupId(true);
		add(wrapper);

		Reservation filter = new Reservation();
		filter.setTouragent_id(((MySession) getSession()).getUser().getId());
		filter.setType(reservationType);
		filter.setStatus(reservationStatus);
		add(new ReportFilterPanel("searchFilterForm", filter, wrapper, feedback, fromHomePage));

		wrapper.add(new ReservationsResultWithActionPanel("resultPanel", ReservationsPanel.this, filter));
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.reservations", null);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
