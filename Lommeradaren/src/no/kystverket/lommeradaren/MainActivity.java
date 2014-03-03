package no.kystverket.lommeradaren;

import no.kystverket.lommeradaren.camera.CameraActivity;
import no.kystverket.lommeradaren.maps.MapActivity;
import no.kystverket.lommeradaren.photo.gallery.GalleryActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * This is the activity for the title screen, it contains logic associated with
 * switching to other activities.
 * 
 * @author Per Olav Flaten
 * 
 */
public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu_activity);
	}

	public void cameraOnClick(View v) {
		startActivity(new Intent(this.getApplicationContext(), CameraActivity.class));
		finish();
	}

	public void galleryOnClick(View v) {
		startActivity(new Intent(this.getApplicationContext(),
				GalleryActivity.class));
		finish();
	}

	public void goToMapScreenOnClick(View v) {
		startActivity(new Intent(this.getApplicationContext(),
				MapActivity.class));
		finish();
	}
}
