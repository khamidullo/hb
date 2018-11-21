package uz.hbs.actions.touragent.reports;

import java.util.HashMap;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.touragent.newbooking.group.TAGroupReservationPanel;
import uz.hbs.actions.touragent.newbooking.individual.TAIndividualReservationPanel;
import uz.hbs.actions.touragent.reservations.ReservationsPanel;
import uz.hbs.actions.touragent.reservations.panels.BookingGroupReservationDetailsView;
import uz.hbs.actions.touragent.reservations.panels.BookingReservationDetailsView;
import uz.hbs.beans.AdditionalServiceDetail;
import uz.hbs.beans.GroupReservation;
import uz.hbs.beans.Guest;
import uz.hbs.beans.IndividualReservation;
import uz.hbs.beans.Insurance;
import uz.hbs.beans.Reservation;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.panel.ActionsPanel;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.HotelUtil;
import uz.hbs.utils.ReserveUtil;
import uz.hbs.utils.email.ReservationEmailNotifier;

public class ReservationsResultWithActionPanel extends ReservationsResultPanel {
	private static final long serialVersionUID = 1L;

	public ReservationsResultWithActionPanel(String id, final ReservationsPanel reservationsPanel, Reservation filter) {
		super(id, filter);

		columns.add(new FilteredAbstractColumn<Reservation, String>(new StringResourceModel("actions.action", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form);
			}

			@Override
			public void populateItem(Item<ICellPopulator<Reservation>> cellItem, String componentId, IModel<Reservation> rowModel) {
				cellItem.add(new ActionsPanel<Reservation>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;

					@Override
					public Link<Reservation> addPrintLink(final IModel<Reservation> model) {
						return new Link<Reservation>("print") {
							private static final long serialVersionUID = 1L;

							public void onClick() {
								reservationsPanel.activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										final ReservationDetail reserve = new MyBatisHelper().selectOne("selectReservationDetailById", model.getObject().getId());
										if (reserve != null) {
											if (reserve.isIs_group()){
												GroupReservation group = new GroupReservation(reserve);
												List<ReservationRoom> reservroom = new MyBatisHelper().selectList("selectReserveRoomAllList", reserve.getId());
												for (ReservationRoom rroom : reservroom) {
													List<Guest> guestList = new MyBatisHelper().selectList("selectGuestsList2", rroom.getId());
													guestList = CommonUtil.getGuestList(guestList, rroom.getRoom().getHolding_capacity().getId());
													rroom.setGuestlist(guestList);
													rroom.setGuest_count((short)guestList.size());
												}
												group.setReserverooms(reservroom);
												return new BookingGroupReservationDetailsView(componentId, breadCrumbModel, group, true); 
											} else {
												return new BookingReservationDetailsView(componentId, breadCrumbModel, new IndividualReservation(reserve), true);
											}
										}
										return null;
									}
								});																
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}

					@Override
					public Link<Reservation> addDeleteLink(IModel<Reservation> model) {
						return new Link<Reservation>("delete") {
							private static final long serialVersionUID = 1L;

							public void onClick() {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}

					@Override
					public Link<Reservation> addEditLink(final IModel<Reservation> model) {
						return new Link<Reservation>("edit") {
							private static final long serialVersionUID = 1L;

							public void onClick() {
								reservationsPanel.activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										final ReservationDetail reserve = new MyBatisHelper().selectOne("selectReservationDetailById", model.getObject().getId());
										if (reserve != null) {
											if (reserve.isIs_group()){
												GroupReservation groupReserve = new GroupReservation(reserve);

												HashMap<String, Object> param = new HashMap<String, Object>();
												param.put("reservations_id", groupReserve.getId());
												param.put("service_type", AdditionalServiceDetail.SERVICE_TYPE_ARRIVED);
												
												AdditionalServiceDetail addservice = new MyBatisHelper().selectOne("selectAdditionalServiceDetail", param);
												
												if (addservice != null) groupReserve.setArrival(addservice);
												
												param.put("service_type", AdditionalServiceDetail.SERVICE_TYPE_DEPARTED);
												
												addservice = new MyBatisHelper().selectOne("selectAdditionalServiceDetail", param);
												
												if (addservice != null) groupReserve.setDeparture(addservice);

												List<Insurance> insurancelist = new MyBatisHelper().selectList("selectInsuranceListByReserveId", param);
												groupReserve.setInsuranceList(insurancelist);
												
												List<ReservationRoom> reservroom = new MyBatisHelper().selectList("selectReserveRoomAllList", reserve.getId());
												for (ReservationRoom rroom : reservroom) {
													List<Guest> guestList = new MyBatisHelper().selectList("selectGuestsList2", rroom.getId());
													guestList = CommonUtil.getGuestList(guestList, rroom.getRoom().getHolding_capacity().getId());
													rroom.setGuestlist(guestList);
													rroom.setGuest_count((short)guestList.size());
												}
												groupReserve.setReserverooms(reservroom);
												return new TAGroupReservationPanel(componentId, breadCrumbModel, new Model<GroupReservation>(groupReserve), null); 
											} else {
												IndividualReservation indvReserve = ReserveUtil.getIndividualReserve(reserve);
												
												HashMap<String, Object> param = new HashMap<String, Object>();
												param.put("reservations_id", indvReserve.getId());
												
												param.put("service_type", AdditionalServiceDetail.SERVICE_TYPE_ARRIVED);
												AdditionalServiceDetail addservice = new MyBatisHelper().selectOne("selectAdditionalServiceDetail", param);
												if (addservice != null) indvReserve.setArrival(addservice);
												
												param.put("service_type", AdditionalServiceDetail.SERVICE_TYPE_DEPARTED);
												addservice = new MyBatisHelper().selectOne("selectAdditionalServiceDetail", param);
												if (addservice != null) indvReserve.setDeparture(addservice);
												
												List<Insurance> insurancelist = new MyBatisHelper().selectList("selectInsuranceListByReserveId", param);
												indvReserve.setInsuranceList(insurancelist);
												
												return new TAIndividualReservationPanel(componentId, breadCrumbModel, new Model<IndividualReservation>(indvReserve), null);
											}
										}
										return null;
									}
								});																
							}

							@Override
							public boolean isVisible() {
								return model.getObject().getStatus() == ReservationStatus.RESERVED;
							}
						};
					}

					@Override
					public Link<Reservation> addViewLink(final IModel<Reservation> model) {
						return new Link<Reservation>("view") {
							private static final long serialVersionUID = 1L;

							public void onClick() {
								reservationsPanel.activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										final ReservationDetail reserve = new MyBatisHelper().selectOne("selectReservationDetailById", model.getObject().getId());
										if (reserve != null) {
											if (reserve.isIs_group()){
												GroupReservation groupReserve = new GroupReservation(reserve);
												List<ReservationRoom> reservroom = new MyBatisHelper().selectList("selectReserveRoomAllList", reserve.getId());
												for (ReservationRoom rroom : reservroom) {
													List<Guest> guestList = new MyBatisHelper().selectList("selectGuestsList2", rroom.getId());
													guestList = CommonUtil.getGuestList(guestList, rroom.getRoom().getHolding_capacity().getId());
													rroom.setGuestlist(guestList);
													rroom.setGuest_count((short)guestList.size());
												}
												groupReserve.setReserverooms(reservroom);
												return new TAGroupReservationPanel(componentId, breadCrumbModel, new Model<GroupReservation>(groupReserve), null, false);
											} else {
												IndividualReservation indvReserve = ReserveUtil.getIndividualReserve(reserve);
												
												HashMap<String, Object> param = new HashMap<String, Object>();
												param.put("reservations_id", indvReserve.getId());
												
												param.put("service_type", AdditionalServiceDetail.SERVICE_TYPE_ARRIVED);
												AdditionalServiceDetail addservice = new MyBatisHelper().selectOne("selectAdditionalServiceDetail", param);
												if (addservice != null) indvReserve.setArrival(addservice);
												
												param.put("service_type", AdditionalServiceDetail.SERVICE_TYPE_DEPARTED);
												addservice = new MyBatisHelper().selectOne("selectAdditionalServiceDetail", param);
												if (addservice != null) indvReserve.setDeparture(addservice);
												
												List<Insurance> insurancelist = new MyBatisHelper().selectList("selectInsuranceListByReserveId", param);
												indvReserve.setInsuranceList(insurancelist);
												
												return new TAIndividualReservationPanel(componentId, breadCrumbModel, new Model<IndividualReservation>(indvReserve), null, false);
											}
										}
										return null;
									}
								});																
							}

							@Override
							public boolean isVisible() {
								return true;
							}
						};
					}

					@Override
					public Link<Reservation> addCancelLink(final IModel<Reservation> model) {
						return new Link<Reservation>("cancel") {
							private static final long serialVersionUID = 1L;

							public void onClick() {
								if (HotelUtil.cancelReserve(model.getObject().getId(), ((MySession) getSession()).getUser().getId())) {
									ReservationEmailNotifier.cancelReservation(model.getObject().getId());
									success(getString("hotels.reservation.cancel.success"));
								} else {
									error(getString("hotels.reservation.cancel.fail"));
								}
							}
							
							@Override
							protected CharSequence getOnClickScript(CharSequence url) {
								return "return confirm('" + getString("confirm") + "');";
							}

							@Override
							public boolean isVisible() {
								return model.getObject().getStatus() == ReservationStatus.RESERVED;
							}
						};
					}

					@Override
					public Link<Reservation> addViewLogLink(final IModel<Reservation> model) {
						return new Link<Reservation>("viewLog") {
							private static final long serialVersionUID = 1L;

							public void onClick() {
								reservationsPanel.activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new ReservationLogPanel(componentId, breadCrumbModel, model.getObject().getId());
									}
								});
							}

							@Override
							public boolean isVisible() {
								return true;
							}
						};
					}

					@Override
					public Link<Reservation> addUserListLink(IModel<Reservation> model) {
						return new Link<Reservation>("userList") {
							private static final long serialVersionUID = 1L;

							public void onClick() {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}
				});
			}
		});
	}
}
