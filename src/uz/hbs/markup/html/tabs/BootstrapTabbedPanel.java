package uz.hbs.markup.html.tabs;

import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Styled version of {@link TabbedPanel}.
 * 
 * @author miha
 */
public class BootstrapTabbedPanel<T extends ITab> extends TabbedPanel<T> {
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	public BootstrapTabbedPanel(String id, List<T> tabs) {
		this(id, tabs, Model.of(0));
	}

	/**
	 * {@inheritDoc}
	 */
	public BootstrapTabbedPanel(String id, List<T> tabs, IModel<Integer> model) {
		super(id, tabs, model);
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);

		checkComponentTag(tag, "div");
		tag.put("class", "tabbable");
	}

	@Override
	protected String getSelectedTabCssClass() {
		return "active";
	}

	@Override
	protected String getLastTabCssClass() {
		return "";
	}

	@Override
	protected String getTabContainerCssClass() {
		return "nav nav-tabs";
	}
}
