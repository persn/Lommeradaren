package no.kystverket.lommeradaren.maps;

import com.google.maps.android.clustering.ClusterManager;

public class BigMapFragment extends BaseMapFragment {

	private ClusterManager<MapMarker> gClusterManager;

	@Override
	public void adjustMap() {
		this.gClusterManager = new ClusterManager<MapMarker>(getActivity(),
				this.getGoogleMap());
		this.getGoogleMap().setOnCameraChangeListener(this.gClusterManager);
	}

	@Override
	public void getMarkerData() {
		this.getDataSourceHandler().refreshData("63.4395831", "10.4007685",
				"0.0", "50000");
	}

	@Override
	public void addMarker(double lat, double lng) {
		this.gClusterManager.addItem(new MapMarker(lat, lng));
	}
}
