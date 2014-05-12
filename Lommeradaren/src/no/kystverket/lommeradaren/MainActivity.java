package no.kystverket.lommeradaren;

import no.kystverket.lommeradaren.camera.CameraActivity;
import no.kystverket.lommeradaren.maps.MapActivity;
import no.kystverket.lommeradaren.photo.PhotoHandler;
import no.kystverket.lommeradaren.photo.gallery.GalleryActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * This is the activity for the title screen, it contains logic associated with
 * switching to other activities.
 * 
 * @author Per Olav Flaten
 * 
 */
public class MainActivity extends Activity {

	private Point screenSize;
	private ImageView backgroundImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		screenSize = new Point();
		getWindowManager().getDefaultDisplay().getSize(screenSize);
		setContentView(R.layout.mainmenu_activity);
		backgroundImage = (ImageView) findViewById(R.id.mainmenu_background);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (screenSize.x < screenSize.y) {
			backgroundImage.setImageDrawable(new BitmapDrawable(getResources(),
					PhotoHandler.decodeSampledBitmapFromResource(
							getResources(), R.drawable.mainmenu_background,
							screenSize.x, screenSize.y)));
		} else {
			backgroundImage.setImageDrawable(new BitmapDrawable(getResources(),
					PhotoHandler.decodeSampledBitmapFromResource(
							getResources(),
							R.drawable.mainmenu_background_land, screenSize.x,
							screenSize.y)));
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		PhotoHandler.stripImageView(backgroundImage);
	}

	/**
	 * Switches to the camera activity
	 * 
	 * @param v
	 */
	public void cameraOnClick(View v) {
		startActivity(new Intent(this.getApplicationContext(),
				CameraActivity.class));
	}

	/**
	 * Switches to the gallery activity
	 * 
	 * @param v
	 */
	public void galleryOnClick(View v) {
		startActivity(new Intent(this.getApplicationContext(),
				GalleryActivity.class));
	}

	/**
	 * Switches to the map activity
	 * 
	 * @param v
	 */
	public void goToMapScreenOnClick(View v) {
		startActivity(new Intent(this.getApplicationContext(),
				MapActivity.class));
	}

}
