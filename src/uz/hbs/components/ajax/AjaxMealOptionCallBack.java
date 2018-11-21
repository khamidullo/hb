package uz.hbs.components.ajax;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.string.StringValue;

public abstract class AjaxMealOptionCallBack extends AbstractDefaultAjaxBehavior {
	private static final long serialVersionUID = 1L;
	private String javascript = "\n";  


	@Override
	protected void respond(AjaxRequestTarget target) {
		RequestCycle cycle = RequestCycle.get();
	    WebRequest webRequest = (WebRequest) cycle.getRequest();
	    StringValue param1 = webRequest.getQueryParameters().getParameterValue("param1");
		onUpdate(target, (byte) param1.toInt());
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		javascript = javascript.concat("function mealoptioncallback(mealoption){\n");
		javascript = javascript.concat(" " + getCallbackScript() + "\n");
		javascript = javascript.concat("}\n");
		getJavaScript(javascript);
	}
	
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
	  super.updateAjaxAttributes(attributes);
	  attributes.getExtraParameters().put("param1", "PLACEHOLDER1");
	}
	
	@Override
	public CharSequence getCallbackScript() {
	  String script = super.getCallbackScript().toString();
	  script = script.replace("\"PLACEHOLDER1\"", "mealoption");
	  return script;
	}

	protected abstract void onUpdate(AjaxRequestTarget target, byte meal_options);
	protected abstract void getJavaScript(String jscript);
}
