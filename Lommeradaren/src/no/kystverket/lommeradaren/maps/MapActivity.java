package no.kystverket.lommeradaren.maps;

import no.kystverket.lommeradaren.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * This class initializes the Map screen, the logic is divided to the class
 * CustomGoogleMapFragment, for convenience so that the code may be reused in
 * other Activites.
 * 
 * @author Per Olav Flaten
 * 
 */
public class MapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
	}

}
