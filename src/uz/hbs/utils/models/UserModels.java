package uz.hbs.utils.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.User;

public class UserModels {
	public static LoadableDetachableModel<List<? extends IdAndValue>> getStatusList() {
		LoadableDetachableModel<List<? extends IdAndValue>> statusList = new LoadableDetachableModel<List<? extends IdAndValue>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends IdAndValue> load() {
				List<IdAndValue> list = new ArrayList<IdAndValue>();
				list.add(new IdAndValue((int) User.STATUS_NEW, new StringResourceModel("users.status" + User.STATUS_NEW, null).getString()));
				list.add(new IdAndValue((int) User.STATUS_ACTIVE, new StringResourceModel("users.status" + User.STATUS_ACTIVE, null).getString()));
				list.add(new IdAndValue((int) User.STATUS_DISABLED, new StringResourceModel("users.status" + User.STATUS_DISABLED, null).getString()));
				return list;
			}
		};
		return statusList;
	}

	public static LoadableDetachableModel<List<? extends IdAndValue>> getStatusList(final boolean withNew) {
		LoadableDetachableModel<List<? extends IdAndValue>> statusList = new LoadableDetachableModel<List<? extends IdAndValue>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends IdAndValue> load() {
				List<IdAndValue> list = new ArrayList<IdAndValue>();
				if (withNew)
					list.add(new IdAndValue((int) User.STATUS_NEW, new StringResourceModel("users.status" + User.STATUS_NEW, null).getString()));
				list.add(new IdAndValue((int) User.STATUS_ACTIVE, new StringResourceModel("users.status" + User.STATUS_ACTIVE, null).getString()));
				list.add(new IdAndValue((int) User.STATUS_DISABLED, new StringResourceModel("users.status" + User.STATUS_DISABLED, null).getString()));
				return list;
			}
		};
		return statusList;
	}

	public static String getUserStatus(int status) {
		switch (status) {
			case User.STATUS_NEW:
				return new StringResourceModel("users.status" + User.STATUS_NEW, null).getString();
			case User.STATUS_ACTIVE:
				return new StringResourceModel("users.status" + User.STATUS_ACTIVE, null).getString();
			case User.STATUS_DISABLED:
				return new StringResourceModel("users.status" + User.STATUS_DISABLED, null).getString();
		}
		return "";
	}

	public static LoadableDetachableModel<List<? extends IdAndValue>> getTypeList(final boolean isOnlyUsers, final Integer userType) {
		LoadableDetachableModel<List<? extends IdAndValue>> typeList = new LoadableDetachableModel<List<? extends IdAndValue>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends IdAndValue> load() {
				List<IdAndValue> list = new ArrayList<IdAndValue>();
				if (userType == null) { 
					// list.add(new IdAndValue((int) User.TYPE_UNDEFINED, new StringResourceModel("users.type" + User.TYPE_UNDEFINED, null).getString()));
					list.add(new IdAndValue((int) User.TYPE_ADMIN_USER, new StringResourceModel("users.type" + User.TYPE_ADMIN_USER, null).getString()));
					if (!isOnlyUsers)
						list.add(new IdAndValue((int) User.TYPE_HOTEL, new StringResourceModel("users.type" + User.TYPE_HOTEL, null).getString()));
					list.add(new IdAndValue((int) User.TYPE_HOTEL_USER, new StringResourceModel("users.type" + User.TYPE_HOTEL_USER, null).getString()));
					if (!isOnlyUsers)
						list.add(new IdAndValue((int) User.TYPE_TOURAGENCY, new StringResourceModel("users.type" + User.TYPE_TOURAGENCY, null).getString()));
					list.add(new IdAndValue((int) User.TYPE_TOURAGENT_USER, new StringResourceModel("users.type" + User.TYPE_TOURAGENT_USER, null).getString()));
					list.add(new IdAndValue((int) User.TYPE_SPECTATOR_USER, new StringResourceModel("users.type" + User.TYPE_SPECTATOR_USER, null).getString()));
				} else {
					list.add(new IdAndValue(userType, new StringResourceModel("users.type" + userType, null).getString()));
				}
				return list;
			}
		};
		return typeList;
	}
}
