package uz.hbs.markup.html.wizard;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardButton;
/**
 * A bar of buttons for wizards utilizing {@link AjaxFormSubmitBehavior}.
 * 
 * @see Wizard#newButtonBar(String)
 * 
 */
public class BootstrapAjaxWizardButtonBar extends BootstrapWizardButtonBar {
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param id
	 *            The component id
	 * @param wizard
	 *            The containing wizard
	 */
	public BootstrapAjaxWizardButtonBar(String id, Wizard wizard) {
		super(id, wizard);

		wizard.setOutputMarkupId(true);
	}

	@Override
	public MarkupContainer add(Component... childs) {
		for (Component component : childs) {
			if (component instanceof WizardButton) {
				ajaxify((WizardButton) component);
			}
		}
		return super.add(childs);
	}

	private void ajaxify(final WizardButton button) {
		button.add(new AjaxFormSubmitBehavior("click") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);

				BootstrapAjaxWizardButtonBar.this.updateAjaxAttributes(attributes);
			}

			@Override
			public boolean getDefaultProcessing() {
				return button.getDefaultFormProcessing();
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(findParent(Wizard.class));

				button.onSubmit();
			}

			@Override
			protected void onAfterSubmit(AjaxRequestTarget target) {
				button.onAfterSubmit();
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				target.add(findParent(Wizard.class));

				button.onError();
			}
		});
	}

	/**
	 * Hook method to update Ajax attributes.
	 * 
	 * @param attributes
	 *            Ajax attributes
	 */
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
	}
}