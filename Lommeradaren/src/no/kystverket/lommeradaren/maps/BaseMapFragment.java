package no.kystverket.lommeradaren.maps;

import no.kystverket.lommeradaren.R;
import no.kystverket.lommeradaren.markers.DataSource;
import no.kystverket.lommeradaren.markers.DataSourceHandler;
import no.kystverket.lommeradaren.markers.POI;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

/**
 * Abstract class with base functionality for the Google Map implementation in
 * the app.
 * 
 * @author Per Olav Flaten
 * 
 */
public abstract class BaseMapFragment extends Fragment {

	// View components
	private MapView gMapView;
	private GoogleMap gMap;

	// Controller components
	private DataSourceHandler datasourceHandler;

	// Checks
	private boolean firstMarkerLoad = true;

	// Android OS classes
	private Handler handler;
	private Runnable updateMarkersThread;

	// private TextView markersUpdated;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String datasource[] = getString(R.string.datasource_url).split("\\|");
		this.datasourceHandler = new DataSourceHandler(new DataSource(
				datasource[0], datasource[1]));

		this.handler = new Handler();
		this.updateMarkersThread = new MarkerRefresh();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflatedView = inflater.inflate(R.layout.googlemap_fragment,
				container, false);
		// this.markersUpdated =
		// (TextView)getActivity().findViewById(R.id.marker_update);

		MapsInitializer.initialize(getActivity());
		this.gMapView = (MapView) inflatedView.findViewById(R.id.map);
		this.gMapView.onCreate(savedInstanceState);
		setUpMapIfNeeded(inflatedView);

		this.setMapSettings();

		return inflatedView;
	}

	@Override
	public void onResume() {
		super.onResume();
		this.gMapView.onResume();
		getActivity().runOnUiThread(this.updateMarkersThread);
	}

	@Override
	public void onPause() {
		this.handler.removeCallbacks(this.updateMarkersThread);
		this.gMapView.onPause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		this.gMapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		this.gMapView.onLowMemory();
	}

	/**
	 * Setup the UI components of the map that should be active. For example,
	 * onn a small mini-map we'll want to disable several components to avoid
	 * cluttering.
	 */
	public abstract void setMapSettings();

	public abstract void clearMapMarkers();

	/**
	 * Abstract method to differentiate criteria for marker search.
	 */
	public abstract void getMarkerData();

	public abstract void addMapMarker(POI poi);

	public abstract int getRefreshTimer();

	public boolean isFirstMarkerLoad() {
		return this.firstMarkerLoad;
	}

	public GoogleMap getGoogleMap() {
		return this.gMap;
	}

	public DataSourceHandler getDataSourceHandler() {
		return this.datasourceHandler;
	}

	private void setUpMapIfNeeded(View inflatedView) {
		if (gMap == null) {
			gMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
			if (gMap != null) {
				this.gMap.setMyLocationEnabled(true);
			}
		}
	}

	/**
	 * A inner nested Thread class that retrieves marker data relative to user
	 * location.
	 * 
	 * @author Per Olav Flaten
	 * 
	 */
	private class MarkerRefresh implements Runnable {

		@Override
		public void run() {
			getMarkerData();
			if (datasourceHandler.isReadyToRead()) {
				clearMapMarkers();
				for (int i = 0; i < datasourceHandler.getPointOfInterestsSize(); i++) {
					addMapMarker(datasourceHandler.getPOI(i));
				}
				// TODO --- Replace toast with a Label in GUI
				Toast.makeText(getActivity(), "such tracking\nmuch ships wow",
						Toast.LENGTH_SHORT).show();
				// markersUpdated.setText("Last updated: " );
				firstMarkerLoad = false;
			}
			handler.postDelayed(this, getRefreshTimer());
		}
	}

}
