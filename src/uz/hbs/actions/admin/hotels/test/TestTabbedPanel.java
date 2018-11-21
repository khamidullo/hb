package uz.hbs.actions.admin.hotels.test;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.components.ajax.AjaxOnBlurEvent;

public class TestTabbedPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public TestTabbedPanel(String id, ValueMap model) {
		super(id);
		Form<ValueMap> form = new Form<ValueMap>("form", new CompoundPropertyModel<ValueMap>(model));
		add(form);
		form.add(new TextField<String>("field").add(new AjaxOnBlurEvent()));
	}

}
