package uz.hbs.actions.hotel.reports;

import java.util.Date;
import java.util.List;

import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.hotel.reports.panels.AllReservationsReportPanel;
import uz.hbs.actions.hotel.reports.panels.ExpectedGuestsReportPanel;
import uz.hbs.actions.hotel.reports.panels.GroupFromTAReportPanel;
import uz.hbs.actions.hotel.reports.panels.LeavingGuestsReportPanel;
import uz.hbs.actions.hotel.reports.panels.LeftGuestsReportPanel;
import uz.hbs.actions.hotel.reports.panels.RegisteredGuestsReportPanel;
import uz.hbs.actions.hotel.reports.panels.ReservationByTAReportPanel;
import uz.hbs.beans.reports.HotelReservationReportType;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.utils.FormatUtil;

public class ReservationReportPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;

	public ReservationReportPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
		this(id, breadCrumbModel, new ValueMap("date_from=" + FormatUtil.toString(new Date(), MyWebApplication.DATE_FORMAT) + ",date_to=" + FormatUtil.toString(new Date(), MyWebApplication.DATE_FORMAT)));
	}
	
	public ReservationReportPanel(final String id, final IBreadCrumbModel breadCrumbModel, ValueMap model) {
		super(id, breadCrumbModel);
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		final Form<ValueMap> form;
		add(form = new ReportForm("reportform", model));
		form.add(new Button("submit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit() {
				final ValueMap model = (ValueMap) form.getDefaultModelObject();
				byte report_type = ((HotelReservationReportType) model.get("type")).getId();
				switch (report_type){
					case HotelReservationReportType.ALL_RESERVATIONS: {
						setResponsePage(new HotelReportPage(new AllReservationsReportPanel("panel", breadCrumbModel, model)));
					} break;	
					case HotelReservationReportType.GROUP_FROM_TA: {
						setResponsePage(new HotelReportPage(new GroupFromTAReportPanel("panel", breadCrumbModel, model)));
					} break;	
					case HotelReservationReportType.REGISTERED_GUEST: {
						setResponsePage(new HotelReportPage(new RegisteredGuestsReportPanel("panel", breadCrumbModel, model)));
					} break;
					case HotelReservationReportType.RESERVATION_BY_TA: {
						setResponsePage(new HotelReportPage(new ReservationByTAReportPanel("panel", breadCrumbModel, model)));
					} break;
					case HotelReservationReportType.EXPECTED_GUESTS: {
						setResponsePage(new HotelReportPage(new ExpectedGuestsReportPanel("panel", breadCrumbModel, model)));
					} break;
					case HotelReservationReportType.LEAVING_GUESTS: {
						setResponsePage(new HotelReportPage(new LeavingGuestsReportPanel("panel", breadCrumbModel, model)));
					} break;
					case HotelReservationReportType.LEFT_GUESTS: {
						setResponsePage(new HotelReportPage(new LeftGuestsReportPanel("panel", breadCrumbModel, model)));
					} break;
				}
			}
		});
	}
	
	private class ReportForm extends Form<ValueMap>{
		private static final long serialVersionUID = 1L;

		public ReportForm(String id, ValueMap model) {
			super(id, new CompoundPropertyModel<ValueMap>(model));
			DateTextField datefrom;
			add(datefrom = new DateTextField("date_from", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			datefrom.add(new MyDatePicker());
			datefrom.setLabel(new StringResourceModel("from", null));
			datefrom.setRequired(true);

			DateTextField dateto;
			add(dateto = new DateTextField("date_to", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			dateto.add(new MyDatePicker());
			Date dateToModel = new MyBatisHelper().selectOne("selectCustomDate", 30);
			dateto.setModelObject(dateToModel);
			dateto.setLabel(new StringResourceModel("from", null));
			dateto.setRequired(true);
			
			DropDownChoice<HotelReservationReportType> reservation;
			add(reservation = new DropDownChoice<HotelReservationReportType>("type", new LoadableDetachableModel<List<HotelReservationReportType>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<HotelReservationReportType> load() {
					return HotelReservationReportType.getReservationList();
				}
			}, new ChoiceRenderer<HotelReservationReportType>("name", "id")){
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean isDisabled(HotelReservationReportType object, int index, String selected) {
					if (index == 3 || index == 4) 
						return true;
					else if (index >= 7) return true;
					return super.isDisabled(object, index, selected);
				}
			});
			reservation.setRequired(true);
			reservation.setLabel(new StringResourceModel("hotels.reservation.report.reservations", null));
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.reservation.report.title", null);
	}
}
