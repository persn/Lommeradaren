package no.kystverket.lommeradaren.maps;

import no.kystverket.lommeradaren.R;
import no.kystverket.lommeradaren.camera.augmented.opengl.MarkerSurfaceView;
import no.kystverket.lommeradaren.markers.DataSource;
import no.kystverket.lommeradaren.markers.DataSourceHandler;
import no.kystverket.lommeradaren.markers.LocationHandler;
import no.kystverket.lommeradaren.markers.POI;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

/**
 * A custom android Fragment component, to implement GoogleMaps with our own
 * GUI-components.
 * 
 * @author Per Olav Flaten
 * 
 */
public class CustomGoogleMapFragment extends Fragment {

	private MapView gMapView;
	private GoogleMap gMap;
	private ClusterManager<MapMarker> gClusterManager;
	private Bundle bundle;
	private LocationHandler currentLocation;
	private DataSourceHandler datasourceHandler;
	private float zoom;
	private boolean bigmap = true;

	private Handler handler;
	private Runnable updateMarkersThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.bundle = savedInstanceState;

		this.currentLocation = new LocationHandler(getActivity()
				.getApplicationContext());
		
		String datasource[] = getString(R.string.datasource_url).split("\\|");
		this.datasourceHandler = new DataSourceHandler(new DataSource(datasource[0],datasource[1]),
				this.currentLocation.getLatitude(),
				this.currentLocation.getLongtitude(),
				this.currentLocation.getAltitude());
		
		this.handler = new Handler();
		this.updateMarkersThread = new MarkerRefresh();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflatedView = inflater.inflate(R.layout.googlemap_fragment,
				container, false);

		MapsInitializer.initialize(getActivity());
		this.gMapView = (MapView) inflatedView.findViewById(R.id.map);
		this.gMapView.onCreate(bundle);
		setUpMapIfNeeded(inflatedView);
		
		this.gClusterManager = new ClusterManager<MapMarker>(getActivity(),this.gMap);
		this.gMap.setOnCameraChangeListener(this.gClusterManager);
		//this.gMap.setOnMarkerClickListener(this.gClusterManager);

		return inflatedView;
	}

	/**
	 * onResume() is the last method called before an Activity starts running,
	 * for this reason starting the thread that handles map markers should be
	 * put here, to reduce the risk of referencing objects not yet instantiated.
	 */
	@Override
	public void onResume() {
		super.onResume();
		this.gMapView.onResume();
		startMarkerRefresh();
		this.zoom = calculateRelativeZoomDistance();
	}

	/**
	 * onPause() is the first method called whenever the Activity stops for any
	 * reason, for this reason stopping the thread that refreshes the markers
	 * should be put in here.
	 */
	@Override
	public void onPause() {
		this.stopMarkerRefresh();
		this.gMapView.onPause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		this.gMapView.onDestroy();
		super.onDestroy();
	}

	/**
	 * 
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		this.gMapView.onLowMemory();
	}

	/**
	 * Refresh of map so that it gets redrawn, but rotated to pointing
	 * direction. For use with mini-map.
	 * 
	 * @param bearing
	 *            The direction the unit is pointing. From 0-360 degrees.
	 */
	public void updateBearing(float bearing) {
		if (this.gMap.getMyLocation() != null) {
			LatLng location;
			location = new LatLng(this.gMap.getMyLocation().getLatitude(),
					this.gMap.getMyLocation().getLongitude());
			CameraPosition currentPlace = new CameraPosition.Builder()
					.target(location).bearing(bearing).zoom(this.zoom).build();
			gMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));
		}
	}

	/**
	 * Toggles GUI-settings specific for mini-map.
	 */
	public void toggleMiniMapSettings() {
		this.gMap.getUiSettings().setMyLocationButtonEnabled(false);
		this.gMap.getUiSettings().setZoomControlsEnabled(false);
		this.gMap.getUiSettings().setAllGesturesEnabled(false);
		this.bigmap = false;
	}

	/**
	 * Marker information is needed multiple places in the application, we
	 * assume they get instantiated in this class, and let it act as a master
	 * class for marker insances.
	 * 
	 * @return
	 */
//	public DataSourceCollection getDataSourceCollection() {
//		return this.dataSourceCollection;
//	}

	/**
	 * http://stackoverflow.com/questions/6002563/android-how-do-i-set-the-zoom-
	 * level-of-map-view-to-1-km-radius-around-my-curren
	 * 
	 * @return
	 */
	private float calculateRelativeZoomDistance() {
		final float EQUATOR_LENGTH_METER = 40075016.6856f;
		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		float widthInPixels = size.x;
		float metersPerPixel = EQUATOR_LENGTH_METER / 256;
		float zoomLevel = 1;
		//TODO --- 50000 refers to 50km, the constant number should be replaced when user adjusted radius is implemented.
		while ((metersPerPixel * widthInPixels) > 50000) {
			metersPerPixel /= 2;
			zoomLevel++;
		}
		return zoomLevel;
	}

	/**
	 * Help method that makes checks to ensure that the system won't attempt to
	 * draw the map when it is not initialized
	 * 
	 * @param inflatedView
	 *            View component that will be drawn on the screen, it is
	 *            utilized automatically by the android compiler. In this method
	 *            it is used to fetch the GoogleMaps View component in
	 *            googlemap_fragment.xml
	 */
	private void setUpMapIfNeeded(View inflatedView) {
		if (gMap == null) {
			gMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
			if (gMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * Help method that sets up the map with specified settings.
	 */
	private void setUpMap() {
		this.gMap.setMyLocationEnabled(true);
	}

	/**
	 * Starts the tread that handles marker refreshing.
	 */
	private void startMarkerRefresh() {
		getActivity().runOnUiThread(this.updateMarkersThread);
	}

	/**
	 * Stops the thread that handles marker refreshing.
	 */
	private void stopMarkerRefresh() {
		this.handler.removeCallbacks(this.updateMarkersThread);
	}

	private class MarkerRefresh implements Runnable {
		
		@Override
		public void run() {
			//gMap.clear();

			// Make sure the marker data associated with the Augmented
			// Reality Engine is up to date as well.
			MarkerSurfaceView markerView = (MarkerSurfaceView) getActivity()
					.findViewById(R.id.marker_surface_view);
			if (markerView != null) {
				markerView.setDataSourceHandler(datasourceHandler);
				markerView.setCurrentLocation(currentLocation);
			}

				if(bigmap){
					datasourceHandler.refreshData(
							"63.4395831", "10.4007685", "0.0", "50000");
				}else{
					datasourceHandler.refreshData(
							currentLocation.getLatitude(),
							currentLocation.getLongtitude(),
							currentLocation.getAltitude(), "50");
				}	
				
//				if(dataSourceCollection.getPOIArrayLength(i) > 0);				
				
				for (int i = 0; i < datasourceHandler.getPointOfInterestsSize(); i++) {
					POI poi = datasourceHandler.getPOI(i);
					gClusterManager.addItem(new MapMarker(poi.getLat(),poi.getLng()));
//					gMap.addMarker(new MarkerOptions().position(
//							new LatLng(poi.getLat(), poi.getLng())).title(
//							poi.getName()));
			}
			handler.postDelayed(this, (1000 * 10));
		}
	}

}
