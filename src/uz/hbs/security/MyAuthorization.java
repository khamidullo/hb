package uz.hbs.security;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;

import uz.hbs.beans.User;
import uz.hbs.session.MySession;

public class MyAuthorization {
    public static boolean isActionAuthorized(final Component component, final Action action, final MySession session) {

        if(component instanceof AuthenticatedWebPage) {
        	if(session == null)
        		return false;
        	
        	User user = session.getUser();
            
            return (user != null && ((AuthenticatedWebPage)component).isAuthorized(user.getActionMap()));
        }
        return true;
    }
}
