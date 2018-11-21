package uz.hbs.markup.navigation.repeater.data.table.filter;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;

public class MyGoAndClearFilter extends GoAndClearFilter {

	private static final long serialVersionUID = 1L;

	public MyGoAndClearFilter(String id, FilterForm<?> form) {
		super(id, form);
		
		getClearButton().add(new AttributeModifier("class", "btn btn-sm btn-default"));
		getClearButton().add(new AttributeModifier("style", "margin:2px; padding:2px 4px;"));
		getGoButton().add(new AttributeModifier("class", "btn btn-sm btn-primary"));
		getGoButton().add(new AttributeModifier("style", "margin:2px; padding:2px 4px;"));
	}
}
