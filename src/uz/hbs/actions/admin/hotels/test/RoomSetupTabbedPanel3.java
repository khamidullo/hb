package uz.hbs.actions.admin.hotels.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import uz.hbs.actions.admin.hotels.tabbed.RoomSetupRoomTypePanel;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.ReferenceInfo;
import uz.hbs.beans.RoomSetup;
import uz.hbs.beans.RoomType;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.tabs.BootstrapAjaxTabbedPanel;

public abstract class RoomSetupTabbedPanel3 extends  Panel {
	private static final long serialVersionUID = 1L;
	private ReferenceInfo info = null;
	private RoomSetup setup = null;
	private Hotel hotel;
	private MyFeedbackPanel feedback;

	public RoomSetupTabbedPanel3(String id, IModel<Hotel> model) {
		super(id, model);
		
		hotel = model.getObject();
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		setup = hotel.getRoomSetup();
		info = hotel.getReferenceInfo();
		if (setup == null){
			setup = new RoomSetup();
			hotel.setRoomSetup(setup);
			for (RoomType type: info.getRoom_types()){
				setup.addMap(type.getId(), new RoomType(type.getId(), type.getName(), type.getNumber_of_rooms()));
			}
		}
		
		add(new AjaxLink<Void>("next") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onNext(target, setup, feedback);
			}
			
			@Override
			public boolean isEnabled() {
				return hotel.getStep() == Hotel.ROOM_SETUP;
			}
			
			@Override
			public boolean isVisible() {
				return isVisibleNextButton();
			}
		});
		add(new AjaxLink<Void>("save") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				onSave(target, setup, feedback);
			}
			
			@Override
			public boolean isEnabled() {
				return hotel.getStep() == Hotel.ROOM_SETUP;
			}
		});
		
		List<ITab> tabbedlist = new ArrayList<ITab>();
		List<RoomType> list = new ArrayList<RoomType>();
		list.addAll(setup.getRoom_types().values());
		
		for (final RoomType roomtype : list){
			tabbedlist.add(new AbstractTab(Model.of(roomtype.getName())) {
				private static final long serialVersionUID = 1L;

				@Override
				public WebMarkupContainer getPanel(String componentId) {
					return new RoomSetupRoomTypePanel(componentId, roomtype, info, hotel.getUsers_id()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected void onSave(AjaxRequestTarget target, RoomType roomtype, MyFeedbackPanel feedback) {
						}
					};
				}
			});
		}
		add(new BootstrapAjaxTabbedPanel<ITab>("tabbed", tabbedlist));
	}
	
	protected abstract void onSave(AjaxRequestTarget target, RoomSetup setup, MyFeedbackPanel feedback); 
	protected abstract void onNext(AjaxRequestTarget target, RoomSetup setup, MyFeedbackPanel feedback); 
	protected abstract boolean isVisibleNextButton();
}
