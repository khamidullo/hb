package uz.hbs.markup.html.wizard;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.extensions.wizard.Wizard;

public class BootstrapWizard extends Wizard {
	private static final long serialVersionUID = 1L;

	public BootstrapWizard(String id) {
		super(id);
	}

	public BootstrapWizard(String id, IWizardModel wizardModel) {
		super(id, wizardModel);
	}

	public BootstrapWizard(String id, boolean addDefaultCssStyle) {
		super(id, addDefaultCssStyle);
	}

	public BootstrapWizard(String id, IWizardModel wizardModel, boolean addDefaultCssStyle) {
		super(id, wizardModel, addDefaultCssStyle);
	}

	/**
	 * Create a new button bar. Clients can override this method to provide a custom button bar.
	 * 
	 * @param id
	 *            The id to be used to construct the component
	 * 
	 * @return A new button bar
	 */
	@Override
	protected Component newButtonBar(final String id) {
		return new BootstrapWizardButtonBar(id, this);
	}
}
