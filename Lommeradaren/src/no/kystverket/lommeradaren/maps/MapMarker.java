package no.kystverket.lommeradaren.maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapMarker implements ClusterItem {
	
	private final LatLng mPosition;

	public MapMarker(double lat, double lng){
		this.mPosition = new LatLng(lat,lng);
	}
	
	@Override
	public LatLng getPosition() {
		return mPosition;
	}

}
