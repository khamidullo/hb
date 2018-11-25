package uz.hbs.actions.hotel.reservations.panels;

import java.math.BigDecimal;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import uz.hbs.beans.Bill;
import uz.hbs.beans.Guest;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.FormatUtil;

public class IssueBillPrintPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private BigDecimal total = new BigDecimal("0");
	private BigDecimal subtotal = new BigDecimal("0");
	private BigDecimal paid = new BigDecimal("0");

	public IssueBillPrintPanel(String id, final long reservation_id) {
		super(id);
		
		Guest guest = new MyBatisHelper().selectOne("selectIssueBillPrintGuestDetail", reservation_id);
		
		WebMarkupContainer guest_detail;
		add(guest_detail = new WebMarkupContainer("guest_detail"));
		guest_detail.add(new Label("guest_name", guest.getFirst_name()+ " " + guest.getLast_name()));
		guest_detail.add(new Label("address", guest.getAddress()));
		guest_detail.add(new Label("country", guest.getCountry().getName()));
		guest_detail.add(new Label("city", guest.getCity()));
		guest_detail.add(new Label("passport", guest.getPassport_number()));
		
		Hotel hotel =  new MyBatisHelper().selectOne("selectIssueBillPrintHotelDetail", reservation_id);
		
		WebMarkupContainer hotel_detail;
		add(hotel_detail = new WebMarkupContainer("hotel_detail"));
		hotel_detail.add(new Label("hotel_name", hotel.getName()));
		hotel_detail.add(new Label("address", hotel.getAddress()));
		hotel_detail.add(new Label("country", hotel.getCountry_name()));
		hotel_detail.add(new Label("city", hotel.getCity()));
		hotel_detail.add(new Label("phone", hotel.getPrimary_phone()));
		
		final ReservationDetail reserv = new MyBatisHelper().selectOne("selectIssueBillPrintReservationDetail", reservation_id);
		
		add(new Label("actual_check_in", FormatUtil.toString(reserv.getCheck_in(), "dd/MM/yyyy HH:mm:ss")));
		add(new Label("check_out", FormatUtil.toString(reserv.getCheck_out(), "dd/MM/yyyy")));
		add(new Label("nights", reserv.getNumber_of_nights()));
		add(new Label("children", reserv.getChildren()));
		add(new Label("adults", reserv.getAdults()));
		//add(new Label("extra_bed", reserv.getExtra_bed()));
		
		add(new ListView<Bill>("billlist", new LoadableDetachableModel<List<Bill>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Bill> load() {
				return new MyBatisHelper().selectList("selectIssueBills", reservation_id);
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Bill> item) {
				Bill bill = (Bill) item.getDefaultModelObject();
				item.add(new Label("bill_date", FormatUtil.toString(bill.getBill_date(), "dd/MM/yyyy")));
				item.add(new Label("description", bill.getDescription()));
				item.add(new Label("charge", FormatUtil.toString(bill.getCharge().doubleValue())));
				item.add(new Label("debit", FormatUtil.toString(bill.getDebit().doubleValue())));
				item.add(new Label("credit", FormatUtil.toString(bill.getCredit().doubleValue())));
				item.add(new Label("note", bill.getNote()).setEscapeModelStrings(false));
				subtotal = subtotal.add(bill.getCredit());
				paid = paid.add(bill.getDebit());
			}
		});
		
		add(new Label("bill_number", new LoadableDetachableModel<Long>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Long load() {
				return reserv.getId();
			}
		}));
		
		final ReservationRuleType rule  = new MyBatisHelper().selectOne("selectIssueBillReservationRuleById", reservation_id);
		
		add(new Label("subtotal", new LoadableDetachableModel<BigDecimal>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected BigDecimal load() {
				return subtotal;
			}
		}));
//		add(new Label("service_charge", new Model<Float>(rule.getService_charge())));
		add(new Label("city_tax", new Model<Float>(rule.getcity_tax())));
		add(new Label("total", new LoadableDetachableModel<BigDecimal>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected BigDecimal load() {
//				total = total.add(subtotal).add(subtotal.multiply(BigDecimal.valueOf(rule.getService_charge()).divide(BigDecimal.valueOf(100))));
				total = total.add(BigDecimal.valueOf(rule.getcity_tax()));
				return total;
			}
		}));
		add(new Label("paid", new LoadableDetachableModel<BigDecimal>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected BigDecimal load() {
				return paid;
			}
		}));
		add(new Label("total_due", new LoadableDetachableModel<BigDecimal>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected BigDecimal load() {
				return total.subtract(paid);
			}
		}));
	}

}
