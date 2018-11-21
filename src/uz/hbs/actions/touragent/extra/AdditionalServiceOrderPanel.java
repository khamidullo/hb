package uz.hbs.actions.touragent.extra;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.AdditionalServiceDetail;
import uz.hbs.beans.AdditionalServiceOrder;
import uz.hbs.beans.AdditionalServicePrice;
import uz.hbs.beans.Guest;
import uz.hbs.beans.Insurance;
import uz.hbs.beans.Nationality;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.models.MyAjaxCallListener;
import uz.hbs.utils.models.MyChoiceRenderer;

public class AdditionalServiceOrderPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private AdditionalServicePrice price;
	private FeedbackPanel feedback;
	private boolean editable;
	private ModalWindow dialog;
	
	public AdditionalServiceOrderPanel(String id, final IBreadCrumbModel breadCrumbModel) {
		this(id, breadCrumbModel, new AdditionalServiceOrder(), true);
	}

	public AdditionalServiceOrderPanel(String id, final IBreadCrumbModel breadCrumbModel, final AdditionalServiceOrder reserve, final boolean editable) {
		super(id, breadCrumbModel);
		this.editable = editable;
		add(dialog = new ModalWindow("dialog"));
		dialog.setOutputMarkupId(true);
		
		reserve.getArrival().setLocation_type(AdditionalServiceDetail.LOCATION_TYPE_ADDRESS);
		reserve.getDeparture().setLocation_type(AdditionalServiceDetail.LOCATION_TYPE_ADDRESS);
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		add(new MyForm("form", reserve).add(new Button("submit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit() {
				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						return new AdditionalServiceSummaryPanel(componentId, breadCrumbModel, reserve);
					}
				});
			}
			
			@Override
			public boolean isVisible() {
				return editable;
			}
		}));
	}
	
	private class MyForm extends Form<AdditionalServiceOrder>{
		private static final long serialVersionUID = 1L;
		private String javascript;

		public MyForm(String id, final AdditionalServiceOrder reserve) {
			super(id, new CompoundPropertyModel<AdditionalServiceOrder>(reserve));
			if (reserve.getPerson() == null) reserve.setPerson(reserve.getArrival().getPerson());
			
			DateTextField arrival_date;
			add(arrival_date = new DateTextField("arrival_date", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			arrival_date.add(new MyDatePicker(new Date(), null));
			arrival_date.setLabel(new StringResourceModel("touragents.reservation.guest.service.arrival.time", null));
			arrival_date.setRequired(true);
			arrival_date.add(new AttributeModifier("data-date-format", MyWebApplication.DATE_FORMAT));
			arrival_date.add(new AttributeModifier("placeholder", MyWebApplication.DATE_FORMAT));
			arrival_date.add(new IValidator<Date>() {
				private static final long serialVersionUID = 1L;

				@Override
				public void validate(IValidatable<Date> validatable) {
					Date date = validatable.getValue();
					Date currDate = null;
					try {
						currDate = DateUtil.parseDate(DateUtil.toString(new Date(), MyWebApplication.DATE_FORMAT), MyWebApplication.DATE_FORMAT);
					} catch (ParseException e) {
						logger.error("ParseException", e);
					}
					if (date.before(currDate)) {
						ValidationError error = new ValidationError(this);
						error.setMessage(new StringResourceModel("date.validator.before.check_in", null).getString());
						validatable.error(error);
					}
				}
			});

			DateTextField departure_date;
			add(departure_date = new DateTextField("departure_date", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			departure_date.add(new MyDatePicker(new Date(), null));
			departure_date.setLabel(new StringResourceModel("touragents.reservation.guest.service.departure.time", null));
			departure_date.setRequired(true);
			departure_date.add(new AttributeModifier("data-date-format", MyWebApplication.DATE_FORMAT));
			departure_date.add(new AttributeModifier("placeholder", MyWebApplication.DATE_FORMAT));
			departure_date.add(new IValidator<Date>() {
				private static final long serialVersionUID = 1L;

				@Override
				public void validate(IValidatable<Date> validatable) {
					Date date = validatable.getValue();
					Date currDate = null;
					try {
						currDate = DateUtil.parseDate(DateUtil.toString(new Date(), MyWebApplication.DATE_FORMAT), MyWebApplication.DATE_FORMAT);
					} catch (ParseException e) {
						logger.error("ParseException", e);
					}
					if (date.before(currDate)) {
						ValidationError error = new ValidationError(this);
						error.setMessage(new StringResourceModel("date.validator.before.check_out", null).getString());
						validatable.error(error);
					}
				}
			});
			
			final WebMarkupContainer insurance_container;
			add(insurance_container = new WebMarkupContainer("insurance_container"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible() {
					return reserve.isInsurance();
				}
			});
			insurance_container.setOutputMarkupPlaceholderTag(true);
			insurance_container.add(new ListView<Insurance>("list", new LoadableDetachableModel<List<Insurance>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Insurance> load() {
					reserve.setInsuranceList(CommonUtil.getInsuranceList(reserve.getInsuranceList(), reserve.getPerson(), ((MySession) getSession()).getUser().getId()));
					return reserve.getInsuranceList();
				}
			}){
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final ListItem<Insurance> item) {
					item.add(new Label("order", item.getIndex() + 1).setRenderBodyOnly(true));
					item.add(new RequiredTextField<String>("first_name", new PropertyModel<String>(item.getModel(), "first_name")).setLabel(new StringResourceModel("hotels.guest.details.guest.name.first", null)).add(new AttributeModifier("data-item-index", item.getIndex())).add(new AjaxOnBlurEvent()));
					item.add(new RequiredTextField<String>("last_name", new PropertyModel<String>(item.getModel(), "last_name")).setLabel(new StringResourceModel("hotels.guest.details.guest.name.last", null)).add(new AttributeModifier("data-item-index", item.getIndex())).add(new AjaxOnBlurEvent()));
					DropDownChoice<Nationality> nationality;
					item.add(nationality = new DropDownChoice<Nationality>("nationality", new PropertyModel<Nationality>(item.getModel(), "nationality"), new LoadableDetachableModel<List<Nationality>>() {
						private static final long serialVersionUID = 1L;

						@Override
						protected List<Nationality> load() {
							return new MyBatisHelper().selectList("selectNationalityList");
						}
					}, new ChoiceRenderer<Nationality>("name", "code")));
					nationality.setNullValid(true);
					nationality.add(new AjaxOnBlurEvent());
					nationality.add(new AttributeModifier("data-item-index", item.getIndex()));
					nationality.setLabel(new StringResourceModel("hotels.guest.details.nationality", null));
					
					DateTextField birth_date;
					item.add(birth_date = new DateTextField("birth_date", new PropertyModel<Date>(item.getModel(), "birth_date"), new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
					birth_date.add(new MyDatePicker());
					birth_date.add(new AttributeModifier("data-date-format", MyWebApplication.DATE_FORMAT));
					birth_date.setLabel(new StringResourceModel("hotels.guest.details.passport.date_of_issue", null));
					
					TextField<String> passport_number;
					item.add(passport_number = new TextField<String>("passport_number", new PropertyModel<String>(item.getModel(), "passport_number")));
					passport_number.setRequired(true);
					passport_number.add(new AjaxOnBlurEvent());
					passport_number.setLabel(new StringResourceModel("hotels.guest.details.passport.number", null));
					
					DateTextField passport_issue_date;
					item.add(passport_issue_date = new DateTextField("passport_issue_date", new PropertyModel<Date>(item.getModel(), "passport_issue_date"), new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
					passport_issue_date.add(new MyDatePicker());
					passport_issue_date.add(new AjaxOnBlurEvent());
					passport_issue_date.add(new AttributeModifier("data-date-format", MyWebApplication.DATE_FORMAT));
					passport_issue_date.setLabel(new StringResourceModel("hotels.guest.details.passport.date_of_issue", null));
					
					item.add(new AjaxLink<Void>("delete"){
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target) {
							new MyBatisHelper().delete("deleteInsuranceById", item.getModelObject().getId());
							reserve.getInsuranceList().remove(item.getIndex());
							target.add(insurance_container);
						}
						
						@Override
						public boolean isVisible() {
							return item.getIndex() > 0;
						}
						
						@Override
						public boolean isEnabled() {
							return editable;
						}
						
						@Override
						protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
							super.updateAjaxAttributes(attributes);
							attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
							IAjaxCallListener listener = new AjaxCallListener(){
								private static final long serialVersionUID = 1L;

								@Override
								public CharSequence getSuccessHandler(Component component) {
									return "doneDuplicate();";
								}
							};
							attributes.getAjaxCallListeners().add(listener);
						}
					});

				}
			}.setReuseItems(true));

			final DropDownChoice<Short> arrival_person;
			add(arrival_person = new DropDownChoice<Short>("person", new LoadableDetachableModel<List<Short>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Short> load() {
					return CommonUtil.getShortList((short) 25, false);
				}
			}, new MyChoiceRenderer<Short>()));
			
			add(new AjaxCheckBox("insurance") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					reserve.setInsurance(Strings.isTrue(getValue()));
					target.appendJavaScript("doneDuplicate();");
					target.add(insurance_container);
				}
				
			});
			add(new AjaxLink<Void>("insurance_info"){
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					dialog.setTitle("Закон Республики Узбекистан «О Туризме»");
					dialog.setContent(new InsuranceInfo(dialog.getContentId()));
					dialog.show(target);
				}
			});
			add(new Label("insurance_label", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return new StringResourceModel("touragents.reservation.guest.service.insurance.label", null, new Object[] { price.getInsurance(), "UZS" }).getString();
				}
			}));
			
			price = new MyBatisHelper().selectOne("selectCurrentAdditionalServicePrice");
			if (price == null) price = new AdditionalServicePrice();
			
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
			air_numb.add(new AjaxOnBlurEvent());
			
			container_service_type_airport.add(air_time = new TextField<String>("arrival.air_time"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT;
				}
			});
			air_time.setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.flight.arrival.time", null));
			air_time.add(new AjaxOnBlurEvent());
			
			final DropDownChoice<Byte> air_class;
			container_service_type_airport.add(air_class = new DropDownChoice<Byte>("arrival.air_class", new LoadableDetachableModel<List<Byte>>() {
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
			air_class.setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.class", null));
			container_service_type_airport.add(new TextField<String>("arrival.destination").setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.destination.arrival", null)).add(new AjaxOnBlurEvent()));
			
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
					return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(price.getArrival_air_green_hall()) * reserve.getArrival().getPerson());
				}
			}));
			*/
			container_air_service_type.add(new Label("air_service_vip_hall_cost", new AbstractReadOnlyModel<Double>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Double getObject() {
					return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(price.getArrival_air_vip_hall())  * reserve.getArrival().getPerson());
				}
			}));
			
			final HiddenField<Float> air_service_value;
			final HiddenField<Byte> air_service_type;
			container_air_service_type.add(air_service_type = new HiddenField<Byte>("arrival.air_service_type", Byte.class) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.put("data-green-hall-cost", String.valueOf(CommonUtil.nvl(price.getArrival_air_green_hall()) * reserve.getArrival().getPerson()));
					tag.put("data-vip-hall-cost", String.valueOf(CommonUtil.nvl(price.getArrival_air_vip_hall()) * reserve.getArrival().getPerson()));
				}
			});
			air_service_type.setOutputMarkupId(true);
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
			
			final TextField<String> train_numb, train_time, guest;
			container_service_type_train.add(train_numb = new TextField<String>("arrival.train_numb"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN;
				}
			});
			train_numb.setLabel(new StringResourceModel("touragents.reservation.guest.service.train.number", null));
			train_numb.add(new AjaxOnBlurEvent());
			
			container_service_type_train.add(train_time = new TextField<String>("arrival.train_time"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN;
				}
			});
			train_time.setLabel(new StringResourceModel("touragents.reservation.guest.service.train.arrival.time", null));
			train_time.add(new AjaxOnBlurEvent());
			
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
				
				@Override
				public boolean isRequired() {
					return reserve.getArrival().isTaxi_order();
				}
			});
			taxi_order_car.setOutputMarkupId(true);
			taxi_order_car.add(new AttributeModifier("onchange", "recalcTaxiOrderCost('" + taxi_order_car.getMarkupId() + "');"));
			
			container_taxi_order.add(new HiddenField<Double>("arrival.taxi_price", new Model<Double>(){
				private static final long serialVersionUID = 1L;

				@Override
				public Double getObject() {
					return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), price.getArrival());
				}
			}));
			container_taxi_order.add(new TextField<Double>("arrival.taxi_order_cost", Double.class));
			
			container_taxi_order.add(new HiddenField<Byte>("arrival.location_type"));
			final TextField<String> location_value;
			container_taxi_order.add(location_value = new TextField<String>("arrival.location_value"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled() {
					return reserve.getArrival().getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_ADDRESS;
				}
			});
			location_value.add(new AjaxOnBlurEvent());
			location_value.setLabel(new StringResourceModel("touragents.reservation.guest.service.taxi_order.deliver.location_type.address", null));
			container_taxi_order.add(guest = new TextField<String>("arrival.guest"){
				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isRequired() {
					return reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN;
				}
			});
			guest.setLabel(new StringResourceModel("touragents.reservation.guest.service.train.arrival.guest", null));
			guest.add(new AjaxOnBlurEvent());
			
			
			container_taxi_order.add(new AjaxCheckBox("arrival.taxi_order") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					reserve.getArrival().setTaxi_order(Strings.isTrue(getValue()));
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

			transport_type_airport.add(new AjaxEventBehavior("onchange"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onEvent(AjaxRequestTarget target) {
					reserve.getArrival().setTransport_type(AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
					if (reserve.getArrival().getGuestlist().isEmpty()) reserve.getArrival().getGuestlist().add(new Guest(Guest.MAIN_GUEST));
					container_service_type_airport.setVisible(reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
					container_service_type_train.setVisible(reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
					target.appendJavaScript("doneDuplicate();");
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
			
			final WebMarkupContainer container_guests;
			transport_type.add(container_guests =  new WebMarkupContainer("container_guests"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible() {
					return reserve.getArrival().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT &&
						   (reserve.getArrival().getAir_service_type() != AdditionalServiceDetail.AIR_SERVICE_TYPE_UNKNOWN);
				}
			});
			container_guests.setOutputMarkupPlaceholderTag(true);
			
			final HiddenField<Short> person;
			container_guests.add(person = new HiddenField<Short>("arrival.person"));
			person.setOutputMarkupId(true);
			
			arrival_person.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (! Strings.isEmpty(arrival_person.getValue())) {
						reserve.setPerson(Short.valueOf(arrival_person.getValue()));
						reserve.getArrival().setPerson(reserve.getPerson());
						reserve.getArrival().setGuestlist(CommonUtil.getGuestList(reserve.getArrival().getGuestlist(), Integer.parseInt(arrival_person.getValue())));
					}
					target.appendJavaScript("doneDuplicate();");
					target.add(insurance_container);
					target.add(container_guests);
				}
			});
			
			container_guests.add(new ListView<Guest>("guestlist", new LoadableDetachableModel<List<Guest>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Guest> load() {
					return reserve.getArrival().getGuestlist();
				}
			}) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<Guest> item) {
					TextField<String> first_name, last_name;

					item.add(first_name = new TextField<String>("first_name", new PropertyModel<String>(item.getModel(), "first_name")));
					first_name.setLabel(new StringResourceModel("touragents.accountant_details.first_name", null));
					first_name.setOutputMarkupId(true);
					first_name.add(new AttributeModifier("data-item-index", item.getIndex()));
					first_name.add(new AjaxOnBlurEvent());

					item.add(last_name = new TextField<String>("last_name", new PropertyModel<String>(item.getModel(), "last_name")));
					last_name.setLabel(new StringResourceModel("touragents.accountant_details.last_name", null));
					last_name.setOutputMarkupId(true);
					last_name.add(new AttributeModifier("data-item-index", item.getIndex()));
					last_name.add(new AjaxOnBlurEvent());
					
					DropDownChoice<Nationality> nationality;
					item.add(nationality = new DropDownChoice<Nationality>("nationality", new PropertyModel<Nationality>(item.getModel(), "nationality"), new LoadableDetachableModel<List<Nationality>>() {
						private static final long serialVersionUID = 1L;

						@Override
						protected List<Nationality> load() {
							return new MyBatisHelper().selectList("selectNationalityList");
						}
					}, new ChoiceRenderer<Nationality>("name", "code")));
					nationality.setNullValid(true);
					nationality.setLabel(new StringResourceModel("hotels.guest.details.nationality", null));
					nationality.add(new AjaxOnBlurEvent());
					nationality.add(new AttributeModifier("data-item-index", item.getIndex()));
				}
			}.setReuseItems(true));
			
			//********************************************************** DEPARTURE ****************************************************************************//
			
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
			container_service_type_airport2.add(new TextField<String>("departure.destination").setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.destination.departure", null)).add(new AjaxOnBlurEvent()));
			final TextField<String> air_numb2, air_time2;
			container_service_type_airport2.add(air_numb2 = new TextField<String>("departure.air_numb"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT;
				}
			});
			air_numb2.setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.flight", null));
			air_numb2.add(new AjaxOnBlurEvent());
			
			container_service_type_airport2.add(air_time2 = new TextField<String>("departure.air_time"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT;
				}
			});
			air_time2.setLabel(new StringResourceModel("touragents.reservation.guest.service.airport.flight.departure.time", null));
			air_time2.add(new AjaxOnBlurEvent());
			
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
					return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(price.getDeparture_air_green_hall()) * reserve.getDeparture().getPerson());
				}
			}));
			*/
			container_air_service_type2.add(new Label("air_service_vip_hall_cost", new AbstractReadOnlyModel<Double>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Double getObject() {
					return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), CommonUtil.nvl(price.getDeparture_air_vip_hall())  * reserve.getDeparture().getPerson());
				}
			}));
			
			final HiddenField<Float> air_service_value2;
			final HiddenField<Byte> air_service_type2;
			container_air_service_type2.add(air_service_type2 = new HiddenField<Byte>("departure.air_service_type", Byte.class) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.put("data-green-hall-cost", String.valueOf(CommonUtil.nvl(price.getDeparture_air_green_hall()) * reserve.getDeparture().getPerson()));
					tag.put("data-vip-hall-cost", String.valueOf(CommonUtil.nvl(price.getDeparture_air_vip_hall()) * reserve.getDeparture().getPerson()));
				}
			});
			air_service_type2.setOutputMarkupId(true);
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
						reserve.getDeparture().setAir_terminal(Byte.parseByte(air_terminal2.getValue()));
					}
					target.add(container_air_service_type2);
				}
			});
			

			final WebMarkupContainer container_service_type_train2;
			transport_type2.add(container_service_type_train2 = new WebMarkupContainer("container_service_type_train"));
			container_service_type_train2.setOutputMarkupPlaceholderTag(true);
			container_service_type_train2.setVisible(reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
			
			final TextField<String> train_numb2, train_time2, guest2;
			container_service_type_train2.add(train_numb2 = new TextField<String>("departure.train_numb"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN;
				}
			});
			train_numb2.setLabel(new StringResourceModel("touragents.reservation.guest.service.train.number", null));
			train_numb2.add(new AjaxOnBlurEvent());
			
			container_service_type_train2.add(train_time2 = new TextField<String>("departure.train_time"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired() {
					return reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN;
				}
			});
			train_time2.setLabel(new StringResourceModel("touragents.reservation.guest.service.train.departure.time", null));
			train_time2.add(new AjaxOnBlurEvent());
			
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
				
				@Override
				public boolean isRequired() {
					return reserve.getDeparture().isTaxi_order();
				}
			});
			taxi_order_car2.setOutputMarkupId(true);
			taxi_order_car2.add(new AttributeModifier("onchange", "recalcTaxiOrderCost('" + taxi_order_car2.getMarkupId() + "');"));
			
			container_taxi_order2.add(new HiddenField<Double>("departure.taxi_price", new Model<Double>(){
				private static final long serialVersionUID = 1L;

				@Override
				public Double getObject() {
					return CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), price.getDeparture());
				}
			}));
			container_taxi_order2.add(new TextField<Double>("departure.taxi_order_cost", Double.class));
			container_taxi_order2.add(new HiddenField<Byte>("departure.location_type"));
			
			final TextField<String> location_value2;
			container_taxi_order2.add(location_value2 = new TextField<String>("departure.location_value"){
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
			location_value2.add(new AjaxOnBlurEvent());
			location_value2.setLabel(new StringResourceModel("touragents.reservation.guest.service.taxi_order.pickup.location_type.address", null));
			
			container_taxi_order2.add(guest2 = new TextField<String>("departure.guest"){
				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isRequired() {
					return reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN;
				}
			});
			guest2.setLabel(new StringResourceModel("touragents.reservation.guest.service.train.departure.guest", null));
			guest2.add(new AjaxOnBlurEvent());

			
			container_taxi_order2.add(new AjaxCheckBox("departure.taxi_order") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					reserve.getDeparture().setTaxi_order(Strings.isTrue(getValue()));
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
			
			container_taxi_order2.add(new TextField<String>("departure.taxi_order_time").add(new AjaxOnBlurEvent()));
			container_taxi_order2.add(new TextField<String>("departure.phone_number"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled() {
					return reserve.getDeparture().getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_ADDRESS;
				};
			}.add(new AjaxOnBlurEvent()));
			
			final WebMarkupContainer container_guests2;
			transport_type2.add(container_guests2 =  new WebMarkupContainer("container_guests"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible() {
					return reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT &&
						   (reserve.getDeparture().getAir_service_type() != AdditionalServiceDetail.AIR_SERVICE_TYPE_UNKNOWN); 
				}
			});
			container_guests2.setOutputMarkupPlaceholderTag(true);
			
			final DropDownChoice<Short> person2;
			container_guests2.add(person2 = new DropDownChoice<Short>("departure.person", new LoadableDetachableModel<List<Short>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Short> load() {
					return CommonUtil.getShortList((short) 25, false);
				}
			}, new MyChoiceRenderer<Short>()));
			
			container_guests2.add(new ListView<Guest>("guestlist", new LoadableDetachableModel<List<Guest>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Guest> load() {
					return reserve.getDeparture().getGuestlist();
				}
			}) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<Guest> item) {
					TextField<String> first_name, last_name;

					item.add(first_name = new TextField<String>("first_name", new PropertyModel<String>(item.getModel(), "first_name")));
					first_name.setLabel(new StringResourceModel("touragents.accountant_details.first_name", null));
					first_name.setOutputMarkupId(true);
					first_name.add(new AttributeModifier("data-item-index", item.getIndex()));
					first_name.add(new AjaxOnBlurEvent());

					item.add(last_name = new TextField<String>("last_name", new PropertyModel<String>(item.getModel(), "last_name")));
					last_name.setLabel(new StringResourceModel("touragents.accountant_details.last_name", null));
					last_name.setOutputMarkupId(true);
					last_name.add(new AttributeModifier("data-item-index", item.getIndex()));
					last_name.add(new AjaxOnBlurEvent());
					
					DropDownChoice<Nationality> nationality;
					item.add(nationality = new DropDownChoice<Nationality>("nationality", new PropertyModel<Nationality>(item.getModel(), "nationality"), new LoadableDetachableModel<List<Nationality>>() {
						private static final long serialVersionUID = 1L;

						@Override
						protected List<Nationality> load() {
							return new MyBatisHelper().selectList("selectNationalityList");
						}
					}, new ChoiceRenderer<Nationality>("name", "code")));
					nationality.setNullValid(true);
					nationality.add(new AttributeModifier("data-item-index", item.getIndex()));
					nationality.setLabel(new StringResourceModel("hotels.guest.details.nationality", null));
					nationality.add(new AjaxOnBlurEvent());
				}
			}.setReuseItems(true));
			
			person2.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (! Strings.isEmpty(person2.getValue())) {
						reserve.getDeparture().setGuestlist(CommonUtil.getGuestList(reserve.getDeparture().getGuestlist(), Integer.parseInt(person2.getValue())));
						reserve.getDeparture().setPerson(Short.parseShort(person2.getValue()));
					}
					target.appendJavaScript("myInit();");
					target.add(transport_type2);
				}
			});
			

			transport_type_airport2.add(new AjaxEventBehavior("onchange"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onEvent(AjaxRequestTarget target) {
					reserve.getDeparture().setTransport_type(AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
					if (reserve.getDeparture().getGuestlist().isEmpty()) reserve.getDeparture().getGuestlist().add(new Guest(Guest.MAIN_GUEST));
					container_service_type_airport2.setVisible(reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT);
					container_service_type_train2.setVisible(reserve.getDeparture().getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_TRAIN);
					target.appendJavaScript("doneDuplicate();");
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
			
			add(new AjaxAirServiceTypeCallBack(){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, String typeId, byte typeVal, String objId, float objVal) {
					Component component = MyForm.this.get(typeId);
					component.setDefaultModelObject(typeVal);
					component = MyForm.this.get(objId);
					component.setDefaultModelObject(objVal);
					if (typeId.equals(air_service_type.getInputName())) target.add(container_guests);
					else if (typeId.equals(air_service_type2.getInputName())) target.add(container_guests2);
				}

				@Override
				protected void getJavaScript(String jscript) {
					javascript = jscript;
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

			add(new Label("jscript", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return javascript;
				}
			}).setEscapeModelStrings(false).setMarkupId("js"));
		}
		
		@Override
		protected void onAfterRender() {
			super.onAfterRender();
			if (! editable) JavaScriptUtils.writeJavaScript(getResponse(), "$('input,select').prop('disabled', true); $('span.input-group-addon').remove()");
		}
		
	}
	
	private class InsuranceInfo extends Panel{
		private static final long serialVersionUID = 1L;

		public InsuranceInfo(String id) {
			super(id);
		}
	}
	
	public abstract class AjaxAirServiceTypeCallBack extends AbstractDefaultAjaxBehavior {
		private static final long serialVersionUID = 1L;
		private String javascript = "\n";  

		@Override
		protected void respond(AjaxRequestTarget target) {
			RequestCycle cycle = RequestCycle.get();
		    WebRequest webRequest = (WebRequest) cycle.getRequest();
		    StringValue param1 = webRequest.getQueryParameters().getParameterValue("param1");
		    StringValue param2 = webRequest.getQueryParameters().getParameterValue("param2");
		    StringValue param3 = webRequest.getQueryParameters().getParameterValue("param3");
		    StringValue param4 = webRequest.getQueryParameters().getParameterValue("param4");
			onUpdate(target, param1.toString(), (byte) param2.toInt(0), param3.toString(), (float) param4.toDouble(0));
		}
		
		@Override
		public void renderHead(Component component, IHeaderResponse response) {
			super.renderHead(component, response);
			javascript = "";  
			javascript = javascript.concat("function airServiceTypeCallback(typeId,typeVal,objId,objVal){\n");
			javascript = javascript.concat(" " + getCallbackScript() + "\n");
			javascript = javascript.concat("}\n");
			getJavaScript(javascript);
		}
		
		@Override
		protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		  super.updateAjaxAttributes(attributes);
		  attributes.getExtraParameters().put("param1", "PLACEHOLDER1");
		  attributes.getExtraParameters().put("param2", "PLACEHOLDER2");
		  attributes.getExtraParameters().put("param3", "PLACEHOLDER3");
		  attributes.getExtraParameters().put("param4", "PLACEHOLDER4");
		}
		
		@Override
		public CharSequence getCallbackScript() {
		  String script = super.getCallbackScript().toString();
		  script = script.replace("\"PLACEHOLDER1\"", "typeId");
		  script = script.replace("\"PLACEHOLDER2\"", "typeVal");
		  script = script.replace("\"PLACEHOLDER3\"", "objId");
		  script = script.replace("\"PLACEHOLDER4\"", "objVal");
		  return script;
		}

		protected abstract void onUpdate(AjaxRequestTarget target, String typeId, byte typeVal, String objId, float objVal);
		protected abstract void getJavaScript(String jscript);
	}

	
	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.reservation.guest.service.order", null);
	}
}
