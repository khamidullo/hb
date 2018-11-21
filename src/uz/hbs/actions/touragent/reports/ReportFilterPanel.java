package uz.hbs.actions.touragent.reports;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Reservation;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.session.MySession;

public class ReportFilterPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ReportFilterPanel(String id, final Reservation filter, final WebMarkupContainer wrapper, final MyFeedbackPanel feedback, boolean fromHomePage) {
		super(id);
		
		if (fromHomePage) {
			Reservation reservation = new MyBatisHelper().selectOne("selectTouragentReservationDatePeriod", ((MySession)getSession()).getUser().getId());
			if (reservation != null) {
				filter.setFromDate(reservation.getFromDate());
				filter.setToDate(reservation.getToDate());
			} else {
				Date toDateValue = new MyBatisHelper().selectOne("selectCustomDate" , 30);
				filter.setFromDate(new Date());
				filter.setToDate(toDateValue);
			}
		} else {
			Date toDateValue = new MyBatisHelper().selectOne("selectCustomDate" , 30);
			filter.setFromDate(new Date());
			filter.setToDate(toDateValue);
		}
		
		Form<Reservation> form = new Form<Reservation>("form", new CompoundPropertyModel<Reservation>(filter));
		add(form);
		
		DateTextField fromDate = new DateTextField("fromDate", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false));
		fromDate.add(new MyDatePicker());
		form.add(fromDate);

		
		DateTextField toDate = new DateTextField("toDate", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false));
		toDate.add(new MyDatePicker());
		form.add(toDate);

		TextField<String> hotel_name = new TextField<String>("hotel_name");
		form.add(hotel_name);
		
		IndicatingAjaxButton submit = new IndicatingAjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
				target.add(wrapper);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				target.add(feedback);
				target.add(wrapper);
			}
		};
		form.add(submit);
	}
}
