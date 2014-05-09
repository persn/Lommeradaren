package no.kystverket.lommeradaren.markers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class handling queries to DataSources to receive information about POI based
 * on our location
 * 
 * @author Per Olav Flaten
 * 
 */
public class DataSourceHandler {

	private String rawData;

	private DataSource dataSource;
	private List<POI> pointOfInterests;
	private TextHandler textHandler;
	private boolean readyToRead;

	public DataSourceHandler(DataSource newDataSource) {
		this.textHandler = new TextHandler();
		this.dataSource = newDataSource;

		this.readyToRead = false; // Default value false, since data must have
									// been loaded and initated before ready to
									// read.
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
	 * Refreshes POIs from the given DataSource stored in the object of this
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
	public void refreshData(final String lat, final String lng,
			final String alt, final String radius) {

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

	public boolean isReadyToRead() {
		return this.readyToRead;
	}

	/**
	 * Extracts all JSON-objects from a JSON array stored in a URL.
	 * 
	 * @param rawData
	 */
	private void extractAllPOI(String rawData) {
		try {
			JSONObject json = new JSONObject(rawData);
			if (json.getString("status").equals("OK")) {
				this.readyToRead = true;
				this.pointOfInterests = new ArrayList<POI>();
				JSONArray jsonArray = json.getJSONArray("results");
				for (int i = 0; i < jsonArray.length(); i++) {
					this.pointOfInterests
							.add(extractPOIFromDataSource(jsonArray
									.getJSONObject(i)));
				}
			} else {
				this.readyToRead = false;
			}
		} catch (JSONException jsonE) {
			jsonE.printStackTrace();
		}
	}

	/**
	 * Extracts all information from a JSON-object.
	 * 
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

			String imo = currentJSON.optString("imo");
			String mmsi = currentJSON.optString("mmsi");
			String speed = currentJSON.optString("speed");
			String course = currentJSON.optString("course");

			String positionTime = currentJSON.getString("positionTime");
			pointOfInterest = new POI(id, title, lat, lng, alt, mmsi, distance,
					hasPage, webpage, positionTime, speed, course, imo);
		} catch (JSONException jsonE) {
			jsonE.printStackTrace();
		}
		return pointOfInterest;
	}

}
