package uz.hbs.components.panels.hotel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.beans.Equipment;
import uz.hbs.beans.Facility;
import uz.hbs.beans.Service;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.tabs.BootstrapTabbedPanel;

public class ReferenceHotelPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private ModalWindow dialog;
	private MyFeedbackPanel feedback;

	public ReferenceHotelPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);

		add(dialog = new ModalWindow("dialog"));

		List<ITab> tablist = new ArrayList<ITab>();
		// *************************** Facility **********************************//
		tablist.add(new AbstractTab(new StringResourceModel("hotels.preference.facilities", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String componentId) {
				final List<Facility> list = new MyBatisHelper().selectList("selectFacilities");
				return new AddReferenceHotelPanel<Facility>(componentId, list, dialog, "selectAutoCompleteFacility", true) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onAppend(AjaxRequestTarget target, Form<?> form) {
						ValueMap model = (ValueMap) form.getDefaultModelObject();
						Facility facility;
						try {
							if (new MyBatisHelper().insert("insertFacility", facility = new Facility(model.getString("name"))) > 0) {
								list.add(facility);
							}
						} catch (Exception e) {
							logger.error("Exception", e);
							error("Error, When the facility is added.");
						}
					}

					@Override
					protected void onEdit(AjaxRequestTarget target, Form<?> form, int id, int index) {
						ValueMap model = (ValueMap) form.getDefaultModelObject();
						Facility facility;
						try {
							facility = new Facility(id, model.getString("name"));
							facility.setFilter_flag(model.getBoolean("filter_flag"));
							if (new MyBatisHelper().update("updateFacility", facility) > 0) {
								list.get(index).setName(facility.getName());
								list.get(index).setFilter_flag(facility.getFilter_flag());
							}
						} catch (Exception e) {
							logger.error("Exception", e);
							error("Error, When the facility is edited.");
						}
					}

					@Override
					protected boolean isDeleted(AjaxRequestTarget target, int id, String name) {
						try {
							if (new MyBatisHelper().delete("deleteFacility", id) > 0) {
								feedback.success(new StringResourceModel("hotels.preference.facilities.delete.success", null, new Object[] { name }).getString());
								return true;
							} else {
								feedback.error(new StringResourceModel("hotels.preference.facilities.delete.error", null, new Object[] { name }).getString());
								return false;
							}
						} catch (Exception e) {
							logger.error("Exception", e);
							feedback.error("Error, When the facility is deleted.");
						} finally {
							target.add(feedback);
						}
						return false;
					}

					@Override
					protected void onChangeFlag(Facility object, Boolean flag) {
						Facility facility = new Facility(object.getId());
						facility.setFilter_flag(flag);
						new MyBatisHelper().update("updateFacility", facility);
					}
				};
			}
		});
		// *************************** Services **********************************//
		tablist.add(new AbstractTab(new StringResourceModel("hotels.preference.services", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String componentId) {
				final List<Service> list = new MyBatisHelper().selectList("selectAvailableRoomService");
				return new AddReferenceHotelPanel<Service>(componentId, list, dialog, "selectAutoCompleteService") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onAppend(AjaxRequestTarget target, Form<?> form) {
						ValueMap model = (ValueMap) form.getDefaultModelObject();
						Service service;
						try {
							if (new MyBatisHelper().insert("insertService", service = new Service(model.getString("name"))) > 0) {
								list.add(service);
							}
						} catch (Exception e) {
							logger.error("Exception", e);
							error("Error, When the service is added.");
						}
					}

					@Override
					protected void onEdit(AjaxRequestTarget target, Form<?> form, int id, int index) {
						ValueMap model = (ValueMap) form.getDefaultModelObject();
						Service service;
						try {
							if (new MyBatisHelper().update("updateService", service = new Service(id, model.getString("name"))) > 0) {
								list.get(index).setName(service.getName());
								feedback.success(new StringResourceModel("update.successfully", null));
							}
						} catch (Exception e) {
							logger.error("Exception", e);
							error("Error, When the service is edited.");
						}
					}

					@Override
					protected boolean isDeleted(AjaxRequestTarget target, int id, String name) {
						try {
							if (new MyBatisHelper().delete("deleteService", id) > 0) {
								feedback.success(new StringResourceModel("hotels.preference.services.delete.success", null, new Object[] { name }).getString());
								return true;
							} else {
								feedback.error(new StringResourceModel("hotels.preference.services.delete.error", null, new Object[] { name }).getString());
								return false;
							}
						} catch (Exception e) {
							logger.error("Exception", e);
							error("Error, When the service is deleted.");
						} finally {
							target.add(feedback);
						}
						return false;
					}

					@Override
					protected void onChangeFlag(Service object, Boolean flag) {
						// TODO Auto-generated method stub
						
					}
				};
			}
		});
		// *************************** Equipment **********************************//
		tablist.add(new AbstractTab(new StringResourceModel("hotels.preference.equipments", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String componentId) {
				final List<Equipment> list = new MyBatisHelper().selectList("selectEquipments");
				return new AddReferenceHotelPanel<Equipment>(componentId, list, dialog, "selectAutoCompleteEquipment", true) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onAppend(AjaxRequestTarget target, Form<?> form) {
						ValueMap model = (ValueMap) form.getDefaultModelObject();
						Equipment equipment;
						try {
							if (new MyBatisHelper().insert("insertEquipment", equipment = new Equipment(model.getString("name"))) > 0) {
								list.add(equipment);
							}
						} catch (Exception e) {
							logger.error("Exception", e);
							error("Error, When the service is added.");
						}
					}

					@Override
					protected void onEdit(AjaxRequestTarget target, Form<?> form, int id, int index) {
						ValueMap model = (ValueMap) form.getDefaultModelObject();
						Equipment equipment;
						try {
							equipment = new Equipment(id, model.getString("name"));
							equipment.setFilter_flag(model.getBoolean("filter_flag"));
							if (new MyBatisHelper().update("updateEquipment", equipment) > 0) {
								list.get(index).setName(equipment.getName());
								list.get(index).setFilter_flag(equipment.getFilter_flag());
							}
						} catch (Exception e) {
							logger.error("Exception", e);
							error("Error, When the service is edited.");
						}
					}

					@Override
					protected boolean isDeleted(AjaxRequestTarget target, int id, String name) {
						try {
							if (new MyBatisHelper().delete("deleteEquipment", id) > 0) {
								feedback.success(new StringResourceModel("hotels.preference.equipments.delete.success", null, new Object[] { name }).getString());
								return true;
							} else {
								feedback.error(new StringResourceModel("hotels.preference.equipments.delete.error", null, new Object[] { name }).getString());
								return true;
							}
						} catch (Exception e) {
							logger.error("Exception", e);
							error("Error, When the equipment is deleted.");
						} finally {
							target.add(feedback);
						}
						return false;
					}

					@Override
					protected void onChangeFlag(Equipment object, Boolean flag) {
						Equipment equipment = new Equipment(object.getId());
						equipment.setFilter_flag(flag);
						new MyBatisHelper().update("updateEquipment", equipment);
					}
				};
			}
		});
		add(new BootstrapTabbedPanel<ITab>("tabbed", tablist));
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.preference", null);
	}
}
