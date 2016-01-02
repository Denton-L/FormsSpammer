package denton.formsspammer;

import java.net.MalformedURLException;
import java.net.URL;

public class Main implements Runnable {
	
	public static final int THREADS = 1;
	
	private HTMLPoster htmlPoster;
	private ElementProcessor elementProcessor;
	
	private Main(HTMLPoster htmlPoster, ElementProcessor elementProcessor) {
		this.htmlPoster = htmlPoster;
		this.elementProcessor = elementProcessor;
	}
	
	public void sendOnce() {
		htmlPoster.doPost(elementProcessor.getParams());
	}
	
	public void run() {
		while (true) {
			sendOnce();
		}
	}
	
	public static void formSpam(String target) throws MalformedURLException {
		URL url;
		FormParser formParser;
		HTMLPoster htmlPoster;
		ElementProcessor elementProcessor;
		
		url = new URL(target);
		formParser = new FormParser(url);
		htmlPoster = new HTMLPoster(formParser.getPostURL());
		elementProcessor = new ElementProcessor(formParser.getFormElementSet());
		
		new Main(htmlPoster, elementProcessor).sendOnce();
		
		for (int i = 1; i < THREADS; i++) {
			new Thread(new Main(htmlPoster, elementProcessor)).start();
		}
	}
	
	public static void main(String[] args) {
		try {
			for (String arg : args) {
				formSpam(arg);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
