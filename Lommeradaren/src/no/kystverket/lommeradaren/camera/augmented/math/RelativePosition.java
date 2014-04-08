package no.kystverket.lommeradaren.camera.augmented.math;

import android.util.Log;

/**
 * 
 * @author Per Olav Flaten
 * 
 */
public class RelativePosition {

	private final static float FACTOR = 10000;
	private final static float MIN_RELATIVE_DISTANCE = 15;
	private final static float MAX_RELATIVE_DISTANCE = 70;

	public static float getAltitudeDifference(float droidAltitude,
			float poiAltitude) {
		return poiAltitude - droidAltitude;
	}

	public static float getDifference(float droidPosition, float poiPosition) {
		float difference = (poiPosition - droidPosition) * FACTOR;
//		if (Math.abs(difference) < MIN_RELATIVE_DISTANCE) {
//			if (difference < 0) {
//				difference = -MIN_RELATIVE_DISTANCE;
//			} else {
//				difference = MIN_RELATIVE_DISTANCE;
//			}
//		} else if (Math.abs(difference) > MAX_RELATIVE_DISTANCE) {
//			if (difference < 0) {
//				difference = -MAX_RELATIVE_DISTANCE;
//			} else {
//				difference = MAX_RELATIVE_DISTANCE;
//			}
//		}
		return difference;
	}

}
