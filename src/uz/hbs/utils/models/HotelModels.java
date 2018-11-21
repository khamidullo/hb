package uz.hbs.utils.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Country;
import uz.hbs.beans.Equipment;
import uz.hbs.beans.Facility;
import uz.hbs.beans.HotelCategory;
import uz.hbs.beans.HotelNearByPlace;
import uz.hbs.beans.HotelStar;
import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.PaymentMethod;
import uz.hbs.beans.Region;
import uz.hbs.beans.ReservationCancellationPolicy;
import uz.hbs.beans.RoomType;
import uz.hbs.beans.Service;
import uz.hbs.db.MyBatisHelper;

public class HotelModels {
	public static LoadableDetachableModel<List<? extends HotelStar>> getHotelStarsList(final boolean isAscending) {
		LoadableDetachableModel<List<? extends HotelStar>> hotelStarsList = new LoadableDetachableModel<List<? extends HotelStar>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends HotelStar> load() {
				if (isAscending) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("sortOrder", "DESC");
					return new MyBatisHelper().selectList("selectHotelStarsList", params);
				} else {
					return new MyBatisHelper().selectList("selectHotelStarsList");
				}
			}
		};

		return hotelStarsList;
	}

	public static LoadableDetachableModel<List<? extends Country>> getCountriesHasHotelList(String language) {
		LoadableDetachableModel<List<? extends Country>> countriesList = new LoadableDetachableModel<List<? extends Country>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Country> load() {
				Map<String, Object> params = new HashMap<>();
				params.put("locale", language);

				return new MyBatisHelper().selectList("selectCountriesHasHotels", params);
			}
		};

		return countriesList;
	}

	public static LoadableDetachableModel<List<? extends Country>> getCountriesList() {
		LoadableDetachableModel<List<? extends Country>> countriesList = new LoadableDetachableModel<List<? extends Country>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Country> load() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("sortField", "c.name");
				params.put("sortOrder", "ASC");
				return new MyBatisHelper().selectList("selectCountriesList", params);
			}
		};

		return countriesList;
	}

	public static LoadableDetachableModel<List<? extends Region>> getRegionsList(final Country countries) {
		LoadableDetachableModel<List<? extends Region>> regionsList = new LoadableDetachableModel<List<? extends Region>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Region> load() {
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("countries_id", countries != null ? countries.getId() : -1);
				params.put("sortField", "r.name");
				params.put("sortOrder", "ASC");
				return new MyBatisHelper().selectList("selectRegionsList", params);
			}
		};

		return regionsList;
	}

	public static LoadableDetachableModel<List<? extends RoomType>> getRoomTypesModel() {
		LoadableDetachableModel<List<? extends RoomType>> list = new LoadableDetachableModel<List<? extends RoomType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends RoomType> load() {
				return getRoomTypesList();
			}
		};
		return list;
	}

	public static List<RoomType> getRoomTypesList() {
		HashMap<String, Byte> param = new HashMap<String, Byte>();
		param.put("status", RoomType.STATUS_ACTIVE); // Active
		return new MyBatisHelper().selectList("selectRoomTypesList", param);
	}

	public static List<? extends Service> getHotelRoomService() {
		return new MyBatisHelper().selectList("selectAvailableRoomService");
	}

	public static LoadableDetachableModel<List<? extends Service>> getHotelRoomServiceModel() {
		return new LoadableDetachableModel<List<? extends Service>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Service> load() {
				return getHotelRoomService();
			}
		};
	}

	public static LoadableDetachableModel<List<? extends Facility>> getHotelFacilities(final Long hotelsusers_id) {
		return new LoadableDetachableModel<List<? extends Facility>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Facility> load() {
				return new MyBatisHelper().selectList("selectHotelFacilitiesList", hotelsusers_id);
			}
		};
	}

	public static LoadableDetachableModel<List<? extends Service>> getServicesInRooms(final Long hotelsusers_id) {
		return new LoadableDetachableModel<List<? extends Service>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Service> load() {
				return new MyBatisHelper().selectList("selectHotelRoomService", hotelsusers_id);
			}
		};
	}

	public static LoadableDetachableModel<List<? extends HotelNearByPlace>> getHotelNearbyPlaces(final Long hotelsusers_id) {
		return new LoadableDetachableModel<List<? extends HotelNearByPlace>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends HotelNearByPlace> load() {
				return new MyBatisHelper().selectList("selectHotelNearByPlacesByHotelId", hotelsusers_id);
			}
		};
	}

	public static LoadableDetachableModel<List<? extends Short>> getShortListModel(final short count, final boolean withZero) {
		return new LoadableDetachableModel<List<? extends Short>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Short> load() {
				List<Short> list = new ArrayList<Short>();
				if (withZero)
					list.add((short) 0);
				for (short sh = 1; sh <= count; sh++) {
					list.add(sh);
				}
				return list;
			}
		};
	}

	public static LoadableDetachableModel<List<? extends Short>> getShortListModel(final short offset, final short count) {
		return new LoadableDetachableModel<List<? extends Short>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Short> load() {
				List<Short> list = new ArrayList<Short>();
				for (short sh = offset; sh <= count; sh++) {
					list.add(sh);
				}
				return list;
			}
		};
	}

	public static LoadableDetachableModel<List<? extends Byte>> getTimeModel(final byte time) {
		return new LoadableDetachableModel<List<? extends Byte>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Byte> load() {
				List<Byte> list = new ArrayList<Byte>();
				for (byte b = 0; b <= time; b++) {
					list.add(b);
				}
				return list;
			}
		};
	}

	public static LoadableDetachableModel<List<? extends PaymentMethod>> getPaymentMethodModel() {
		return new LoadableDetachableModel<List<? extends PaymentMethod>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<PaymentMethod> load() {
				return Arrays.asList(new PaymentMethod(PaymentMethod.CASH, new StringResourceModel("payment_method.cash", null).getString()),
						new PaymentMethod(PaymentMethod.BANK_CARD_VISA, new StringResourceModel("payment_method.bank_card.visa", null).getString()),
						new PaymentMethod(PaymentMethod.BANK_CARD_MASTER,
								new StringResourceModel("payment_method.bank_card.master", null).getString()),
						new PaymentMethod(PaymentMethod.BANK_TRANSFER, new StringResourceModel("payment_method.bank_transfer", null).getString()),
						new PaymentMethod(PaymentMethod.OTHER, new StringResourceModel("payment_method.other", null).getString()));
			}
		};
	}

	public static LoadableDetachableModel<List<? extends IdAndValue>> getNoPenaltyBeforeDaysModel() {
		return new LoadableDetachableModel<List<? extends IdAndValue>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<IdAndValue> load() {
				List<IdAndValue> list = new ArrayList<IdAndValue>();
				list.add(new IdAndValue(ReservationCancellationPolicy.NoPenaltyOnCancelation, new StringResourceModel("day.-1", null).getString()));
				list.add(new IdAndValue(1, new StringResourceModel("day.1", null).getString()));
				list.add(new IdAndValue(2, new StringResourceModel("day.2", null).getString()));
				list.add(new IdAndValue(3, new StringResourceModel("day.3", null).getString()));
				list.add(new IdAndValue(4, new StringResourceModel("day.4", null).getString()));
				for (int i = 5; i <= 60; i++)
					list.add(new IdAndValue(i, new StringResourceModel("day.other", null, new Object[] { i }).getString()));
				return list;
			}
		};
	}

	public static LoadableDetachableModel<List<? extends IdAndValue>> getSupportTentativeReservationDueDayModel() {
		return new LoadableDetachableModel<List<? extends IdAndValue>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<IdAndValue> load() {
				List<IdAndValue> list = new ArrayList<IdAndValue>();
				list.add(new IdAndValue(1, new StringResourceModel("day.1", null).getString()));
				list.add(new IdAndValue(2, new StringResourceModel("day.2", null).getString()));
				list.add(new IdAndValue(3, new StringResourceModel("day.3", null).getString()));
				list.add(new IdAndValue(4, new StringResourceModel("day.4", null).getString()));
				list.add(new IdAndValue(5, new StringResourceModel("day.5", null).getString()));
				list.add(new IdAndValue(6, new StringResourceModel("day.6", null).getString()));
				list.add(new IdAndValue(7, new StringResourceModel("day.7", null).getString()));
				list.add(new IdAndValue(8, new StringResourceModel("day.8", null).getString()));
				list.add(new IdAndValue(9, new StringResourceModel("day.9", null).getString()));
				list.add(new IdAndValue(10, new StringResourceModel("day.10", null).getString()));
				for (int i = 11; i <= 45; i++)
					list.add(new IdAndValue(i, new StringResourceModel("day.other", null, new Object[] { i }).getString()));
				return list;
			}
		};
	}

	public static LoadableDetachableModel<List<? extends Short>> getAgeModel(final short age) {
		return new LoadableDetachableModel<List<? extends Short>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Short> load() {
				List<Short> list = new ArrayList<Short>();
				for (short i = age; i <= 16; i++)
					list.add(i);
				return list;
			}
		};
	}

	public static String getNearByPlacesType(byte type) {
		switch (type) {
			case HotelNearByPlace.CITY_CENTER:
				return new StringResourceModel("hotels.details.nearbyplaces.city_center", null).getString();
			case HotelNearByPlace.AIROPORT:
				return new StringResourceModel("hotels.details.nearbyplaces.airport", null).getString();
			case HotelNearByPlace.TRAIN:
				return new StringResourceModel("hotels.details.nearbyplaces.train", null).getString();
			case HotelNearByPlace.METRO:
				return new StringResourceModel("hotels.details.nearbyplaces.metro", null).getString();
			case HotelNearByPlace.OTHERs:
				return new StringResourceModel("hotels.details.nearbyplaces.others", null).getString();
		}
		return null;
	}

	public static LoadableDetachableModel<List<? extends HotelCategory>> getHotelsCategories() {
		return new LoadableDetachableModel<List<? extends HotelCategory>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends HotelCategory> load() {
				return new MyBatisHelper().selectList("selectHotelsCategories");
			}
		};
	}

	public static LoadableDetachableModel<List<Equipment>> getHotelsEquipments(final long hoteslusers_id) {
		return new LoadableDetachableModel<List<Equipment>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Equipment> load() {
				return new MyBatisHelper().selectList("selectHotelsEquipments", hoteslusers_id);
			}
		};
	}
}
