package uz.hbs.markup.html.bootstrapmodal;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class BootstrapModalPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public BootstrapModalPanel(String id, String header) {
		super(id);

		// Modal window header
		Label label = new Label("header", header);
		add(label);

		// the windows content
		add(getContent("content"));

		// Buttons
		AjaxLink<Void> cancel = new AjaxLink<Void>("cancel") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onCancel(target);
			}

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				// tag.put("onclick", "javascript:$(document).ready(function(){$('.modal').modal('hide');});");
				tag.put("onclick", "javascript: closeModalWin('" + getMarkupId() + "');");
			}

			// @Override
			// public void renderHead(IHeaderResponse response) {
			// response.render(JavaScriptHeaderItem.forScript("$(document).ready(function(){$('#" + getMarkupId()
			// + "').click(function () {$('.modal').modal('hide');});});", "" + new Random().nextLong()));
			// super.renderHead(response);
			// }
		};
		add(cancel);

		AjaxLink<Void> submit = new AjaxLink<Void>("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onSubmit(target);
			}

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				// tag.put("onclick", "javascript:$(document).ready(function(){$('.modal').modal('hide');});");
				tag.put("onclick", "javascript: closeModalWin('" + getMarkupId() + "');");
			}

			// @Override
			// public void renderHead(IHeaderResponse response) {
			// response.render(JavaScriptHeaderItem.forScript("$(document).ready(function(){$('#" + getMarkupId()
			// + "').click(function () {$('.modal').modal('hide');});});", "" + new Random().nextLong()));
			// super.renderHead(response);
			// }
		};
		add(submit);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forScript(
				"function closeModalWin(markId) {$(document).ready(function(){$('#' + markId).click(function () {$('.modal').modal('hide');});});}",
				null));
		super.renderHead(response);
	}

	public abstract Panel getContent(String id);

	public abstract void onCancel(AjaxRequestTarget target);

	public abstract void onSubmit(AjaxRequestTarget target);
}
