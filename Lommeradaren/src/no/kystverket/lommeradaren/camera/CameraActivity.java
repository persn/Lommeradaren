package no.kystverket.lommeradaren.camera;

import java.util.List;

import no.kystverket.lommeradaren.MainActivity;
import no.kystverket.lommeradaren.R;
import no.kystverket.lommeradaren.camera.CameraController.OnPhotoTakenListener;
import no.kystverket.lommeradaren.camera.augmented.SensorHandler;
import no.kystverket.lommeradaren.camera.augmented.opengl.MarkerSurfaceView;
import no.kystverket.lommeradaren.camera.augmented.opengl.MarkerWrapper;
import no.kystverket.lommeradaren.maps.MapActivity;
import no.kystverket.lommeradaren.maps.MiniMapFragment;
import no.kystverket.lommeradaren.maps.MiniMapFragment.OnMarkerDataUpdatedListener;
import no.kystverket.lommeradaren.markers.DataSourceHandler;
import no.kystverket.lommeradaren.markers.POI;
import no.kystverket.lommeradaren.photo.gallery.GalleryActivity;
import no.kystverket.lommeradaren.photo.gallery.PhotoHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

/**
 * 
 * @author Per Olav Flaten
 * 
 */
public class CameraActivity extends Activity implements SensorEventListener,
		OnMarkerDataUpdatedListener, OnPhotoTakenListener {

	private MarkerSurfaceView mGLView;
	private CameraView mPreview;
	private MiniMapFragment gMap;
	private SensorManager mSensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;
	private SensorHandler sensorHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cameraview);
		mGLView = (MarkerSurfaceView) findViewById(R.id.marker_surface_view);
		this.setRendererScreenSize();
		sensorHandler = new SensorHandler();
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		this.gMap = ((MiniMapFragment) getFragmentManager().findFragmentById(
				R.id.mini_map_fragment));
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_GAME);
		initCameraView();
		mPreview.registerOnPhotoTakenListener(this);
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
		if (sensorHandler.handleEvent(evt)) {
			float[] orientation = sensorHandler.getOrientation();
			mGLView.getSensorData(orientation.clone());
			this.gMap
					.updateBearing((float) Math.toDegrees(orientation[0]) + 90);
		}
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

	@Override
	public void onMarkerDataUpdated(DataSourceHandler datasourceHandler,
			Location myLocation) {
		this.mGLView.set3DMarkerData(datasourceHandler, myLocation);
	}

	/**
	 * This is the method called as designated from the TakePicture-button in
	 * cameraview.xml
	 * 
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

	private void setRendererScreenSize() {
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		this.mGLView.setRendererScreenSize(size.x, size.y);
	}

	private void displayShipListDialog(String imgPath){
		final List<MarkerWrapper> markers = mGLView.getMarkerList();
		final String path = imgPath;
		//Log.d("DISPLAYSHIPLISTDIALOG 1", "Markers size"+markers.size()+" path: "+imgPath);
		CharSequence[] options = new CharSequence[markers.size()];
		for(int i = 0; i<markers.size();i++){
			options[i] = markers.get(i).getPOI().getName();
			//Log.d("DISPLAYSHIPLISTDIALOG 2", "i: "+i+" name: "+options[i]);
		}
		if(markers.size() > 0){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select Ship to associate with picture");
			builder.setCancelable(true);
			builder.setItems(options, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					String data = "";
					POI p = markers.get(which).getPOI();
					data += ("id:"+p.getId()+",");
					data += ("lat:"+p.getLat()+",");
					data += ("lng:"+p.getLng()+",");
					data += ("elevation:"+p.getAlt()+",");
					data += ("title:"+p.getName()+",");
					data += ("distance:"+p.getDistance()+",");
					data += ("has_detail_page:"+p.getHas_detail_page()+",");
					data += ("webpage:"+p.getWebpage()+",");
					data += ("mmsi+"+p.getMmsi()+",");
					data += ("imo:"+p.getImo()+",");
					data += ("positionTime:"+p.getPositionTime()+",");
					data += ("speed:"+p.getSpeed()+",");
					data += ("course:"+p.getCourse()+",");
					PhotoHandler.setExifData(path, data);
				}
			});
			builder.create().show();
		} else {
			PhotoHandler.setExifData(path, "no_ship_selected");
		}
	}

	@Override
	public void onPhotoTaken(String imgPath) {
		displayShipListDialog(imgPath);
	}
}
