package uz.hbs.utils.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.AdditionalBed;
import uz.hbs.beans.BedType;
import uz.hbs.beans.Condition;
import uz.hbs.beans.Equipment;
import uz.hbs.beans.HoldingCapacity;
import uz.hbs.beans.IdAndName;
import uz.hbs.beans.Room;
import uz.hbs.db.MyBatisHelper;

public class RoomSetupModel {
	
	public static LoadableDetachableModel<List<HoldingCapacity>> getHoldingCapacity(){
		return new LoadableDetachableModel<List<HoldingCapacity>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<HoldingCapacity> load() {
				List<HoldingCapacity> list = new ArrayList<HoldingCapacity>();
				for (byte b = 1; b <= 25; b++) list.add(new HoldingCapacity(b, String.valueOf(b)));
				return list;
			}
		};
	}
	
	public static String getHoldingCapacity(byte capacity){
		if (capacity >= 25) return new StringResourceModel("holding.capacity.more", null).getString();
		return String.valueOf(capacity);
	}
	
	public static LoadableDetachableModel<List<AdditionalBed>> getAdditionalBed(){
		return new LoadableDetachableModel<List<AdditionalBed>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<AdditionalBed> load() {
				List<AdditionalBed> list = new ArrayList<AdditionalBed>();
				list.add(new AdditionalBed(AdditionalBed.APPLICABLE, new StringResourceModel("additional.bed.applicable", null).getString()));
				list.add(new AdditionalBed(AdditionalBed.INAPPLICABLE, new StringResourceModel("additional.bed.inapplicable", null).getString()));
				return list;
			}
		};
	}
	
	public static LoadableDetachableModel<List<BedType>> getBedType(){
		return new LoadableDetachableModel<List<BedType>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected List<BedType> load() {
				List<BedType> list = new ArrayList<BedType>();
				list.add(new BedType(BedType.SINGLE, new StringResourceModel("bed.type.single", null).getString()));
				list.add(new BedType(BedType.DOUBLE, new StringResourceModel("bed.type.double", null).getString()));
				list.add(new BedType(BedType.TWIN,   new StringResourceModel("bed.type.twin", null).getString()));
				list.add(new BedType(BedType.TRIPLE, new StringResourceModel("bed.type.triple", null).getString()));
				list.add(new BedType(BedType.QUADRUPLE, new StringResourceModel("bed.type.quadruple", null).getString()));
				for (byte b = 6; b <= 26; b++) 
					list.add(new BedType(b, new StringResourceModel("bed.type.more", null, new Object[]{ b - 1 }).getString()));
				return list;
			}
		};
	}
	
	public static LoadableDetachableModel<List<IdAndName>> getRoomFloors(final int floors){
		if (floors < 1) throw new IllegalArgumentException("number of floors must be greater than 1 floor");
		return new LoadableDetachableModel<List<IdAndName>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<IdAndName> load() {
				List<IdAndName> list = new ArrayList<IdAndName>();
				for (int floor = 1; floor <= floors; floor ++) list.add(new IdAndName(floor, String.valueOf(floor)));
				return list;
			}
		};
	}
	
	public static List<Short> getNumberOfFloor(int floors){
		if (floors < 1) throw new IllegalArgumentException("number of floors must be greater than 1 floor");
		List<Short> list = new ArrayList<Short>();
		for (short floor = 1; floor <= floors; floor ++) list.add(floor);
		return list;
	}
	
	public static List<Equipment> getAvailableEquipments(){
		return new MyBatisHelper().selectList("selectEquipments");
	}
	
	public static LoadableDetachableModel<List<Equipment>> getAvailableEquipmentsModel() {
		return new LoadableDetachableModel<List<Equipment>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Equipment> load() {
				return new MyBatisHelper().selectList("selectEquipments");
			}
		};
	}

	public static List<Condition> getAvailableCondition(final long hotelsusers_id){
		List<Condition> list = new MyBatisHelper().selectList("selectConditionsByHotel", hotelsusers_id);
		return list;
	}
	
	public static List<IModel<Room>> getRooms(){
		List<IModel<Room>> list = new ArrayList<IModel<Room>>();
		list.add(Model.of(new Room()));
		list.add(Model.of(new Room()));
		return list;
	}
	
	public static List<Room> getRoomsList(int number_of_rooms){
		if (number_of_rooms < 1) throw new IllegalArgumentException("number of floors must be greater than 1 floor");
		List<Room> list = new ArrayList<Room>();
		for (long i = 1; i<=number_of_rooms; i++) list.add(new Room(i));
		return list;
	}
}
