package no.kystverket.lommeradaren.maps;

import no.kystverket.lommeradaren.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

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
	
	private ImageButton renderActionBarBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		
		this.renderActionBarBtn = (ImageButton) findViewById(R.id.btn_mapscrn_render_bar);
		this.renderActionBarBtn.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
		switch (keycode) {
		// case KeyEvent.KEYCODE_BACK:
		// startActivity(new Intent(this.getApplicationContext(),
		// MainActivity.class));
		// finish();
		// return true;
		}
		return super.onKeyDown(keycode, e);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_map_screen, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.sub_item_normal_map:
			((MapView) findViewById(R.id.map)).getMap()
			.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			return true;
		case R.id.sub_item_terrain_map:
			((MapView) findViewById(R.id.map)).getMap()
			.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			return true;
		case R.id.sub_item_hybrid_map:
			((MapView) findViewById(R.id.map)).getMap()
			.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			return true;
		case R.id.sub_item_satellite_map:
			((MapView) findViewById(R.id.map)).getMap()
			.setMapType(
					GoogleMap.MAP_TYPE_SATELLITE);
			return true;
		case R.id.menu_hide_bar:
			getActionBar().hide();
			this.renderActionBarBtn.setVisibility(View.VISIBLE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	public void renderActionBarOnClick(View view){
		getActionBar().show();
		this.renderActionBarBtn.setVisibility(View.GONE);
	}

}
