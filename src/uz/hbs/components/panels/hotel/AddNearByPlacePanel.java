package uz.hbs.components.panels.hotel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.beans.HotelNearByPlace;
import uz.hbs.beans.IdByteAndName;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.models.HotelModels;
import uz.hbs.utils.models.MyChoiceRenderer;

public class AddNearByPlacePanel extends Panel {
	private static final long serialVersionUID = 1L;
	private WebMarkupContainer container;

	public AddNearByPlacePanel(String id, final List<HotelNearByPlace> list, final ModalWindow dialog) {
		super(id);
		add(container = new WebMarkupContainer("container"));
		container.setOutputMarkupId(true);
		container.add(new ListView<HotelNearByPlace>("list", new LoadableDetachableModel<List<HotelNearByPlace>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<HotelNearByPlace> load() {
				return list;
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<HotelNearByPlace> item) {
				item.add(new Label("name", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return item.getModelObject().getName();
					}
				}));
				item.add(new Label("value", new AbstractReadOnlyModel<Float>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public Float getObject() {
						return item.getModelObject().getValue();
					}
				}));
				item.add(new Label("type", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						return HotelModels.getNearByPlacesType(item.getModelObject().getType());
					}
				}));
				item.add(new AjaxLink<Void>("remove"){
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						list.remove(item.getModelObject());
						target.add(getContainer());
					}
					
					@Override
					public boolean isVisible() {
						return item.getModelObject().getId() == null; 
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
						dialog.setTitle(new StringResourceModel("hotels.details.nearbyplaces.edit", null));
						ValueMap model = new ValueMap();
						model.put("name", item.getModelObject().getName());
						model.put("type", new IdByteAndName(item.getModelObject().getType()));
						model.put("value", item.getModelObject().getValue());
						dialog.setContent(new AddDialogPanel(dialog.getContentId(), model){
							private static final long serialVersionUID = 1L;

							@Override
							protected void onSave(AjaxRequestTarget target, Form<?> form) {
								ValueMap model = (ValueMap) form.getDefaultModelObject();
								HotelNearByPlace nearByPlace;
								if (list.contains(nearByPlace = new HotelNearByPlace(model.getString("name"), (float) model.get("value"), ((IdByteAndName) model.get("type")).getId()))) {
									nearByPlace.setId(item.getModelObject().getId());
									nearByPlace.setInitiator_user_id(((MySession) getSession()).getUser().getId());;
									if (new MyBatisHelper().update("updateHotelsNearByPlaces", nearByPlace) > 0) {
										((HotelNearByPlace) list.get(item.getIndex())).setName(nearByPlace.getName());
										((HotelNearByPlace) list.get(item.getIndex())).setValue(nearByPlace.getValue());
										((HotelNearByPlace) list.get(item.getIndex())).setType(nearByPlace.getType());
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
				});
			}
		}.setReuseItems(true));
		add(new AjaxLink<Void>("append"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				dialog.setTitle(new StringResourceModel("hotels.details.nearbyplaces", null));
				dialog.setContent(new AddDialogPanel(dialog.getContentId()){
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form) {
						ValueMap model = (ValueMap) form.getDefaultModelObject();
						HotelNearByPlace nearByPlace;
						if (! list.contains(nearByPlace = new HotelNearByPlace(model.getString("name"), (float) model.get("value"), ((IdByteAndName) model.get("type")).getId()))) list.add(nearByPlace);
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

		public AddDialogPanel(String id, final ValueMap model) {
			super(id);
			Form<ValueMap> form;
			add(feedback = new MyFeedbackPanel("feedback"));
			feedback.setOutputMarkupId(true);
			
			add(form = new Form<ValueMap>("form", new CompoundPropertyModel<ValueMap>(model)));
			
			final AutoCompleteTextField<String> namefield;
			
			form.add(namefield = new AutoCompleteTextField<String>("name", String.class) {
				private static final long serialVersionUID = 1L;

				@Override
				protected Iterator<String> getChoices(String input) {
					if (Strings.isEmpty(input)) {
						List<String> emptyList = Collections.emptyList();
						return emptyList.iterator();
					}
					List<String> selectList = new MyBatisHelper().selectList("selectHotelNearByPlacesName", input);
					return selectList.iterator();
				}
				
				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					if (model.get("type") != null && ((IdByteAndName) model.get("type")).getId() <= HotelNearByPlace.CITY_CENTER) tag.put("readonly", "readonly");
				}
			});
			namefield.setRequired(true);
			namefield.setOutputMarkupId(true);
			
			final DropDownChoice<IdByteAndName> typefield;
			
			form.add(typefield = new DropDownChoice<IdByteAndName>("type", new LoadableDetachableModel<List<IdByteAndName>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<IdByteAndName> load() {
					List<IdByteAndName> list = new ArrayList<IdByteAndName>();
					list.add(new IdByteAndName(HotelNearByPlace.AIROPORT, getString("hotels.details.nearbyplaces.airport")));
					list.add(new IdByteAndName(HotelNearByPlace.TRAIN, getString("hotels.details.nearbyplaces.train")));
					list.add(new IdByteAndName(HotelNearByPlace.CITY_CENTER, getString("hotels.details.nearbyplaces.city_center")));
					list.add(new IdByteAndName(HotelNearByPlace.METRO, getString("hotels.details.nearbyplaces.metro")));
					list.add(new IdByteAndName(HotelNearByPlace.OTHERs, getString("hotels.details.nearbyplaces.others")));
					return list;
				}
			}, new ChoiceRenderer<IdByteAndName>("name", "id")));
			
			typefield.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (Byte.parseByte(typefield.getValue().isEmpty()?"99":typefield.getValue()) <= HotelNearByPlace.CITY_CENTER) {
						target.appendJavaScript("$('#" + namefield.getMarkupId() + "').attr('readonly','readonly');");
						target.appendJavaScript("$('#" + namefield.getMarkupId() + "').val($('#" + typefield.getMarkupId() + "').find('option:selected').text());");
					} else {
						target.appendJavaScript("$('#" + namefield.getMarkupId() + "').removeAttr('readonly');");
						target.appendJavaScript("$('#" + namefield.getMarkupId() + "').val('');");
					}
				}
			});
			form.add(new DropDownChoice<Float>("value", CommonUtil.getNearByPlaceList(), new MyChoiceRenderer<Float>()).setRequired(true));
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
