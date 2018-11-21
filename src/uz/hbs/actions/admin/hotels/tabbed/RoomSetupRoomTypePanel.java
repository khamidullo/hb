package uz.hbs.actions.admin.hotels.tabbed;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.model.util.ListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.actions.admin.hotels.tabbed.image.ImagesEditPanel;
import uz.hbs.beans.BedType;
import uz.hbs.beans.Condition;
import uz.hbs.beans.Equipment;
import uz.hbs.beans.HoldingCapacity;
import uz.hbs.beans.IdAndName;
import uz.hbs.beans.ReferenceInfo;
import uz.hbs.beans.Room;
import uz.hbs.beans.RoomType;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.palette.BootstrapPalette;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.models.HotelModels;
import uz.hbs.utils.models.RoomSetupModel;

public abstract class RoomSetupRoomTypePanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final Logger _log = LoggerFactory.getLogger(RoomSetupRoomTypePanel.class);
	private ImagesEditPanel imagePanel;
	private ReferenceInfo info;
	private long hotel_id;
	private MyFeedbackPanel feedback;

	public RoomSetupRoomTypePanel(String id, final RoomType roomtype, ReferenceInfo info, long hotel_id) {
		super(id);
		setOutputMarkupId(true);
		this.info = info;
		this.hotel_id = hotel_id;
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		add(new RoomSetupForm("form", roomtype));
	}
	
	private class RoomSetupForm extends Form<RoomType>{
		private static final long serialVersionUID = 1L;


		public RoomSetupForm(String id, final RoomType roomtype) {
			super(id, new CompoundPropertyModel<RoomType>(roomtype));
			
			add(new Label("area", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return CommonUtil.nvl(String.valueOf(roomtype.getRoom_area()));
				}
			}));
			add(new Label("holding_capacity", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return String.valueOf(roomtype.getHolding_capacity().getId());
				}
			}));
			add(new Label("additional_bed", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getObject() {
					return "";
				}
			}).add(new AttributeModifier("class", roomtype.getAdditional_bed().isId()?"fa fa-check":"fa fa-times")).add(new AttributeModifier("style", roomtype.getAdditional_bed().isId()?"color: green;":"color: red;")));
			
			final WebMarkupContainer container;
			add(container = new WebMarkupContainer("container"));
			container.setOutputMarkupId(true);
			ListView<Room> listview = new ListView<Room>("roomlist", new LoadableDetachableModel<List<Room>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Room> load() {
					return roomtype.getRooms(); 
				}
			}) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final ListItem<Room> item) {
					Room room = (Room) item.getDefaultModelObject();
					
					TextField<String> room_number;
					
					item.add(room_number = new TextField<String>("room_number", new PropertyModel<String>(room, "room_number"), String.class));
					room_number.setLabel(new StringResourceModel("hotels.room_setup.room_number", null));
					room_number.add(new AjaxOnBlurEvent());
					
					DropDownChoice<Short> room_floor;
					item.add(room_floor = new DropDownChoice<Short>("room_floor", new PropertyModel<Short>(room, "room_floor"), new AbstractReadOnlyModel<List<Short>>() {
						private static final long serialVersionUID = 1L;

						@Override
						public List<Short> getObject() {
							return RoomSetupModel.getNumberOfFloor(info.getFloors());
						}
					}, new ChoiceRenderer<Short>(){
						private static final long serialVersionUID = 1L;
						
						@Override
						public Object getDisplayValue(Short object) {
							return object;
						}
						
						@Override
						public String getIdValue(Short object, int index) {
							return String.valueOf(object);
						}
					}));
					room_floor.setLabel(new StringResourceModel("hotels.room_setup.room_floor", null));
					room_floor.setRequired(true);
					room_floor.add(new AjaxOnBlurEvent());
					
					final DropDownChoice<BedType> bed_type;
					item.add(bed_type = new DropDownChoice<BedType>("bed_type", new PropertyModel<BedType>(room, "bed_type"), RoomSetupModel.getBedType(), new ChoiceRenderer<BedType>("name", "id")){
						private static final long serialVersionUID = 1L;

						@Override
						protected boolean isSelected(BedType object, int index, String selected) {
							if (roomtype.getHolding_capacity().getId() != HoldingCapacity.HOLDING_CAPACITY_2) {
								if (roomtype.getHolding_capacity().getId() == object.getHolding_capacity()) {
									return true;
								}
							}
							return super.isSelected(object, index, selected);
						}
						
						@Override
						protected boolean isDisabled(BedType object, int index, String selected) {
							//_log.debug(roomtype.getHolding_capacity().getId() + " : " + object.getHolding_capacity());
							if (roomtype.getHolding_capacity().getId() != object.getHolding_capacity()) return true;
							return super.isDisabled(object, index, selected);
						}
					});
					bed_type.setLabel(new StringResourceModel("hotels.room_setup.bed_type", null));
					bed_type.setRequired(true);
					bed_type.add(new AttributeModifier("data-item", "bed-type"));
					bed_type.add(new AjaxOnBlurEvent());
					
					final int selectedItem = item.getIndex();
					
					item.add(new Link<Void>("delete"){
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							if (item.getModelObject().getId() != null) {
								SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
								try {
									sql.delete("deleteRoom", item.getModelObject().getId());
									sql.update("updateRoomTypeRoomNumber", roomtype.getId());
									sql.commit();
									roomtype.getRooms().remove(selectedItem);
									feedback.success(getString("actions.hotels.room.delete.success"));
								} catch (Exception e) {
									sql.rollback();
									_log.error("Exception", e);
									feedback.error(getString("actions.hotels.room.delete.fail"));
								} finally {
									sql.close();
								}
							} else {
								new MyBatisHelper().update("updateRoomTypeRoomNumber", roomtype.getId());
								roomtype.getRooms().remove(selectedItem);
							}
						}
						
						@Override
						protected CharSequence getOnClickScript(CharSequence url) {
							return url = "return confirm('Are you sure?');";
						}
					});
				}
			};
			ChoiceRenderer<Equipment> renderer = new ChoiceRenderer<Equipment>("name", "id");

			BootstrapPalette<Equipment> equipmentsPalette = new BootstrapPalette<Equipment>("equipmentsPalette", 
															new ListModel<Equipment>(roomtype.getEquipments()), 
															new CollectionModel<Equipment>(HotelModels.getHotelsEquipments(hotel_id).getObject()), renderer, 10, false, true);
			add(equipmentsPalette);
			
			IModel<List<Condition>> conditionsModel = new LoadableDetachableModel<List<Condition>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Condition> load() {
					return roomtype.getConditions();
				}
			};
			
			CheckGroup<Condition> conditionGroup = new CheckGroup<Condition>("conditionGroup", conditionsModel);
			add(conditionGroup);
			ListView<Condition> conditionlist = new ListView<Condition>("conditionlist", RoomSetupModel.getAvailableCondition(hotel_id)) {
				private static final long serialVersionUID = 1L;
	
				@Override
				protected void populateItem(ListItem<Condition> item) {
					item.add(new Check<Condition>("condition", item.getModel()));
					item.add(new Label("name", new PropertyModel<IdAndName>(item.getModel(), "name")).setRenderBodyOnly(true));
				}
			};
			conditionGroup.add(conditionlist);
			conditionlist.setReuseItems(true);
			conditionlist.setOutputMarkupId(true);
			conditionGroup.setRequired(! conditionsModel.getObject().isEmpty());
			conditionGroup.setLabel(new StringResourceModel("hotels.room_setup.room_conditions", null));
			container.add(listview);
			listview.setReuseItems(true);
			
			FileUploadField roomImageField;
			add(roomImageField = new FileUploadField("room_images"));
			roomtype.setRoomImageField(roomImageField);
			
			add(imagePanel = new ImagesEditPanel("imgPanel", (long) roomtype.getId(), false));
			imagePanel.setOutputMarkupId(true);
			
			final HiddenField<Short> room_count;
			add(room_count = new HiddenField<>("room_count", new Model<Short>(){
				private static final long serialVersionUID = 1L;

				@Override
				public Short getObject() {
					return CommonUtil.nvl((Short) new MyBatisHelper().selectOne("selectRoomCountByRoomType", roomtype.getId()));
				}
			}));
			room_count.setOutputMarkupId(true);
			
			TextArea<String> description;
			add(description = new TextArea<String>("description"));
			description.add(new AttributeModifier("id", "reditor" + roomtype.getId()));
			description.setLabel(new StringResourceModel("hotels.details.description", null));
			
			Label descriptionScript = new Label("descriptionScript", "CKEDITOR.replace('reditor"+roomtype.getId()+"');");
			descriptionScript.setEscapeModelStrings(false);
			add(descriptionScript);
			
			AjaxButton save = new AjaxButton("save") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					RoomType roomtype = (RoomType) form.getDefaultModelObject();
					onSave(target, roomtype, feedback);
					imagePanel.refresh();
					target.add(room_count);
					target.add(imagePanel);
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(RoomSetupForm.this.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
				}
			};
			
			Label saveLabel = new Label("saveLabel", new StringResourceModel("button.save.room_type", new Model<RoomType>(roomtype)));
			save.add(saveLabel);
			
			add(save);
		}
	}
	
	protected abstract void onSave(AjaxRequestTarget target, RoomType roomtype, MyFeedbackPanel feedback);
}
