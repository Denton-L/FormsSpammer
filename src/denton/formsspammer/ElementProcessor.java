package denton.formsspammer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ElementProcessor {

	public static final double TEXT_CHANCE = 0.5;
	public static final double CHECKBOX_CHANCE = 0.5;
	public static final double TIME_CHANCE = 0.5;

	private static class CheckboxElement {
		public final String name;
		public final String value;

		public CheckboxElement(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}

	private Set<String> texts = new HashSet<>();
	private Map<String, List<String>> radios = new HashMap<>();
	private Set<CheckboxElement> checkboxes = new HashSet<>();
	private Set<String> dates = new HashSet<>();
	private Set<String> times = new HashSet<>();
	private Map<String, List<String>> selects = new HashMap<>();
	private Random random = new Random();

	public ElementProcessor(Set<FormElement> formElements) {
		for (FormElement formElement : formElements) {
			switch (formElement.type) {
			case TEXT:
				texts.add(formElement.name);
				break;
			case RADIO:
				if (radios.containsKey(formElement.name)) {
					radios.get(formElement.name).add(formElement.value);
				} else {
					List<String> newSet = new ArrayList<>();
					newSet.add(formElement.value);
					radios.put(formElement.name, newSet);
				}
				break;
			case CHECKBOX:
				checkboxes.add(new CheckboxElement(formElement.name,
						formElement.value));
				break;
			case DATE:
				dates.add(formElement.name);
				break;
			case TIME:
				times.add(formElement.name);
				break;
			case SELECT:
				if (selects.containsKey(formElement.name)) {
					selects.get(formElement.name).add(formElement.value);
				} else {
					List<String> newSet = new ArrayList<>();
					newSet.add(formElement.value);
					selects.put(formElement.name, newSet);
				}
			}
		}
	}

	public Set<NameValuePair> getParams() {
		Set<NameValuePair> params = new HashSet<>();

		for (String text : texts) {
			StringBuilder randomString = new StringBuilder();
			while (random.nextGaussian() < TEXT_CHANCE) {
				randomString.append((char) (' ' + random.nextInt(95)));
			}
			params.add(new NameValuePair(text, randomString));
		}

		for (Map.Entry<String, List<String>> radio : radios.entrySet()) {
			List<String> options = radio.getValue();
			params.add(new NameValuePair(radio.getKey(), options.get(random
					.nextInt(options.size()))));
		}

		for (CheckboxElement checkbox : checkboxes) {
			if (random.nextGaussian() < CHECKBOX_CHANCE) {
				params.add(new NameValuePair(checkbox.name, checkbox.value));
			}
		}

		for (String date : dates) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			params.add(new NameValuePair(date, dateFormatter.format(new Date(
					random.nextLong()))));
		}

		for (String time : times) {
			SimpleDateFormat timeFormatter;
			String timeFormat = "HH:mm";

			if (random.nextGaussian() < TIME_CHANCE) {
				timeFormat += ":ss";
				if (random.nextGaussian() < TIME_CHANCE) {
					timeFormat += ".SSS";
				}
			}

			timeFormatter = new SimpleDateFormat(timeFormat);
			params.add(new NameValuePair(time, timeFormatter.format(new Date(
					random.nextLong()))));

		}

		for (Map.Entry<String, List<String>> select : selects.entrySet()) {
			List<String> options = select.getValue();
			params.add(new NameValuePair(select.getKey(), options.get(random
					.nextInt(options.size()))));
		}

		return params;
	}
}
