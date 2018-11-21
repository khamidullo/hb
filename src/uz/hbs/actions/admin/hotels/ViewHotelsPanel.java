package uz.hbs.actions.admin.hotels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Contract;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.User;
import uz.hbs.components.panels.ChangeStatusPanel;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.models.UserModels;

public class ViewHotelsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private Hotel hotel;
	private User user;
	private ModalWindow dialog;

	public ViewHotelsPanel(String id, IBreadCrumbModel breadCrumbModel, IModel<Hotel> model) {
		super(id, breadCrumbModel);

		add(dialog = new ModalWindow("dialog"));

		hotel = (Hotel) new MyBatisHelper().selectOne("selectHotelFullyByUserId", model.getObject().getUsers_id());

		add(new Label("manager", hotel.getManager()));
//		add(new Label("manager_email", hotel.getManager_email()));
		add(new Label("corporate_email", hotel.getCorporate_email()));
		add(new Label("legal_name", hotel.getLegal_name()));
		add(new Label("primary_phone", hotel.getPrimary_phone()));
		add(new Label("accountant", hotel.getAccountant()));
		add(new Label("accountant_phone", hotel.getAccountant_phone()));
		add(new Label("reservation_dep", hotel.getReservation_dep()));
		add(new Label("reservation_dep_phone", hotel.getReservation_dep_phone()));
//		add(new Label("secondary_phone", hotel.getSecondary_phone()));

		Contract contract = new MyBatisHelper().selectOne("selectContract", model.getObject().getUsers_id());
		if (contract == null) {
			contract = new Contract();
		}

		add(new Label("contract_type", (contract.getContract_type() == null ? ""
				: new StringResourceModel("touragents.contract.type." + contract.getContract_type().getId(), null).getString())));
		add(new Label("contract_number", (contract.getContract_number() == null ? "" : contract.getContract_number())));
		add(new Label("contract_date", (contract.getContract_date() == null ? "" : DateUtil.toString(contract.getContract_date(), "dd/MM/yyyy"))));
		add(new Label("commission_value", (contract.getCommission_value() == null ? "" : contract.getCommission_value() + "%")));

		final WebMarkupContainer status_container;
		add(status_container = new WebMarkupContainer("status_container"));
		status_container.setOutputMarkupId(true);
		status_container.add(new Label("status", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				user = new MyBatisHelper().selectOne("selectUserByUserId", hotel.getUsers_id());
				return UserModels.getUserStatus(user.getStatus().getId());
			}
		}));
		add(new AjaxLink<Void>("statuslink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				dialog.setTitle(getString("users.status.change"));
				dialog.setContent(new ChangeStatusPanel(dialog.getContentId(), user) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onOk(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						try {
							User user = (User) form.getDefaultModelObject();
							new MyBatisHelper().update("updateUserStatus", user);
							feedback.success(getString("users.status.change.success"));
						} catch (Exception e) {
							feedback.error(getString("users.status.change.fail"));
							logger.error("Exception", e);
						} finally {
							target.add(feedback);
						}
					}
				});
				dialog.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClose(AjaxRequestTarget target) {
						target.add(status_container);
					}
				});
				dialog.show(target);
			}
		});
		//		User user = new User();
		//		user.setWork(new IdLongAndName(model.getObject().getUsers_id()));
		//		user.setType(new IdAndValue((int) User.TYPE_HOTEL_USER));
		//		add(new ListUsersPanel("userListPanel", breadCrumbModel, user, model.getObject().getLegal_name()));
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.view", new Model<Hotel>(hotel));
	}
}