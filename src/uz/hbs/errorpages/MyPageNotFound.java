package uz.hbs.errorpages;

import org.apache.wicket.RestartResponseAtInterceptPageException;

import uz.hbs.security.SignIn;

public class MyPageNotFound extends AMyErrorPage {
	private static final long serialVersionUID = 1L;

	public MyPageNotFound() {
		throw new RestartResponseAtInterceptPageException(SignIn.class);
	}
}
