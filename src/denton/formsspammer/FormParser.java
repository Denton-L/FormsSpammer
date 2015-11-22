package denton.formsspammer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormParser {

	private URL postURL;
	private Set<FormElement> formElements;

	public FormParser(URL url) {
		formElements = new HashSet<>();
		String content = getContent(url);
		parsePostURL(content);
		parseElements(content);
	}

	private String getContent(URL url) {
		int c;
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				url.openStream()));) {
			while ((c = br.read()) != 0) {
				sb.append((char) c);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return sb.toString();
	}

	private void parsePostURL(String content) {
		Matcher matcher = Pattern
				.compile(
						"<form action=\"(https://docs\\.google\\.com/forms/d/[a-zA-Z0-9]+/formResponse)\" method=\"POST\" id=\"ss-form\" target=\"_self\" onsubmit=\"\">")
				.matcher(content);
		matcher.find();
		try {
			postURL = new URL(matcher.group(1));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			postURL = null;
		}
	}

	private void parseElements(String content) {
		final Map<String, ElementType> elementMap = new HashMap<>();
		Matcher matcher = Pattern.compile(
				"<input type=\"([a-z]+)\" name=\"(entry\\.[0-9]+)").matcher(
				content);

		// this sets up the Map
		for (ElementType type : ElementType.values()) {
			elementMap.put(type.toString().toLowerCase(), type);
		}

		while (matcher.find()) {
			formElements.add(new FormElement(elementMap.get(matcher.group(1)), matcher.group(2)));
		}

		parseTextAreas(content);
	}

	private void parseTextAreas(String content) {
		Matcher matcher = Pattern.compile("<textarea name=\"(entry.[0-9]+)\"")
				.matcher(content);
		while (matcher.find()) {
			formElements.add(new FormElement(ElementType.TEXTAREA, matcher.group(1)));
		}

	}

	public URL getPostURL() {
		return this.postURL;
	}

	public Set<FormElement> getFormElementSet() {
		return this.formElements;
	}
}
