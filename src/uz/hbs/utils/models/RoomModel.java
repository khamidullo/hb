package uz.hbs.utils.models;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.CleanState;
import uz.hbs.beans.OnSaleState;
import uz.hbs.beans.RoomState;
import uz.hbs.db.MyBatisHelper;

public class RoomModel {
	
	public static LoadableDetachableModel<List<? extends CleanState>> getCleanModel(){
		return new LoadableDetachableModel<List<? extends CleanState>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<CleanState> load() {
				return Arrays.asList(new CleanState(CleanState.UNCLEAN, new StringResourceModel("hotels.room.status.clean.unclean", null).getString()),
									 new CleanState(CleanState.CLEAN, new StringResourceModel("hotels.room.status.clean.clean", null).getString()));
			}
		};
	}

	public static LoadableDetachableModel<List<? extends OnSaleState>> getOnSaleModel(){
		return new LoadableDetachableModel<List<? extends OnSaleState>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected List<OnSaleState> load() {
				return Arrays.asList(new OnSaleState(OnSaleState.CAN_SALE, new StringResourceModel("hotels.room.status.onsale.can_sale", null).getString()),
						             new OnSaleState(OnSaleState.UNDER_REPAIR, new StringResourceModel("hotels.room.status.onsale.under_repair", null).getString()));
			}
		};
	}
	
	public static LoadableDetachableModel<List<? extends RoomState>> getRoomStateModel(){
		return new LoadableDetachableModel<List<? extends RoomState>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected List<RoomState> load() {
				return Arrays.asList(new RoomState(RoomState.VACANT, new StringResourceModel("hotels.room.status.vacant", null).getString()),
						             new RoomState(RoomState.OCCUPIED, new StringResourceModel("hotels.room.status.occupied", null).getString()));
			}
		};
	}
	
	public static String cleanStateToString(byte id){
		switch (id){
		case CleanState.CLEAN: return new StringResourceModel("hotels.room.status.clean.clean", null).getString();
		case CleanState.UNCLEAN: return new StringResourceModel("hotels.room.status.clean.unclean", null).getString();
		default: return new StringResourceModel("hotels.room.status.clean.other", null).getString();
		}
	}
	
	public static String onSaleStateToString(byte id){
		switch (id){
		case OnSaleState.CAN_SALE: return new StringResourceModel("hotels.room.status.onsale.closed", null).getString();
		case OnSaleState.UNDER_REPAIR: return new StringResourceModel("hotels.room.status.onsale.under_repair", null).getString();
		default: return new StringResourceModel("hotels.room.status.onsale.unknown", null).getString();
		}
	}
	
	public static String roomStateToString(byte id){
		switch (id){
		case RoomState.VACANT: return new StringResourceModel("hotels.room.status.vacant", null).getString();
		case RoomState.OCCUPIED: return new StringResourceModel("hotels.room.status.occupied", null).getString();
		default: return new StringResourceModel("hotels.room.status.onsale.unknown", null).getString();
		}
	}
	
	public static String roomRateToString(int id){
		return new MyBatisHelper().selectOne("selectRoomRateNameById", id);
	}
	
}
