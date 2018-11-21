package uz.hbs.markup.html.form;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;

import uz.hbs.beans.City;
import uz.hbs.db.MyBatisHelper;

public class SelectCityField extends FormComponentPanel<City> {
	private static final long serialVersionUID = 1L;
	private DropDownChoice<City> dropDownChoice;
	private City city;
	
	public SelectCityField(String id) {
		this(id, null);
	}	
	
	public SelectCityField(String id, IModel<City> model) {
		super(id, model);
		
		add(dropDownChoice = new DropDownChoice<City>("select", new PropertyModel<City>(this, "city"), new LoadableDetachableModel<List<City>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<City> load() {
				return new MyBatisHelper().selectList("selectCitiesNameListForHotel");
			}
		}, new ChoiceRenderer<City>("name", "id")) {
	        private static final long serialVersionUID = 1L;
	        private City last;

	        private boolean isLast(int index) {
	            return index - 1 == getChoices().size();
	        }

	        private boolean isFirst(int index) {
	            return index == 0;
	        }

	        private boolean isNewGroup(City current) {
	            return last == null || ! current.getRegions_id().equals(last.getRegions_id());
	        }

	        @Override
	        protected void appendOptionHtml(AppendingStringBuffer buffer, City choice, int index, String selected) {
	            if (isNewGroup(choice)) {
	                if (!isFirst(index)) {
	                    buffer.append("</optgroup>");
	                }
	                buffer.append("<optgroup label='");
	                buffer.append(Strings.escapeMarkup(choice.getRegion_name()));
	                buffer.append("'>");
	            }
	            super.appendOptionHtml(buffer, choice, index, selected);
	            if (isLast(index)) {
	                buffer.append("</optgroup>");
	            }
	            last = choice;
	        }
	    });
		dropDownChoice.setOutputMarkupId(true);
		dropDownChoice.setLabel(new StringResourceModel("hotels.details.city", null));
		if (dropDownChoice.isRequired()) dropDownChoice.add(new AttributeModifier("required", true));
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
		setDefaultModelObject(city);
	}
	
	@Override
	public String getInput(){
		return getDropDownChoice().getInput();
	}
	
	@Override
	protected void convertInput(){
		City city = getDropDownChoice().getConvertedInput();
		if (city != null) setConvertedInput(city);
		else setConvertedInput(null);
	}
	
	@Override
	protected void onBeforeRender()	{
		getDropDownChoice().setRequired(isRequired());
		city = (City) getDefaultModelObject();
		super.onBeforeRender();
	}

	public DropDownChoice<City> getDropDownChoice() {
		return dropDownChoice;
	}
}
