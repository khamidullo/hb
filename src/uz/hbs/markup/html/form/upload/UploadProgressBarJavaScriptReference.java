package uz.hbs.markup.html.form.upload;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.resource.JQueryPluginResourceReference;

import uz.hbs.utils.Dependencies;

/**
 * Provides a JavaScript that overrides Wicket-Extensions' progressbar.js
 * methods with ones which are more suitable for Twitter Bootstrap Progress component
 */
public class UploadProgressBarJavaScriptReference extends JQueryPluginResourceReference {
	private static final long serialVersionUID = 1L;

	public UploadProgressBarJavaScriptReference() {
		super(UploadProgressBarJavaScriptReference.class, "progressbar.js");
	}

	@Override
	public Iterable<? extends HeaderItem> getDependencies() {
		JavaScriptReferenceHeaderItem headerItem = JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
				org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar.class, "progressbar.js"));

		return Dependencies.combine(super.getDependencies(), headerItem);
	}
}
