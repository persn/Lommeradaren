package no.kystverket.lommeradaren.camera;

import no.kystverket.lommeradaren.R;
import no.kystverket.lommeradaren.camera.augmented.opengl.MarkerSurfaceView;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CameraActivity extends Activity implements SensorEventListener,
		OnTouchListener {

	private MarkerSurfaceView mGLView;
	private CameraView mPreview;
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
		mGLView = (MarkerSurfaceView) findViewById(R.id.myGLSurfaceView1);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		rotationSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		compass = (ImageView) findViewById(R.id.imageView1);
		initCameraView();
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
			float R[] = new float[9];
			float I[] = new float[9];
			if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				animateCompass((int) (Math.toDegrees(orientation[0]) + 90));
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
