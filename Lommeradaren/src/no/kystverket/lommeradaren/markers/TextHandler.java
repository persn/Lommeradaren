package no.kystverket.lommeradaren.markers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

/**
 * Class for creating an url with the correct parameters and then using that url
 * to query for a JSON string.
 * 
 * @author Henrik Reitan
 * 
 */
public class TextHandler {

	/**
	 * Creates an url with the correct parameters for querying the DataSource.
	 * 
	 * @param startUrl
	 * @param lat
	 * @param lng
	 * @param alt
	 * @param radius
	 * @return
	 */
	public String makeUrl(String startUrl, String lat, String lng, String alt,
			String radius) {
		return startUrl + "?latitude=" + lat + "&longitude=" + lng
				+ "&altitude=" + alt + "&radius=" + radius;
	}

	/**
	 * Queries the DataSource using the url created by the makeUrl method.
	 * 
	 * @param url
	 *            url to use with the query
	 * @return JSON string containing information from the DataSource
	 */
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
