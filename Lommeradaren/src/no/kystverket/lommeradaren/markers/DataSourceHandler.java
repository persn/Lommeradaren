package no.kystverket.lommeradaren.markers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 
 * @author Per Olav Flaten
 * 
 */
public class DataSourceHandler {

	private String rawData;

	private DataSource dataSource;
	private List<POI> pointOfInterests;
	private TextHandler textHandler;

	private String lat;
	private String lng;
	private String alt;
	private String radius;

	public DataSourceHandler(DataSource newDataSource, String newLat,
			String newLng, String newAlt, String newRadius) {
		this.textHandler = new TextHandler();
		this.dataSource = newDataSource;

		this.refreshData(newLat, newLng, newAlt, newRadius);
	}

	public String getDataSourceName() {
		return dataSource.getName();
	}

	public POI getPOI(int index) {
		return pointOfInterests.get(index);
	}

	public int getPointOfInterestsSize() {
		if (this.pointOfInterests != null) {
			return this.pointOfInterests.size();
		}
		return 0;
	}

	/**
	 * Refreshes POIs from the given datasource stored in the object of this
	 * class.
	 * 
	 * @param newLat
	 *            Latitude.
	 * @param newLng
	 *            Longitude.
	 * @param newAlt
	 *            Altitude.
	 * @param newRadius
	 *            Given radius to search for ships from current location.
	 */
	public void refreshData(String newLat, String newLng, String newAlt,
			String newRadius) {
		this.lat = newLat;
		this.lng = newLng;
		this.alt = newAlt;
		this.radius = newRadius;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				rawData = textHandler.getRawDataFromUrl(textHandler.makeUrl(
						dataSource.getUrl(), lat, lng, alt, radius));
				extractAllPOI(rawData);
			}
		});
		thread.start();
	}

	/**
	 * Extracts all JSON-objects from a JSON array stored in a URL.
	 * @param rawData
	 */
	private void extractAllPOI(String rawData) {
		this.pointOfInterests = new ArrayList<POI>();
		try {
			JSONObject json = new JSONObject(rawData);
			JSONArray jsonArray = json.getJSONArray("results");
			for (int i = 0; i < jsonArray.length(); i++) {
				this.pointOfInterests.add(extractPOIFromDataSource(jsonArray
						.getJSONObject(i)));
			}
		} catch (JSONException jsonE) {
			jsonE.printStackTrace();
		}
	}

	/**
	 * Extracts all information from a JSON-object.
	 * @param currentJSON
	 * @return
	 */
	private POI extractPOIFromDataSource(JSONObject currentJSON) {
		POI pointOfInterest = null;
		try {
			int id = currentJSON.getInt("id");
			double lat = currentJSON.getDouble("lat");
			double lng = currentJSON.getDouble("lng");
			double alt = currentJSON.getDouble("elevation");
			String title = currentJSON.getString("title");
			double distance = currentJSON.getDouble("distance");
			String hasPage = currentJSON.getString("has_detail_page");
			String webpage = currentJSON.getString("webpage");
			// int mmsi = currentJSON.getInt("mmsi");
			// double speed = currentJSON.getDouble("speed");
			// double course = currentJSON.getDouble("course");
			String positionTime = currentJSON.getString("positionTime");
			pointOfInterest = new POI(id, title, lat, lng, alt, distance,
					hasPage, webpage, positionTime);
		} catch (JSONException jsonE) {
			jsonE.printStackTrace();
		}
		return pointOfInterest;
	}

}
