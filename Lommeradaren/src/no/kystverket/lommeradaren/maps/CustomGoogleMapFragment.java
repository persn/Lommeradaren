package no.kystverket.lommeradaren.maps;

import no.kystverket.lommeradaren.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.MapView;

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
	private Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.bundle = savedInstanceState;
	}

	/**
	 * Initializing of the custom GoogleMap Fragment, with the view components
	 * btn_choosemaptype and map
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflatedView = inflater.inflate(R.layout.googlemap_fragment,
				container, false);
		this.chooseMapTypeOnClick(inflatedView);

		MapsInitializer.initialize(getActivity());
		this.gMapView = (MapView) inflatedView.findViewById(R.id.map);
		this.gMapView.onCreate(bundle);
		setUpMapIfNeeded(inflatedView);

		return inflatedView;
	}

	@Override
	public void onResume() {
		super.onResume();
		this.gMapView.onResume();
	}

	@Override
	public void onPause() {
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
	 * Fetches the btn_choosemaptype Button component, and initialized the
	 * onClickListener set to it. Note that this is not built exactly to the
	 * method set in the xml-form googlemap_fragment, because the button exists
	 * inside of a fragment, but xml-listeners require Activities. The xml form
	 * will still contain the method name so for practical reasons, so people
	 * know which method to look up.
	 * 
	 * @param v
	 */
	private void chooseMapTypeOnClick(View v) {
		final Button chooseMapTypeButton = ((Button) v
				.findViewById(R.id.btn_choosemaptype));
		chooseMapTypeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setItems(R.array.map_options,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0: // Set normal map mode
									gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
									break;
								case 1: // Set terrain map mode
									gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
									break;
								case 2: // Set hybrid map mode
									gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
									break;
								case 3: // Set satellite map mode
									gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
									break;
								default:
									Log.d("no.kystverket",
											"Entered unaccessible case in switch CustomGoogleMapFragment.java chooseMapTypeOnClick");
									break;
								}
							}
						});
				builder.create().show();
			}
		});
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
		this.gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				66.47821, 14.67773), 3));
		this.gMap.getUiSettings().setZoomControlsEnabled(false);
		// gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(
		// "Marker"));
	}

}
