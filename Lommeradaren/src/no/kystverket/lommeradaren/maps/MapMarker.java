package no.kystverket.lommeradaren.maps;

import no.kystverket.lommeradaren.markers.POI;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Wrapper class for map markers in clusters on Google Map fullscreen mode.
 * 
 * @author Per Olav Flaten
 * 
 */
public class MapMarker implements ClusterItem {

	private final POI poi;

	public MapMarker(POI poi) {
		this.poi = poi;
	}

	@Override
	public LatLng getPosition() {
		return new LatLng(poi.getLat(), poi.getLng());
	}

	public POI getPOI() {
		return this.poi;
	}

}
