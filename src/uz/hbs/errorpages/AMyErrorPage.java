package uz.hbs.errorpages;

import org.apache.wicket.markup.html.WebPage;

import uz.hbs.template.MyFooterPanel;

public abstract class AMyErrorPage extends WebPage {
	private static final long serialVersionUID = 1L;

	public AMyErrorPage() {
		add(new MyFooterPanel("footer", true));
	}
	
	@Override
	public boolean isVersioned() {
		return false;
	}

	@Override
	public boolean isErrorPage() {
		return true;
	}
}
