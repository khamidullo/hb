package uz.hbs.actions.admin.hotels.tabbed;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.Hotel;
import uz.hbs.beans.ReferenceInfo;
import uz.hbs.beans.RoomSetup;
import uz.hbs.beans.RoomType;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.HotelUtil;

public abstract class RoomSetupTabbedPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(RoomSetupTabbedPanel.class);
	private ReferenceInfo info = null;
	private RoomSetup setup = null;
	private Hotel hotel;
	private MyFeedbackPanel feedback;

	public RoomSetupTabbedPanel(String id, IModel<Hotel> model) {
		super(id, model);
		
		hotel = model.getObject();
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		setup = hotel.getRoomSetup();
		info = hotel.getReferenceInfo();
		if (setup == null){
			setup = new RoomSetup();
			hotel.setRoomSetup(setup);
			for (RoomType roomtype: info.getRoom_types()){
				setup.addMap(roomtype.getId(), new RoomType(roomtype, true));
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
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getPrecondition(Component component) {
						return "return isCheckNextBtn();";
					}
					
					@Override
					public CharSequence getBeforeSendHandler(Component component) {
						return "this.disabled = true;";
					}
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "this.disabled = false;";
					}
					
					@Override
					public CharSequence getFailureHandler(Component component) {
						return "this.disabled = true;";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
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
			
			@Override
			public boolean isVisible() {
				return isVisibleSaveButton();
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getBeforeSendHandler(Component component) {
						return "this.disabled = true;";
					}
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "this.disabled = false;";
					}
					
					@Override
					public CharSequence getFailureHandler(Component component) {
						return "this.disabled = true;";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});
		
		IModel<List<RoomType>> roomtypeModel = new LoadableDetachableModel<List<RoomType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<RoomType> load() {
				List<RoomType> list = new ArrayList<RoomType>();
				list.addAll(setup.getRoom_types().values());
				return list;
			}
		};
		
		ListView<RoomType> tab_header = new ListView<RoomType>("tab_header", roomtypeModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<RoomType> item) {
				final RoomType roomtype = (RoomType) item.getDefaultModelObject();
				WebMarkupContainer tablink;
				item.add(tablink = new WebMarkupContainer("tablink"){
					private static final long serialVersionUID = 1L;

					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.put("href", "#roomtype" + roomtype.getId());
					}
				});
				tablink.add(new Label("name", roomtype.getName()).setRenderBodyOnly(true));
				if (item.getIndex() == 0) item.add(new AttributeModifier("class","active"));
			}
		};
		add(tab_header);
		
		ListView<RoomType> tab_body = new ListView<RoomType>("tab_body", roomtypeModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(final ListItem<RoomType> item) {
				final RoomType roomtype = (RoomType) item.getDefaultModelObject();
				item.add(new RoomSetupRoomTypePanel("container", (RoomType) setup.getMap(roomtype.getId()), info, hotel.getUsers_id()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSave(AjaxRequestTarget target, RoomType roomtype, MyFeedbackPanel feedback) {
						try {
							if (HotelUtil.roomSetup(roomtype, hotel, (MySession) getSession())) feedback.success(getString("hotels.room_setup.update.success"));
							else feedback.error(getString("hotels.room_setup.update.fail"));
						} catch (Exception e) {
							feedback.error(getString("hotels.room_setup.update.fail"));
							logger.error("Exception", e);
						} finally {
							target.add(feedback);
						}
					}
				});
				item.add(new AttributeModifier("id", "roomtype" + roomtype.getId()));
				if (item.getIndex() == 0)  item.add(new AttributeAppender("class", " active"));
			}
		};
		add(tab_body);
	}
	
	protected abstract void onSave(AjaxRequestTarget target, RoomSetup setup, MyFeedbackPanel feedback); 
	protected abstract void onNext(AjaxRequestTarget target, RoomSetup setup, MyFeedbackPanel feedback); 
	protected abstract boolean isVisibleNextButton();
	protected abstract boolean isVisibleSaveButton();
}
