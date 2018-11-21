package uz.hbs.markup.html.panel;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class ActionsPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;

	public ActionsPanel(String id, final IModel<T> model) {
		super(id, model);

		add(addPrintLink(model));
		add(addEditLink(model));
		add(addViewLink(model));
		add(addViewLogLink(model));
		add(addDeleteLink(model));
		add(addCancelLink(model));
		add(addUserListLink(model));
		add(viewRates(model));
		add(viewSales(model));
		add(makeReservation(model));
	}

	protected AbstractLink addPrintLink(final IModel<T> model) {
		return new AbstractLink("print") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return false;
			}
		};
	}

	protected AbstractLink addEditLink(final IModel<T> model) {
		return new AbstractLink("edit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return false;
			}
		};
	}

	protected AbstractLink addViewLink(final IModel<T> model) {
		return new AbstractLink("view") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return false;
			}
		};
	}

	protected AbstractLink addViewLogLink(final IModel<T> model) {
		return new AbstractLink("viewLog") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return false;
			}
		};
	}

	protected AbstractLink addDeleteLink(final IModel<T> model) {
		return new AbstractLink("delete") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return false;
			}
		};
	}

	protected AbstractLink addCancelLink(final IModel<T> model) {
		return new AbstractLink("cancel") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return false;
			}
		};
	}

	protected AbstractLink addUserListLink(final IModel<T> model) {
		return new AbstractLink("userList") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return false;
			}
		};
	}

	protected AbstractLink viewRates(final IModel<T> model) {
		return new AbstractLink("viewRates") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return false;
			}
		};
	}	

	protected AbstractLink viewSales(final IModel<T> model) {
		return new AbstractLink("viewSales") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return false;
			}
		};
	}

	
	protected AbstractLink makeReservation(final IModel<T> model) {
		return new AbstractLink("makeReservation") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return false;
			}
		};
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
