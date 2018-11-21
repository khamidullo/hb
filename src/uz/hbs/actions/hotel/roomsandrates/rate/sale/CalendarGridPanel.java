package uz.hbs.actions.hotel.roomsandrates.rate.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.rate.SalePlane;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.DateUtil;

public class CalendarGridPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private IDataProvider<SalePlane> dataProvider;
	private GridView<SalePlane> gridView;
	private boolean resident;

	public CalendarGridPanel(String id, final HashMap<String, Serializable> param, final long hotelsusers_id, final int roomtypes_id, final List<SalePlane> salelist, final ModalWindow dialog) {
		super(id);
		param.put("hotelsusers_id", hotelsusers_id);
		param.put("roomtypes_id", roomtypes_id);
		
		resident = (Boolean) new MyBatisHelper().selectOne("selectSupportResidentRate", hotelsusers_id);
		
		add(new Label("month", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new StringResourceModel("month." + DateUtil.toString((Date) param.get("date_from"), "M"), null).getString();
			}
		}));
		
		RepeatingView weeklist = new RepeatingView("weeklist");
		weeklist.add(new Label(weeklist.newChildId(), getString("week.day.1.short")).add(new AttributeModifier("colspan", 1)).add(new AttributeModifier("class", "text-danger")));
		weeklist.add(new Label(weeklist.newChildId(), getString("week.day.2.short")).add(new AttributeModifier("colspan", 1)));
		weeklist.add(new Label(weeklist.newChildId(), getString("week.day.3.short")).add(new AttributeModifier("colspan", 1)));
		weeklist.add(new Label(weeklist.newChildId(), getString("week.day.4.short")).add(new AttributeModifier("colspan", 1)));
		weeklist.add(new Label(weeklist.newChildId(), getString("week.day.5.short")).add(new AttributeModifier("colspan", 1)));
		weeklist.add(new Label(weeklist.newChildId(), getString("week.day.6.short")).add(new AttributeModifier("colspan", 1)));
		weeklist.add(new Label(weeklist.newChildId(), getString("week.day.7.short")).add(new AttributeModifier("colspan", 1)).add(new AttributeModifier("class", "text-danger")));
		add(weeklist);
		
		dataProvider = new ListDataProvider<SalePlane>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<SalePlane> getData() {
				List<SalePlane> list = new MyBatisHelper().selectList("selectSalePlaneByPeriod", param);
				salelist.addAll(list);
				return getCalendar(list, param);
			}
		};

		gridView = new GridView<SalePlane>("rows", dataProvider) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<SalePlane> item) {
				item.add(new SaleItemPanel("item", item.getModel(), dialog, resident));
			}

			@Override
			protected void populateEmptyItem(Item<SalePlane> item) {
				item.add(new SaleEmptyItemPanel("item"));
			}
		};
		gridView.setColumns(7);
		gridView.setRows(6);
		add(gridView);
	}
	
	public List<SalePlane> getCalendar(List<SalePlane> list, HashMap<String, Serializable> param){
		List<SalePlane> result = new ArrayList<SalePlane>();
		Date date = (Date) param.get("date_from");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int wod = calendar.get(Calendar.DAY_OF_WEEK);
		for (int i = 0; i < wod - 1; i++) {
			result.add(new SalePlane());
		}
		result.addAll(list);
		return result;
	}
}
