package uz.hbs.actions.hotel.roomsandrates;

import java.util.ArrayList;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.CleanState;
import uz.hbs.beans.OnSaleState;
import uz.hbs.beans.Room;
import uz.hbs.beans.RoomState;
import uz.hbs.beans.filters.RoomFilter;
import uz.hbs.components.panels.hotel.CleanStatePanel;
import uz.hbs.components.panels.hotel.OnSaleStatePanel;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableRoomDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyChoiceFilteredPropertyColumn;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.models.RoomModel;

public class ListRoom extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private ModalWindow dialog;
	private WebMarkupContainer container;

	public ListRoom(String id, IBreadCrumbModel breadCrumbModel, final long hotel_id) {
		super(id, breadCrumbModel);
		
		add(dialog = new ModalWindow("dialog"));
		dialog.setMinimalHeight(400);
		dialog.setMinimalWidth(600);
		dialog.setCookieName("listHotelRooms");
		dialog.setInitialHeight(400);
		dialog.setInitialWidth(600);
		
		add(container = new WebMarkupContainer("container"));
		container.setOutputMarkupId(true);
		
		ArrayList<IColumn<Room, String>> columns = new ArrayList<IColumn<Room, String>>();
		columns.add(new PropertyColumn<Room, String>(Model.of("#"), "rowid"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Room>> item, String componentId, IModel<Room> rowModel) {
				item.add(new Label(componentId, (((Item<?>) item.getParent().getParent()).getIndex() + 1))); 
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<Room, Room, String>(new StringResourceModel("hotels.room.number", null), "room_number", "room_number"));
		columns.add(new PropertyColumn<Room, String>(new StringResourceModel("hotels.room.type", null), "roomtype", "roomtype"));
		columns.add(new PropertyColumn<Room, String>(new StringResourceModel("hotels.room.floor", null), "room_floor", "room_floor"));
		columns.add(new MyChoiceFilteredPropertyColumn<Room, RoomState, String>(new StringResourceModel("hotels.room.condition", null), "room_state", RoomModel.getRoomStateModel()){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(Item<ICellPopulator<Room>> item, String componentId, final IModel<Room> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						if (rowModel.getObject().getOnsale_state() != null){
							return RoomModel.roomStateToString(rowModel.getObject().getRoom_state().getId());
						}
						return null;
					}
				}));
			}
		});
		columns.add(new MyChoiceFilteredPropertyColumn<Room, CleanState, String>(new StringResourceModel("hotels.room.status.clean", null), "clean_state", "clean_state",  RoomModel.getCleanModel()){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Room>> item, String componentId, final IModel<Room> rowModel) {
				item.add(new CleanStatePanel(componentId, new Model<Boolean>(rowModel.getObject().getClean_state().getId() == CleanState.CLEAN)) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onChangeState(AjaxRequestTarget target, boolean value) {
						new MyBatisHelper().update("updateCleanState", new Room(rowModel.getObject().getId(), new CleanState(value?CleanState.CLEAN:CleanState.UNCLEAN)));
					}
				});
				item.add(new AttributeModifier("style", "padding-left: 30px;"));
			}
		});
		columns.add(new MyChoiceFilteredPropertyColumn<Room, OnSaleState, String>(new StringResourceModel("hotels.room.status.onsale", null), "onsale_state", "onsale_state", RoomModel.getOnSaleModel()){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(Item<ICellPopulator<Room>> item, String componentId, final IModel<Room> rowModel) {
				item.add(new OnSaleStatePanel(componentId, new Model<Boolean>(rowModel.getObject().getOnsale_state().getId() == OnSaleState.CAN_SALE)) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onChangeState(AjaxRequestTarget target, boolean value) {
						new MyBatisHelper().update("updateOnSaleState", new Room(rowModel.getObject().getId(), new OnSaleState(value?OnSaleState.CAN_SALE:OnSaleState.UNDER_REPAIR)));
					}
				});
				item.add(new AttributeModifier("style", "padding-left: 30px;"));
			}
		});
		columns.add(new PropertyColumn<Room, String>(new StringResourceModel("hotels.room.update_date", null), "update_date", "update_date"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Room>> item, String componentId, IModel<Room> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getUpdate_date(), "dd/MM/yyyy")));
			}
		});
		columns.add(new FilteredAbstractColumn<Room, String>(new StringResourceModel("users.operation", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form);
			}

			@Override
			public void populateItem(Item<ICellPopulator<Room>> item, String componentId, final IModel<Room> model) {
				item.add(new WebMarkupContainer(componentId).setVisible(false));
			}	
		});
		
		SortableRoomDataProvider provider = new SortableRoomDataProvider();
		RoomFilter filter = new RoomFilter(hotel_id);
		provider.setFilterState(filter);
		
		
		final DataTable<Room, String> dataTable = new DataTable<Room, String>("table", columns, provider, getMySession().getSettings().getTable_rows()){
			private static final long serialVersionUID = 1L;

			@Override
			protected Item<Room> newRowItem(String id, int index, IModel<Room> model) {
				return new OddEvenItem<Room>(id, index, model);
			}
		};
		dataTable.setOutputMarkupId(true);

		final FilterForm<RoomFilter> form = new FilterForm<RoomFilter>("filterForm", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				dataTable.setCurrentPage(0);
			}

			@Override
			public boolean isVisible() {
				return dataTable.size() > 0;
			}
		};

		form.add(dataTable);
		dataTable.addTopToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addTopToolbar(new HeadersToolbar<String>(dataTable, provider));
		dataTable.addTopToolbar(new FilterToolbar(dataTable, form, provider));
		dataTable.addBottomToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addBottomToolbar(new MyNoRecordsToolbar(dataTable));
		container.add(form);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.rooms_and_rates.room.list", null);
	}

}
