package no.kystverket.lommeradaren.photo.gallery;

import java.util.ArrayList;

import no.kystverket.lommeradaren.MainActivity;
import no.kystverket.lommeradaren.R;
import no.kystverket.lommeradaren.camera.CameraActivity;
import no.kystverket.lommeradaren.maps.MapActivity;
import no.kystverket.lommeradaren.photo.Photo;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * 
 * @author Henrik Reitan
 *
 */
public class GalleryActivity extends Activity implements
		AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {

	private ArrayList<Photo> pictures;
	private ImageSwitcher mSwitcher;
	private TextView textSwitcher;
	private PhotoHandler pHandler;
	int targetW;
	int targetH;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gallerylayout);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		targetW = size.x;
		targetH = size.y;
		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		textSwitcher = (TextView) findViewById(R.id.imageTextField);
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));
		pHandler = new PhotoHandler(getString(R.string.app_name));
		pictures = pHandler.getPictures();
		Gallery g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(new ImageAdapter(this, pictures));
		g.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		mSwitcher.setImageDrawable(new BitmapDrawable(getApplicationContext()
				.getResources(), pHandler.getLargeImage(pictures.get(position)
				.getImgName(), targetW, targetH)));
		textSwitcher.setText(pictures.get(position).getImgName());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
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
		getMenuInflater().inflate(R.menu.menu_gallery_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sub_menu_gallery_camera:
			startActivity(new Intent(this.getApplicationContext(),
					CameraActivity.class));
			finish();
			return true;
		case R.id.sub_menu_gallery_map:
			startActivity(new Intent(this.getApplicationContext(),
					MapActivity.class));
			finish();
			return true;
		case R.id.sub_menu_gallery_user:
			return false;// Not yet implemented
		}
		return super.onOptionsItemSelected(item);
	}

}