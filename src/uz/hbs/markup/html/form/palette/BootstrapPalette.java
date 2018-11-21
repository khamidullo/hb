package uz.hbs.markup.html.form.palette;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;

import uz.hbs.MyWebApplication;

public class BootstrapPalette<T> extends Palette<T> {
	private static final long serialVersionUID = 1L;

	public BootstrapPalette(String id, IModel<? extends List<? extends T>> model, IModel<? extends Collection<? extends T>> choicesModel,
			IChoiceRenderer<T> choiceRenderer, int rows, boolean allowOrder, boolean allowMoveAll) {
		super(id, model, choicesModel, choiceRenderer, rows, allowOrder, allowMoveAll);
	}

	public BootstrapPalette(String id, IModel<? extends List<? extends T>> model, IModel<? extends Collection<? extends T>> choicesModel,
			IChoiceRenderer<T> choiceRenderer, int rows, boolean allowOrder) {
		super(id, model, choicesModel, choiceRenderer, rows, allowOrder);
	}

	public BootstrapPalette(String id, IModel<? extends Collection<? extends T>> choicesModel, IChoiceRenderer<T> choiceRenderer, int rows,
			boolean allowOrder) {
		super(id, choicesModel, choiceRenderer, rows, allowOrder);
	}
	
	@Override
	protected CssResourceReference getCSS() {
		return new CssResourceReference(MyWebApplication.class, "em.css");//empty.css
	}
}
