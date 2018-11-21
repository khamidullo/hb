package uz.hbs.components.panels.hotel;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.beans.IdAndName;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.utils.CommonUtil;

public abstract class AddReferenceHotelPanel<T extends IdAndName> extends Panel {
	private static final long serialVersionUID = 1L;

	public AddReferenceHotelPanel(String id, final List<T> list, final ModalWindow dialog, final String autocomplete_selector) {
		this(id, list, dialog, autocomplete_selector, false);
	}

	public AddReferenceHotelPanel(String id, final List<T> list, final ModalWindow dialog, final String autocomplete_selector, final boolean withFlag) {
		super(id);
		final WebMarkupContainer container;
		add(container = new WebMarkupContainer("container"));
		container.setOutputMarkupId(true);
		container.setOutputMarkupPlaceholderTag(true);
		container.setVisible(list.size() > 0);

		final WebMarkupContainer use_search_container;
		container.add(use_search_container = new WebMarkupContainer("use_search_container"));
		use_search_container.setVisible(withFlag);

		container.add(new ListView<T>("list", new ListModel<T>(list)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<T> item) {
				item.add(new Label("name", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return item.getModelObject().getName();
					}
				}));
				item.add(new AjaxCheckBox("filter_flag", new Model<Boolean>(item.getModelObject().getFilter_flag())){
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						onChangeFlag(item.getModelObject(), Strings.isTrue(getValue()));
					}
					
					@Override
					public boolean isVisible() {
						return withFlag;
					};
				});
				item.add(new AjaxLink<Void>("remove") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						if (item.getModelObject().getId() != null) {
							if (isDeleted(target, item.getModelObject().getId(), item.getModelObject().getName()))
								list.remove(item.getModelObject());
						} else
							list.remove(item.getModelObject());
						container.setVisible(list.size() > 0);
						target.add(container);
					}

					@Override
					protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
						super.updateAjaxAttributes(attributes);
						IAjaxCallListener listener = new AjaxCallListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public CharSequence getPrecondition(Component component) {
								return "return confirm('" + getString("confirm") + "');";
							}

						};
						attributes.getAjaxCallListeners().add(listener);
					}
				});
				item.add(new AjaxLink<Void>("edit") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						dialog.setTitle(getString("preference.edit"));
						ValueMap model = new ValueMap();
						model.put("name", item.getModelObject().getName());
						model.put("filter_flag", CommonUtil.nvl(item.getModelObject().getFilter_flag()));
						dialog.setContent(new DialogPanel(dialog.getContentId(), autocomplete_selector, model, withFlag) {
							private static final long serialVersionUID = 1L;

							@Override
							protected void onSave(AjaxRequestTarget target, Form<?> form) {
								onEdit(target, form, item.getModelObject().getId(), item.getIndex());
								dialog.close(target);
							}
						});
						dialog.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClose(AjaxRequestTarget target) {
								target.add(container);
							}
						});
						dialog.show(target);
					}

					@Override
					public boolean isVisible() {
						return item.getModelObject().getId() != null;
					};
				});
			}
		}.setReuseItems(true));
		add(new AjaxLink<Void>("append") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				dialog.setTitle(getString("preference.add"));
				dialog.setContent(new DialogPanel(dialog.getContentId(), autocomplete_selector, withFlag) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form) {
						onAppend(target, form);
						dialog.close(target);
						container.setVisible(true);
					}
				});
				dialog.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClose(AjaxRequestTarget target) {
						target.add(container);
					}
				});
				dialog.show(target);
			}
		});
	}

	private abstract class DialogPanel extends Panel {
		private static final long serialVersionUID = 1L;
		private MyFeedbackPanel feedback;

		public DialogPanel(String id, final String autocomplete_selector, boolean withFlag) {
			this(id, autocomplete_selector, new ValueMap(), withFlag);
		}

		public DialogPanel(String id, final String autocomplete_selector, ValueMap model, final boolean withFlag) {
			super(id);
			Form<ValueMap> form;
			add(feedback = new MyFeedbackPanel("feedback"));
			feedback.setOutputMarkupId(true);

			add(form = new Form<ValueMap>("form", new CompoundPropertyModel<ValueMap>(model)));
			form.add(new AutoCompleteTextField<String>("name", String.class) {
				private static final long serialVersionUID = 1L;

				@Override
				protected Iterator<String> getChoices(String input) {
					if (Strings.isEmpty(input)) {
						List<String> emptyList = Collections.emptyList();
						return emptyList.iterator();
					}
					List<String> selectList = new MyBatisHelper().selectList(autocomplete_selector, input);
					return selectList.iterator();
				}
			}.setRequired(true));
			form.add(new CheckBox("filter_flag").setVisible(withFlag));
			form.add(new AjaxButton("save") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					onSave(target, form);
				}
			});
		}

		protected abstract void onSave(AjaxRequestTarget target, Form<?> form);
	}

	protected abstract void onAppend(AjaxRequestTarget target, Form<?> form);
	
	protected abstract void onChangeFlag(T object, Boolean flag);

	protected abstract void onEdit(AjaxRequestTarget target, Form<?> form, int id, int index);

	protected abstract boolean isDeleted(AjaxRequestTarget target, int id, String name);
}
