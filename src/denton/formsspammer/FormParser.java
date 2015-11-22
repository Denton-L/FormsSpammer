package denton.formsspammer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FormParser {

	private String content;

	public FormParser(URL url) {
		this.content = getContent(url);
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
			return null;
		}
		
		return sb.toString();
	}

}
