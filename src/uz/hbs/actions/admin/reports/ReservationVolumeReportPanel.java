package uz.hbs.actions.admin.reports;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;

public class ReservationVolumeReportPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ReservationVolumeReportPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		
		
	}

	@Override
	public Class<?> implementedClass() {
		return getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("report.title.report_by_reservation_volume", null);
	}
}