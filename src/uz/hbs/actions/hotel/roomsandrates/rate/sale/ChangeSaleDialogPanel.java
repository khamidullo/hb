package uz.hbs.actions.hotel.roomsandrates.rate.sale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.rate.RateDetails;
import uz.hbs.beans.rate.SalePlane;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.models.HotelModels;

public class ChangeSaleDialogPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ChangeSaleDialogPanel.class);
	private HashMap<Short,List<SalePlane>> salemap = new HashMap<Short,List<SalePlane>>();
	private FeedbackPanel feedback;

	public ChangeSaleDialogPanel(String id, final SalePlane plane, final boolean resident) {
		super(id);
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		final Form<ValueMap> form;
		add(form = new Form<ValueMap>("form", new CompoundPropertyModel<ValueMap>(new ValueMap())));
		
		final short max_person = new MyBatisHelper().selectOne("selectHoldingCapacityRoomType", plane.getRoomtypes_id());
		
		salemap = getSaleMap(plane.getHotelsusers_id(), plane.getRoomtypes_id(), max_person, plane.getSale_date());
		
		form.add(new Label("roomtype", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new MyBatisHelper().selectOne("selectRoomTypeNameById", plane.getRoomtypes_id());
			}
		}));
		
		form.add(new ListView<Short>("personlist", HotelModels.getShortListModel((short) 1, max_person)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Short> item) {
				item.add(new Label("person_number", new StringResourceModel("hotels.rate.plane.season.person", null, new Object[]{ item.getModelObject() })));
				item.add(new ListView<SalePlane>("salelist", new LoadableDetachableModel<List<SalePlane>>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<SalePlane> load() {
						return salemap.get(item.getModelObject());
					}
				}) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem<SalePlane> childItem) {
						childItem.add(new RequiredTextField<BigDecimal>("sale", new PropertyModel<BigDecimal>(childItem.getModel(), "sale")));
						childItem.add(new TextField<BigDecimal>("sale_uz", new PropertyModel<BigDecimal>(childItem.getModel(), "sale_uz")).setEnabled(resident));
						childItem.add(new AjaxCheckBox("status", new PropertyModel<Boolean>(childItem.getModel(), "status")) {
							private static final long serialVersionUID = 1L;

							@Override
							protected void onUpdate(AjaxRequestTarget target) {
								SalePlane plane = childItem.getModelObject();
								plane.setInitiator_user_id(((MySession) getSession()).getUser().getId());
								new MyBatisHelper().update("updateSalePlaneChangeStatus", plane);
							}
						});
					}
				}.setReuseItems(true));
			}
		}.setReuseItems(true));
		
		form.add(new IndicatingAjaxButton("save") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
				try {
					for (short person = 1; person <= max_person; person ++){
						for (SalePlane plane : salemap.get(person)){
							plane.setInitiator_user_id(((MySession) getSession()).getUser().getId());
							sql.update("updateSalePlane", plane);
						}
					}
					feedback.success(getString("hotels.sale.plane.update.success"));
					sql.commit();
				} catch (Exception e) {
					logger.error("Exception", e);
					sql.rollback();
					feedback.error(getString("hotels.sale.plane.update.fail"));
				} finally {
					sql.close();
					target.add(feedback);
				}
			}
		});
	}
	
	private HashMap<Short,List<SalePlane>> getSaleMap(long hotelsusers_id, int roomtype_id, short max_person, Date sale_date){
		HashMap<Short,List<SalePlane>> salemap = new HashMap<Short,List<SalePlane>>();
		for (short person = 1; person <= max_person; person ++){
			List<SalePlane> list = new ArrayList<SalePlane>();
			list.add((SalePlane) new MyBatisHelper().selectOne("selectSalePlane", new SalePlane(hotelsusers_id, roomtype_id, person, sale_date, RateDetails.INDIVIDUAL)));
			list.add((SalePlane) new MyBatisHelper().selectOne("selectSalePlane", new SalePlane(hotelsusers_id, roomtype_id, person, sale_date, RateDetails.GROUP)));
			salemap.put(person, list);
		}
		return salemap;
	}
}
