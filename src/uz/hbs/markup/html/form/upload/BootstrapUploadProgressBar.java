package uz.hbs.markup.html.form.upload;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;

public class BootstrapUploadProgressBar extends UploadProgressBar {
	private static final long serialVersionUID = 1L;

	/**
	 * The color type of the stack
	 */
	private BootstrapProgressBar.Type type = BootstrapProgressBar.Type.DEFAULT;
	/**
	 * A flag indicating whether the progress bar is animated/active.
	 */
	private boolean active = false;

	/**
	 * A flag indicating whether the progress bar is striped.
	 */
	private boolean striped = false;

	/**
	 * Constructor that will display the upload progress bar for every submit of the given form.
	 * Starts with initial progress value of {@value de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar#MIN}
	 *
	 * @param id
	 *            The component id
	 * @param form
	 *            The form with the file upload fields
	 */
	public BootstrapUploadProgressBar(String id, Form<?> form) {
		this(id, form, Model.of(BootstrapProgressBar.MIN));
	}

	/**
	 * Constructor that will display the upload progress bar for every submit of the given form.
	 *
	 * @param id
	 *            The component id
	 * @param form
	 *            The form with the file upload fields
	 * @param model
	 *            The initial value of the progress
	 */
	public BootstrapUploadProgressBar(String id, Form<?> form, IModel<Integer> model) {
		this(id, form, null, model);
	}

	/**
	 * Constructor that will display the upload progress bar for the given file upload field.
	 *
	 * @param id
	 *            The component id
	 * @param form
	 *            The form with the file upload fields
	 * @param fileUploadField
	 *            The file upload field which progress will be followed.
	 * @param model
	 *            The initial value of the progress
	 */
	public BootstrapUploadProgressBar(String id, Form<?> form, FileUploadField fileUploadField, IModel<Integer> model) {
		super(id, form, fileUploadField);

		setRenderBodyOnly(false);

		setDefaultModel(model);
	}

	@Override
	protected ResourceReference getCss() {
		return null;
	}

	public boolean striped() {
		return striped;
	}

	public BootstrapUploadProgressBar striped(boolean value) {
		striped = value;
		return this;
	}

	public boolean active() {
		return active;
	}

	public BootstrapUploadProgressBar active(boolean value) {
		active = value;
		if (value) {
			striped(true);
		}
		return this;
	}

	public BootstrapProgressBar.Type type() {
		return type;
	}

	public BootstrapUploadProgressBar type(BootstrapProgressBar.Type type) {
		this.type = type;
		return this;
	}

	@Override
	protected MarkupContainer newBarComponent(String id) {
		return new WebMarkupContainer(id) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);

				if (!BootstrapProgressBar.Type.DEFAULT.equals(type)) {
					tag.put("class", type().cssClassName());
				}

				tag.put("style", createStyleValue().getObject());
				
				String classes = "";
				for (String s : cssClassNames()) {
					classes += " " + s;
				}

				tag.put("class", classes);
				tag.put("role", "progressbar");
				tag.put("aria-valuenow", value());
				tag.put("aria-valuemin", 0);
				tag.put("aria-valuemax", 100);
			}
		};
	}

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

	
	@Override
	protected MarkupContainer newStatusComponent(String id) {
		MarkupContainer status = super.newStatusComponent(id);
		status.setVisible(false);
		return status;
	}

	private IModel<String> createStyleValue() {
		return Model.of(String.format("width: %s%%", value()));
	}

	/**
	 * Returns whether the progress bar is complete or not.
	 *
	 * @return {@code true} if the progress bar is complete.
	 */
	public final boolean complete() {
		return value() == BootstrapProgressBar.MAX;
	}

	/**
	 * Sets a new value for the progress.
	 *
	 * @return this instance, for method chaining.
	 */
	public UploadProgressBar value(IModel<Integer> value) {
		setDefaultModel(value);
		return this;
	}

	/**
	 * Sets a new value for the progress.
	 *
	 * @return this instance, for method chaining.
	 */
	public UploadProgressBar value(Integer value) {
		setDefaultModelObject(value);
		return this;
	}

	/**
	 * Returns the current value of the progress.
	 *
	 * @return the current value of the progress.
	 */
	public Integer value() {
		return Math.max(Math.min((Integer) getDefaultModelObject(), BootstrapProgressBar.MAX), BootstrapProgressBar.MIN);
	}

//	@Override
//	protected void onComponentTag(ComponentTag tag) {
//		super.onComponentTag(tag);
//
//		BootstrapProgressBar.internalOnComponentTag(tag, active(), striped());
//		
//	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		// monkey patches the JavaScript provided by super.renderHead(response)
		response.render(JavaScriptHeaderItem.forReference(new UploadProgressBarJavaScriptReference()));
	}
}
