package uz.hbs.components.feedback;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

public class ShinyFormVisitor implements IVisitor<Component, Void>, Serializable{
	private static final long serialVersionUID = 1L;
	
	Set<Component> visited = new HashSet<Component>();

	@Override
	public void component(Component c, IVisit<Void> visit) {
		if (! visited.contains(c)) {
			visited.add(c);
			c.add(new RequiredBorder());
			c.add(new ValidationMsgBehavior());
			c.add(new ErrorHighlightBehavior());
		}
	}
}