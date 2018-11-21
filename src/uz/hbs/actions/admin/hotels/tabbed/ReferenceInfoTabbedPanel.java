package uz.hbs.actions.admin.hotels.tabbed;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.beans.Condition;
import uz.hbs.beans.Equipment;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.MealOption;
import uz.hbs.beans.ReferenceInfo;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.components.panels.AdditionalPanel;
import uz.hbs.components.panels.hotel.AddReferenceHotelPanel;
import uz.hbs.components.panels.hotel.MealOptionsPanel;
import uz.hbs.components.panels.hotel.RoomTypePanel;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.palette.BootstrapPalette;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.models.HotelModels;
import uz.hbs.utils.models.RoomSetupModel;

public abstract class ReferenceInfoTabbedPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private ModalWindow dialog;
	private final Hotel hotel;
	private MyFeedbackPanel feedback;
	private RoomTypePanel roomtypepanel;

	public ReferenceInfoTabbedPanel(String id, IModel<Hotel> model) {
		super(id, model);

		hotel = model.getObject();

		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);

		ReferenceInfo info = hotel.getReferenceInfo();
		if (info == null) {
			info = new ReferenceInfo();
			hotel.setReferenceInfo(info);
		}

		add(dialog = new ModalWindow("dialog"));

		final Form<ReferenceInfo> form;

		add(form = new ReferenceInfoForm("form", info));

		form.add(new IndicatingAjaxButton("next") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onNext(target, form, feedback);
				target.add(roomtypepanel.getContainer());
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(
						new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
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

			@Override
			public boolean isEnabled() {
				return hotel.getStep() == Hotel.REFERENCE_INFO;
			}

			@Override
			public boolean isVisible() {
				return isVisibleNextButton();
			}
		});

		form.add(new IndicatingAjaxButton("save") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(target, form, feedback);
				target.add(roomtypepanel.getContainer());
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(
						new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
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

			@Override
			public boolean isEnabled() {
				return hotel.getStep() == Hotel.REFERENCE_INFO;
			}
		});
	}

	private class ReferenceInfoForm extends Form<ReferenceInfo> {
		private static final long serialVersionUID = 1L;

		public ReferenceInfoForm(String id, final ReferenceInfo info) {
			super(id, new CompoundPropertyModel<ReferenceInfo>(info));

			final WebMarkupContainer equipment_container;
			add(equipment_container = new WebMarkupContainer("equipment_container"));
			equipment_container.setOutputMarkupId(true);

			info.setEquipments(HotelModels.getHotelsEquipments(hotel.getUsers_id()).getObject());

			ChoiceRenderer<Equipment> renderer = new ChoiceRenderer<Equipment>("name", "id");
			final BootstrapPalette<Equipment> equipmentsPalette = new BootstrapPalette<Equipment>("equipmentsPalette", new ListModel<Equipment>(
					info.getEquipments()), new CollectionModel<Equipment>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Collection<Equipment> getObject() {
					return RoomSetupModel.getAvailableEquipments();
				}
			}, renderer, 10, false, true);
			equipment_container.add(equipmentsPalette);

			add(new AjaxLink<Void>("add_equipment") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					dialog.setContent(new AdditionalPanel<Equipment>(dialog.getContentId(), new Equipment()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected void onSave(AjaxRequestTarget target, Form<?> form) {
							Equipment equipment = (Equipment) form.getDefaultModelObject();
							new MyBatisHelper().insert("insertEquipment", equipment);
							target.add(equipment_container);
							dialog.close(target);
						}

						@Override
						protected String onJavaScript() {
							return "return after();";
						}
					});
					dialog.show(target);
				}
				
				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					IAjaxCallListener listener = new AjaxCallListener(){
						private static final long serialVersionUID = 1L;

						@Override
						public CharSequence getBeforeSendHandler(Component component) {
							return "return before();";
						}
					};
					attributes.getAjaxCallListeners().add(listener);
				}
			});

			add(roomtypepanel = new RoomTypePanel("roomtype_container", info.getRoom_types(), dialog, feedback));
			roomtypepanel.setOutputMarkupId(true);

			RadioGroup<Byte> self_payment_possibility = new RadioGroup<Byte>("self_payment_possibility");
			add(self_payment_possibility);
			self_payment_possibility.add(new Radio<Byte>("self_payment_possible", new Model<Byte>((byte) 1)));
			self_payment_possibility.add(new Radio<Byte>("self_payment_impossible", new Model<Byte>((byte) 0)));

			TextField<Short> how_many_floors;
			add((how_many_floors = new TextField<Short>("floors")).setRequired(true));
			how_many_floors.setLabel(new StringResourceModel("hotels.reference_info.how_many_floors", null));
			how_many_floors.add(new AjaxOnBlurEvent());

			TextField<Short> how_many_rooms;
			add((how_many_rooms = new TextField<Short>("rooms")));
			how_many_rooms.setLabel(new StringResourceModel("hotels.reference_info.how_many_rooms", null));
			how_many_rooms.add(new AjaxOnBlurEvent());

			add(new MealOptionsPanel("meal_options", new Model<MealOption>(info.getMeal_options())));
			add(new AddReferenceHotelPanel<Condition>("condition_container", info.getConditions(), dialog, "selectConditionNameByName") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onAppend(AjaxRequestTarget target, Form<?> form) {
					ValueMap model = (ValueMap) form.getDefaultModelObject();
					Condition condition;
					if (!info.getConditions().contains(condition = new Condition(model.getString("name"))))
						info.getConditions().add(condition);
					target.add(form);
				}

				@Override
				protected void onEdit(AjaxRequestTarget target, Form<?> form, int id, int index) {
					ValueMap model = (ValueMap) form.getDefaultModelObject();
					Condition condition;
					if (!info.getConditions().contains(condition = new Condition(model.getString("name")))) {
						condition.setId(id);
						if (new MyBatisHelper().update("updateConditions", condition) > 0) {
							info.getConditions().get(index).setName(condition.getName());
						}
					}
				}

				@Override
				protected boolean isDeleted(AjaxRequestTarget target, int id, String name) {
					if (new MyBatisHelper().delete("deleteConditions", id) > 0) {
						feedback.success(new StringResourceModel("hotels.reference_info.room_conditions.delete.success", null, new Object[] { name }).getString());
						target.add(feedback);
						return true;
					} else {
						feedback.error(new StringResourceModel("hotels.reference_info.room_conditions.delete.error", null, new Object[] { name }).getString());
						target.add(feedback);
						return false;
					}
				}

				@Override
				protected void onChangeFlag(Condition object, Boolean flag) {
				}
			});
			CommonUtil.setFormComponentRequired(this);
		}
	}

	protected abstract void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback);

	protected abstract void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback);

	protected abstract boolean isVisibleNextButton();
}
