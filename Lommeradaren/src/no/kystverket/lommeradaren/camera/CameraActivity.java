package no.kystverket.lommeradaren.camera;

import java.util.List;

import no.kystverket.lommeradaren.R;
import no.kystverket.lommeradaren.camera.CameraView.OnPhotoTakenListener;
import no.kystverket.lommeradaren.camera.augmented.SensorHandler;
import no.kystverket.lommeradaren.camera.augmented.opengl.MarkerSurfaceView;
import no.kystverket.lommeradaren.camera.augmented.opengl.MarkerWrapper;
import no.kystverket.lommeradaren.maps.MiniMapFragment;
import no.kystverket.lommeradaren.maps.MiniMapFragment.OnMarkerDataUpdatedListener;
import no.kystverket.lommeradaren.markers.DataSourceHandler;
import no.kystverket.lommeradaren.markers.POI;
import no.kystverket.lommeradaren.photo.PhotoHandler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

/**
 * Activity class for the augmented reality/camera part of the application.
 * Contains logic for handling events that occurs in sensor and camera classes.
 * 
 * @author Henrik
 * 
 */
public class CameraActivity extends Activity implements SensorEventListener,
		OnMarkerDataUpdatedListener, OnPhotoTakenListener {

	// Android view-controller components
	private MarkerSurfaceView mGLView;
	private CameraView mCameraView;
	private MiniMapFragment gMap;
	private ImageButton btn_takePicture;
	private ImageButton btn_renderActionBar;

	// Android sensors
	private SensorManager mSensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;
	private SensorHandler sensorHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.cameraview);
		this.mGLView = (MarkerSurfaceView) findViewById(R.id.marker_surface_view);
		this.mCameraView = (CameraView) findViewById(R.id.camera_preview_layout);
		this.setRendererScreenSize();
		this.sensorHandler = new SensorHandler();
		this.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		this.accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		this.gMap = ((MiniMapFragment) getFragmentManager().findFragmentById(
				R.id.mini_map_fragment));

		this.turnOffSystemBar();
		this.getActionBar().hide();
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActionBar().setDisplayShowTitleEnabled(false);

		this.btn_takePicture = (ImageButton) findViewById(R.id.btn_take_picture);
		this.btn_renderActionBar = (ImageButton) findViewById(R.id.btn_galscrn_render_bar);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.mSensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_GAME);
		this.mSensorManager.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_GAME);
		this.mCameraView.setOnPhotoTakenListener(this);
	}

	@Override
	protected void onPause() {
		this.mSensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onMarkerDataUpdated(DataSourceHandler datasourceHandler,
			Location myLocation) {
		this.mGLView.set3DMarkerData(datasourceHandler, myLocation);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (sensorHandler.handleEvent(event)) {
			float[] orientation = sensorHandler.getOrientation();
			mGLView.getSensorData(orientation.clone());
			this.gMap
					.updateBearing((float) Math.toDegrees(orientation[0]) + 90);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_CAMERA:
			this.btn_takePicture.setEnabled(false);
			this.mCameraView.snapPicture();
			this.btn_takePicture.setEnabled(true);
			return true;
		case KeyEvent.KEYCODE_FOCUS:
			this.btn_takePicture.setEnabled(false);
			this.mCameraView.autoFocusAndSnapPicture();
			this.btn_takePicture.setEnabled(true);
			return true;
		default:
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_camera_screen, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.sub_item_normal_map:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_NORMAL);
			return true;
		case R.id.sub_item_terrain_map:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_TERRAIN);
			return true;
		case R.id.sub_item_hybrid_map:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_HYBRID);
			return true;
		case R.id.sub_item_satellite_map:
			((MapView) findViewById(R.id.map)).getMap().setMapType(
					GoogleMap.MAP_TYPE_SATELLITE);
			return true;
		case R.id.menu_hide_bar:
			getActionBar().hide();
			this.btn_renderActionBar.setVisibility(View.VISIBLE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPhotoTaken(String imgPath) {
		this.displayShipListDialog(imgPath);
	}

	/**
	 * Disables the camerabutton and tells the cameraview to save a picture of
	 * the current focus to storage.
	 * 
	 * @param view
	 */
	public void takePictureOnClick(View view) {
		this.btn_takePicture.setEnabled(false);
		this.mCameraView.autoFocusAndSnapPicture();
		this.btn_takePicture.setEnabled(true);
	}

	public void renderActionBarOnClick(View view) {
		turnOnSystemBar();
		getActionBar().show();
		this.btn_renderActionBar.setVisibility(View.GONE);
	}

	private void setRendererScreenSize() {
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		this.mGLView.setRendererScreenSize(size.x, size.y);
	}

	@SuppressLint("InlinedApi")
	private void turnOnSystemBar() {
		if (Build.VERSION.SDK_INT < 16) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_FULLSCREEN);
		}
	}

	@SuppressLint("InlinedApi")
	private void turnOffSystemBar() {
		if (Build.VERSION.SDK_INT < 16) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		} else {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_FULLSCREEN);
		}
	}

	/**
	 * Creates a popup dialog asking which ship a photo was just taken. Gets
	 * called once a picture has been successfully created by the cameraview.
	 * The information about that ship is then written to the images metadata.
	 * 
	 * @param imgPath Image to write metadata into.
	 */
	private void displayShipListDialog(String imgPath) {
		final List<MarkerWrapper> markers = mGLView.getMarkerList();
		final String path = imgPath;
		CharSequence[] options = new CharSequence[markers.size()];
		for (int i = 0; i < markers.size(); i++) {
			options[i] = markers.get(i).getPOI().getName();
		}
		if (markers.size() > 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select Ship to associate with picture");
			builder.setCancelable(true);
			builder.setItems(options, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					String data = "";
					POI p = markers.get(which).getPOI();
					data += ("id;" + p.getId() + ",");
					data += ("lat;" + p.getLat() + ",");
					data += ("lng;" + p.getLng() + ",");
					data += ("elevation;" + p.getAlt() + ",");
					data += ("title;" + p.getName() + ",");
					data += ("distance;" + p.getDistance() + ",");
					data += ("has_detail_page;" + p.getHas_detail_page() + ",");
					data += ("webpage;" + p.getWebpage() + ",");
					data += ("mmsi;" + p.getMmsi() + ",");
					data += ("imo;" + p.getImo() + ",");
					data += ("positionTime;" + p.getPositionTime() + ",");
					data += ("speed;" + p.getSpeed() + ",");
					data += ("course;" + p.getCourse() + ",");
					PhotoHandler.setExifData(path, data);
				}
			});
			builder.create().show();
		} else {
			PhotoHandler.setExifData(path, "no_ship_selected");
		}
	}

}