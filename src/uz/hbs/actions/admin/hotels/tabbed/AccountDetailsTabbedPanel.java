package uz.hbs.actions.admin.hotels.tabbed;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import uz.hbs.beans.Hotel;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.components.panels.contract.ContractPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;

public abstract class AccountDetailsTabbedPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;

	public AccountDetailsTabbedPanel(String id, IModel<Hotel> model) {
		super(id, model);
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		final Hotel hotel = model.getObject();
		
		final Form<Hotel> form;

		add(form = new AccountDetailsForm("form", hotel));
		
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
			public boolean isEnabled() {
				return hotel.getStep() == Hotel.HOTEL_ACCOUNT_DETAILS;
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
				return hotel.getStep() == Hotel.HOTEL_ACCOUNT_DETAILS;
			}
		});
	}
	
	private class AccountDetailsForm extends Form<Hotel>{
		private static final long serialVersionUID = 1L;
		private boolean required = true;

		public AccountDetailsForm(String id, Hotel hotel) {
			super(id, new CompoundPropertyModel<Hotel>(hotel));
			add(new TextField<String>("legal_name").setLabel(new StringResourceModel("hotels.legal.name", null)).setRequired(required).add(new AjaxOnBlurEvent()));
			add(new EmailTextField("corporate_email").setLabel(new StringResourceModel("hotels.corporate.email", null)).add(EmailAddressValidator.getInstance()).setRequired(required).add(new AjaxOnBlurEvent()));
			add(new TextField<String>("primary_phone").setLabel(new StringResourceModel("hotels.primary.contact", null)).setRequired(required).add(new AjaxOnBlurEvent()));
			//add(new TextField<String>("secondary_phone").setLabel(new StringResourceModel("hotels.secondary.contact", null)).add(new AjaxOnBlurEvent()));
			add(new TextField<String>("manager").setLabel(new StringResourceModel("hotels.manager", null)).setRequired(required));//.add(new AjaxOnBlurEvent()));
			add(new EmailTextField("manager_email").setLabel(new StringResourceModel("hotels.manager.email", null)).add(EmailAddressValidator.getInstance()).setRequired(required).add(new AjaxOnBlurEvent()));
			add(new TextField<String>("accountant").setLabel(new StringResourceModel("hotels.manager.accountant", null)).setRequired(false).add(new AjaxOnBlurEvent()));
			add(new TextField<String>("accountant_phone").setLabel(new StringResourceModel("hotels.phone", null)).setRequired(false).add(new AjaxOnBlurEvent()));
			add(new TextField<String>("reservation_dep").setLabel(new StringResourceModel("hotels.manager.reservation_dep", null)).setRequired(false).add(new AjaxOnBlurEvent()));
			add(new TextField<String>("reservation_dep_phone").setLabel(new StringResourceModel("hotels.phone", null)).setRequired(false).add(new AjaxOnBlurEvent()));
			add(new ContractPanel("contractPanel", hotel.getContract()));
			CommonUtil.setFormComponentRequired(this);
		}
		
//		@Override
//		public void renderHead(IHeaderResponse response) {
//			super.renderHead(response);
//			response.render(JavaScriptHeaderItem.forUrl("js/hb.js"));
//		}
	}
	
	protected abstract void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback); 
	protected abstract void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback); 
	protected abstract boolean isVisibleNextButton();
}