package uz.hbs.actions.spectator.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.touragent.reports.CommissionsReportPanel;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.tabs.BootstrapAjaxTabbedPanel;

public class ReportsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ReportsPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		AbstractTab commissionsTab = new AbstractTab(new StringResourceModel("touragents.report.commissions", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return true;
			}
			
			@Override
			public WebMarkupContainer getPanel(String containerId) {
				return new CommissionsReportPanel(containerId);
			}
		};
		
		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(commissionsTab);
		
		BootstrapAjaxTabbedPanel<ITab> tabPanel = new BootstrapAjaxTabbedPanel<ITab>("tabPanel", tabs);
		add(tabPanel);
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.reports", null);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
