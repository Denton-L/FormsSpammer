package denton.formsspammer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ElementProcessor {
	
	private static class CheckboxElement {
		public final String name;
		public final String value;
		
		public CheckboxElement(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}
	
	
	private Set<String> texts = new HashSet<>();
	private Map<String, Set<String>> radios = new HashMap<>();
	private Set<CheckboxElement> checkboxes = new HashSet<>();
	private Set<String> dates = new HashSet<>();
	private Set<String> times = new HashSet<>();
	private Random random = new Random();
	
	public ElementProcessor(Set<FormElement> formElements) {
		for (FormElement formElement : formElements) {
			switch (formElement.type) {
			//TEXT, TEXTAREA, RADIO, CHECKBOX, DATE, TIME;
			case TEXT:
				texts.add(formElement.name);
				break;
			case RADIO:
				if (radios.containsKey(formElement.name)) {
					radios.get(formElement.name).add(formElement.value);
				} else {
					Set<String> newSet = new HashSet<>();
					newSet.add(formElement.value);
					radios.put(formElement.name, newSet);
				}
				break;
			case CHECKBOX:
				checkboxes.add(new CheckboxElement(formElement.name, formElement.value));
				break;
			case DATE:
				dates.add(formElement.name);
				break;
			case TIME:
				times.add(formElement.name);
				break;
			}
		}
	}
	
	public Set<NameValuePair> getParams() {
		Set<NameValuePair> params = new HashSet<>();
		return null;
	}
}
