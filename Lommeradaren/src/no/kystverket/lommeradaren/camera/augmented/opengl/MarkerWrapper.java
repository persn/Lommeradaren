package no.kystverket.lommeradaren.camera.augmented.opengl;

import no.kystverket.lommeradaren.markers.POI;

/**
 * Class for combining a POI object and its associated cartesian and 2d onscreen
 * coordinates for easier handling.
 * 
 * @author Per Olav Flaten
 * 
 */
public class MarkerWrapper {

	private POI poi;
	private String[] tag; // Placeholder for debugging on texture touch
	private float[] cartesianCoordinates; // Should be accessible through the
											// POI when implemented and can be
											// removed
	private int[] screenCoordinates;

	public MarkerWrapper(POI poi, String[] tag, float[] cartesianCoordinates,
			int[] screenCoordinates) {
		this.poi = poi;
		this.tag = tag;
		this.cartesianCoordinates = cartesianCoordinates;
		this.screenCoordinates = screenCoordinates;
	}

	public POI getPOI() {
		return this.poi;
	}

	public String[] getTag() {
		return this.tag;
	}

	public float[] getCartesianCoordinates() {
		return this.cartesianCoordinates;
	}

	public int[] getScreenCoordinates() {
		return this.screenCoordinates;
	}

	public void setScreenCoordinates(int[] screenCoordinates) {
		this.screenCoordinates = screenCoordinates;
	}

}
