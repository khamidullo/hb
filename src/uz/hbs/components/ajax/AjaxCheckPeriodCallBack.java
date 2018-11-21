package uz.hbs.components.ajax;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.string.StringValue;

public abstract class AjaxCheckPeriodCallBack extends AbstractDefaultAjaxBehavior {
	private static final long serialVersionUID = 1L;
	private String javascript = "\n";  


	@Override
	protected void respond(AjaxRequestTarget target) {
		RequestCycle cycle = RequestCycle.get();
	    WebRequest webRequest = (WebRequest) cycle.getRequest();
	    StringValue param1 = webRequest.getQueryParameters().getParameterValue("param1");
	    StringValue param2 = webRequest.getQueryParameters().getParameterValue("param2");
	    StringValue param3 = webRequest.getQueryParameters().getParameterValue("param3");
		onUpdate(target, param1.toString(), param2.toString(), (short) param3.toInt());
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		javascript = javascript.concat("function mycallback(check_in, check_out, number_of_nights){\n");
		javascript = javascript.concat(" " + getCallbackScript() + "\n");
		javascript = javascript.concat("}\n");
		getJavaScript(javascript);
	}
	
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
	  super.updateAjaxAttributes(attributes);
	  attributes.getExtraParameters().put("param1", "PLACEHOLDER1");
	  attributes.getExtraParameters().put("param2", "PLACEHOLDER2");
	  attributes.getExtraParameters().put("param3", "PLACEHOLDER3");
	}
	
	@Override
	public CharSequence getCallbackScript() {
	  String script = super.getCallbackScript().toString();
	  script = script.replace("\"PLACEHOLDER1\"", "check_in");
	  script = script.replace("\"PLACEHOLDER2\"", "check_out");
	  script = script.replace("\"PLACEHOLDER3\"", "number_of_nights");
	  return script;
	}


	
	protected abstract void onUpdate(AjaxRequestTarget target, String check_in, String check_out, Short number_of_nights);
	protected abstract void getJavaScript(String jscript);

}
