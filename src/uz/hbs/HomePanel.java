package uz.hbs;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.admin.AdminHomePanel;
import uz.hbs.actions.api.ApiUserHomePanel;
import uz.hbs.actions.hotel.HotelHomePanel;
import uz.hbs.actions.spectator.SpectatorHomePanel;
import uz.hbs.actions.touragent.TourAgentHomePanel;
import uz.hbs.beans.User;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;

public class HomePanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public HomePanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		
		int userType = getMySession().getUser().getType().getId();
		Panel panel;
		if (userType == User.TYPE_ADMIN_USER) {
			panel = new AdminHomePanel("panel", breadCrumbModel);
		} else if (userType == User.TYPE_HOTEL_USER) {
			panel = new HotelHomePanel("panel");
		} else if (userType == User.TYPE_TOURAGENT_USER) {
			panel = new TourAgentHomePanel("panel");
		} else if (userType == User.TYPE_SPECTATOR_USER) {
			panel = new SpectatorHomePanel("panel");
		} else if (userType == User.TYPE_API) {
			panel = new ApiUserHomePanel("panel");
		} else {
			panel = new EmptyHomePanel("panel");
		}
		add(panel);
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("home.page", null);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
