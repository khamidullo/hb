package uz.hbs.actions.hotel.roomsandrates.rate.sale;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import uz.hbs.beans.rate.SalePlane;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.FormatUtil;

public class SaleItemPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public SaleItemPanel(String id, final IModel<SalePlane> model, final ModalWindow dialog, final boolean resident) {
		super(id, new CompoundPropertyModel<SalePlane>(model));
		setOutputMarkupId(true);
		add(new CheckBox("checked"));
		add(new Label("dom", FormatUtil.toString(model.getObject().getSale_date(), "dd")));
		add(new AjaxLink<Void>("change"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				dialog.setInitialHeight(300);
				dialog.setInitialWidth(600);
				dialog.setMinimalHeight(300);
				dialog.setMinimalWidth(600);
				dialog.setTitle(getString("hotels.sale.plane.change"));
				dialog.setContent(new ChangeSaleDialogPanel(dialog.getContentId(), model.getObject(), resident));
				dialog.show(target);
				dialog.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClose(AjaxRequestTarget target) {
						target.add(getPage());
					}
				});
			}
			
			@Override
			public boolean isVisible() {
				return model.getObject().getHotelsusers_id() != null;
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				HashMap<String, Serializable> param = new HashMap<String, Serializable>();
				param.put("hotel_id", model.getObject().getHotelsusers_id());
				param.put("roomtype_id", model.getObject().getRoomtypes_id());
				param.put("sale_date", model.getObject().getSale_date());
				param.put("is_group", Boolean.FALSE);
				short indvl = new MyBatisHelper().selectOne("selectStatusIndicateSalePlane", param);
				param.put("is_group", Boolean.TRUE);
				short group = new MyBatisHelper().selectOne("selectStatusIndicateSalePlane", param);
				
				if (indvl == 1 && group == 1) tag.put("style", "color: green");
				else if (indvl == 0 && group == 0) tag.put("style", "color: red");
				else if (indvl == 1 && group == 0) tag.put("style", "color: blue");
				else if (indvl == 0 && group == 1) tag.put("style", "color: yellow");
			}
		});
		
	}
}
