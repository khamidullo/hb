package uz.hbs.actions.admin.hotels.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.value.ValueMap;

public class TestPage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	public TestPage() {
		List<ITab> tablist = new ArrayList<ITab>();
		final ValueMap model = new ValueMap();
		tablist.add(new AbstractTab(Model.of("Test1")) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String id) {
				return new TestTabbedPanel(id, model);
			}
		});
		tablist.add(new AbstractTab(Model.of("Test2")) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public WebMarkupContainer getPanel(String id) {
				return new TestTabbedPanel(id, model);
			}
		});
		add(new AjaxTabbedPanel<ITab>("tabbed", tablist){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected WebMarkupContainer newLink(String linkId, final int index) {
				final WebMarkupContainer c = new IndicatingAjaxLink<Void>(linkId) {
		         private static final long serialVersionUID = 1L;

			     	public void onClick(AjaxRequestTarget target) {
			     		setSelectedTab(index);
			     		if (target != null) {
			     			target.add(TestPage.this);
			     		}
			     		onAjaxUpdate(target);
			     	}
			    };
			    return c;
			}
		});
	}

}
