package uz.hbs.utils.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.User;

public class ReserveModels {
	public static LoadableDetachableModel<List<? extends IdAndValue>> getB2CStatusList() {
		LoadableDetachableModel<List<? extends IdAndValue>> b2cStatusList = new LoadableDetachableModel<List<? extends IdAndValue>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends IdAndValue> load() {
				List<IdAndValue> list = new ArrayList<IdAndValue>();
				list.add(new IdAndValue((int) User.STATUS_ACTIVE, new StringResourceModel("users.status" + User.STATUS_ACTIVE, null).getString()));
				list.add(new IdAndValue((int) User.STATUS_DISABLED, new StringResourceModel("users.status" + User.STATUS_DISABLED, null).getString()));
				return list;
			}
		};
		return b2cStatusList;
	}

	public static LoadableDetachableModel<List<? extends String>> getArrivalTimeListModel() {
		return new LoadableDetachableModel<List<? extends String>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<String> load() {
				List<String> list = new ArrayList<String>();
				list.add("Не знаю");
				list.add("00:00 - 01:00");
				list.add("01:00 - 02:00");
				list.add("02:00 - 03:00");
				list.add("03:00 - 04:00");
				list.add("04:00 - 05:00");
				list.add("05:00 - 06:00");
				list.add("06:00 - 07:00");
				list.add("07:00 - 08:00");
				list.add("08:00 - 09:00");
				list.add("09:00 - 10:00");
				list.add("10:00 - 11:00");
				list.add("11:00 - 12:00");
				list.add("12:00 - 13:00");
				list.add("13:00 - 14:00");
				list.add("14:00 - 15:00");
				list.add("15:00 - 16:00");
				list.add("16:00 - 17:00");
				list.add("17:00 - 18:00");
				list.add("18:00 - 19:00");
				list.add("19:00 - 20:00");
				list.add("20:00 - 21:00");
				list.add("21:00 - 22:00");
				list.add("22:00 - 23:00");
				list.add("23:00 - 24:00");
				return list;
			}
		};
	}
}
