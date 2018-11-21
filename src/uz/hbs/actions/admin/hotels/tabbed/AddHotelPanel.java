package uz.hbs.actions.admin.hotels.tabbed;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.admin.AdminHomePage;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.HotelDetail;
import uz.hbs.beans.ReferenceInfo;
import uz.hbs.beans.ReservationRule;
import uz.hbs.beans.RoomSetup;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.tabs.BootstrapAjaxTabbedPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.HotelUtil;

public class AddHotelPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private Hotel hotel;
	private BootstrapAjaxTabbedPanel<?> tabbed;
	
	public AddHotelPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		hotel = new Hotel();
		
		final CompoundPropertyModel<Hotel> model = new CompoundPropertyModel<Hotel>(hotel);
		
		setDefaultModel(model);
		List<ITab> tabList = new ArrayList<ITab>();
		tabList.add(new AbstractTab(new StringResourceModel("hotels.title.account_details", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String componentId) {
				return new AccountDetailsTabbedPanel(componentId, model) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
					}

					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						try {
							Hotel hotel = (Hotel) form.getDefaultModelObject();
							SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
							HotelUtil.addNewHotel(sql, hotel, feedback, (MySession) getSession());
							hotel.setStep(Hotel.HOTEL_DETAILS);
							tabbed.setSelectedTab(tabbed.getSelectedTab() + 1);
							target.add(tabbed);
						} finally {
							target.add(feedback);
						}
					}

					@Override
					protected boolean isVisibleNextButton() {
						return false;
					}
				};
			}
			
			@Override
			public boolean isVisible() {
				return hotel.getStep() >= Hotel.HOTEL_ACCOUNT_DETAILS; 
			}
		});
		tabList.add(new AbstractTab(new StringResourceModel("hotels.title.hotel_details", null)) {//Fail
			private static final long serialVersionUID = 1L;
			
			@Override
			public WebMarkupContainer getPanel(String componentId) {
				return new HotelDetailsTabberPanel(componentId, model) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						HotelDetail model = (HotelDetail) form.getDefaultModelObject();
						try {
							SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
							try {
								HotelUtil.addHotelDetails(sql, model, hotel, (MySession) getSession());
								setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_LIST));
							} catch (Exception e) {
								logger.error("Exception", e);
								sql.rollback();
							} finally {
								sql.close();
							}
						} finally {
							target.add(feedback);
						}
					}
					
					@Override
					protected void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						HotelDetail model = (HotelDetail) form.getDefaultModelObject();
						try {
							SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
							try {
								HotelUtil.addHotelDetails(sql, model, hotel, (MySession) getSession());
							} catch (Exception e) {
								logger.error("Exception", e);
								sql.rollback();
							} finally {
								sql.close();
							}
							hotel.setStep(Hotel.RESERVATION_RULES);
							tabbed.setSelectedTab(tabbed.getSelectedTab() + 1);
							target.add(tabbed);
						} finally {
							target.add(feedback);
						}
					}

					@Override
					protected boolean isVisibleNextButton() {
						return true;
					}
				};
			}
			
			@Override
			public boolean isVisible() {
				return hotel.getStep() >= Hotel.HOTEL_DETAILS;
			}
		});
		tabList.add(new AbstractTab(new StringResourceModel("hotels.title.reservation_rules", null)) {//Ok
			private static final long serialVersionUID = 1L;
			
			@Override
			public WebMarkupContainer getPanel(String componentId) {
				return new ReservationRulesTabbedPanel(componentId, model) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						ReservationRule model = (ReservationRule) form.getDefaultModelObject();
						try {
							SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
							try {
								HotelUtil.addReservationRules(sql, model, hotel);
								setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_LIST));
							} catch (Exception e) {
								sql.rollback();
								logger.error("Exception", e);
							} finally {
								sql.close();
							}
						} finally {
							target.add(feedback);
						}
					}
					
					@Override
					protected void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						ReservationRule model = (ReservationRule) form.getDefaultModelObject();
						try {
							SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
							try {
								HotelUtil.addReservationRules(sql, model, hotel);//ok
							} catch (Exception e) {
								sql.rollback();
								logger.error("Exception", e);
							} finally {
								sql.close();
							}
							hotel.setStep(Hotel.REFERENCE_INFO);
							tabbed.setSelectedTab(tabbed.getSelectedTab() + 1);
							target.add(tabbed);
						} finally {
							target.add(feedback);
						}
					}

					@Override
					protected boolean isVisibleNextButton() {
						return true;
					}
				};
			}
			
			@Override
			public boolean isVisible() {
				return hotel.getStep() >= Hotel.RESERVATION_RULES;
			}
		});
		tabList.add(new AbstractTab(new StringResourceModel("hotels.title.reference_info", null)) {//Ok
			private static final long serialVersionUID = 1L;
			
			@Override
			public WebMarkupContainer getPanel(String componentId) {
				return new ReferenceInfoTabbedPanel(componentId, model) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						ReferenceInfo model = (ReferenceInfo) form.getDefaultModelObject();
						try {
							if (model.getRoom_types() == null || model.getRoom_types().isEmpty()) {
								feedback.error("Room Types is required");
								return;
							}
							SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
							try {
								HotelUtil.addReferenceInfo(sql, model, hotel, (MySession) getSession());
								setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_LIST));
							} catch (Exception e) {
								logger.error("Exception", e);
								sql.rollback();
							} finally {
								sql.close();
							}
						} finally {
							target.add(feedback);
						}
					}

					@Override
					protected void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						ReferenceInfo model = (ReferenceInfo) form.getDefaultModelObject();
						try {
							if (model.getRoom_types() == null || model.getRoom_types().isEmpty()) {
								feedback.error("Room Types is required");
								return;
							}
							SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
							try {
								HotelUtil.addReferenceInfo(sql, model, hotel, (MySession) getSession());//Ok
							} catch (Exception e) {
								logger.error("Exception", e);
								sql.rollback();
							} finally {
								sql.close();
							}
							hotel.setStep(Hotel.ROOM_SETUP);
							tabbed.setSelectedTab(tabbed.getSelectedTab() + 1);
							target.add(tabbed);
						} finally {
							target.add(feedback);
						}
					}

					@Override
					protected boolean isVisibleNextButton() {
						return true;
					}
				};
			}
			
			@Override
			public boolean isVisible() {
				return hotel.getStep() >= Hotel.REFERENCE_INFO;
			}
		});
		tabList.add(new AbstractTab(new StringResourceModel("hotels.title.room_setup", null)) {//Fail
			private static final long serialVersionUID = 1L;
			
			@Override
			public WebMarkupContainer getPanel(String componentId) {
				return new RoomSetupTabbedPanel(componentId, model) {
					private static final long serialVersionUID = 1L;

					@Override
					protected boolean isVisibleNextButton() {
						return true;
					}

					@Override
					protected void onSave(AjaxRequestTarget target, RoomSetup setup, MyFeedbackPanel feedback) {
						setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_LIST));
					}

					@Override
					protected void onNext(AjaxRequestTarget target, RoomSetup setup, MyFeedbackPanel feedback) {
						setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_LIST));
					}

					@Override
					protected boolean isVisibleSaveButton() {
						return true;
					}
				};
			}
			
			@Override
			public boolean isVisible() {
				return hotel.getStep() >= Hotel.ROOM_SETUP;
			}
		});
		add(tabbed = new BootstrapAjaxTabbedPanel<ITab>("tabbed", tabList){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onAjaxUpdate(AjaxRequestTarget target) {
				super.onAjaxUpdate(target);
			}
		});
	}
	
	@Override
	protected void onAfterRender() {
		super.onAfterRender();
	}


	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.add", null);
	}
}
