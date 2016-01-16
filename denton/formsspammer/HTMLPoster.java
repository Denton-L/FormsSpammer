package denton.formsspammer;

//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;

public class HTMLPoster {

	public static final String ENCODING = "UTF-8";

	private final URL url;

	public HTMLPoster(URL url) {
		this.url = url;
	}

	private static String URLEncode(String s)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(s, ENCODING);
	}

	public void doPost(Set<NameValuePair> params) {
		StringBuilder postData = new StringBuilder();
		byte[] postBytes;
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) url.openConnection();

			for (NameValuePair param : params) {
				if (postData.length() > 0) {
					postData.append('&');
				}
				postData.append(URLEncode(param.name));
				postData.append('=');
				postData.append(URLEncode(param.value.toString()));
			}
			postBytes = postData.toString().getBytes(ENCODING);

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length",
					String.valueOf(postBytes.length));
			connection.setDoOutput(true);
			connection.getOutputStream().write(postBytes);

			connection.getInputStream().close();

			// Reader r = new BufferedReader(new
			// InputStreamReader(connection.getInputStream()));
			// int c;
			// while ((c = r.read()) != -1) {
			// System.err.print((char) c);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}