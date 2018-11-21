package uz.hbs.temp;

import java.util.Date;
import java.util.Map;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;

import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.template.MyPage;
import uz.hbs.utils.HeaderItemUtil;

public class Home extends MyPage {
	private static final long serialVersionUID = 1L;

	public Home() {
		add(new HomeForm("form"));
	}
	
	private class HomeForm extends Form<Void>{
		private static final long serialVersionUID = 1L;
//		private BootstrapFileInputField fileInputField;
		public HomeForm(String id) {
			super(id);

			// set this form to multipart mode (always needed for uploads!)
            setMultiPart(true);
			
            MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
			add(feedback);
			info("Information message demonstration");
			warn("Warning message demonstration");
			error("Error message demonstration");
			success("Success message demostration");
			logger.debug("Main page reached");
			
//			FileInputConfig fileInputConfig = new FileInputConfig();
//			fileInputConfig.showPreview(true);
//			fileInputConfig.browseClass("btn btn-default");
//			fileInputConfig.showUpload(true);
//			fileInputConfig.showRemove(true);
//			fileInputConfig.initialCaption("Образец рисунка");
			
//			fileInputField = new BootstrapFileInputField("fileInput", null, fileInputConfig);
//			add(fileInputField);
			
		}
		
		@Override
		protected void onSubmit() {
			super.onSubmit();
//			List<FileUpload> fileUploads = fileInputField.getFileUploads();
//			for (FileUpload fileUpload : fileUploads) {
//				logger.info("FileName=" + fileUpload.getClientFileName());
//			}
		}
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return getMySession().isSignedIn();
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		HeaderItemUtil.setDatetimepickerJavaScriptContentHeaderItem(response, ((MySession) getSession()).getLocale(), new Date());
		super.renderHead(response);
	}


	
	@Override
	public String getTitle() {
		return getString("main_page");
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
