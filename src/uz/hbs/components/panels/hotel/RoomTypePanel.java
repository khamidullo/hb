package uz.hbs.components.panels.hotel;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.RangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.actions.admin.hotels.tabbed.panels.AdditionalBedSwitchPanel;
import uz.hbs.beans.AdditionalBed;
import uz.hbs.beans.HoldingCapacity;
import uz.hbs.beans.RoomType;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.models.RoomSetupModel;

public class RoomTypePanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final Logger _log = LoggerFactory.getLogger(RoomTypePanel.class);
	private WebMarkupContainer container;

	public RoomTypePanel(String id, final List<RoomType> list, final ModalWindow dialog, FeedbackPanel feedback) {
		super(id);
		add(container = new WebMarkupContainer("container"));
		container.setOutputMarkupId(true);
		container.add(new ListView<RoomType>("list", new LoadableDetachableModel<List<RoomType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<RoomType> load() {
				return list;
			}
		}){
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return list.size() > 0;
			}
			
			@Override
			protected void populateItem(final ListItem<RoomType> item) {
				NumberTextField<Integer> number_of_rooms;
				item.add(new Label("name", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return item.getModelObject().getName();
					}
				}));
				item.add(number_of_rooms = new NumberTextField<Integer>("number_of_rooms", new PropertyModel<Integer>(item.getModel(), "number_of_rooms"), Integer.class));
				number_of_rooms.add(RangeValidator.minimum(CommonUtil.nvl(item.getModelObject().getNumber_of_rooms())));
				number_of_rooms.add(new AjaxOnBlurEvent());
				number_of_rooms.setRequired(true);
				number_of_rooms.setLabel(new StringResourceModel("hotels.room_type.number_of_rooms", null));
				
				TextField<Float> room_area;
				
				item.add(room_area = new TextField<Float>("room_area", new PropertyModel<Float>(item.getModel(), "room_area"), Float.class));
				room_area.setLabel(new StringResourceModel("hotels.room_setup.room_area", null));
				room_area.add(new AjaxOnBlurEvent());
				
				final DropDownChoice<HoldingCapacity> holding_capacity;
				item.add(holding_capacity = new DropDownChoice<HoldingCapacity>("holding_capacity", new PropertyModel<HoldingCapacity>(item.getModel(), "holding_capacity"), RoomSetupModel.getHoldingCapacity(), new ChoiceRenderer<HoldingCapacity>("name", "id")));
				holding_capacity.setLabel(new StringResourceModel("hotels.room_setup.holding_capacity", null));
				holding_capacity.setRequired(true);
				holding_capacity.add(new AjaxOnBlurEvent());
				
				item.add(new AdditionalBedSwitchPanel("additional_bed", new PropertyModel<AdditionalBed>(item.getModel(), "additional_bed")));
				
				item.add(new AjaxLink<Void>("remove"){
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						boolean error = false;
						if (item.getModelObject().getId() != null) {
							SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
							try {
								sql.delete("deleteRoomByRoomTypeId", item.getModelObject().getId());
								sql.delete("deleteRoomEquipmentByRoomTypeId", item.getModelObject().getId());
								sql.delete("deleteHotelRoomTypeByRoomTypeId", item.getModelObject().getId());
								sql.delete("deleteHotelConditionByRoomTypeId", item.getModelObject().getId());
								sql.delete("deleteRatePlanDetailByRoomTypeId", item.getModelObject().getId());
								sql.delete("deleteSalePlanByRoomTypeId", item.getModelObject().getId());
								sql.delete("deleteReservationRoomByRoomTypeId", item.getModelObject().getId());
								sql.delete("deleteRoomTypeUploadFileByRoomTypeId", item.getModelObject().getId());
								sql.delete("deleteRoomtypeByRoomtypeId", item.getModelObject().getId());
								sql.commit();
								feedback.success(getString("roomtype.delete.success"));
							} catch (Exception e) {
								error = true;
								_log.error("Exception", e);
								feedback.error(getString("roomtype.delete.fail"));
								sql.rollback();
							} finally {
								sql.close();
								target.add(feedback);
							}
						}
						if (! error) {
							list.remove(item.getModelObject());
							item.remove();
						}
						target.add(getContainer());
					}
					
					@Override
					public boolean isVisible() {
						return true;
					};
					
					@Override
					protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
						super.updateAjaxAttributes(attributes);
						IAjaxCallListener listener = new AjaxCallListener(){
							private static final long serialVersionUID = 1L;
							
							@Override
							public CharSequence getPrecondition(Component component) {
								return "return confirm('" + getString("confirm") + "');";
							}
							
						};
						attributes.getAjaxCallListeners().add(listener);
					}
				});
				item.add(new AjaxLink<Void>("edit"){
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						dialog.setTitle(getString("hotels.room_types.edit"));
						dialog.setContent(new AddDialogPanel(dialog.getContentId(), new ValueMap("name=" + item.getModelObject().getName())){
							private static final long serialVersionUID = 1L;

							@Override
							protected void onSave(AjaxRequestTarget target, Form<?> form) {
								ValueMap model = (ValueMap) form.getDefaultModelObject();
								RoomType roomtype;
								if (! list.contains(roomtype = new RoomType(model.getString("name")))) {
									roomtype.setId(item.getModelObject().getId());
									roomtype.setInitiator_user_id(((MySession) getSession()).getUser().getId());;
									if (new MyBatisHelper().update("updateRoomTypeName", roomtype) > 0) {
										((RoomType) list.get(item.getIndex())).setName(roomtype.getName());
									}
								}
								dialog.close(target);
							}
						});
						dialog.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClose(AjaxRequestTarget target) {
								target.add(getContainer());
							}
						});
						dialog.show(target);
					}
					
					@Override
					public boolean isVisible() {
						return item.getModelObject().getId() != null; 
					};
					
					@Override
					protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
						super.updateAjaxAttributes(attributes);
						IAjaxCallListener listener = new AjaxCallListener(){
							private static final long serialVersionUID = 1L;
							
							@Override
							public CharSequence getPrecondition(Component component) {
								return "return confirm('" + getString("confirm") + "');";
							}
							
						};
						attributes.getAjaxCallListeners().add(listener);
					}
				});
			}
		}.setReuseItems(true));
		getContainer().add(new AjaxLink<Void>("append"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				dialog.setTitle(getString("hotels.room_types.add"));
				dialog.setContent(new AddDialogPanel(dialog.getContentId()){
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form) {
						ValueMap model = (ValueMap) form.getDefaultModelObject();
						RoomType roomtype;
						if (! list.contains(roomtype = new RoomType(model.getString("name")))) list.add(roomtype);
						dialog.close(target);
					}
				});
				dialog.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClose(AjaxRequestTarget target) {
						target.add(getContainer());
					}
				});
				dialog.show(target);
			}
		});
	}
	
	public WebMarkupContainer getContainer() {
		return container;
	}

	private abstract class AddDialogPanel extends Panel{
		private static final long serialVersionUID = 1L;
		private MyFeedbackPanel feedback;
		
		public AddDialogPanel(String id) {
			this(id, new ValueMap());
		}

		public AddDialogPanel(String id, ValueMap model) {
			super(id);
			Form<ValueMap> form;
			add(feedback = new MyFeedbackPanel("feedback"));
			feedback.setOutputMarkupId(true);
			
			add(form = new Form<ValueMap>("form", new CompoundPropertyModel<ValueMap>(model)));
			form.add(new AutoCompleteTextField<String>("name", String.class) {
				private static final long serialVersionUID = 1L;

				@Override
				protected Iterator<String> getChoices(String input) {
					if (Strings.isEmpty(input)) {
						List<String> emptyList = Collections.emptyList();
						return emptyList.iterator();
					}
					List<String> selectList = new MyBatisHelper().selectList("selectRoomTypesName", input);
					return selectList.iterator();
				}
			}.setRequired(true));
			form.add(new AjaxButton("save") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					onSave(target, form);
				}
			});
		}
		
		protected abstract void onSave(AjaxRequestTarget target, Form<?> form);
	}
}
