package uz.hbs.utils.models;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Gender;

public class GenderModel{
	
	public static LoadableDetachableModel<List<Gender>> getGenderListModel(){
		return new LoadableDetachableModel<List<Gender>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<Gender> load() {
				return Arrays.asList(new Gender(Gender.MALE, new StringResourceModel("gender.male", null).getString()),
						 		     new Gender(Gender.FEMALE, new StringResourceModel("gender.female", null).getString()));
			}
		};
	}
	
	public static String getGenderValue(byte b){
		if (b == Gender.MALE) return new StringResourceModel("gender.male", null).getString();
		else if (b == Gender.FEMALE) return new StringResourceModel("gender.female", null).getString();
		return "Unknown";
	}
}
