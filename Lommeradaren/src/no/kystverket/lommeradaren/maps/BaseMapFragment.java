package no.kystverket.lommeradaren.maps;

import java.util.Date;

import no.kystverket.lommeradaren.R;
import no.kystverket.lommeradaren.markers.DataSource;
import no.kystverket.lommeradaren.markers.DataSourceHandler;
import no.kystverket.lommeradaren.markers.POI;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

public abstract class BaseMapFragment extends Fragment {

	private MapView gMapView;
	private GoogleMap gMap;

	private DataSourceHandler datasourceHandler;
	private boolean firstMarkerLoad = true;

	private Handler handler;
	private Runnable updateMarkersThread;
	
//	private TextView markersUpdated;

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
//		this.markersUpdated = (TextView)getActivity().findViewById(R.id.marker_update);

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

	public abstract void setMapSettings();

	public abstract void clearMapMarkers();

	public abstract void getMarkerData();

	public abstract void addMapMarker(double lat, double lng);
	
	public abstract int getRefreshTimer();
	
	public boolean isFirstMarkerLoad(){
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

	private class MarkerRefresh implements Runnable {

		@Override
		public void run() {
			getMarkerData();
			if (datasourceHandler.isReadyToRead()) {
				clearMapMarkers();
				for (int i = 0; i < datasourceHandler.getPointOfInterestsSize(); i++) {
					POI poi = datasourceHandler.getPOI(i);
					addMapMarker(poi.getLat(), poi.getLng());
				}
				// TODO --- Replace toast with a Label in GUI
				Toast.makeText(getActivity(), "such tracking\nmuch ships wow",
						Toast.LENGTH_SHORT).show();
//				markersUpdated.setText("Last updated: " );
				firstMarkerLoad = false;
			}
			handler.postDelayed(this, getRefreshTimer());
		}
	}

}
