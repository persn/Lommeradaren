package no.kystverket.lommeradaren.maps;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MiniMapFragment extends BaseMapFragment {

	@Override
	public void adjustMap() {
		this.getGoogleMap().getUiSettings().setMyLocationButtonEnabled(false);
		this.getGoogleMap().getUiSettings().setZoomControlsEnabled(false);
		this.getGoogleMap().getUiSettings().setAllGesturesEnabled(false);
	}

	@Override
	public void getMarkerData() {
		Location location = this.getGoogleMap().getMyLocation();
		if(location != null){
			this.getDataSourceHandler().refreshData(
					"" + location.getLatitude(),
					"" + location.getLongitude(),
					"" + location.getAltitude(), "50");
		}
	}

	@Override
	public void addMarker(double lat, double lng) {
		this.getGoogleMap().addMarker(
				new MarkerOptions().position(new LatLng(lat, lng)));
	}

}
