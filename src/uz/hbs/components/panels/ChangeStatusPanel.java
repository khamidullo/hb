package uz.hbs.components.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.User;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.utils.models.UserModels;

public abstract class ChangeStatusPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;

	public ChangeStatusPanel(String id, User user) {
		super(id);
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		Form<User> form;
		add(form = new Form<User>("form", new CompoundPropertyModel<User>(new User(user.getId(), user.getName(), user.getStatus()))));
		form.add(new Label("name"));
		form.add(new DropDownChoice<IdAndValue>("status", UserModels.getStatusList(user.getStatus().getId() == User.STATUS_NEW), new ChoiceRenderer<IdAndValue>("value", "id")));
		form.add(new IndicatingAjaxButton("submit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onOk(target, form, feedback);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
		});
	}
	
	protected abstract void onOk(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback);
}
