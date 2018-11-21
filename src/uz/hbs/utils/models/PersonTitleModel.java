package uz.hbs.utils.models;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.PersonTitle;

public class PersonTitleModel {
	
	public static LoadableDetachableModel<List<? extends PersonTitle>> getPersonTitle(){
		return new LoadableDetachableModel<List<? extends PersonTitle>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<PersonTitle> load() {
				return Arrays.asList(new PersonTitle[]{
						new PersonTitle(PersonTitle.MR, 	new StringResourceModel("person_title.mr", null).getString()),
						new PersonTitle(PersonTitle.MRs, 	new StringResourceModel("person_title.mrs", null).getString())
				});
			}
		};
	}
	
	public static String toString(String title){
		switch (title) {
			case PersonTitle.MR: return new StringResourceModel("person_title.mr", null).getString();
			case PersonTitle.MRs: return new StringResourceModel("person_title.mrs", null).getString();
		}
		return "";
	}
}