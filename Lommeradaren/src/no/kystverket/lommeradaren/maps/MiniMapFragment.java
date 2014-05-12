package no.kystverket.lommeradaren.maps;

import no.kystverket.lommeradaren.markers.DataSourceHandler;
import no.kystverket.lommeradaren.markers.POI;
import android.app.Activity;
import android.graphics.Point;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * An extension of BaseMapFragment to deal with customization of Google Map for
 * use in camera mode.
 * 
 * @author Per Olav Flaten
 * 
 */
public class MiniMapFragment extends BaseMapFragment {

	private OnMarkerDataUpdatedListener markerDataCallback;

	@Override
	public void setMapSettings() {
		this.getGoogleMap().getUiSettings().setMyLocationButtonEnabled(false);
		this.getGoogleMap().getUiSettings().setZoomControlsEnabled(false);
		this.getGoogleMap().getUiSettings().setAllGesturesEnabled(false);

		this.getGoogleMap().setOnMapClickListener(null);
		setOnLocationChangeListener();
	}

	@Override
	public void getMarkerData() {
		Location location = this.getGoogleMap().getMyLocation();
		if (location != null) {
			this.getDataSourceHandler().refreshData(
					"" + location.getLatitude(), "" + location.getLongitude(),
					"" + location.getAltitude(), "20");
		}
	}

	@Override
	public void addMapMarker(POI poi) {
		this.getGoogleMap().addMarker(
				new MarkerOptions().position(new LatLng(poi.getLat(), poi
						.getLng())));
	}

	@Override
	public void clearMapMarkers() {
		this.markerDataCallback.onMarkerDataUpdated(getDataSourceHandler(),
				getGoogleMap().getMyLocation());
		this.getGoogleMap().clear();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			this.markerDataCallback = (OnMarkerDataUpdatedListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Rotate the map to give the user a compass feeling.
	 * 
	 * @param bearing
	 *            The direction the user is pointing in compass direction.
	 */
	public void updateBearing(float bearing) {
		if (this.getGoogleMap().getMyLocation() != null) {
			LatLng location = new LatLng(this.getGoogleMap().getMyLocation()
					.getLatitude(), this.getGoogleMap().getMyLocation()
					.getLongitude());
			CameraPosition currentPlace = new CameraPosition.Builder()
					.target(location).bearing(bearing)
					.zoom(this.getGoogleMap().getCameraPosition().zoom).build();
			this.getGoogleMap().moveCamera(
					CameraUpdateFactory.newCameraPosition(currentPlace));
		}
	}

	/**
	 * Sets the refresh timer initially to every five seconds in case of issues
	 * connecting to the data stream with the marker data. Once marker data has
	 * been retrieved the refresh timer is set to every minute, to refresh in
	 * according to that the user might move location.
	 */
	@Override
	public int getRefreshTimer() {
		if (this.isFirstMarkerLoad()) {
			return 1000 * 5;
		} else {
			return 1000 * 60;
		}
	}

	/**
	 * Calculates a relative distance to zoom in on the minimap, this method is
	 * needed because Google Maps decide zoom factor on screen size and not in
	 * meters or other traditional distance measures.
	 * 
	 * @return The level the zoom should be set at. Value between 1-15.
	 */
	private float calculateRelativeZoomDistance() {
		final float EQUATOR_LENGTH_METER = 40075016.6856f;
		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		float widthInPixels = size.x;
		float metersPerPixel = EQUATOR_LENGTH_METER / 256;
		float zoomLevel = 1;
		// TODO --- 50000 refers to 50km, the constant number should be replaced
		// when user adjusted radius is implemented.
		while ((metersPerPixel * widthInPixels) > 50000) {
			metersPerPixel /= 2;
			zoomLevel++;
		}
		return zoomLevel;
	}

	/**
	 * Overrides the onlocationchanged listener to move the minimap when the
	 * user moves.
	 */
	private void setOnLocationChangeListener() {
		this.getGoogleMap().setOnMyLocationChangeListener(
				new OnMyLocationChangeListener() {

					@Override
					public void onMyLocationChange(Location newLocation) {
						LatLng location = new LatLng(newLocation.getLatitude(),
								newLocation.getLongitude());
						CameraPosition currentPlace = new CameraPosition.Builder()
								.target(location)
								.zoom(calculateRelativeZoomDistance()).build();
						getGoogleMap().moveCamera(
								CameraUpdateFactory
										.newCameraPosition(currentPlace));
					}

				});
	}

	public interface OnMarkerDataUpdatedListener {
		public void onMarkerDataUpdated(DataSourceHandler datasourceHandler,
				Location myLocation);
	}

}
