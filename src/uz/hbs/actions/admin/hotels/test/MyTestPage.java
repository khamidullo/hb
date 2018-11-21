package uz.hbs.actions.admin.hotels.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.util.visit.IVisitor;

import uz.hbs.HomePanel;
import uz.hbs.beans.Action;
import uz.hbs.beans.IdAndValue;
import uz.hbs.components.feedback.ShinyFormVisitor;
import uz.hbs.enums.ActionRight;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.label.feedback.ComponentVisualErrorBehavior;
import uz.hbs.markup.html.form.label.feedback.ErrorLevelsFeedbackMessageFilter;
import uz.hbs.markup.html.form.label.feedback.FeedbackLabel;
import uz.hbs.session.MySession;
import uz.hbs.template.MyPage;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;

public class MyTestPage extends MyPage {
	private static final long serialVersionUID = 1L;
	private PersonColor personColor = new PersonColor();
	
	public MyTestPage() {
		BreadCrumbPanel panel = new HomePanel("panel", breadCrumbBar);
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
		final MyFeedbackPanel feedback;
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);

		final Form<ValueMap> form;
		add(form = new ShinyForm("form"));
		form.add(new Button("ok1"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit() {
				feedback.error("Ok1 - Error");
			}
		});
		form.add(new AjaxButton("ok2") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				feedback.error("Ok2 - Error");
				target.add(feedback);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
			}
		});
		form.setMarkupId("defaultForm");
	}
	
	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		
	}
	
	public class ShinyForm extends Form<ValueMap> {
		private static final long serialVersionUID = 1L;
		IVisitor<Component, ?> shinyVisitor = new ShinyFormVisitor();
		
		public ShinyForm(String id) {
			super(id, new CompoundPropertyModel<ValueMap>(new ValueMap()));
			add(new EmailTextField("email").setRequired(true).setEnabled(true));
			add(new DropDownChoice<IdAndValue>("selector", new LoadableDetachableModel<List<IdAndValue>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<IdAndValue> load() {
					final List<IdAndValue> list = new ArrayList<IdAndValue>();
					list.add(new IdAndValue(1, "Value 1"));
					list.add(new IdAndValue(2, "Value 2"));
					list.add(new IdAndValue(3, "Value 3"));
					list.add(new IdAndValue(4, "Value 4"));
					list.add(new IdAndValue(5, "Value 5"));
					return list;
				}
			}, new ChoiceRenderer<IdAndValue>("value", "id")).setRequired(true));
			CommonUtil.setFormComponentRequired(this);
		}
		
		@Override
		public void renderHead(IHeaderResponse response) {
			super.renderHead(response);
			response.render(JavaScriptHeaderItem.forUrl("js/hb.js"));
		}
	}
	
	
	public class TestForm extends Form<PersonColor>{
		
		public TestForm(String id) {
			super(id, new CompoundPropertyModel<PersonColor>(personColor));
			
			// FeedbackPanel
	        final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
	        feedback.setOutputMarkupId(true);
	        add(feedback);

	        // filteredErrorLevels will not be shown in the FeedbackPanel
	        int[] filteredErrorLevels = new int[]{FeedbackMessage.ERROR};
	        feedback.setFilter(new ErrorLevelsFeedbackMessageFilter(filteredErrorLevels));

	        // Textfield for entering a name
	        TextField<String> name = new TextField<String>("name", new PropertyModel<String>(personColor, "name"));
	        name.setOutputMarkupId(true);
	        name.setRequired(true);
	        name.setLabel(Model.of("name"));

	        // This shows feedback when the name input is not correct.
	        final FeedbackLabel nameFeedbackLabel = new FeedbackLabel("name_feedback", name);
	        nameFeedbackLabel.setOutputMarkupId(true);
	        add(nameFeedbackLabel);

	        name.add(new ComponentVisualErrorBehavior("onblur", nameFeedbackLabel));
	        add(name);

	        Label nameLabel = new Label("name.label", "Name");
	        add(nameLabel);

	        // Creating the RadioGroup with all radio buttons
	        RadioGroup<String> color = new RadioGroup<String>("color", new PropertyModel<String>(personColor, "color"));
	        color.add(new Radio<String>("red", new Model<String>("red")));
	        color.add(new Radio<String>("green", new Model<String>("green")));
	        color.add(new Radio<String>("blue", new Model<String>("blue")));
	        color.setLabel(Model.of("Color"));
	        color.setRequired(true);
	        add(color);

	        // This shows feedback when the color input is not correct
	        // Note: it will display a custom text.
	        Model<Serializable> customText = new Model<Serializable>("You don't have a favorite color?!");
	        FeedbackLabel colorFeedbackLabel = new FeedbackLabel("color_feedback", color, customText);
	        add(colorFeedbackLabel);

	        // Add the Save button
	        add(new Button("saveButton", new Model<String>("Save")));
	        add(new Label("js", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return CommonUtil.setFormValidator(TestForm.this);
				}
			}).setEscapeModelStrings(false));
		}

		private static final long serialVersionUID = 1L;
		
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return true;
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public String getTitle() {
		return "Test Page";
	}

	public class PersonColor implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name;
		private String color;
		private String name_feedback;
		private String color_feedback;
		
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getName_feedback() {
			return name_feedback;
		}
		public void setName_feedback(String name_feedback) {
			this.name_feedback = name_feedback;
		}
		public String getColor_feedback() {
			return color_feedback;
		}
		public void setColor_feedback(String color_feedback) {
			this.color_feedback = color_feedback;
		}
		
	}
}
