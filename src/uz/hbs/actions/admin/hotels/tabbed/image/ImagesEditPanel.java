package uz.hbs.actions.admin.hotels.tabbed.image;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.UploadedFile;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.utils.FileUtil;

public class ImagesEditPanel extends Panel {
	private static final Logger logger = LoggerFactory.getLogger(ImagesEditPanel.class);
	private static final long serialVersionUID = 1L;
	private static final int MAX_COLUMNS = 6;
	private IDataProvider<UploadedFile> dataProvider;
	private GridView<UploadedFile> gridView;

	public ImagesEditPanel(String id, final Long objectId, final boolean isHotel) {
		super(id);
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("type", isHotel ? 0 : 1);

		final WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		container.add(feedback);

		dataProvider = new ListDataProvider<UploadedFile>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<UploadedFile> getData() {
				param.put("id", objectId);
				return new MyBatisHelper().selectList("selectHotelImages", param);
			}
		};

		gridView = new GridView<UploadedFile>("rows", dataProvider) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<UploadedFile> item) {
				item.add(new ImagePanel("item", logger, feedback, container, item) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onDelete(AjaxRequestTarget target) {
						SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
						try {
							param.put("id", item.getModelObject().getId());
							sql.delete("deleteHotelImages", param);
							sql.delete("deleteHotelUploadFile", item.getModelObject().getId());
							sql.commit();
							FileUtil.deleteFile(getRequestCycle().getRequest().getContextPath() + "/" + item.getModelObject().getLink());

							dataProvider = new ListDataProvider<UploadedFile>() {
								private static final long serialVersionUID = 1L;

								@Override
								protected List<UploadedFile> getData() {
									param.put("id", objectId);
									return new MyBatisHelper().selectList("selectHotelImages", param);
								}
							};
							setColumns(getColumnsCount((int) dataProvider.size()));
							setRows(getRowsCount((int) dataProvider.size()));
							target.add(container);
							feedback.success(new StringResourceModel("hotels.image.delete.success", null).getString());
							logger.debug("Image deleted, Id=" + item.getModelObject().getId());
						} catch (Exception e) {
							logger.error("Image not deleted or not existed, Id=" + item.getModelObject().getId());
							logger.error("Exception", e);
							feedback.error(new StringResourceModel("hotels.image.delete.error", null).getString());
							sql.rollback();
						} finally {
							sql.close();
						}
						target.add(feedback);
					}

					@Override
					protected void onSetDefault(AjaxRequestTarget target, boolean isDefault, String markupId) {
						param.put("id", objectId);
						SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
						try {
							sql.update("updatePullOffAllDefaultImages", param);
							if (isDefault) {
								sql.update("updateSetDefaultImage", item.getModelObject().getId());
							}
							sql.commit();
							String jscript = "$('input:checkbox[data-item=defaultImage]').not($('input#" + markupId + "')).prop('checked', false);";
							//System.out.println(jscript);
							target.appendJavaScript(jscript);
						} catch (Exception e) {
							logger.error("Exception", e);
							sql.rollback();
						} finally {
							sql.close();
						}
					}
				});
			}

			@Override
			protected void populateEmptyItem(Item<UploadedFile> item) {
				item.add(new Label("item", ""));
			}
		};
		gridView.setColumns(getColumnsCount((int) dataProvider.size()));
		gridView.setRows(getRowsCount((int) dataProvider.size()));
		container.add(gridView);
	}

	public void refresh() {
		gridView.setColumns(getColumnsCount((int) dataProvider.size()));
		gridView.setRows(getRowsCount((int) dataProvider.size()));
	}

	private int getColumnsCount(int size) {
		if (size == 0)
			return 1;

		return size > MAX_COLUMNS ? MAX_COLUMNS : size;
	}

	private int getRowsCount(int size) {
		if (size == 0)
			return 1;

		if (size > MAX_COLUMNS)
			return ((int) size / MAX_COLUMNS) + (size % MAX_COLUMNS == 0 ? 0 : 1);
		else
			return 1;
	}
}
