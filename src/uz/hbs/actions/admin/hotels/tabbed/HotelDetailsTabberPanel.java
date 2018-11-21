package uz.hbs.actions.admin.hotels.tabbed;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import uz.hbs.actions.admin.hotels.tabbed.image.ImagesEditPanel;
import uz.hbs.beans.Facility;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.HotelCategory;
import uz.hbs.beans.HotelDetail;
import uz.hbs.beans.HotelStar;
import uz.hbs.beans.Service;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.components.panels.AdditionalPanel;
import uz.hbs.components.panels.address.AddressPanel;
import uz.hbs.components.panels.hotel.AddNearByPlacePanel;
import uz.hbs.components.panels.hotel.FacilitiesCheckingPanel;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.models.HotelModels;

public abstract class HotelDetailsTabberPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private ModalWindow dialog;
	private Hotel hotel;
//	private FileUploadField hotelImagesField;
	private MyFeedbackPanel feedback;
	private ImagesEditPanel imagePanel;

	public HotelDetailsTabberPanel(String id, IModel<Hotel> model) {
		super(id, model);
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		add(dialog = new ModalWindow("dialog"));
		dialog.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				return true;
			}
		});
		
		hotel = model.getObject();
		HotelDetail details = hotel.getHotelsDetails();
		if (details == null){
			details = new HotelDetail();
			hotel.setHotelsDetails(details);
		}
		final Form<HotelDetail> form;
		
		add(form = new HotelDetailsForm("form", details));
		
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
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
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
				return hotel.getStep() == Hotel.HOTEL_DETAILS;
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
				imagePanel.refresh();
				target.add(imagePanel);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
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
				return hotel.getStep() == Hotel.HOTEL_DETAILS;
			}
		});
	}
	
	private class HotelDetailsForm extends Form<HotelDetail>{
		private static final long serialVersionUID = 1L;

		public HotelDetailsForm(String id, final HotelDetail details) {
			super(id, new CompoundPropertyModel<HotelDetail>(details));
			
			setMultiPart(true);
			setMaxSize(Bytes.megabytes(25));
			setFileMaxSize(Bytes.megabytes(1));

			LoadableDetachableModel<List<? extends HotelStar>> hotelStarsList = HotelModels.getHotelStarsList(false);

			DropDownChoice<HotelStar> hotelStars = new DropDownChoice<HotelStar>("hotelStars", hotelStarsList,
			new IChoiceRenderer<HotelStar>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Object getDisplayValue(HotelStar object) {
					return new StringResourceModel("touragents.newbooking.hotel_class.stars" + object.getId(), null).getString();
				}

				@Override
				public String getIdValue(HotelStar object, int index) {
					return object.getId().toString();
				}
			});
			add(hotelStars.add(new AjaxOnBlurEvent()));
			hotelStars.setRequired(true);
			hotelStars.setLabel(new StringResourceModel("hotels.details.stars", null));

			LoadableDetachableModel<List<? extends HotelCategory>> hotelCategoryList = HotelModels.getHotelsCategories();
			
			DropDownChoice<HotelCategory> hotelCategory = new DropDownChoice<HotelCategory>("hotelCategory", hotelCategoryList,
					new IChoiceRenderer<HotelCategory>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public Object getDisplayValue(HotelCategory object) {
					return object.getName();
				}
				
				@Override
				public String getIdValue(HotelCategory object, int index) {
					return object.getId().toString();
				}
			});
			add(hotelCategory.add(new AjaxOnBlurEvent()));
			hotelCategory.setRequired(true);
			hotelCategory.setLabel(new StringResourceModel("hotels.details.hotel_category", null));

			List<Facility> facilitesSelected = details.getFacilities();
			
			add(new AddNearByPlacePanel("nearbyplaces_container", details.getHotelsnearbyplaces(), dialog));

			add(new FacilitiesCheckingPanel("facilitiesPalette", new Model<HotelDetail>(details), new ListModel<Facility>(facilitesSelected), dialog, feedback));
			
			final WebMarkupContainer service_container;
			add(service_container = new WebMarkupContainer("service_container"));
			service_container.setOutputMarkupId(true);
			
			final CheckGroup<Service> serviceGroup;
			service_container.add(serviceGroup = new CheckGroup<Service>("service", new LoadableDetachableModel<List<Service>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Service> load() {
					return details.getService();
				}
			}));
			serviceGroup.setOutputMarkupId(true);
			serviceGroup.setRenderBodyOnly(false);
			serviceGroup.setRequired(true);
			serviceGroup.setLabel(new StringResourceModel("hotels.details.services_in_rooms", null));
			ListView<Service> servicelist;
			serviceGroup.add(servicelist = new ListView<Service>("servicelist", HotelModels.getHotelRoomServiceModel()){
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final ListItem<Service> item) {
					item.add(new Check<Service>("checkService", item.getModel()){
						private static final long serialVersionUID = 1L;

						@Override
						protected void onComponentTag(ComponentTag tag) {
							super.onComponentTag(tag);
							tag.put("data-item", item.getModelObject().getId());
						}
					});
					item.add(new Label("name", new PropertyModel<Service>(item.getModel(), "name")).setRenderBodyOnly(true));
				}
			});
			servicelist.setReuseItems(true);
			service_container.add(new AjaxLink<Void>("add_service"){
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					dialog.setTitle(new StringResourceModel("hotels.rooms.service.add", null));
					dialog.setContent(new AdditionalPanel<Service>(dialog.getContentId(),new Service()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected void onSave(AjaxRequestTarget target, Form<?> form) {
							Service obj = (Service) form.getDefaultModelObject();
							if ((Integer) new MyBatisHelper().insert("insertService", obj) > 0){
								target.add(service_container);
								dialog.close(target);
							}
						}

						@Override
						protected String onJavaScript() {
							// TODO Auto-generated method stub
							return null;
						}
					});
					dialog.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClose(AjaxRequestTarget target) {
							target.appendJavaScript(" getter(); ");
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
							return "setter();";
						}
					};
					attributes.getAjaxCallListeners().add(listener);
				}
				
			});
			add(new AddressPanel("address_container", details.getAddress()));
			
			TextField<String> display_name = new TextField<String>("display_name");
			display_name.setRequired(true);
			display_name.add(new AjaxOnBlurEvent());
			display_name.setLabel(new StringResourceModel("language.ru", null));
			add(display_name);

			TextField<String> display_name_en = new TextField<String>("display_name_en");
			display_name_en.setRequired(true);
			display_name_en.add(new AjaxOnBlurEvent());
			display_name_en.setLabel(new StringResourceModel("language.en", null));
			add(display_name_en);
			
			TextField<String> display_name_uz = new TextField<String>("display_name_uz");
			display_name_uz.setRequired(true);
			display_name_uz.add(new AjaxOnBlurEvent());
			display_name_uz.setLabel(new StringResourceModel("language.uz", null));
			add(display_name_uz);
			
			TextField<String> contact_number = new TextField<String>("contact_number");
			contact_number.setRequired(true);
			contact_number.add(new AjaxOnBlurEvent());
			contact_number.setLabel(new StringResourceModel("hotels.details.custom_contact_number_1", null));
			add(contact_number);

			TextField<String> contact_number2 = new TextField<String>("contact_number2");
			//contact_number2.setRequired(true);
			//contact_number2.add(new AjaxOnBlurEvent());
			contact_number2.setLabel(new StringResourceModel("hotels.details.custom_contact_number_2", null));
			add(contact_number2);
			
			TextField<String> fax = new TextField<String>("fax");
			fax.setRequired(true);
			fax.add(new AjaxOnBlurEvent());
			fax.setLabel(new StringResourceModel("hotels.details.custom_fax_1", null));
			add(fax);
			
			TextField<String> fax2 = new TextField<String>("fax2"); 
			//fax2.setRequired(true);
			//fax2.add(new AjaxOnBlurEvent());
			fax2.setLabel(new StringResourceModel("hotels.details.custom_fax_2", null));
			add(fax2);

			EmailTextField contact_email = new EmailTextField("contact_email");
			contact_email.setRequired(true);
			contact_email.add(new AjaxOnBlurEvent());;
			contact_email.add(EmailAddressValidator.getInstance());
			contact_email.setLabel(new StringResourceModel("hotels.details.custom_contact_email_1", null));
			add(contact_email);

			EmailTextField contact_email2 = new EmailTextField("contact_email2");
			//contact_email2.setRequired(true);
			//contact_email2.add(new AjaxOnBlurEvent());;
			contact_email2.add(EmailAddressValidator.getInstance());
			contact_email2.setLabel(new StringResourceModel("hotels.details.custom_contact_email_2", null));
			add(contact_email2);
			
			FileUploadField hotelImagesField = new FileUploadField("hotel_images");
			add(hotelImagesField);

//			final BootstrapUploadProgressBar progressBar = new BootstrapUploadProgressBar("progressBar", HotelDetailsForm.this, hotelImagesField, Model.of(20));
//			add(progressBar);
//
//			BootstrapProgressBar progressBar2 = new BootstrapProgressBar("progressBar2", Model.of(50));
//			progressBar2.active(true);
//			progressBar2.striped(true);
//			progressBar2.type(Type.WARNING);
//			add(progressBar2);
//			
//			BootstrapUpdatableProgressBar progressBar3 = new BootstrapUpdatableProgressBar("progressBar3", Model.of(20)) {
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				protected IModel<Integer> newValue() {
//					int newValue = (value() + 5) % BootstrapProgressBar.MAX;
//					 
//					return Model.of(newValue);
//				}
//			};
//			progressBar3.updateInterval(Duration.ONE_SECOND);
//			progressBar3.type(Type.WARNING);
//			add(progressBar3);
			
			details.setHotelImagesfield(hotelImagesField);
			
			add(imagePanel = new ImagesEditPanel("imgPanel", details.getHotelsusers_id(), true));
			imagePanel.setOutputMarkupId(true);
			
			add(new CheckBox("support_resident_rate"));
			
			TextArea<String> description = new TextArea<String>("description");
			description.setLabel(Model.of(getString("hotels.details.description") + "(" + getString("language.ru") + ")"));
			add(description);
			
			TextArea<String> description_en = new TextArea<String>("description_en");
			description_en.setLabel(Model.of(getString("hotels.details.description") + "(" + getString("language.en") + ")"));
			add(description_en);
			
			TextArea<String> description_uz = new TextArea<String>("description_uz");
			description_uz.setLabel(Model.of(getString("hotels.details.description") + "(" + getString("language.uz") + ")"));
			add(description_uz);
			
			CommonUtil.setFormComponentRequired(this);
		}
	}
	
	protected abstract void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback); 
	protected abstract void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback); 
	protected abstract boolean isVisibleNextButton();
}
