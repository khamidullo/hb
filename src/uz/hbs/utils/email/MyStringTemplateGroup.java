package uz.hbs.utils.email;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

public class MyStringTemplateGroup extends StringTemplateGroup {

	public MyStringTemplateGroup(String name) {
		super(name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized StringTemplate defineTemplate(String name, String template) {
		StringTemplate st = createStringTemplate();
		st.setName(name);
		st.setGroup(this);
		st.setNativeGroup(this);
		st.setTemplate(template);
		st.setErrorListener(listener);
		templates.put(name, st);
		return st;
	}
	
	@Override
	public String getFileNameFromTemplateName(String templateName) {
		return templateName;
	}
}
