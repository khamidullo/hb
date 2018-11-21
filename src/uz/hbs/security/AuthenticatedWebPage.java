package uz.hbs.security;

import java.util.Map;

import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.session.MySession;

/**
 * Marker interface to indicate that a page should be viewable by authenticated users only.
 * 
 */
public interface AuthenticatedWebPage {
    public MySession getMySession();

    public boolean isAuthorized(final Map<ActionRight, Action> actionMap);
}
