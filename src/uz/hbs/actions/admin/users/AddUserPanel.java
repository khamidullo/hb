package uz.hbs.actions.admin.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import uz.hbs.beans.Account;
import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.IdLongAndName;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DigestUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.models.UserModels;

public class AddUserPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private User owner;
	public AddUserPanel(String id, IBreadCrumbModel breadCrumbModel, User owner) {
		this(id, breadCrumbModel, owner, false);
	}
	
	public AddUserPanel(String id, IBreadCrumbModel breadCrumbModel, User owner, boolean workable) {
		super(id, breadCrumbModel);
		this.owner = owner;
		User model = new User();
		if (owner != null) {
			model.setType(owner.getType());
			model.setWork(owner.getWork());
		}
		add(new UserForm("form", new CompoundPropertyModel<User>(model), false));
	}

	private class UserForm extends Form<User> {
		private static final long serialVersionUID = 1L;

		public UserForm(String id, final IModel<User> model, final boolean workable) {
			super(id, model);
			
			final User user = getModelObject();
			
			final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			feedback.setEscapeModelStrings(false);
			add(feedback);
			
			final TextField<String> login = new TextField<String>("login");
			login.setLabel(new StringResourceModel("users.login", null));
			login.setOutputMarkupId(true);
			login.setRequired(!(owner != null && owner.getType() != null && (owner.getType().getId() == User.TYPE_HOTEL_USER || owner.getType().getId() == User.TYPE_TOURAGENT_USER)));
			login.setEnabled(!(owner != null && owner.getType() != null && (owner.getType().getId() == User.TYPE_HOTEL_USER || owner.getType().getId() == User.TYPE_TOURAGENT_USER)));
			login.setOutputMarkupPlaceholderTag(true);
			add(login);
			
			PasswordTextField password = new PasswordTextField("password");
			password.setRequired(true);
			password.setLabel(new StringResourceModel("users.password", null));
			add(password);
			
			RequiredTextField<String> name = new RequiredTextField<String>("name");
			name.setLabel(new StringResourceModel("users.name", null));
			add(name);
			
			EmailTextField email = new EmailTextField("email");
			email.setRequired(true);
			email.setLabel(new StringResourceModel("users.email", null));
			email.add(EmailAddressValidator.getInstance());
			add(email);

			TextField<String> phoneNumber = new TextField<String>("phone_number");
			phoneNumber.setLabel(new StringResourceModel("hotels.phone", null));
			add(phoneNumber);
			
			LoadableDetachableModel<List<? extends IdAndValue>> typeList = UserModels.getTypeList(true, owner != null && owner.getType() != null ? owner.getType().getId() : null);
			
			DropDownChoice<IdAndValue> type = new DropDownChoice<IdAndValue>("type", typeList, new IChoiceRenderer<IdAndValue>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getDisplayValue(IdAndValue object) {
					return object.getValue();
				}

				@Override
				public String getIdValue(IdAndValue object, int index) {
					return object.getId().toString();
				}
			});
			type.setRequired(true);
			type.setLabel(new StringResourceModel("users.type", null));
			add(type);
			
			IModel<List<IdLongAndName>> workListModel = new LoadableDetachableModel<List<IdLongAndName>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<IdLongAndName> load() {
					User user = model.getObject();
					List<IdLongAndName> list = new MyBatisHelper().selectList("selectWorkList", user.getType().getId());
					
					if (owner != null) {
						for (IdLongAndName idLongAndName : list) {
							if (owner.getWork().getId().longValue() == idLongAndName.getId().longValue()) {
								owner.getWork().setName(idLongAndName.getName());
							}
						}
						list = new ArrayList<IdLongAndName>();
						list.add(owner.getWork());
						return list;
					} else {
						return list;
					}
				}
			};
			
			final WebMarkupContainer work_container;
			add(work_container = new WebMarkupContainer("work_container"));
			work_container.setOutputMarkupPlaceholderTag(true);
			work_container.setVisible(user.getType() != null && (user.getType().getId() == User.TYPE_TOURAGENT_USER || user.getType().getId() == User.TYPE_HOTEL_USER));
			
			final DropDownChoice<IdLongAndName> work;
			work_container.add(work = new DropDownChoice<IdLongAndName>("work", workListModel, new ChoiceRenderer<IdLongAndName>("name", "id")));
			work.setLabel(new StringResourceModel("users.type.work", null));
			
			type.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					
					if(user.getType() != null && (user.getType().getId() == User.TYPE_TOURAGENT_USER || user.getType().getId() == User.TYPE_HOTEL_USER)) {
						login.setEnabled(false);
						login.setRequired(false);
						model.getObject().setLogin("");
					} else {
						login.setEnabled(true);
						login.setRequired(true);
					}
					
					work_container.setVisible(user.getType() != null && (user.getType().getId() == User.TYPE_TOURAGENT_USER || user.getType().getId() == User.TYPE_HOTEL_USER));
					if (work_container.isVisible()){
						work.setRequired(work_container.isVisible());
					} else {
						user.setWork(null);
					}
					target.add(work_container);
					target.add(login);
				}
			});
			
			LoadableDetachableModel<List<? extends IdAndValue>> statusList = UserModels.getStatusList();
			
			DropDownChoice<IdAndValue> status = new DropDownChoice<IdAndValue>("status", statusList, new IChoiceRenderer<IdAndValue>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getDisplayValue(IdAndValue object) {
					return object.getValue();
				}
				
				@Override
				public String getIdValue(IdAndValue object, int index) {
					return object.getId().toString();
				}
			});
			status.setRequired(true);
			status.setLabel(new StringResourceModel("users.status", null));
			add(status);
			
			add(new CheckBox("workable"){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible() {
					return workable;
				}
			});
			
			IndicatingAjaxButton submit = new IndicatingAjaxButton("submit") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					User user = (User) form.getDefaultModelObject();
					SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
					try {
						user.setPassword(DigestUtil.getDigest(user.getPassword()));
						user.setInitiator_user_id(((MySession) getSession()).getUser().getId());
//						if (user.getType().getId() == User.TYPE_HOTEL)
//							user.setType(new IdAndValue((int) User.TYPE_HOTEL_USER));
//						else if (user.getType().getId() == User.TYPE_TOURAGENCY)
//							user.setType(new IdAndValue((int) User.TYPE_TOURAGENT_USER));
						
						CommonUtil.generateLogin(user, sql);
						
						sql.insert("insertUser", user);

						logger.debug("Step #1: User created=" + user);
						
						Account acc = CommonUtil.insertUserAccount(sql, user);
						
						logger.debug("Step #2: User Account created, Id=" + acc.getId());
						
						HashMap<String, Serializable> param = new HashMap<String, Serializable>();
						param.put("users_id", user.getId());
						param.put("roles_id", 1);//SIGN_IN
						sql.insert("insertUserRoles", param);
						
						logger.debug("Step #2: User role created=" + param);
						
						sql.commit();
						
						feedback.success(new StringResourceModel("users.add.success", null, new Object[]{ user.getName() }).getString());
					} catch (Exception e) {
						sql.rollback();
						logger.error("Exception", e);
						feedback.error(new StringResourceModel("users.add.fail", null, new Object[]{ user.getName() }).getString());
					} finally {
						sql.close();
						target.add(feedback);
						form.setEnabled(false);
						target.add(form);
					}
				}
				
				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(UserForm.this.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
				}
			};
			add(submit);
			CommonUtil.setFormComponentRequired(this);
		}
	} 
	
	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("users.add", null);
	}
	
	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	} 
}
