package uz.hbs.utils;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.IResourceStream;

public abstract class AJAXDownload extends AbstractAjaxBehavior {
	private static final long serialVersionUID = 1L;

	public void initiate(AjaxRequestTarget target) {
		CharSequence url = getCallbackUrl();
		target.appendJavaScript("window.location.href='" + url + "'");
	}

	@Override
	public void onRequest() {
		ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(getResourceStream(), getFileName());
		handler.setContentDisposition(ContentDisposition.ATTACHMENT);
		getComponent().getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
	}

	protected abstract String getFileName();
	protected abstract IResourceStream getResourceStream();
}
