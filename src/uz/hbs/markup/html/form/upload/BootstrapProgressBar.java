package uz.hbs.markup.html.form.upload;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class BootstrapProgressBar extends Panel {
	private static final long serialVersionUID = 1L;
	public static final int MIN = 0;
	public static final int MAX = 100;

	private WebMarkupContainer indicator;
	private boolean active = false;
	private Type type = Type.DEFAULT;
	private boolean striped = false;

	public enum Type {
		DEFAULT, INFO, SUCCESS, WARNING, DANGER;

		public String cssClassName() {
			return equals(DEFAULT) ? "" : "progress-bar-" + name().toLowerCase();
		}	

		public AttributeAppender newCssClassNameModifier() {
			return new AttributeAppender("class", cssClassName());
		}
	}

	public BootstrapProgressBar(String id) {
		super(id, Model.of(MIN));

		commonInit();
	}

	public BootstrapProgressBar(String id, IModel<Integer> model) {
		super(id, model);

		commonInit();
	}

	public BootstrapProgressBar striped(boolean value) {
		striped = value;
		return this;
	}

	public BootstrapProgressBar active(boolean value) {
		active = value;
		return this;
	}

	public BootstrapProgressBar type(Type type) {
		this.type = type;
		return this;
	}

	public BootstrapProgressBar value(IModel<Integer> value) {
		setDefaultModel(value);
		return this;
	}

	public BootstrapProgressBar value(Integer value) {
		setDefaultModelObject(value);
		return this;
	}
	
	private void commonInit() {
		indicator = newIndicator("indicator");
		AbstractReadOnlyModel<String> labelModel = new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return value().toString() + "%";
			}
		};
		indicator.add(new Label("label", labelModel));
		add(indicator);
	}

	protected final Component indicator() {
		return indicator;
	}

	public boolean striped() {
		return striped;
	}

	public boolean active() {
		return active;
	}

	public final boolean complete() {
		return value() >= MAX;
	}

	public Type type() {
		return type;
	}

	private WebMarkupContainer newIndicator(final String markupId) {
		return new WebMarkupContainer(markupId) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onConfigure() {
				super.onConfigure();

				add(new AttributeModifier("style", createStyleValue()));
				String classes = "";
				for (String s : cssClassNames()) {
					classes += " " + s;
				}

				add(new AttributeModifier("class", classes));
				add(new AttributeModifier("role", "progressbar"));
				add(new AttributeModifier("aria-valuenow", value()));
				add(new AttributeModifier("aria-valuemin", 0));
				add(new AttributeModifier("aria-valuemax", 100));
			}
		};
	}

	private IModel<String> createStyleValue() {
		return Model.of("width: " + value() + "%");
	}

	public Integer value() {
		return Math.max(Math.min((Integer) getDefaultModelObject(), MAX), MIN);
	}

//	@Override
//	protected void onConfigure() {
//		super.onConfigure();
//		String classes = "";
//		for (String s : cssClassNames()) {
//			classes += " " + s;
//		}
//
//		add(new AttributeAppender("class", classes));
//	}

	private List<String> cssClassNames() {
		List<String> classNames = new ArrayList<String>();
		classNames.add("progress-bar");
		classNames.add(type().cssClassName());

		if (striped()) {
			classNames.add("progress-bar-striped");
		}

		if (active()) {
			classNames.add("active");
		}
		
		return classNames;
	}
}
