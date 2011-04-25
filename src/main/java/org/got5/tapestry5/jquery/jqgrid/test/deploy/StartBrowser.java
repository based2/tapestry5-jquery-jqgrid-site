package org.got5.tapestry5.jquery.jqgrid.test.deploy;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// http://www.mkyong.com/java/open-browser-in-java-windows-or-linux/
public class StartBrowser {
	Logger log = LoggerFactory.getLogger(StartBrowser.class);

	public StartBrowser(String url, int sleep, String preCommand) {
		if (url == null) {
			log.error("Empty URL, using the default Tapestry URL for test.");
			url = "http://tapestry.apache.org/";
		}
		this.launchAt(url, sleep, preCommand);
	}

	public void launchAt(String url, int sleep, String preCommand) {
		String os = System.getProperty("os.name").toLowerCase();
		Runtime rt = Runtime.getRuntime();
		if (preCommand != null) {
			try {
				rt.exec(preCommand);
			} catch (IOException e1) {
				log.error("", e1);
			}
		}
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e1) {
			log.error("", e1);
		}
		try {

			if (os.indexOf("win") >= 0) {

				// this doesn't support showing URLs in the form of
				// "page.html#nameLink"
				rt.exec("rundll32 url.dll,FileProtocolHandler " + url);

			} else if (os.indexOf("mac") >= 0) {

				rt.exec("open " + url);

			} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {

				// Do a best guess on unix until we get a platform independent
				// way
				// Build a list of browsers to try, in this order.
				String[] browsers = { "firefox", "chrome", "opera", "mozilla",
						"konqueror", "netscape", "epiphany", "links", "lynx",
						"ff", "ch" };

				// Build a command string which looks like
				// "browser1 "url" || browser2 "url" ||..."
				StringBuffer cmd = new StringBuffer();
				for (int i = 0; i < browsers.length; i++)
					cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \""
							+ url + "\" ");

				rt.exec(new String[] { "sh", "-c", cmd.toString() });

			} else {
				return;
			}
		} catch (Exception e) {
			log.error("", e);
			return;
		}
		return;
	}

	public static void main(String args[]) {
		String url = "http://tapestry.apache.org/";
		if (args != null && args.length > 0) {
			url = args[0];
		}
		int sleep = 0;
		if (args != null && args.length > 1) {
			sleep = new Integer(args[1]);
		}
		String preCommand = null;
		if (args != null && args.length > 2) {
			preCommand = args[2];
		}
		StartBrowser sb = new StartBrowser(url, sleep, preCommand);
	}

}