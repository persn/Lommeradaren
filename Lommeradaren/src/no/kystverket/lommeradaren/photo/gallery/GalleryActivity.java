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
import android.support.v4.view.GestureDetectorCompat;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

/**
 * 
 * @author Henrik Reitan
 * 
 */
public class GalleryActivity extends Activity implements
		GestureDetector.OnGestureListener, AdapterView.OnItemSelectedListener,
		ViewSwitcher.ViewFactory {

	private final int SWIPE_THRESHOLD = 100;
	private final int SWIPE_VELOCITY_THRESHOLD = 100;
	private GestureDetectorCompat mDetector;
	private ArrayList<Photo> pictures;
	private ImageSwitcher mSwitcher;
	private TextView textSwitcher;
	private PhotoHandler pHandler;
	private Gallery gallery;
	private int targetW;
	private int targetH;
	private int selectedPosition;
	private int animationTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gallerylayout);
		mDetector = new GestureDetectorCompat(this, this);
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
		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this, pictures));
		gallery.setOnItemSelectedListener(this);
		animationTime = getResources().getInteger(
				android.R.integer.config_shortAnimTime);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		selectedPosition = position;
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

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		boolean result = false;
		try {
			float diffY = e2.getY() - e1.getY();
			float diffX = e2.getX() - e1.getX();
			if (Math.abs(diffX) > Math.abs(diffY)) {
				if (Math.abs(diffX) > SWIPE_THRESHOLD
						&& Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
					if (diffX > 0) {
						onSwipeRight();
					} else {
						onSwipeLeft();
					}
				}
			} else {
				if (Math.abs(diffY) > SWIPE_THRESHOLD
						&& Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
					if (diffY > 0) {
						onSwipeBottom();
					} else {
						onSwipeTop();
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return result;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	// @Override
	// public boolean onSingleTapUp(MotionEvent arg0) {
	// if (textSwitcher.getVisibility() == View.VISIBLE
	// && gallery.getVisibility() == View.VISIBLE) {
	// textSwitcher.setVisibility(View.INVISIBLE);
	// gallery.setVisibility(View.INVISIBLE);
	// } else {
	// textSwitcher.setVisibility(View.VISIBLE);
	// gallery.setVisibility(View.VISIBLE);
	// }
	// return false;
	// }

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		if (textSwitcher.getAlpha() == 1f && gallery.getAlpha() == 1f) {
			textSwitcher.animate().alpha(0f).setDuration(animationTime)
					.setListener(null);
			gallery.animate().alpha(0f).setDuration(animationTime)
					.setListener(null);
		} else {
			textSwitcher.animate().alpha(1f).setDuration(animationTime)
					.setListener(null);
			gallery.animate().alpha(1f).setDuration(animationTime)
					.setListener(null);

		}
		return false;
	}

	public void onSwipeRight() {
		if (selectedPosition > 0) {
			selectedPosition--;
			mSwitcher.setImageDrawable(new BitmapDrawable(
					getApplicationContext().getResources(), pHandler
							.getLargeImage(pictures.get(selectedPosition)
									.getImgName(), targetW, targetH)));
			textSwitcher.setText(pictures.get(selectedPosition).getImgName());
		}
	}

	public void onSwipeLeft() {
		if (selectedPosition < pictures.size()) {
			selectedPosition++;
			mSwitcher.setImageDrawable(new BitmapDrawable(
					getApplicationContext().getResources(), pHandler
							.getLargeImage(pictures.get(selectedPosition)
									.getImgName(), targetW, targetH)));
			textSwitcher.setText(pictures.get(selectedPosition).getImgName());
		}
	}

	public void onSwipeTop() {
	}

	public void onSwipeBottom() {
	}

}