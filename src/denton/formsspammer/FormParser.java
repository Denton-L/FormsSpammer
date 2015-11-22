package denton.formsspammer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormParser {

	private URL postURL;

	public FormParser(URL url) {
		String content = getContent(url);
		parsePostURL(content);
	}

	private String getContent(URL url) {
		String s;
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				url.openStream()));) {
			while ((s = br.readLine()) != null) {
				sb.append(s);
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
						"<form action=\"(.*)\" method=\"POST\" id=\"ss-form\" target=\"_self\" onsubmit=\"\">")
				.matcher(content);
		matcher.find();
		try {
			postURL = new URL(matcher.group(1));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			postURL = null;
		}
	}

	public URL getPostURL() {
		return this.postURL;
	}

}
