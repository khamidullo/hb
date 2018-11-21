package uz.hbs.components.panels;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.model.util.ListModel;

import uz.hbs.markup.html.form.palette.BootstrapPalette;

public abstract class AssignedPaletteDataPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;

	public AssignedPaletteDataPanel(String id, List<T> selected, List<T> available) {
		super(id);
		ChoiceRenderer<T> renderer = new ChoiceRenderer<T>("name", "id");
		BootstrapPalette<T> palette = new BootstrapPalette<T>("palette", 
 									  new ListModel<T>(selected), 
									  new CollectionModel<T>(available), renderer, 10, false, true);
		add(palette);
		
		add(new IndicatingAjaxLink<Void>("save") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onSave(target);
			}
		});
	}
	protected abstract void onSave(AjaxRequestTarget target);
}
