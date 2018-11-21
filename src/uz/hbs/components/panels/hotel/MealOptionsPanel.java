package uz.hbs.components.panels.hotel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;

import uz.hbs.beans.MealOption;
import uz.hbs.components.ajax.AjaxOnBlurEvent;

public class MealOptionsPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public MealOptionsPanel(String id, IModel<MealOption> model) {
		super(id, model);
		
		final WebMarkupContainer breakfast_container = new WebMarkupContainer("breakfast_container");
		breakfast_container.setOutputMarkupId(true);
		breakfast_container.setEnabled(model.getObject().isBreakfast());
		add(breakfast_container);

		
		final TextField<Double> breakfast_per_person_per_night = new TextField<Double>("meal_options.breakfast_per_person_per_night");
		breakfast_per_person_per_night.setLabel(new StringResourceModel("hotels.reference_info.meal_options.costs", null));
		breakfast_per_person_per_night.setEnabled(model.getObject().isBreakfast() && !model.getObject().isBreakfast_included_to_room_rate());
		breakfast_per_person_per_night.setOutputMarkupId(true);
		breakfast_per_person_per_night.add(new AjaxOnBlurEvent());
		breakfast_container.add(breakfast_per_person_per_night);
		
		AjaxCheckBox breakfastIcludedCheckBox = new AjaxCheckBox("meal_options.breakfast_included_to_room_rate") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String value = getValue();
				breakfast_per_person_per_night.setEnabled(!Strings.isTrue(value));
				breakfast_per_person_per_night.setRequired(!Strings.isTrue(value));
				target.add(breakfast_per_person_per_night);
			}
		};
		breakfastIcludedCheckBox.setLabel(new StringResourceModel("hotels.reference_info.meal_options.included_to_room_rate", null));
		breakfast_container.add(breakfastIcludedCheckBox);

		add(new AjaxCheckBox("meal_options.breakfast") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String value = getValue();
				breakfast_per_person_per_night.setEnabled(Strings.isTrue(value));
				breakfast_per_person_per_night.setRequired(Strings.isTrue(value));
				breakfast_container.setEnabled(Strings.isTrue(value));
				target.add(breakfast_per_person_per_night);
				target.add(breakfast_container);
			}
		}.setLabel(new StringResourceModel("hotels.reference_info.meal_options.breakfast", null)));

		final WebMarkupContainer lunch_container = new WebMarkupContainer("lunch_container");
		lunch_container.setOutputMarkupId(true);
		lunch_container.setEnabled(model.getObject().isLunch());
		add(lunch_container);

		
		final TextField<Double> lunch_per_person_per_night = new TextField<Double>("meal_options.lunch_per_person_per_night");
		lunch_per_person_per_night.setLabel(new StringResourceModel("hotels.reference_info.meal_options.costs", null));
		lunch_per_person_per_night.setEnabled(model.getObject().isLunch() && !model.getObject().isLunch_included_to_room_rate());
		lunch_per_person_per_night.setOutputMarkupId(true);
		lunch_per_person_per_night.add(new AjaxOnBlurEvent());
		lunch_container.add(lunch_per_person_per_night);
		
		AjaxCheckBox lunchIncludedCheckBox = new AjaxCheckBox("meal_options.lunch_included_to_room_rate") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String value = getValue();
				lunch_per_person_per_night.setEnabled(!Strings.isTrue(value));
				lunch_per_person_per_night.setRequired(!Strings.isTrue(value));
				target.add(lunch_per_person_per_night);
			}
		};
		lunchIncludedCheckBox.setLabel(new StringResourceModel("hotels.reference_info.meal_options.included_to_room_rate", null));
		lunch_container.add(lunchIncludedCheckBox);
		
		add(new AjaxCheckBox("meal_options.lunch") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String value = getValue();
				lunch_per_person_per_night.setEnabled(Strings.isTrue(value));
				lunch_per_person_per_night.setRequired(Strings.isTrue(value));
				lunch_container.setEnabled(Strings.isTrue(value));
				target.add(lunch_per_person_per_night);
				target.add(lunch_container);
			}
		}.setLabel(new StringResourceModel("hotels.reference_info.meal_options.lunch", null)));
		
		final WebMarkupContainer dinner_container = new WebMarkupContainer("dinner_container");
		dinner_container.setOutputMarkupId(true);
		dinner_container.setEnabled(model.getObject().isDinner());
		add(dinner_container);
		
		final TextField<Double> dinner_per_person_per_night = new TextField<Double>("meal_options.dinner_per_person_per_night");
		dinner_per_person_per_night.setLabel(new StringResourceModel("hotels.reference_info.meal_options.costs", null));
		dinner_per_person_per_night.setEnabled(model.getObject().isDinner() && !model.getObject().isDinner_included_to_room_rate());
		dinner_per_person_per_night.setOutputMarkupId(true);
		dinner_per_person_per_night.add(new AjaxOnBlurEvent());
		dinner_container.add(dinner_per_person_per_night);
		
		AjaxCheckBox dinnerIncludedCheckBox = new AjaxCheckBox("meal_options.dinner_included_to_room_rate") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String value = getValue();
				dinner_per_person_per_night.setEnabled(!Strings.isTrue(value));
				dinner_per_person_per_night.setRequired(!Strings.isTrue(value));
				target.add(dinner_per_person_per_night);
			}
		};
		dinnerIncludedCheckBox.setLabel(new StringResourceModel("hotels.reference_info.meal_options.included_to_room_rate", null));
		dinner_container.add(dinnerIncludedCheckBox);

		add(new AjaxCheckBox("meal_options.dinner") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String value = getValue();
				dinner_per_person_per_night.setEnabled(Strings.isTrue(value));
				dinner_per_person_per_night.setRequired(Strings.isTrue(value));
				dinner_container.setEnabled(Strings.isTrue(value));
				target.add(dinner_per_person_per_night);
				target.add(dinner_container);
			}
		}.setLabel(new StringResourceModel("hotels.reference_info.meal_options.dinner", null)));
	}

}
