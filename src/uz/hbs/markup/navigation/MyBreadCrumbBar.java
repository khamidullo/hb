package uz.hbs.markup.navigation;

import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;
import org.apache.wicket.markup.ComponentTag;

/**
 * A component that renders bread crumbs like {@link BreadCrumbBar} that is
 * styled with twitter-bootstrap.
 * 
 * @author miha
 */
public class MyBreadCrumbBar extends BreadCrumbBar {
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param markupId
	 */
	public MyBreadCrumbBar(final String markupId) {
		super(markupId);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);

		checkComponentTag(tag, "ol");
		tag.put("class", "breadcrumb");
	}

	/**
	 * Overrides the method in the super class to remove the default / separator since twitter-bootstrap adds the separators via CSS.
	 * 
	 * @see org.apache.wicket.extensions.breadcrumb.BreadCrumbBar#getSeparatorMarkup()
	 */
	@Override
	protected String getSeparatorMarkup() {
		return " ";
	}
}
