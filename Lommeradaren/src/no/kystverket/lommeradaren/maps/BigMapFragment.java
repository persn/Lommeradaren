package no.kystverket.lommeradaren.maps;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

public class BigMapFragment extends BaseMapFragment {

	private ClusterManager<MapMarker> gClusterManager;

	@Override
	public void setMapSettings() {
		this.gClusterManager = new ClusterManager<MapMarker>(getActivity(),
				this.getGoogleMap());
		this.getGoogleMap().setOnCameraChangeListener(this.gClusterManager);
		this.getGoogleMap().setOnMarkerClickListener(this.gClusterManager);

		this.getGoogleMap().moveCamera(
				CameraUpdateFactory.newLatLngZoom(new LatLng(63.4395831,
						10.4007685), 3));
	}

	@Override
	public void getMarkerData() {
		this.getDataSourceHandler().refreshData("63.4395831", "10.4007685",
				"0.0", "5000");
	}

	@Override
	public void addMapMarker(double lat, double lng) {
		this.gClusterManager.addItem(new MapMarker(lat, lng));
	}

	@Override
	public void clearMapMarkers() {
		this.gClusterManager.clearItems();
	}
	
	@Override
	public int getRefreshTimer() {
		if (this.isFirstMarkerLoad()) {
			return 1000 * 5;
		} else {
			return 1000 * 60 * 10;
		}
	}
}
