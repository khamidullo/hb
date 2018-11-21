package uz.hbs.security;

import org.apache.wicket.markup.html.WebPage;

public class Logout extends WebPage {
	private static final long serialVersionUID = 1L;

	public Logout() {
		getSession().invalidateNow();
		setResponsePage(new SignIn());
	}
}
