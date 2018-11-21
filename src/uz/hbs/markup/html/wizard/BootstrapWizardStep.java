package uz.hbs.markup.html.wizard;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.wizard.IWizard;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

public class BootstrapWizardStep extends WizardStep {
	private static final long serialVersionUID = 1L;
	private final boolean titleAllowHtml;
	private final boolean summaryAllowHtml;
	protected static final String devider = " <i class=\"fa fa-minus\"></i><i class=\"fa fa-dot-circle-o\"></i><i class=\"fa fa-minus\"></i> ";

	public BootstrapWizardStep(final boolean titleAllowHtml, final boolean summaryAllowHtml) {
		super();
		this.titleAllowHtml = titleAllowHtml;
		this.summaryAllowHtml = summaryAllowHtml;
	}

	/**
	 * Creates a new step with the specified title and summary. The title and summary are displayed in the
	 * wizard title block while this step is active.
	 * 
	 * @param title
	 *            the title of this step.
	 * @param summary
	 *            a brief summary of this step or some usage guidelines.
	 * @param titleAllowHtml
	 *            Sets whether title strings should allow HTML tags.
	 * @param summaryAllowHtml
	 *            Sets whether summary strings should allow HTML tags
	 */
	public BootstrapWizardStep(final IModel<String> title, final IModel<String> summary, final boolean titleAllowHtml, final boolean summaryAllowHtml) {
		super(title, summary);
		this.titleAllowHtml = titleAllowHtml;
		this.summaryAllowHtml = summaryAllowHtml;
	}

	/**
	 * Creates a new step with the specified title and summary. The title and summary are displayed in the
	 * wizard title block while this step is active.
	 * 
	 * @param title the title of this step.
	 * @param summary a brief summary of this step or some usage guidelines.
	 * @param model Any model which is to be used for this step
	 * @param titleAllowHtml Sets whether title strings should allow HTML tags.
	 * @param summaryAllowHtml Sets whether summary strings should allow HTML tags
	 */
	public BootstrapWizardStep(final IModel<String> title, final IModel<String> summary, final IModel<?> model, final boolean titleAllowHtml,
			final boolean summaryAllowHtml) {
		super(title, summary, model);
		this.titleAllowHtml = titleAllowHtml;
		this.summaryAllowHtml = summaryAllowHtml;
	}

	public BootstrapWizardStep(final String title, final String summary, final boolean titleAllowHtml, final boolean summaryAllowHtml) {
		super(title, summary);
		this.titleAllowHtml = titleAllowHtml;
		this.summaryAllowHtml = summaryAllowHtml;
	}

	public BootstrapWizardStep(final String title, final String summary, final IModel<?> model, final boolean titleAllowHtml,
			final boolean summaryAllowHtml) {
		super(title, summary, model);
		this.titleAllowHtml = titleAllowHtml;
		this.summaryAllowHtml = summaryAllowHtml;
	}

	@Override
	public Component getHeader(final String id, final Component parent, final IWizard wizard) {
		return new Header(id, wizard);
	}

	private final class Header extends Panel {
		private static final long serialVersionUID = 1L;

		/**
		 * Construct.
		 * 
		 * @param id
		 *            The component id
		 * @param wizard
		 *            The containing wizard
		 */
		public Header(final String id, final IWizard wizard) {
			super(id);
			setDefaultModel(new CompoundPropertyModel<IWizard>(wizard));
			add(new Label("title", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return getTitle();
				}
			}).setEscapeModelStrings(!titleAllowHtml));
			add(new Label("summary", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return getSummary();
				}
			}).setEscapeModelStrings(!summaryAllowHtml));
		}
	}
}
