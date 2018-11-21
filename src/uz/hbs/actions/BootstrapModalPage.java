package uz.hbs.actions;

import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.markup.html.bootstrapmodal.BootstrapModalBehavior;
import uz.hbs.markup.html.bootstrapmodal.BootstrapModalPanel;
import uz.hbs.markup.html.bootstrapmodal.BootstrapModalWindow;
import uz.hbs.template.MyPage;

public class BootstrapModalPage extends MyPage {
	private static final long serialVersionUID = 1L;

	public BootstrapModalPage() {

		final WebMarkupContainer queryTableContainer = new WebMarkupContainer("queryTableContainer");
		add(queryTableContainer);
		queryTableContainer.setOutputMarkupId(true);

		final Label clicked = new Label("clicked", "Init");
		clicked.setOutputMarkupId(true);
		queryTableContainer.add(clicked);

		BootstrapModalWindow mw = new BootstrapModalWindow("mw", new BootstrapModalPanel(BootstrapModalWindow.MODAL_PANEL_ID, "Delete") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				clicked.setDefaultModelObject("Submit is clicked");
				target.add(clicked);
				target.add(queryTableContainer);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				clicked.setDefaultModelObject("Cancel is clicked");
				target.add(clicked);
				target.add(queryTableContainer);
			}

			@Override
			public Panel getContent(String id) {
				return new ModalPanel(id);
			}
		});
		add(mw);

		Link<Void> link = new Link<Void>("link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
			}
		};

		link.add(new BootstrapModalBehavior(mw.getModalWindowId()));
		add(link);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return getMySession().isSignedIn();
	}

	@Override
	public String getTitle() {
		return "Bootstrap modal window example";
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	private class ModalPanel extends Panel {
		private static final long serialVersionUID = 1L;

		public ModalPanel(String id) {
			super(id);
			add(new Label(
					"panelLabel",
					"M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
					+ "l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l l o a d e d M o d a l p a n e l "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
//					+ "Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded Modal panel loaded "
));
			
		}
	}
}
