package no.kystverket.lommeradaren.camera.augmented;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 
 * @author Per Olav Flaten
 *
 */
public class RotationVectorSensor {

	private SensorManager sensorManager;
	private Sensor rotSensor;
	float[] rotationValues = { 0.0f, 0.0f, 0.0f };

	public RotationVectorSensor(SensorManager sensorManager) {
		this.sensorManager = sensorManager;
		this.rotSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
	}

	public void ajustValues(SensorEvent event) {
		if (event.sensor == rotSensor) {
			rotationValues = event.values;
		}
	}

	public void onResume(SensorEventListener sensorListener) {
		sensorManager.registerListener(sensorListener, rotSensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	public void onPause(SensorEventListener sensorListener) {
		sensorManager.unregisterListener(sensorListener);
	}

	public float[] getRotation() {
		return rotationValues;
	}

	public boolean isSensor() {
		if (rotSensor != null) {
			return true;
		}
		return false;
	}

}
