package uz.hbs.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

public class ReferenceInfo implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Short floors;
	private Integer rooms;
	
	private MealOption meal_options;
	
	private List<Equipment> equipments;
	private List<RoomType> room_types;
	private HashMap<String, Boolean> roomtypes;
	private List<Condition> conditions;
	private Byte self_payment_possibility;
	
	public ReferenceInfo() {
		equipments = new ArrayList<Equipment>();
		room_types = new ArrayList<RoomType>();
		conditions = new ArrayList<Condition>();
		roomtypes = new HashMap<String, Boolean>();
		meal_options = new MealOption();
	}
	
	public Short getFloors() {
		return floors;
	}

	public void setFloors(Short floors) {
		this.floors = floors;
	}

	public List<Equipment> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}

	public List<RoomType> getRoom_types() {
		return room_types;
	}

	public void setRoom_types(List<RoomType> room_types) {
		this.room_types = room_types;
	}
	
	public HashMap<String, Boolean> getRoomtypes() {
		return roomtypes;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public MealOption getMeal_options() {
		return meal_options;
	}

	public void setMeal_options(MealOption meal_options) {
		this.meal_options = meal_options;
	}

	public Integer getRooms() {
		return rooms;
	}

	public void setRooms(Integer rooms) {
		this.rooms = rooms;
	}

	public Byte getSelf_payment_possibility() {
		return self_payment_possibility;
	}

	public void setSelf_payment_possibility(Byte self_payment_possibility) {
		this.self_payment_possibility = self_payment_possibility;
	}
}
