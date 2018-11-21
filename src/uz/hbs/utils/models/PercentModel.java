package uz.hbs.utils.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

import uz.hbs.beans.IdAndValue;

public class PercentModel {
	
	public static LoadableDetachableModel<List<IdAndValue>> getPercent(){
		return new LoadableDetachableModel<List<IdAndValue>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<IdAndValue> load() {
				List<IdAndValue> list = new ArrayList<IdAndValue>();
				list.add(new IdAndValue(150, "150 %"));
				list.add(new IdAndValue(125, "125 %"));
				list.add(new IdAndValue(100, "100 %"));
				list.add(new IdAndValue(75,  "75 %"));
				list.add(new IdAndValue(50, "50 %"));
				list.add(new IdAndValue(25, "25 %"));
				list.add(new IdAndValue(0, "0 %"));
				return list;
			}
		};
	}
	
	public static LoadableDetachableModel<List<IdAndValue>> getDiscount(){
		return new LoadableDetachableModel<List<IdAndValue>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected List<IdAndValue> load() {
				List<IdAndValue> list = new ArrayList<IdAndValue>();
				for (int i = 0; i <= 100; i += 5){
					list.add(new IdAndValue(i, i + " %"));
				}
				return list;
			}
		};
	}
}
