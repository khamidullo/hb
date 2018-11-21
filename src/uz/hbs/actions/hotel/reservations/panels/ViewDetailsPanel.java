package uz.hbs.actions.hotel.reservations.panels;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.ReservationDetail;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.tabs.BootstrapAjaxTabbedPanel;

public class ViewDetailsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private ReservationDetail detail;

	public ViewDetailsPanel(String id, IBreadCrumbModel breadCrumbModel, final IModel<ReservationDetail> model) {
		super(id, breadCrumbModel);
		detail = model.getObject();
		//add(new Label("room_number", model.getObject().getRoom_number()));
		List<ITab> tablist = new ArrayList<ITab>();
		tablist.add(new AbstractTab(new StringResourceModel("hotels.reservation.details", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String componentId) {
				return new ViewReservationDetailPanel(componentId, model);
			}
		});
		tablist.add(new AbstractTab(new StringResourceModel("hotels.reservation.guest.details", null)) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public WebMarkupContainer getPanel(String componentId) {
				return new ViewGuestDetailsPanel(componentId, model);
			}
		});
		add(new BootstrapAjaxTabbedPanel<ITab>("tabbed", tablist));
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.reservation.details.title", new Model<ReservationDetail>(detail));
	}
}
