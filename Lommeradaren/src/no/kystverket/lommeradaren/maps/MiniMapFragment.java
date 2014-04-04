package no.kystverket.lommeradaren.maps;

import android.graphics.Point;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MiniMapFragment extends BaseMapFragment {

	@Override
	public void setMapSettings() {
		// Update UI
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
					"" + location.getAltitude(), "50");
		}
	}

	@Override
	public void addMapMarker(double lat, double lng) {
		this.getGoogleMap().addMarker(
				new MarkerOptions().position(new LatLng(lat, lng)));
	}

	@Override
	public void clearMapMarkers() {
		this.getGoogleMap().clear();
	}

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
	
	@Override
	public int getRefreshTimer(){
		if (this.isFirstMarkerLoad()) {
			return 1000 * 5;
		} else {
			return 1000 * 60 * 10;
		}
	}

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

}
