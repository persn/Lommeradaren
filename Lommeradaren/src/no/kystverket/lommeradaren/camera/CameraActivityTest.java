package no.kystverket.lommeradaren.camera;

import no.kystverket.lommeradaren.R;
import no.kystverket.lommeradaren.camera.CameraController.OnPhotoTakenListener;
import no.kystverket.lommeradaren.camera.augmented.SensorHandler;
import no.kystverket.lommeradaren.camera.augmented.opengl.MarkerSurfaceView;
import no.kystverket.lommeradaren.maps.MiniMapFragment;
import no.kystverket.lommeradaren.maps.MiniMapFragment.OnMarkerDataUpdatedListener;
import no.kystverket.lommeradaren.markers.DataSourceHandler;
import android.app.Activity;
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
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

public class CameraActivityTest extends Activity implements
		SensorEventListener, OnMarkerDataUpdatedListener, OnPhotoTakenListener {

	// Android view-controller components
	private MarkerSurfaceView mGLView;
	private MiniMapFragment gMap;
	private ImageButton renderActionBarBtn;

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
		this.setRendererScreenSize();
		this.sensorHandler = new SensorHandler();
		this.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		this.accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		this.gMap = ((MiniMapFragment) getFragmentManager().findFragmentById(
				R.id.mini_map_fragment));

		if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
        	getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
		this.getActionBar().hide();
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActionBar().setDisplayShowTitleEnabled(false);

		this.renderActionBarBtn = (ImageButton) findViewById(R.id.btn_galscrn_render_bar);
	}

	@Override
	protected void onResume() {
		super.onResume();

		//SurfaceView surfaceView = ((SurfaceView) findViewById(R.id.camera_preview_layout));
		
		
		this.mSensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_GAME);
		this.mSensorManager.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_GAME);
		// initCameraView();
		// mPreview.registerOnPhotoTakenListener(this);
	}
	
	@Override
	protected void onPause(){
		this.mSensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onPhotoTaken(String imgPath) {
		// TODO Auto-generated method stud
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
			Toast.makeText(getApplicationContext(), "Currently missing while code is restructured", Toast.LENGTH_SHORT).show();
			return false;
		case KeyEvent.KEYCODE_FOCUS:
			Toast.makeText(getApplicationContext(), "Currently missing while code is restructured", Toast.LENGTH_SHORT).show();
			return false;
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
			this.renderActionBarBtn.setVisibility(View.VISIBLE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void takePictureOnClick(View view) {
		Toast.makeText(getApplicationContext(), "TakePicturePlaceholder",
				Toast.LENGTH_SHORT).show();
	}

	public void renderActionBarOnClick(View view) {
		if (Build.VERSION.SDK_INT < 16) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
        	getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
		getActionBar().show();
		this.renderActionBarBtn.setVisibility(View.GONE);
	}	

	private void setRendererScreenSize() {
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		this.mGLView.setRendererScreenSize(size.x, size.y);
	}

}
