package uz.hbs.markup.navigation;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;

public class BootstrapAjaxPagingNavigator extends BootstrapPagingNavigator {
	private static final long serialVersionUID = 1L;

	public BootstrapAjaxPagingNavigator(String markupId, IPageable pageable, Size size) {
		super(markupId, pageable, size);
	}

	public BootstrapAjaxPagingNavigator(String markupId, IPageable pageable, IPagingLabelProvider labelProvider, Size size) {
		super(markupId, pageable, labelProvider, size);
	}
}
