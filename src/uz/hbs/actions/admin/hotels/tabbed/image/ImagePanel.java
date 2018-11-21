package uz.hbs.actions.admin.hotels.tabbed.image;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;

import uz.hbs.beans.UploadedFile;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.utils.models.MyAjaxCallListener;

public abstract class ImagePanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ImagePanel(String id, final Logger logger, final MyFeedbackPanel feedback, final WebMarkupContainer container, final Item<UploadedFile> item) {
		super(id);
		ContextImage img = new ContextImage("img", item.getModelObject().getLink());
		add(img);
		
		add(new AjaxCheckBox("setDefault", new Model<Boolean>(item.getModelObject().isIs_default())){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				onSetDefault(target, Strings.isTrue(getValue()), getMarkupId());
			}
		});
		
		AjaxLink<Void> delLink = new AjaxLink<Void>("delLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onDelete(target);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
			}
		};
		add(delLink);
		
		add(new AjaxEditableLabel<String>("caption", new PropertyModel<String>(item.getModel(), "comment")){
			private static final long serialVersionUID = 1L;
			private TextField<String> editor;
			
			@Override
			protected FormComponent<String> newEditor(MarkupContainer parent, String componentId, IModel<String> model) {
				editor = (TextField<String>) super.newEditor(parent, componentId, model);
				editor.add(new AttributeModifier("placeholder", "Caption"));
				editor.add(new AttributeModifier("class", "form-control"));
				return editor;
			}
			
			@Override
			protected Component newLabel(MarkupContainer parent, String componentId, IModel<String> model) {
				if (model.getObject() == null) model.setObject("");
				return super.newLabel(parent, componentId, model);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				super.onSubmit(target);
				if (new MyBatisHelper().update("updateUploadedFileComment", item.getModelObject()) > 0){
					logger.info("Image Caption is updated, id = " + item.getModelObject().getId());
					feedback.success(new StringResourceModel("hotels.image.cation.edited.success", null).getString());
				}
				target.add(feedback);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target) {
				super.onError(target);
				feedback.error(new StringResourceModel("hotels.image.cation.edited.error", null).getString());
				target.add(feedback);
			}
		});
	}
	
	protected abstract void onDelete(AjaxRequestTarget target);
	protected abstract void onSetDefault(AjaxRequestTarget target, boolean isDefault, String markupId);
}
