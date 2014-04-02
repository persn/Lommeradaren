package no.kystverket.lommeradaren.camera.augmented;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

/**
 * 
 * @author Henrik Reitan
 * 
 */
public class SensorHandler {

	private float[] orientation;
	private float[] gravity = { 0, 0, 0 };
	private float[] magnetic = { 0, 0, 0 };
	private final float alpha = 0.15f;

	public boolean handleEvent(SensorEvent evt) {
		if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			lowPass(evt.values.clone(), gravity);
		if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			lowPass(evt.values.clone(), magnetic);
		if (gravity == null || magnetic == null) {
			return false;
		} else {
			float rotationMatrix[] = new float[9];
			float inclinationMatrix[] = new float[9];
			if (SensorManager.getRotationMatrix(rotationMatrix,
					inclinationMatrix, gravity, magnetic)) {
				orientation = new float[3];
				orientation = SensorManager.getOrientation(rotationMatrix,
						orientation);
				return true;
			} else {
				return false;
			}
		}
	}

	public float[] getOrientation() {
		return orientation;
	}

	private void lowPass(float[] input, float[] output) {
		for (int i = 0; i < input.length; i++) {
			output[i] = output[i] + alpha * (input[i] - output[i]);
		}
	}
}
