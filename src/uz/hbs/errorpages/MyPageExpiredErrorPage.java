package uz.hbs.errorpages;

import org.apache.wicket.RestartResponseAtInterceptPageException;

import uz.hbs.security.SignIn;

public class MyPageExpiredErrorPage extends AMyErrorPage {
	private static final long serialVersionUID = 1L;

	public MyPageExpiredErrorPage() {
		throw new RestartResponseAtInterceptPageException(SignIn.class);
	}
}
