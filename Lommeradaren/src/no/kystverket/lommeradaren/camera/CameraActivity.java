package no.kystverket.lommeradaren.camera;

import no.kystverket.lommeradaren.MainActivity;
import no.kystverket.lommeradaren.R;
import no.kystverket.lommeradaren.camera.augmented.opengl.MarkerSurfaceView;
import no.kystverket.lommeradaren.maps.CustomGoogleMapFragment;
import no.kystverket.lommeradaren.maps.MapActivity;
import no.kystverket.lommeradaren.markers.DataSourceCollection;
import no.kystverket.lommeradaren.photo.gallery.GalleryActivity;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

/**
 * 
 * @author Per Olav Flaten
 *
 */
public class CameraActivity extends Activity implements SensorEventListener,
		OnTouchListener {

	private MarkerSurfaceView mGLView;
	private CameraView mPreview;
	private CustomGoogleMapFragment gMap;
	private ImageView compass;
	private int currentDegree = 0;
	private SensorManager mSensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;
	private Sensor rotationSensor;
	private float[] mGravity;
	private float[] mGeomagnetic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cameraview);
		mGLView = (MarkerSurfaceView) findViewById(R.id.marker_surface_view);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		rotationSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		compass = (ImageView) findViewById(R.id.mainmenu_background);

		this.gMap = ((CustomGoogleMapFragment) getFragmentManager()
				.findFragmentById(R.id.fragment1));
		this.gMap.toggleMiniMapSettings();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, rotationSensor,
				SensorManager.SENSOR_DELAY_GAME);
		initCameraView();
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
		switch (keycode) {
		case KeyEvent.KEYCODE_CAMERA:
			this.mPreview.snapPicture();
			return true;
		case KeyEvent.KEYCODE_FOCUS:
			this.mPreview.autoFocusAndTakePicture();
			return true;
		case KeyEvent.KEYCODE_BACK:
			startActivity(new Intent(this.getApplicationContext(),
					MainActivity.class));
			finish();
			return true;
		}
		return super.onKeyDown(keycode, e);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent evt) {
		if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			mGravity = evt.values.clone();
		if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			mGeomagnetic = evt.values.clone();
		if (evt.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
			mGLView.getSensorData(evt.values.clone());
		if (mGravity != null && mGeomagnetic != null) {
			float rotationMatrix[] = new float[9];
			float inclinationMatrix[] = new float[9];
			if (SensorManager.getRotationMatrix(rotationMatrix,
					inclinationMatrix, mGravity, mGeomagnetic)) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(rotationMatrix, orientation);
				animateCompass((int) (Math.toDegrees(orientation[0]) + 90));
				this.gMap
						.updateBearing((float) Math.toDegrees(orientation[0]) + 90);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_camera_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sub_menu_camera_normal:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_NORMAL);
			return true;
		case R.id.sub_menu_camera_terrain:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_TERRAIN);
			return true;
		case R.id.sub_menu_camera_hybrid:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_HYBRID);
			return true;
		case R.id.sub_menu_camera_satellite:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_SATELLITE);
			return true;
		case R.id.sub_menu_camera_map:
			startActivity(new Intent(this.getApplicationContext(),
					MapActivity.class));
			finish();
			return true;
		case R.id.sub_menu_camera_gallery:
			startActivity(new Intent(this.getApplicationContext(),
					GalleryActivity.class));
			finish();
			return true;
		case R.id.sub_menu_map_user:
			return false;// Not yet implemented
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This is the method called as designated from the TakePicture-button in cameraview.xml
	 * @param v
	 */
	public void takePictureOnClick(View v) {
		this.mPreview.autoFocusAndTakePicture();
	}

	private void initCameraView() {
		this.mPreview = null;
		this.mPreview = new CameraView(this, getString(R.string.app_name));
		((RelativeLayout) findViewById(R.id.camera_preview_layout))
				.addView(mPreview);
	}

	private void animateCompass(int degree) {
		RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		TranslateAnimation ta = new TranslateAnimation(0, 0,
				compass.getHeight() / 2, compass.getHeight() / 2);
		AnimationSet as = new AnimationSet(false);
		as.addAnimation(ra);
		as.addAnimation(ta);
		compass.startAnimation(as);
		currentDegree = (int) -degree;
	}
}
