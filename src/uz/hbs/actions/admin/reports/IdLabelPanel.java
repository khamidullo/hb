package uz.hbs.actions.admin.reports;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.hotel.reservations.panels.ViewDetailsPanel;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.markup.html.form.label.AjaxLinkLabel;

public class IdLabelPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public IdLabelPanel(String id, ModalWindow dialog, ReservationDetail model) {
		super(id);

		add(new AjaxLinkLabel("idLinkLabel",
				("<i class=\"fa fa-" + (model.isIs_group() ? "group" : "user") + "\"></i> ") + String.valueOf(model.getId())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onLinkClick(AjaxRequestTarget target) {
				dialog.setMinimalWidth(800);
				dialog.setMinimalHeight(600);
				dialog.setAutoSize(true);
				dialog.setTitle(new StringResourceModel("hotels.reservation.details.title", new Model<ReservationDetail>(model)));
				dialog.setContent(new ViewDetailsPanel(dialog.getContentId(), null, Model.of(model)));
				dialog.show(target);
			}

			@Override
			protected boolean isLinkEnabled() {
				return true;
			}
		});
		add(new AjaxLinkLabel("emailLinkLabel", "<span class=\"fa fa-envelope-o "+(model.getEmails_sent() > 0 ? "text-success" : "text-danger")+"\" title=\""+ new StringResourceModel(
				model.getEmails_sent() > 0 ? "reservation.email_was_sent_to_hotel" : "reservation.email_was_not_sent_to_hotel", null).getString() +"\"></span>") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onLinkClick(AjaxRequestTarget target) {
				dialog.setMinimalWidth(800);
				dialog.setMinimalHeight(600);
				dialog.setAutoSize(true);
				dialog.setTitle(new StringResourceModel("reservation.email_sent.title", new Model<ReservationDetail>(model)));
				dialog.setContent(new ViewEmailSentPanel(dialog.getContentId(), model.getId()));
				dialog.show(target);
			}

			@Override
			protected boolean isLinkEnabled() {
				return true;
			}
		});
	}
}
