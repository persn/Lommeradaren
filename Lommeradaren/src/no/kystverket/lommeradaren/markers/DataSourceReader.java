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
public class DataSourceReader {

	private String rawData;

	private DataSource dataSource;
	private List<POI> pointOfInterests;

	public DataSourceReader(DataSource dataSource, String lat, String lng,
			String alt, String radius) {
		TextHandler textHandler = new TextHandler();
		this.rawData = textHandler.getRawDataFromUrl(textHandler.makeUrl(
				dataSource.getUrl(), lat, lng, alt, radius));

		this.dataSource = dataSource;
		refreshAllPOI(rawData);
	}

	public String getDataSourceName() {
		return dataSource.getName();
	}

	public POI getPOI(int index) {
		return pointOfInterests.get(index);
	}

	public void refreshAllPOI(String rawData) {
		this.pointOfInterests = new ArrayList<POI>();
		try {
			JSONObject json = new JSONObject(rawData);
			JSONArray jsonArray = json.getJSONArray("results");
			for (int i = 0; i < jsonArray.length(); i++) {
				this.pointOfInterests.add(extractPOIFromDataSource(jsonArray
						.getJSONObject(i)));
			}
		} catch (JSONException jsonE) {
			Log.d("no.kystverket",
					"DataSourcereader.java extractPOIFromDataSource");
		}
	}

	private POI extractPOIFromDataSource(JSONObject currentJSON) {
		POI pointOfInterest = null;
		try {
			int id = currentJSON.getInt("id");
			double lat = currentJSON.getDouble("lat");
			double lng = currentJSON.getDouble("lat");
			double alt = currentJSON.getDouble("elevation");
			String title = currentJSON.getString("title");
			double distance = currentJSON.getDouble("distance");
			boolean hasPage = currentJSON.getBoolean("has_detail_page");
			String webpage = currentJSON.getString("webpage");
			String mmsi = currentJSON.getString("mmsi");
			String imo = currentJSON.getString("imo");
			// double speed = currentJSON.getDouble("speed");
			// double course = currentJSON.getDouble("course");
			String positionTime = currentJSON.getString("positionTime");
			pointOfInterest = new POI(id, title, lat, lng, alt, mmsi, distance,
					hasPage, webpage, positionTime, imo);
		} catch (JSONException jsonE) {
			Log.d("no.kystverket",
					"DataSourcereader.java extractPOIFromDataSource");
		}
		return pointOfInterest;
	}

}
