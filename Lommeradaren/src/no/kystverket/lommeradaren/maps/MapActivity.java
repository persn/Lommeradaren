package no.kystverket.lommeradaren.maps;

import no.kystverket.lommeradaren.MainActivity;
import no.kystverket.lommeradaren.R;
import no.kystverket.lommeradaren.camera.CameraActivity;
import no.kystverket.lommeradaren.photo.gallery.GalleryActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

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
			startActivity(new Intent(this.getApplicationContext(),
					MainActivity.class));
			finish();
			return true;
		}
		return super.onKeyDown(keycode, e);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_map_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sub_menu_map_normal:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_NORMAL);
			return true;
		case R.id.sub_menu_map_terrain:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_TERRAIN);
			return true;
		case R.id.sub_menu_map_hybrid:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_HYBRID);
			return true;
		case R.id.sub_menu_map_satellite:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_SATELLITE);
			return true;
		case R.id.sub_menu_map_camera:
			startActivity(new Intent(this.getApplicationContext(),
					CameraActivity.class));
			finish();
			return true;
		case R.id.sub_menu_map_gallery:
			startActivity(new Intent(this.getApplicationContext(),
					GalleryActivity.class));
			finish();
			return true;
		case R.id.sub_menu_map_user:
			return false;// Not yet implemented
		}
		return super.onOptionsItemSelected(item);
	}

}
