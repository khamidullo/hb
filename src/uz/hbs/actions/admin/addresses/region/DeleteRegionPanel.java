package uz.hbs.actions.admin.addresses.region;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.Region;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class DeleteRegionPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DeleteRegionPanel.class);

	public DeleteRegionPanel(String id, final Region model, final ModalWindow dialog, final MyFeedbackPanel feedback,
			final WebMarkupContainer container) {
		super(id);
		feedback.setEscapeModelStrings(false);

		Form<Void> form = new Form<Void>("form");
		add(form);

		Label deleteTitle = new Label("deleteTitle",
				new StringResourceModel("address.region.delete", null).getString() + " '<strong>" + model.getName() + "</strong>'");
		deleteTitle.setEscapeModelStrings(false);
		form.add(deleteTitle);
		
		AjaxButton yes = new AjaxButton("yes") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				int result = 0;

				try {
					result = new MyBatisHelper().delete("deleteRegion", model.getId());
				} catch (Exception e) {
					logger.error("Exception", e);
				}

				if (result > 0) {
					feedback.success(new StringResourceModel("address.region.delete.success", null, new Object[] { model.getName() }).getString());
					logger.debug("Region {} successfully deleted", model.getName());
				} else {
					feedback.error(new StringResourceModel("address.region.delete.unsuccess", null, new Object[] { model.getName() }).getString());
					logger.error("Error, Region {} is not deleted", model.getName());
				}
				target.add(feedback);
				target.add(container);
				dialog.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
				dialog.close(target);
			}
		};
		form.add(yes);

		AjaxButton no = new AjaxButton("no") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				target.add(feedback);
				dialog.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
				dialog.close(target);
			}
		};
		form.add(no);
	}
}
