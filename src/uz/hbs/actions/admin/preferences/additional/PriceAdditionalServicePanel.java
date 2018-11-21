package uz.hbs.actions.admin.preferences.additional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.AdditionalServicePrice;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class PriceAdditionalServicePanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private FeedbackPanel feedback;
	
	public PriceAdditionalServicePanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		AdditionalServicePrice model = new MyBatisHelper().selectOne("selectCurrentAdditionalServicePrice");
		if (model == null) model = new AdditionalServicePrice();
		
		add(new MyForm("form", model).add(new IndicatingAjaxButton("submit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {
					AdditionalServicePrice model = (AdditionalServicePrice) form.getDefaultModelObject();
					new MyBatisHelper().insert("insertAdditionalServiceOrderPrice", model);
					feedback.success(getString("price.additional.service.change.success"));
				} catch (Exception e) {
					feedback.error(getString("price.additional.service.change.fail"));
				} finally {
					target.add(feedback);
				}
			}
		}));
	}
	
	private class MyForm extends Form<AdditionalServicePrice>{
		private static final long serialVersionUID = 1L;
		//Leonid Ibragimov (booking. uz), [07.05.18 11:18]
		//	"Зеленый коридор (за 1 персону)"
		//	этой услуги в аэропорту больше нет. ее надо закрыть и в в2с не выдавать тоже
		public MyForm(String id, AdditionalServicePrice model) {
			super(id, new CompoundPropertyModel<AdditionalServicePrice>(model));
			add(new TextField<Double>("arrival", Double.class));
			//add(new TextField<Double>("arrival_air_green_hall", Double.class));
			add(new TextField<Double>("arrival_air_vip_hall", Double.class));
			add(new TextField<Double>("departure", Double.class));
			//add(new TextField<Double>("departure_air_green_hall", Double.class));
			add(new TextField<Double>("departure_air_vip_hall", Double.class));
			add(new TextField<Double>("insurance", Double.class));
			add(new EmailTextField("email_send_request").setLabel(new StringResourceModel("price.additional.service.send.request", null)));
			//add(new EmailTextField("email_send_request_green").setLabel(new StringResourceModel("price.additional.service.send.request.green", null)));
			add(new EmailTextField("email_send_request_vip").setLabel(new StringResourceModel("price.additional.service.send.request.vip", null)));
			add(new EmailTextField("email_send_request_insurance").setLabel(new StringResourceModel("price.additional.service.send.request.insurance", null)));
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("additional.service", null);
	}
}

