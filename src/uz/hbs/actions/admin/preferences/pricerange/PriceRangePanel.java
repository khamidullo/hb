package uz.hbs.actions.admin.preferences.pricerange;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.PricesRange;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class PriceRangePanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public PriceRangePanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		
		PricesRange pricesRange = new MyBatisHelper().selectOne("selectPricesRange");
		
		final Form<PricesRange> form  = new Form<PricesRange>("form", new CompoundPropertyModel<PricesRange>(pricesRange));
		form.setOutputMarkupId(true);
		add(form);
		
		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		form.add(feedback);
		
		NumberTextField<Double> start_price = new NumberTextField<Double>("start_price");
		start_price.setLabel(new StringResourceModel("pricerange.minimum", null));
		start_price.setRequired(true);
		form.add(start_price);
		
		NumberTextField<Double> end_price = new NumberTextField<Double>("end_price");
		end_price.setLabel(new StringResourceModel("pricerange.maximum", null));
		end_price.setRequired(true);
		form.add(end_price);
		
		form.add(new AjaxButton("submit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				PricesRange pricesRange = (PricesRange) form.getModelObject();
				new MyBatisHelper().update("updatePricesRange", pricesRange);
				feedback.success(new StringResourceModel("update.successfully", null).getString());
				target.add(feedback);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
		});
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("pricerange", null);
	}
}
