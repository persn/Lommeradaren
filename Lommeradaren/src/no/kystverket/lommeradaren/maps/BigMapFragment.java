package no.kystverket.lommeradaren.maps;

import no.kystverket.lommeradaren.MarkerDialogFragment;
import no.kystverket.lommeradaren.markers.POI;
import android.app.DialogFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener;

/**
 * 
 * @author Per Olav Flaten
 *
 */
public class BigMapFragment extends BaseMapFragment {

	private ClusterManager<MapMarker> gClusterManager;

	@Override
	public void setMapSettings() {
		this.gClusterManager = new ClusterManager<MapMarker>(getActivity(),
				this.getGoogleMap());
		this.setOnClusterItemClicked();
		
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
	public void addMapMarker(POI poi) {
		this.gClusterManager.addItem(new MapMarker(poi));
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
	
	private void setOnClusterItemClicked(){
		this.gClusterManager.setOnClusterItemClickListener(new OnClusterItemClickListener<MapMarker>(){

			@Override
			public boolean onClusterItemClick(MapMarker item) {
				if(item != null){
					DialogFragment newFragment = new MarkerDialogFragment();
					((MarkerDialogFragment)newFragment).setContent(
							item.getPOI().getName(),
							"" + item.getPOI().getLat(), 
							"" + item.getPOI().getLng(),
							"" + item.getPOI().getAlt(),
							item.getPOI().getImo(),
							item.getPOI().getMmsi(),
							item.getPOI().getSpeed(),
							item.getPOI().getPositionTime(),
							item.getPOI().getWebpage());
					newFragment.show(getFragmentManager(), "marker_dialog");
				}
				return false;
			}
			
		});
	}
}
