package no.kystverket.lommeradaren.maps;

import no.kystverket.lommeradaren.MainActivity;
import no.kystverket.lommeradaren.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

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
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
		switch (keycode) {
		case KeyEvent.KEYCODE_BACK:
			startActivity(new Intent(this.getApplicationContext(), MainActivity.class));
			finish();
			return true;
		}
		return super.onKeyDown(keycode, e);
	}

}
