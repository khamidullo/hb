package uz.hbs.utils.models;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

import uz.hbs.beans.IdAndName;
import uz.hbs.db.MyBatisHelper;

public class EquipmentListModel extends LoadableDetachableModel<List<IdAndName>> {
	private static final long serialVersionUID = 1L;

	public EquipmentListModel() {
		
	}

	@Override
	protected List<IdAndName> load() {
		List<IdAndName> list = Collections.emptyList();
		list = new MyBatisHelper().selectList("selectEquipments");
		return list;
	}

}
