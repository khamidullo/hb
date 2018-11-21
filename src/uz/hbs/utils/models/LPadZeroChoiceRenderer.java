package uz.hbs.utils.models;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

public class LPadZeroChoiceRenderer<T> extends ChoiceRenderer<T> {
	private static final long serialVersionUID = 1L;
	public static final byte DEFAULT = 2;
	private byte cnt;
	
	public LPadZeroChoiceRenderer() {
		this(DEFAULT);
	}
	
	public LPadZeroChoiceRenderer(byte cnt) {
		this.cnt = cnt;
	}
	
	@Override
	public Object getDisplayValue(T object) {
		return lpad(object);
	}
	
	@Override
	public String getIdValue(T object, int index) {
		return String.valueOf(object);
	}
	
	public String lpad(T object){
		String s = String.valueOf(object);
		String result = "";
		for (byte b = 0; b < cnt - s.length(); b++){
			result = result.concat("0");
		}
		return result.concat(s);
	}
}
