package no.kystverket.lommeradaren.camera.augmented.opengl;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;

public class MyGLSurfaceView extends GLSurfaceView {

	private MyGLRenderer mRenderer;
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float mPreviousX;
	private float mPreviousY;
	private Context cxt;
	//
	// private SensorManager sensorManager;
	// private RotationVectorSensor rotationSensor;

	private float[] vectorBuffer = { 0.0f, 0.0f, 0.0f };

	public MyGLSurfaceView(Context context, AttributeSet att) {
		super(context, att);
		this.cxt = context;
		this.setEGLContextClientVersion(2);
		this.mRenderer = new MyGLRenderer();

		Activity activity = (Activity) context;
		// this.sensorManager = (SensorManager) activity
		// .getSystemService(Context.SENSOR_SERVICE);
		// this.rotationSensor = new RotationVectorSensor(sensorManager);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);

		this.setRenderer(this.mRenderer);

		getHolder().setFormat(PixelFormat.TRANSLUCENT);

		// this made it work for me - works only from sdk level 6 on, though....
		//setZOrderOnTop(true);
		setZOrderMediaOverlay(true);
		
		
		this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	public void getSensorData(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			float rotationVector[] = event.values.clone();
			float quaternion[] = new float[4];
			SensorManager.getQuaternionFromVector(quaternion, rotationVector);
			float[] rotationMatrix = new float[16]; // DOES IT WORK, CHANING 9
													// TO 16?
			SensorManager.getRotationMatrixFromVector(rotationMatrix,
					quaternion);// quaternion or rotatinVector?
			float orientation[] = new float[3];
			// getOrientation was here
			int rot = ((WindowManager) cxt
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay().getRotation();
			// Log.d("RotMat1","" + rotationMatrix[0] + " " + rotationMatrix[1]
			// + " " + rotationMatrix[2] + " " + rotationMatrix[3]);
			// Log.d("RotMat2","" + rotationMatrix[4] + " " + rotationMatrix[5]
			// + " " + rotationMatrix[6] + " " + rotationMatrix[7]);
			// Log.d("RotMat3","" + rotationMatrix[8] + " " + rotationMatrix[9]
			// + " " + rotationMatrix[10] + " " + rotationMatrix[11]);
			// Log.d("RotMat4","" + rotationMatrix[12] + " " +
			// rotationMatrix[13] + " " + rotationMatrix[14] + " " +
			// rotationMatrix[15]);
			SensorManager.remapCoordinateSystem(rotationMatrix,
					SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X,
					rotationMatrix);
			SensorManager.getOrientation(rotationMatrix, orientation);
			// mRenderer.setRotationMatrix(rotationMatrix);
			this.mRenderer.setEye(0, 0, 0);
			orientation[0] = (float) Math.toDegrees(orientation[0]);
			orientation[1] = (float) Math.toDegrees(orientation[1]);
			orientation[2] = (float) Math.toDegrees(orientation[2]);

			// if (orientation[0] < 0) {
			// orientation[0] += 360;
			// }
			// Pitch scaling
			// if (orientation[1] < -90) {
			// orientation[1] += (-2 * (90 + orientation[1]));
			// } else if (orientation[1] > 90) {
			// orientation[1] += (2 * (90 - orientation[1]));
			// }
			// lat = azimuth
			//
			// float r = 100;
			// float
			// float asimuth = orientation[0];

			// float xView = (float) Math.tan

			filterVector(0.8f, orientation);

			this.mRenderer.setCenter(orientation[1], orientation[2],
					orientation[0]);
			// this.mRenderer.setCenter((float)Math.toDegrees(orientation[1]),
			// (float)Math.toDegrees(orientation[2]),
			// -180);

			this.mRenderer.setUp(0, 1, 0);

			Log.d("Orientation", "" + orientation[0] + " " + orientation[1]
					+ " " + orientation[2]);

			// getContext().
			// (WindowManager) getSystemService(WINDOW_SERVICE);
			// Activity activity = (Activity) getContext();
			// int rotationTest =
			// activity.getWindowManager().getDefaultDisplay().getRotation();
			// Log.d("CurrentRotation","" + rotationTest);

			this.requestRender();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// this.rotationSensor.onPause(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		// this.rotationSensor.onResume(this);
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent e) {
	// // MotionEvent reports input details from the touch screen
	// // and other input controls. In this case, you are only
	// // interested in events where the touch position changed.
	//
	// float x = e.getX();
	// float y = e.getY();
	//
	// switch (e.getAction()) {
	// case MotionEvent.ACTION_MOVE:
	//
	// float dx = x - mPreviousX;
	// float dy = y - mPreviousY;
	//
	// // reverse direction of rotation above the mid-line
	// if (y > getHeight() / 2) {
	// dx = dx * -1;
	// }
	//
	// // reverse direction of rotation to left of the mid-line
	// if (x < getWidth() / 2) {
	// dy = dy * -1;
	// }
	//
	// mRenderer.setAngle(mRenderer.getAngle()
	// + ((dx + dy) * TOUCH_SCALE_FACTOR)); // = 180.0f / 320
	// requestRender();
	// }
	//
	// mPreviousX = x;
	// mPreviousY = y;
	// return true;
	// }

	/**
	 * Low-pass filter might not be the solution for smoothening data, or might
	 * need an alternative implementation. Since the idea is that what we
	 * compare the result with the previous buffered value, it gives off strange
	 * results when sudden movements are made. Example: If I suddenly turn my
	 * phone so that the azimuth, pitch or roll exceeds their given
	 * interval(From 360 to 5), the buffered value is completely incosistent
	 * with the new value. Taking this into consideration the coordinate system
	 * doesn't turn when I want it to, and continues in the direction it was
	 * already moving.
	 * 
	 * @param alpha
	 * @param vectorToFilter
	 */
	public void filterVector(float alpha, float[] vectorToFilter) {
		for (int i = 0; i < vectorToFilter.length; i++) {
			vectorToFilter[i] = alpha * vectorToFilter[i] + (1 - alpha)
					* this.vectorBuffer[i];
			this.vectorBuffer[i] = vectorToFilter[i];
		}
	}

	// @Override
	// public void onSensorChanged(SensorEvent event) {
	// // TODO Auto-generated method stub
	//
	// }

}
