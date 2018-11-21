package uz.hbs.components.panels.hotel;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.string.Strings;

import uz.hbs.beans.Facility;
import uz.hbs.beans.HotelDetail;
import uz.hbs.components.panels.AdditionalPanel;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class FacilitiesCheckingPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public FacilitiesCheckingPanel(String id, IModel<HotelDetail> model,
            ListModel<Facility> selectedModel,
            final ModalWindow dialog, final MyFeedbackPanel feedback) {
		super(id, model);
		
		final WebMarkupContainer container;
		add(container = new WebMarkupContainer("container"));
		container.setOutputMarkupId(true);
		final List<Facility> selectedList = selectedModel.getObject();
		
		LoadableDetachableModel<List<Facility>> availableModel = new LoadableDetachableModel<List<Facility>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Facility> load() {
				List<Facility> availableList = new MyBatisHelper().selectList("selectFacilitiesList");
				for (Facility avail : availableList){
					for (Facility select : selectedList){
						if (avail.getId().equals(select.getId())){
							avail.setPaid(select.isPaid());
							continue;
						}
					}
				}
				return availableList;
			}
		}; 
		
		final ListView<Facility> listView = new ListView<Facility>("selectedlist", availableModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Facility> listItem) {
            	final Facility facility = (Facility) listItem.getModelObject();
    			final CheckBox paid;
    			listItem.add(new Label("name", facility.getName()));
				listItem.add(paid = new AjaxCheckBox("paid",new Model<Boolean>(facility.isPaid())){
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						facility.setPaid(Strings.isTrue(getValue()));
					}
					
				});
				paid.setEnabled(selectedList.contains(facility));
				paid.setOutputMarkupId(true);
                listItem.add(new AjaxCheckBox("checkbox", new Model<Boolean>(selectedList.contains(facility))) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						boolean checked = Strings.isTrue(getValue());
						if (checked) {
							if (! selectedList.contains(facility)) selectedList.add((Facility) facility);
						} else {
							if (selectedList.contains(facility)) selectedList.remove(facility);
						}
						
						paid.setEnabled(checked);
						target.add(paid);
					}
				});
            }
        };
        listView.setOutputMarkupId(true);
        listView.setReuseItems(true);
        container.add(listView);
        
        add(new AjaxLink<Void>("add") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				dialog.setTitle(new StringResourceModel("facility.add", null));
				dialog.setMinimalHeight(200);
				dialog.setMinimalWidth(400);
				dialog.setInitialHeight(200);
				dialog.setInitialWidth(400);
				dialog.setContent(new AdditionalPanel<Facility>(dialog.getContentId(), new Facility()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form) {
						Facility facility = (Facility) form.getDefaultModelObject();
						if (((Integer)new MyBatisHelper().insert("insertFacility", facility)) > 0) {
							feedback.success(new StringResourceModel("facility.add.success", null).getString());
							target.add(feedback);
							dialog.close(target);
						}
					}

					@Override
					protected String onJavaScript() {
						return null;
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

}
