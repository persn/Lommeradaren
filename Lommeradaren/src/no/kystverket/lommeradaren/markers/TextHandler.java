package no.kystverket.lommeradaren.markers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

/**
 * 
 * @author Henrik Reitan
 *
 */
public class TextHandler {

	public String makeUrl(String startUrl, String lat, String lng, String alt,
			String radius) {
		return startUrl + "?latitude=" + lat + "&longitude=" + lng
				+ "&altitude=" + alt + "&radius=" + radius;
	}

	public String getRawDataFromUrl(String url) {
		String rawString = "";
		try {
			InputStream input = new URL(url).openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String line;
			while ((line = br.readLine()) != null) {
				rawString += line;
			}
			br.close();
		} catch (MalformedURLException e) {
			Log.d("GetRawDataFromUrl", "MalformedURLException");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("GetRawDataFromUrl", "IOException");
			e.printStackTrace();
		}
		return rawString;
	}
}
