package uz.hbs.actions.touragent.newbooking;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.LoadableDetachableModel;

import uz.hbs.actions.touragent.newbooking.panel.RoomTypeEquipmentPanel;
import uz.hbs.beans.KeyAndValue;
import uz.hbs.components.panels.slider.ImageSliderPanel;
import uz.hbs.db.MyBatisHelper;

public abstract class RoomTypeDetailPanel extends WebPage {
	private static final long serialVersionUID = 1L;

	public RoomTypeDetailPanel(String id, final Long hotelsUsersId, final Integer roomTypesId) {

		final LoadableDetachableModel<List<KeyAndValue>> imgListModel = new LoadableDetachableModel<List<KeyAndValue>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<KeyAndValue> load() {
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("roomtype_id", roomTypesId);
				return new MyBatisHelper().selectList("selectRoomTypeImages", params);
			}
		};

		add(new ImageSliderPanel("imageSlider", imgListModel.getObject()));
		
		add(new Label("description", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new MyBatisHelper().selectOne("selectRoomTypeDescription", roomTypesId);
			}
		}).setEscapeModelStrings(false));

		add(new RoomTypeEquipmentPanel("equipmentPanel", hotelsUsersId, roomTypesId, false));

		IndicatingAjaxLink<Void> closeLink = new IndicatingAjaxLink<Void>("closeLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onClose(target);
			}
		};

		add(closeLink);
	}

	protected abstract void onClose(AjaxRequestTarget target);
}
