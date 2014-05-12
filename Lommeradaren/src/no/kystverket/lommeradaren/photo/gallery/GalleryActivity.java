package no.kystverket.lommeradaren.photo.gallery;

import java.util.ArrayList;

import no.kystverket.lommeradaren.MarkerDialogFragment;
import no.kystverket.lommeradaren.R;
import no.kystverket.lommeradaren.photo.Photo;
import no.kystverket.lommeradaren.photo.PhotoHandler;
import no.kystverket.lommeradaren.user.GoogleClientActivity;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GestureDetectorCompat;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

/**
 * Activity which displays the gallery. Uses a GridView to display thumbnails
 * and a larger ImageView for a detailed look a selected picture.
 * 
 * @author Henrik Reitan
 */
public class GalleryActivity extends Activity implements
		GestureDetector.OnGestureListener, AdapterView.OnItemClickListener,
		ViewSwitcher.ViewFactory {

	private final int SWIPE_THRESHOLD = 100;
	private final int SWIPE_VELOCITY_THRESHOLD = 100;
	private GestureDetectorCompat mDetector;
	private ArrayList<Photo> pictures;
	private ImageSwitcher mSwitcher;
	private TextView textSwitcher;
	private PhotoHandler pHandler;
	private GridView gallery;
	private ImageButton renderActionBarBtn;
	private int targetW;
	private int targetH;
	private int selectedPosition;
	private int animationTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selectedPosition = -1;
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
		gallery = (GridView) findViewById(R.id.galleryGridView);
		gallery.setAdapter(new ImageAdapter(this, pictures));
		gallery.setOnItemClickListener(this);
		animationTime = getResources().getInteger(
				android.R.integer.config_shortAnimTime);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("");
		// getActionBar().setTitle("Signed in as:");
		// getActionBar().setSubtitle("Flodhesten Flode");

		this.renderActionBarBtn = (ImageButton) findViewById(R.id.btn_galscrn_render_bar);
		this.renderActionBarBtn.setVisibility(View.GONE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	/**
	 * Sets the selected picture to be displayed in the detail view to the one
	 * being clicked in the gridview
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		selectedPosition = position;
		mSwitcher.setImageDrawable(new BitmapDrawable(getApplicationContext()
				.getResources(), pHandler.getLargeImage(pictures.get(position)
				.getImgName(), targetW, targetH)));
		textSwitcher.setText(pictures.get(position).getImgName());
	}

	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		return i;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu_gallery_screen, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.menu_gallery_image_info:
			if (selectedPosition != -1) {
				showInfoDialog();
			}
			return true;
		case R.id.menu_gallery_delete_image:
			if (selectedPosition != -1) {
				deleteImage();
			}
			return true;
		case R.id.menu_hide_bar:
			this.getActionBar().hide();
			this.renderActionBarBtn.setVisibility(View.VISIBLE);
			return true;
		case R.id.sub_menu_google_login:
			startActivity(new Intent(this, GoogleClientActivity.class));
			return true;
		case R.id.sub_menu_stream_picture:
			Intent intent = new Intent(this, GoogleClientActivity.class);
			if (this.selectedPosition >= 0
					&& this.selectedPosition < this.pictures.size()) {
				intent.putExtra("picture-filename-web-upload", this.pictures
						.get(this.selectedPosition).getImgName());
				intent.putExtra("picture-filedata-web-upload", this.pictures
						.get(this.selectedPosition).getPoi().toJSON());
				startActivityForResult(intent, 100);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 100:
			if (resultCode == RESULT_OK) {
				Toast.makeText(
						getApplicationContext(),
						this.pictures.get(this.selectedPosition).getImgName()
								+ " transfered to web", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		default:
			Toast.makeText(getApplicationContext(), "Something went wrong",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Changes the selected image to the next or previous based on the direction
	 * of the finger gesture.
	 */
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

	/**
	 * Toggles hiding/showing the gui elements when tapping a finger on the
	 * screen
	 */
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

	public void renderActionBarOnClick(View view) {
		getActionBar().show();
		this.renderActionBarBtn.setVisibility(View.GONE);
	}

	/**
	 * Method for assisting the onFling method for changing the selected image
	 */
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

	/**
	 * Method for assisting the onFling method for changing the selected image
	 */
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
	
	/**
	 * Displays a popup dialog displaying the information contained in the
	 * currently selected images metadata.
	 */
	private void showInfoDialog() {
		FragmentManager fm = getFragmentManager();
		DialogFragment newFragment = new MarkerDialogFragment();
		((MarkerDialogFragment) newFragment).setContent(
				pictures.get(selectedPosition).getPoi().getName(), ""
						+ pictures.get(selectedPosition).getPoi().getLat(), ""
						+ pictures.get(selectedPosition).getPoi().getLng(), ""
						+ pictures.get(selectedPosition).getPoi().getAlt(),
				pictures.get(selectedPosition).getPoi().getImo(),
				pictures.get(selectedPosition).getPoi().getMmsi(), pictures
						.get(selectedPosition).getPoi().getSpeed(), pictures
						.get(selectedPosition).getPoi().getPositionTime(),
				pictures.get(selectedPosition).getPoi().getWebpage());
		newFragment.show(fm, "marker_dialog");
	}

	/**
	 * Deletes the currently selected image from the device.
	 */
	private void deleteImage() {
		pHandler.deleteImage(pictures.get(selectedPosition));
		pictures = pHandler.getPictures();
		gallery.setAdapter(new ImageAdapter(this, pictures));
		mSwitcher.setImageDrawable(null);
		selectedPosition = -1;
		textSwitcher.setText("");
	}

}
