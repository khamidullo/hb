package uz.hbs.actions.hotel.roomsandrates;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.utils.models.MyAjaxCallListener;

public abstract class AbstractRoomActionPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;
	

	public AbstractRoomActionPanel(String id, final T object, LoadableDetachableModel<List<? extends T>>  listmodel, String title) {
		super(id);
		
		ValueMap model = new ValueMap();
		model.put("selected", object);
		Form<ValueMap> form = new Form<ValueMap>("form", new CompoundPropertyModel<ValueMap>(model));
		form.add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		form.add(new Label("name", getString(title)));
		
		DropDownChoice<T> selected;
		form.add(selected = new DropDownChoice<T>("selected", listmodel, new ChoiceRenderer<T>("name", "id")));
		selected.setRequired(true);
		selected.setLabel(Model.of(getString(title)));
		
	    form.add(new AjaxButton("submit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ValueMap model = (ValueMap) form.getDefaultModelObject();
				onUpdate(target, (T) model.get("selected"), feedback);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new MyAjaxCallListener(false));
			}
		});
	    add(form);
	}
	protected abstract void onUpdate(AjaxRequestTarget target, T selected, MyFeedbackPanel feedback);
}
