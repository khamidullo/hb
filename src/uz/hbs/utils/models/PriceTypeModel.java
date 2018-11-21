package uz.hbs.utils.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.PriceType;

public class PriceTypeModel {
	
	public PriceTypeModel() {
	}
	
	public static LoadableDetachableModel<List<? extends PriceType>> getPriceTypeListModel(){
		return new LoadableDetachableModel<List<? extends PriceType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<PriceType> load() {
				List<PriceType> list = new ArrayList<PriceType>();
				list.add(new PriceType(PriceType.IN_PERCENT, new StringResourceModel("pricetype.in_percent", null).getString()));
				list.add(new PriceType(PriceType.FIXED_AMOUNT, new StringResourceModel("pricetype.fixed_amount", null).getString()));
				return list;
			}
		};
	}
	
	public static LoadableDetachableModel<List<? extends Byte>> getPriceTypeShortListModel(){
		return new LoadableDetachableModel<List<? extends Byte>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Byte> load() {
				List<Byte> list = Arrays.asList(new Byte[]{PriceType.IN_PERCENT,PriceType.FIXED_AMOUNT});
				return list;
			}
		};
	}
	
	public static class PriceTypeShortChoiceRenderer extends ChoiceRenderer<Byte> {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Object getDisplayValue(Byte object) {
			return object == PriceType.IN_PERCENT ? "%" : ".00";
		}
		
		@Override
		public String getIdValue(Byte object, int index) {
			return String.valueOf(object);
		}
		
	}
}
