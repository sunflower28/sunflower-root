package com.sunflower.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author sunflower
 */
public class HttpTools {

	private static final Logger logger = LoggerFactory.getLogger(HttpTools.class);

	public HttpTools() {
	}

	public static String send(String url, String jsonStr) {
		logger.debug(jsonStr);
		String result = "";

		try {
			URL realUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("Content-Type",
					"application/json; charset=UTF-8");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			byte[] bypes = jsonStr.getBytes("UTF-8");
			connection.getOutputStream().write(bypes);
			InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
					"UTF-8");
			BufferedReader buffRead = new BufferedReader(isr);

			for (String line = ""; (line = buffRead.readLine()) != null; result = result
					+ line) {
				;
			}

			buffRead.close();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return result;
	}

}
