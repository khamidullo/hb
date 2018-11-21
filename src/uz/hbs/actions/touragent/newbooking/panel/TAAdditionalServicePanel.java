package uz.hbs.actions.touragent.newbooking.panel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;

import uz.hbs.beans.AdditionalServiceDetail;
import uz.hbs.beans.AdditionalServicePrice;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.models.MyChoiceRenderer;

public class TAAdditionalServicePanel extends Panel {
	private static final long serialVersionUID = 1L;
	private AdditionalServicePrice taxiorder;

	public TAAdditionalServicePanel(String id, final ReservationDetail reserve) {
		super(id, new CompoundPropertyModel<ReservationDetail>(reserve));
		
		final InsuranceDataPanel panel;
		add(panel = new InsuranceDataPanel("insurance_data", reserve){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return reserve.isInsurance();
			}
		});
		panel.setOutputMarkupPlaceholderTag(true);
		
		add(new AjaxCheckBox("insurance") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				reserve.setInsurance(Strings.isTrue(getValue()));
				target.appendJavaScript("doneDuplicate();");
				target.add(panel);
			}
			
		});
		add(new Label("insurance_label", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return new StringResourceModel("touragents.reservation.guest.service.insurance.label", null, new Object[] { "$", taxiorder.getInsurance() }).getString();
			}
		}));
		
		taxiorder = new MyBatisHelper().selectOne("selectCurrentAdditionalServicePrice");
		if (taxiorder == null) taxiorder = new AdditionalServicePrice();
		
		final RadioGroup<Byte> transport_type;
		
		add(transport_type = new RadioGroup<Byte>("arrival.transport_type"));
		transport_type.setOutputMarkupPlaceholderTag(true);
		transport_type.setRenderBodyOnly(false);
		
		Radio<Byte> transport_type_without;
		transport_type.add(transport_type_without = new Radio<Byte>("transport_type_without", new Model<Byte>(AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN)){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return reserve.getArrival().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN;
			}
		});
		
		Radio<Byte> transport_type_airport;
		transport_type.add(transport_type_airport = new Radio<Byte>("transport_type_airport", new Model<Byte>(AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT)));
		
		Radio<Byte> transport_type_train;
		transport_type.add(transport_type_train = new Radio<Byte>("transport_type_train", new Model<Byte>(AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN)));
		
		final WebMarkupContainer container_service_type_airport;
		transport_type.add(container_service_type_airport = new WebMarkupContainer("container_service_type_airport"));
		container_service_type_airport.setOutputMarkupPlaceholderTag(true);
		container_service_type_airport.setVisible(reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
		
		final DropDownChoice<Byte> air_terminal;
		
		container_service_type_airport.add(air_terminal = new DropDownChoice<Byte>("arrival.air_terminal", new LoadableDetachableModel<List<Byte>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Byte> load() {
				return Arrays.asList(AdditionalServiceDetail.AIR_TERMINAL_2, AdditionalServiceDetail.AIR_TERMINAL_3);
			}
		}, new ChoiceRenderer<Byte>(){
			private static final long serialVersionUID = 1L;

			@Override
			public String getIdValue(Byte object, int index) {
				return String.valueOf(object);
			}
			
			@Override
			public Object getDisplayValue(Byte object) {
				return getString("touragents.reservation.guest.service.airport.terminal." + object);	
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return true;
			}
		});
		
		final TextField<String> air_numb, air_time;
		container_service_type_airport.add(air_numb = new TextField<String>("arrival.air_numb"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired() {
				return reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT;
			}
		});
		air_numb.setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.flight", null));
		container_service_type_airport.add(air_time = new TextField<String>("arrival.air_time"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired() {
				return reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT;
			}
		});
		air_time.setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.flight.arrival.time", null));
		
		final DropDownChoice<Byte> air_class;
		container_service_type_airport.add(air_class = new DropDownChoice<Byte>("arrival.air_class", new LoadableDetachableModel<List<Byte>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Byte> load() {
				List<Byte> list = new ArrayList<Byte>();
				list.add(AdditionalServiceDetail.AIR_CLASS_ECONOM); //new IdByteAndName(, );
				list.add(AdditionalServiceDetail.AIR_CLASS_BUSINESS);//, getString("touragents.reservation.guest.service.airport.class.business")));
				return list;
			}
		}, new ChoiceRenderer<Byte>(){
			private static final long serialVersionUID = 1L;

			@Override
			public String getIdValue(Byte object, int index) {
				return String.valueOf(object);
			}
			
			@Override
			public Object getDisplayValue(Byte object) {
				if (object == AdditionalServiceDetail.AIR_CLASS_ECONOM) return getString("touragents.reservation.guest.service.airport.class.econom");
				else if (object == AdditionalServiceDetail.AIR_CLASS_BUSINESS) return getString("touragents.reservation.guest.service.airport.class.business");
				return super.getDisplayValue(object);
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			protected String getNullKeyDisplayValue() {
				return getString("touragents.reservation.guest.service.airport.class");
			}
			
			@Override
			public boolean isEnabled() {
				return true;
			}
		});
		air_class.setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.class", null));
		container_service_type_airport.add(new TextField<String>("arrival.destination").setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.destination.arrival", null)));
		
		final WebMarkupContainer container_air_service_type;
		transport_type.add(container_air_service_type = new WebMarkupContainer("container_air_service_type"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT && 
					   reserve.getArrival().getAir_class() == AdditionalServiceDetail.AIR_CLASS_ECONOM &&
					   reserve.getArrival().getAir_terminal() == AdditionalServiceDetail.AIR_TERMINAL_2;
			}
		});
		container_air_service_type.setOutputMarkupPlaceholderTag(true);
		
		/*
		container_air_service_type.add(new Label("air_service_green_hall_cost", new AbstractReadOnlyModel<Double>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Double getObject() {
				return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(taxiorder.getArrival_air_green_hall()) * reserve.getAdults());
			}
		}));
		*/
		container_air_service_type.add(new Label("air_service_vip_hall_cost", new AbstractReadOnlyModel<Double>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Double getObject() {
				return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(taxiorder.getArrival_air_vip_hall())  * reserve.getAdults());
			}
		}));
		
		final HiddenField<Float> air_service_value;
		container_air_service_type.add(new HiddenField<Byte>("arrival.air_service_type", Byte.class) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("data-green-hall-cost", String.valueOf(CommonUtil.nvl(taxiorder.getArrival_air_green_hall()) * reserve.getAdults()));
				tag.put("data-vip-hall-cost", String.valueOf(CommonUtil.nvl(taxiorder.getArrival_air_vip_hall()) * reserve.getAdults()));
			}
		});
		container_air_service_type.add(air_service_value = new HiddenField<Float>("arrival.air_service_value"));
		air_service_value.setLabel(new StringResourceModel("price.additional.service.price", null));
		
		air_class.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				container_air_service_type.setVisible(false);
				if (air_class.getValue() != null && ! air_class.getValue().isEmpty()) {
					reserve.getArrival().setAir_service_type(Byte.parseByte(air_class.getValue()));
				}
				target.add(container_air_service_type);
			}
		});
		
		air_terminal.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				container_air_service_type.setVisible(false);
				if (air_terminal.getValue() != null && ! air_terminal.getValue().isEmpty()) {
					reserve.getArrival().setAir_terminal(Byte.parseByte(air_terminal.getValue()));
				}
				target.add(container_air_service_type);
			}
		});
		

		final WebMarkupContainer container_service_type_train;
		transport_type.add(container_service_type_train = new WebMarkupContainer("container_service_type_train"));
		container_service_type_train.setOutputMarkupPlaceholderTag(true);
		container_service_type_train.setVisible(reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
		
		final TextField<String> train_numb, train_time;
		container_service_type_train.add(train_numb = new TextField<String>("arrival.train_numb"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired() {
				return reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN;
			}
		});
		train_numb.setLabel(new StringResourceModel("touragents.reservation.guest.service.train.number", null));
		
		container_service_type_train.add(train_time = new TextField<String>("arrival.train_time"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired() {
				return reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN;
			}
		});
		train_time.setLabel(new StringResourceModel("touragents.reservation.guest.service.train.arrival.time", null));
		
		final WebMarkupContainer container_taxi_order;
		transport_type.add(container_taxi_order = new WebMarkupContainer("container_taxi_order"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return reserve.getArrival().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN;
			}
		});
		container_taxi_order.setOutputMarkupPlaceholderTag(true);
		
		final DropDownChoice<Short> taxi_order_car;
		container_taxi_order.add(taxi_order_car = new DropDownChoice<Short>("arrival.taxi_order_car", new LoadableDetachableModel<List<Short>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Short> load() {
				return CommonUtil.getShortList((short) 25, false);
			}
		}, new MyChoiceRenderer<Short>()){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return reserve.getArrival().isTaxi_order();
			}
		});
		taxi_order_car.setOutputMarkupId(true);
		taxi_order_car.add(new AttributeModifier("onchange", "recalcTaxiOrderCost('" + taxi_order_car.getMarkupId() + "');"));
		
		container_taxi_order.add(new HiddenField<Double>("arrival.taxi_price", new Model<Double>(){
			private static final long serialVersionUID = 1L;

			@Override
			public Double getObject() {
				return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), taxiorder.getArrival());
			}
		}));
		container_taxi_order.add(new TextField<Double>("arrival.taxi_order_cost", Double.class));
		
		final RadioGroup<Byte> location_type;
		container_taxi_order.add(location_type = new RadioGroup<Byte>("arrival.location_type"));
		location_type.setRenderBodyOnly(false);
		location_type.setOutputMarkupId(true);
		
		final Radio<Byte> from_reserved_hotel, from_address;
		location_type.add(from_reserved_hotel = new Radio<Byte>("location_type_reserved_hotel", new Model<Byte>(AdditionalServiceDetail.LOCATION_TYPE_RESERVED_HOTEL)));
		location_type.add(from_address = new Radio<Byte>("location_type_address", new Model<Byte>(AdditionalServiceDetail.LOCATION_TYPE_ADDRESS)));
		final TextField<String> location_value;
		location_type.add(location_value = new TextField<String>("arrival.location_value"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return reserve.getArrival().getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_ADDRESS;
			}
			
			@Override
			public boolean isRequired() {
				return reserve.getArrival().getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_ADDRESS; 	
			}
		});
		location_value.setLabel(new StringResourceModel("touragents.reservation.guest.service.taxi_order.deliver.location_type.address", null));
		from_reserved_hotel.setOutputMarkupId(true);
		from_reserved_hotel.add(new AjaxEventBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				reserve.getArrival().setLocation_type(AdditionalServiceDetail.LOCATION_TYPE_RESERVED_HOTEL);
				target.add(location_type);
			}
		});
		from_address.setOutputMarkupId(true);
		from_address.add(new AjaxEventBehavior("onchange") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				reserve.getArrival().setLocation_type(AdditionalServiceDetail.LOCATION_TYPE_ADDRESS);
				target.add(location_type);
			}
		});
		
		
		container_taxi_order.add(new AjaxCheckBox("arrival.taxi_order") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				reserve.getArrival().setTaxi_order(Strings.isTrue(getValue()));
				taxi_order_car.setRequired(reserve.getArrival().isTaxi_order());
				target.add(container_taxi_order);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "return myInit();";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});

		transport_type_airport.add(new AjaxEventBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				reserve.getArrival().setTransport_type(AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
				container_service_type_airport.setVisible(reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
				container_service_type_train.setVisible(reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
				target.add(transport_type);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "return myInit();";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});
		
		transport_type_train.add(new AjaxEventBehavior("onchange"){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				reserve.getArrival().setTransport_type(AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
				container_service_type_train.setVisible(reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
				container_service_type_airport.setVisible(reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
				target.add(transport_type);
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "return myInit();";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});
		
		transport_type_without.add(new AjaxEventBehavior("onchange"){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				reserve.getArrival().setTransport_type(AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN);
				container_service_type_airport.setVisible(reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
				container_service_type_train.setVisible(reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
				target.add(transport_type);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "return myInit();";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});
		//********************************************************** DEPARMENT ****************************************************************************//
		
		final RadioGroup<Byte> transport_type2;
		
		add(transport_type2 = new RadioGroup<Byte>("departure.transport_type"));
		transport_type2.setOutputMarkupPlaceholderTag(true);
		transport_type2.setRenderBodyOnly(false);
		
		Radio<Byte> transport_type_without2;
		transport_type2.add(transport_type_without2 = new Radio<Byte>("transport_type_without2", new Model<Byte>(AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN)){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return reserve.getDeparture().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN;
			}
		});
		
		Radio<Byte> transport_type_airport2;
		transport_type2.add(transport_type_airport2 = new Radio<Byte>("transport_type_airport2", new Model<Byte>(AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT)));
		
		Radio<Byte> transport_type_train2;
		transport_type2.add(transport_type_train2 = new Radio<Byte>("transport_type_train2", new Model<Byte>(AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN)));
		
		final WebMarkupContainer container_service_type_airport2;
		transport_type2.add(container_service_type_airport2 = new WebMarkupContainer("container_service_type_airport"));
		container_service_type_airport2.setOutputMarkupPlaceholderTag(true);
		container_service_type_airport2.setVisible(reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
		
		final DropDownChoice<Byte> air_terminal2;
		
		container_service_type_airport2.add(air_terminal2 = new DropDownChoice<Byte>("departure.air_terminal", new LoadableDetachableModel<List<Byte>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Byte> load() {
				return Arrays.asList(AdditionalServiceDetail.AIR_TERMINAL_2, AdditionalServiceDetail.AIR_TERMINAL_3);
			}
		}, new ChoiceRenderer<Byte>(){
			private static final long serialVersionUID = 1L;

			@Override
			public String getIdValue(Byte object, int index) {
				return String.valueOf(object);
			}
			
			@Override
			public Object getDisplayValue(Byte object) {
				return getString("touragents.reservation.guest.service.airport.terminal." + object);	
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return true;
			}
		});
		container_service_type_airport2.add(new TextField<String>("departure.destination").setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.destination.departure", null)));
		final TextField<String> air_numb2, air_time2;
		container_service_type_airport2.add(air_numb2 = new TextField<String>("departure.air_numb"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired() {
				return reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT;
			}
		});
		air_numb2.setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.flight", null));
		container_service_type_airport2.add(air_time2 = new TextField<String>("departure.air_time"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired() {
				return reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT;
			}
		});
		air_time2.setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.flight.departure.time", null));
		
		final DropDownChoice<Byte> air_class2;
		container_service_type_airport2.add(air_class2 = new DropDownChoice<Byte>("departure.air_class", new LoadableDetachableModel<List<Byte>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Byte> load() {
				List<Byte> list = new ArrayList<Byte>();
				list.add(AdditionalServiceDetail.AIR_CLASS_ECONOM);
				list.add(AdditionalServiceDetail.AIR_CLASS_BUSINESS);
				return list;
			}
		}, new ChoiceRenderer<Byte>(){
			private static final long serialVersionUID = 1L;

			@Override
			public String getIdValue(Byte object, int index) {
				return String.valueOf(object);
			}
			
			@Override
			public Object getDisplayValue(Byte object) {
				if (object == AdditionalServiceDetail.AIR_CLASS_ECONOM) return getString("touragents.reservation.guest.service.airport.class.econom");
				else if (object == AdditionalServiceDetail.AIR_CLASS_BUSINESS) return getString("touragents.reservation.guest.service.airport.class.business");
				return super.getDisplayValue(object);
			}
		}){
			private static final long serialVersionUID = 1L;

			@Override
			protected String getNullKeyDisplayValue() {
				return getString("touragents.reservation.guest.service.airport.class");
			}
			
			@Override
			public boolean isEnabled() {
				return true;
			}
		});
		air_class2.setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.class", null));
		
		final WebMarkupContainer container_air_service_type2;
		transport_type2.add(container_air_service_type2 = new WebMarkupContainer("container_air_service_type"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT && 
					   reserve.getDeparture().getAir_class() == AdditionalServiceDetail.AIR_CLASS_ECONOM &&
					   reserve.getDeparture().getAir_terminal() == AdditionalServiceDetail.AIR_TERMINAL_2;
			}
		});
		container_air_service_type2.setOutputMarkupPlaceholderTag(true);
		/*
		container_air_service_type2.add(new Label("air_service_green_hall_cost", new AbstractReadOnlyModel<Double>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Double getObject() {
				return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(taxiorder.getDeparture_air_green_hall()) * reserve.getAdults());
			}
		}));
		*/
		container_air_service_type2.add(new Label("air_service_vip_hall_cost", new AbstractReadOnlyModel<Double>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Double getObject() {
				return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(taxiorder.getDeparture_air_vip_hall())  * reserve.getAdults());
			}
		}));
		
		final HiddenField<Float> air_service_value2;
		container_air_service_type2.add(new HiddenField<Byte>("departure.air_service_type", Byte.class) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("data-green-hall-cost", String.valueOf(CommonUtil.nvl(taxiorder.getDeparture_air_green_hall()) * reserve.getAdults()));
				tag.put("data-vip-hall-cost", String.valueOf(CommonUtil.nvl(taxiorder.getDeparture_air_vip_hall()) * reserve.getAdults()));
			}
		});
		container_air_service_type2.add(air_service_value2 = new HiddenField<Float>("departure.air_service_value"));
		air_service_value2.setLabel(new StringResourceModel("price.additional.service.price", null));
		
		air_class2.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				container_air_service_type2.setVisible(false);
				if (air_class2.getValue() != null && ! air_class2.getValue().isEmpty()) {
					reserve.getDeparture().setAir_service_type(Byte.parseByte(air_class2.getValue()));
				}
				target.add(container_air_service_type2);
			}
		});
		
		air_terminal2.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				container_air_service_type2.setVisible(false);
				if (air_terminal2.getValue() != null && ! air_terminal2.getValue().isEmpty()) {
					reserve.getArrival().setAir_terminal(Byte.parseByte(air_terminal2.getValue()));
				}
				target.add(container_air_service_type2);
			}
		});
		

		final WebMarkupContainer container_service_type_train2;
		transport_type2.add(container_service_type_train2 = new WebMarkupContainer("container_service_type_train"));
		container_service_type_train2.setOutputMarkupPlaceholderTag(true);
		container_service_type_train2.setVisible(reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
		
		final TextField<String> train_numb2, train_time2;
		container_service_type_train2.add(train_numb2 = new TextField<String>("departure.train_numb"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired() {
				return reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN;
			}
		});
		train_numb2.setLabel(new StringResourceModel("touragents.reservation.guest.service.train.number", null));
		
		container_service_type_train2.add(train_time2 = new TextField<String>("departure.train_time"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired() {
				return reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN;
			}
		});
		train_time2.setLabel(new StringResourceModel("touragents.reservation.guest.service.train.departure.time", null));
		
		final WebMarkupContainer container_taxi_order2;
		transport_type2.add(container_taxi_order2 = new WebMarkupContainer("container_taxi_order"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return reserve.getDeparture().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN;
			}
		});
		container_taxi_order2.setOutputMarkupPlaceholderTag(true);
		
		final DropDownChoice<Short> taxi_order_car2;
		container_taxi_order2.add(taxi_order_car2 = new DropDownChoice<Short>("departure.taxi_order_car", new LoadableDetachableModel<List<Short>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Short> load() {
				return CommonUtil.getShortList((short) 25, false);
			}
		}, new MyChoiceRenderer<Short>()){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return reserve.getDeparture().isTaxi_order();
			}
		});
		taxi_order_car2.setOutputMarkupId(true);
		taxi_order_car2.add(new AttributeModifier("onchange", "recalcTaxiOrderCost('" + taxi_order_car2.getMarkupId() + "');"));
		
		container_taxi_order2.add(new HiddenField<Double>("departure.taxi_price", new Model<Double>(){
			private static final long serialVersionUID = 1L;

			@Override
			public Double getObject() {
				return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), taxiorder.getDeparture());
			}
		}));
		container_taxi_order2.add(new TextField<Double>("departure.taxi_order_cost", Double.class));
		
		final RadioGroup<Byte> location_type2;
		container_taxi_order2.add(location_type2 = new RadioGroup<Byte>("departure.location_type"));
		location_type2.setRenderBodyOnly(false);
		location_type2.setOutputMarkupId(true);
		
		final Radio<Byte> from_reserved_hotel2, from_address2;
		location_type2.add(from_reserved_hotel2 = new Radio<Byte>("location_type_reserved_hotel2", new Model<Byte>(AdditionalServiceDetail.LOCATION_TYPE_RESERVED_HOTEL)));
		location_type2.add(from_address2 = new Radio<Byte>("location_type_address2", new Model<Byte>(AdditionalServiceDetail.LOCATION_TYPE_ADDRESS)));
		final TextField<String> location_value2;
		location_type2.add(location_value2 = new TextField<String>("departure.location_value"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return reserve.getDeparture().getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_ADDRESS;
			}
			
			@Override
			public boolean isRequired() {
				return reserve.getDeparture().getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_ADDRESS;
			}
		});
		location_value2.setOutputMarkupId(true);
		location_value2.setLabel(new StringResourceModel("touragents.reservation.guest.service.taxi_order.pickup.location_type.address", null));
		from_reserved_hotel2.setOutputMarkupId(true);
		from_address2.setOutputMarkupId(true);
		
		container_taxi_order2.add(new AjaxCheckBox("departure.taxi_order") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				reserve.getDeparture().setTaxi_order(Strings.isTrue(getValue()));
				taxi_order_car2.setRequired(reserve.getDeparture().isTaxi_order());
				target.add(container_taxi_order2);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "return myInit();";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});
		container_taxi_order2.add(new TextField<String>("departure.taxi_order_time"));
		
		final TextField<String> phone_number;
		container_taxi_order2.add(phone_number = new TextField<String>("departure.phone_number"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return reserve.getDeparture().getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_ADDRESS;
			}
			
			@Override
			public boolean isRequired() {
				return reserve.getDeparture().getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_ADDRESS;
			}
		});
		phone_number.setLabel(new StringResourceModel("touragents.reservation.guest.service.taxi_order.phone", null));
		phone_number.setOutputMarkupId(true);
		
		from_reserved_hotel2.add(new AjaxEventBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				reserve.getDeparture().setLocation_type(AdditionalServiceDetail.LOCATION_TYPE_RESERVED_HOTEL);
				target.add(location_value2, phone_number);
			}
		});
		
		from_address2.add(new AjaxEventBehavior("onchange") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				reserve.getDeparture().setLocation_type(AdditionalServiceDetail.LOCATION_TYPE_ADDRESS);
				target.add(location_value2, phone_number);
			}
		});
		
		transport_type_airport2.add(new AjaxEventBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				reserve.getDeparture().setTransport_type(AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
				container_service_type_airport2.setVisible(reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
				container_service_type_train2.setVisible(reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
				target.add(transport_type2);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "return myInit();";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});
		
		transport_type_train2.add(new AjaxEventBehavior("onchange"){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				reserve.getDeparture().setTransport_type(AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
				container_service_type_train2.setVisible(reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
				container_service_type_airport2.setVisible(reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
				target.add(transport_type2);
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "return myInit();";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});
		
		transport_type_without2.add(new AjaxEventBehavior("onchange"){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				reserve.getDeparture().setTransport_type(AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN);
				container_service_type_airport2.setVisible(reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
				container_service_type_train2.setVisible(reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
				target.add(transport_type2);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "return myInit();";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});
	}
}
