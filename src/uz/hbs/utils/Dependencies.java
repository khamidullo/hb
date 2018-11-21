package uz.hbs.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.util.lang.Args;

/**
 * # Description
 *
 * Helper class to handle {@link org.apache.wicket.request.resource.ResourceReference} dependencies.
 *
 * @author Michael Haitz <michael.haitz@agilecoders.de>
 */
public class Dependencies {

	/**
	 * # Description
	 *
	 * combines two lists of {@link HeaderItem}.
	 *
	 * # Usage
	 *
	 * when using in `getDependencies()`:
	 *
	 * ```java
	 * public Iterable<? extends HeaderItem> getDependencies() {
	 * return Dependencies.combine(super.getDependencies(), myCustomCssHeaderItem, myCustomJsHeaderItem);
	 * }
	 * ```
	 *
	 * @param headerItems
	 *            the base header item list
	 * @param additional
	 *            all additional {@link HeaderItem}
	 * @return combined list of {@link HeaderItem}
	 */
	public static Iterable<? extends HeaderItem> combine(final Iterable<? extends HeaderItem> headerItems, final HeaderItem... additional) {
		Args.notNull(headerItems, "headerItems");
		Args.notNull(additional, "additional");

		Iterator<? extends HeaderItem> iterator = headerItems.iterator();
		final List<HeaderItem> elements = new ArrayList<HeaderItem>();
		while (iterator.hasNext()) {
			HeaderItem headerItem = (HeaderItem) iterator.next();
			elements.add(headerItem);
		}

		for (HeaderItem headerItem : additional) {
			elements.add(headerItem);
		}

		return elements;
	}

	/**
	 * Private constructor to prevent instantiation
	 */
	private Dependencies() {
		throw new UnsupportedOperationException();
	}
}
