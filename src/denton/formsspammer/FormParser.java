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
			while ((c = br.read()) != -1) {
				sb.append((char) c);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return sb.toString();
	}

	private void parsePostURL(String content) {
		Matcher urlMatcher = Pattern
				.compile(
						"<form action=\"(https://docs\\.google\\.com/forms/d/.+?/formResponse)\" method=\"POST\" id=\"ss-form\" target=\"_self\" onsubmit=\"\">")
				.matcher(content);
		urlMatcher.find();
		try {
			postURL = new URL(urlMatcher.group(1));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			postURL = null;
		}
	}

	private void parseElements(String content) {
		final Map<String, ElementType> elementMap = new HashMap<>();
		Matcher normalMatcher = Pattern
				.compile(
						"<input type=\"([a-z]+)\" name=\"(entry\\..+?)\" value=\"(.*?)\"")
				.matcher(
						content);
		Matcher textAreaMatcher = Pattern.compile(
				"<textarea name=\"(entry.[0-9]+)\" ")
				.matcher(content);
		Matcher selectMatcher = Pattern
				.compile(
						"<select name=\"(entry.[0-9]+)\".+?>(?:<option value=\"(.*?)\">.*?<\\/option>\\s?)*<\\/select>")
				.matcher(
						content);
		Pattern selectPattern = Pattern
				.compile("<option value=\"(.*?)\">\\1<\\/option>\\s?");

		// this sets up the Map
		for (ElementType type : ElementType.values()) {
			elementMap.put(type.toString().toLowerCase(), type);
		}

		while (normalMatcher.find()) {
			formElements
					.add(new FormElement(elementMap.get(normalMatcher
							.group(1)), normalMatcher.group(2), normalMatcher
							.group(3)));
		}

		while (textAreaMatcher.find()) {
			formElements.add(new FormElement(ElementType.TEXT, textAreaMatcher
					.group(1), ""));
		}

		while (selectMatcher.find()) {
			Matcher selectGroupMatcher = selectPattern.matcher(selectMatcher
					.group());
			selectGroupMatcher.find();
			while (selectGroupMatcher.find()) {
				formElements.add(new FormElement(ElementType.SELECT,
						selectMatcher.group(1), selectGroupMatcher.group(1)));
			}
		}
	}

	public URL getPostURL() {
		return this.postURL;
	}

	public Set<FormElement> getFormElementSet() {
		return this.formElements;
	}

}
