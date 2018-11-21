package uz.hbs.actions.admin.users;

import java.util.List;

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
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.IdLongAndName;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.models.UserModels;

public class EditUserPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private User user;

	public EditUserPanel(String id, IBreadCrumbModel breadCrumbModel, User user) {
		this(id, breadCrumbModel, user, false);
	}
	
	public EditUserPanel(String id, IBreadCrumbModel breadCrumbModel, User user, boolean workable) {
		super(id, breadCrumbModel);
		this.user = user;
		add(new UserForm("form", new CompoundPropertyModel<User>(user), workable));
	}
	
	private class UserForm extends Form<User> {
		private static final long serialVersionUID = 1L;

		public UserForm(String id, final IModel<User> model, final boolean workable) {
			super(id, model);
			final User user = model.getObject();
			
			final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			feedback.setEscapeModelStrings(false);
			add(feedback);
			
			TextField<String> login = new TextField<String>("login");
			login.setLabel(new StringResourceModel("users.login", null));
			add(login);
			
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
			
			LoadableDetachableModel<List<? extends IdAndValue>> typeList = UserModels.getTypeList(true, user.getType() != null ? user.getType().getId() : null);
			
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
			type.setEnabled(false);
			type.setLabel(new StringResourceModel("users.type", null));
			add(type);
			
			IModel<List<IdLongAndName>> workListModel = new LoadableDetachableModel<List<IdLongAndName>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<IdLongAndName> load() {
					User user = model.getObject();
					return new MyBatisHelper().selectList("selectWorkList", user.getType().getId());
				}
			};
			
			final WebMarkupContainer work_container;
			add(work_container = new WebMarkupContainer("work_container"));
			work_container.setOutputMarkupPlaceholderTag(true);
			work_container.setVisible(user.getType() != null && (user.getType().getId() == User.TYPE_TOURAGENT_USER || user.getType().getId() == User.TYPE_HOTEL_USER));
			
			final DropDownChoice<IdLongAndName> work;
			work_container.add(work = new DropDownChoice<IdLongAndName>("work", workListModel, new ChoiceRenderer<IdLongAndName>("name", "id")));
			work.setLabel(new StringResourceModel("users.type.work", null));
			work.setEnabled(false);
			
			type.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					work_container.setVisible(user.getType() != null && (user.getType().getId() == User.TYPE_TOURAGENT_USER || user.getType().getId() == User.TYPE_HOTEL_USER));
					if (work_container.isVisible()){
						work.setRequired(work_container.isVisible());
					} else {
						user.setWork(null);
					}
					target.add(work_container);
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
			status.setEnabled(user.getId().longValue() != getMySession().getUser().getId().longValue());
			status.setLabel(new StringResourceModel("users.status", null));
			add(status);
			
			add(new CheckBox("workable") {
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
					try {
						new MyBatisHelper().update("updateUser", user);
						feedback.success(new StringResourceModel("users.edit.success", null, new Object[]{ user.getName() }).getString());
					} catch (Exception e) {
						feedback.error(new StringResourceModel("users.edit.fail", null, new Object[]{ user.getName() }).getString());
						logger.error("Exception", e);
					} finally {
						target.add(feedback);
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
		return new StringResourceModel("users.edit", new Model<User>(user));
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
