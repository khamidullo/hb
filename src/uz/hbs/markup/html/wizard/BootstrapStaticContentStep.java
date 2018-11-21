package uz.hbs.markup.html.wizard;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class BootstrapStaticContentStep extends BootstrapWizardStep {
	private static final long serialVersionUID = 1L;

	/**
	 * Whether HTML codes should be rendered as is (true), or should be escaped (false).
	 */
	private final boolean allowHtml;

	/** The model that provided the actual content. */
	private IModel<?> content;

	/**
	 * Constructor for if you want to set all the properties yourself.
	 * 
	 * @param allowHtml
	 *            If true, any html of the content will be rendered as is. Otherwise, it will be
	 *            escaped.
	 */
	public BootstrapStaticContentStep(final boolean allowHtml, final boolean titleAllowHtml, final boolean summaryAllowHtml) {
		super(titleAllowHtml, summaryAllowHtml);
		this.allowHtml = allowHtml;
		add(new Label("content", ""));
	}

	/**
	 * Construct.
	 * 
	 * @param title
	 *            The title of this step
	 * @param summary
	 *            The summary of this step
	 * @param content
	 *            The content of the step panel
	 * @param allowHtml
	 *            If true, any html of the content will be rendered as is. Otherwise, it will be
	 *            escaped.
	 */
	public BootstrapStaticContentStep(final IModel<String> title, final IModel<String> summary, final IModel<?> content, final boolean allowHtml, final boolean titleAllowHtml, final boolean summaryAllowHtml) {
		super(title, summary, titleAllowHtml, summaryAllowHtml);
		this.content = content;
		this.allowHtml = allowHtml;
		add(new Label("content", content).setEscapeModelStrings(!allowHtml));
	}

	/**
	 * Construct.
	 * 
	 * @param title
	 *            The title of this step
	 * @param summary
	 *            The summary of this step
	 * @param content
	 *            The content of the step panel
	 * @param allowHtml
	 *            If true, any html of the content will be rendered as is. Otherwise, it will be
	 *            escaped.
	 */
	public BootstrapStaticContentStep(final IModel<String> title, final IModel<String> summary, final String content, final boolean allowHtml, final boolean titleAllowHtml, final boolean summaryAllowHtml) {
		this(title, summary, new Model<String>(content), allowHtml, titleAllowHtml, summaryAllowHtml);
	}

	/**
	 * Construct.
	 * 
	 * @param title
	 *            The title of this step
	 * @param summary
	 *            The summary of this step
	 * @param content
	 *            The content of the step panel
	 * @param allowHtml
	 *            If true, any html of the content will be rendered as is. Otherwise, it will be
	 *            escaped.
	 */
	public BootstrapStaticContentStep(final String title, final String summary, final IModel<?> content, final boolean allowHtml, final boolean titleAllowHtml, final boolean summaryAllowHtml) {
		this(new Model<String>(title), new Model<String>(summary), content, allowHtml, titleAllowHtml, summaryAllowHtml);
	}

	/**
	 * Construct.
	 * 
	 * @param title
	 *            The title of this step
	 * @param summary
	 *            The summary of this step
	 * @param content
	 *            The content of the step panel
	 * @param allowHtml
	 *            If true, any html of the content will be rendered as is. Otherwise, it will be
	 *            escaped.
	 */
	public BootstrapStaticContentStep(final String title, final String summary, final String content, final boolean allowHtml, final boolean titleAllowHtml, final boolean summaryAllowHtml) {
		this(title, summary, new Model<String>(content), allowHtml, titleAllowHtml, summaryAllowHtml);
	}

	/**
	 * Gets whether html is allowed as output.
	 * 
	 * @return Whether html is allowed as output
	 */
	public final boolean getAllowHtml() {
		return allowHtml;
	}

	/**
	 * Gets the content from the content model.
	 * 
	 * @return The content
	 */
	public final String getContent() {
		return (content != null) ? (String) content.getObject() : null;
	}

	/**
	 * Gets the content model.
	 * 
	 * @return The content model
	 */
	public final IModel<?> getContentModel() {
		return content;
	}

	/**
	 * Sets the content model.
	 * 
	 * @param <T>
	 *            The model object type
	 * 
	 * @param content
	 *            The content model
	 */
	public final <T> void setContentModel(final IModel<T> content) {
		this.content = content;
		replace(new Label("content", content).setEscapeModelStrings(!allowHtml));
	}
}
