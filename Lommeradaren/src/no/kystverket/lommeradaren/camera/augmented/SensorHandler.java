package no.kystverket.lommeradaren.camera.augmented;

import java.util.LinkedList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

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
	private LinkedList<float[]> values = new LinkedList<float[]>();
	private int counter = 0;
	
	
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
				float orientationMatrix[] = new float[3];
				orientationMatrix = SensorManager.getOrientation(rotationMatrix,
						orientationMatrix);
				orientation = orientationMatrix;//interpolate(orientationMatrix); TODO: add back interpolation
				return true;
			} else {
				return false;
			}
		}
	}

	public float[] interpolate(float[] newValues){
		if(values.size() < 20 ){
			values.add(newValues);
		} else {
			values.set(counter, newValues);
			counter++;
			counter = counter%20;
		}
		float[] res = new float[3];
		for (float[] f : values){
			res[0] += f[0];
			res[1] += f[1];
			res[2] += f[2];
		}
		res[0] = res[0] / values.size();
		res[1] = res[1] / values.size();
		res[2] = res[2] / values.size();
		return res;
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
