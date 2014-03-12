package no.kystverket.lommeradaren.camera.augmented.math;

import android.util.Log;

/**
 * 
 * @author Per Olav Flaten
 * 
 */
public class RelativePosition {

	private final static float FACTOR = 100000;
	private final static float MIN_RELATIVE_DISTANCE = 100;
	private final static float MAX_RELATIVE_DISTANCE = 10000;

	public static float getAltitudeDifference(float droidAltitude, float poiAltitude){
		return poiAltitude - droidAltitude;
	}
	
	public static float getDifference(float droidPosition, float poiPosition) {
		float difference = (poiPosition - droidPosition) * FACTOR;
		Log.d("DIFFERENCE","" + difference);
		if (difference < MIN_RELATIVE_DISTANCE) {
			difference = MIN_RELATIVE_DISTANCE;
		} else if (difference > MAX_RELATIVE_DISTANCE) {
			difference = MAX_RELATIVE_DISTANCE;
		}
		return difference;
	}

}
