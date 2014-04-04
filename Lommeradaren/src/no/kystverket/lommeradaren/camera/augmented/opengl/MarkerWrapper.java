package no.kystverket.lommeradaren.camera.augmented.opengl;

public class MarkerWrapper {

	//POI here
	private String tag; // Placeholder for debugging on texture touch
	private float[] cartesianCoordinates; // Should be accessible through the POI when implemented and can be removed
	private int[] screenCoordinates;
	
	public MarkerWrapper(String tag, float[] cartesianCoordinates, int[] screenCoordinates){
		this.tag = tag;
		this.cartesianCoordinates = cartesianCoordinates;
		this.screenCoordinates = screenCoordinates;
	}
	
	public String getTag(){
		return tag;
	}
	
	public float[] getCartesianCoordinates(){
		return cartesianCoordinates;
	}
	
	public int[] getScreenCoordinates(){
		return screenCoordinates;
	}
	
	public void setScreenCoordinates(int[] screenCoordinates){
		this.screenCoordinates = screenCoordinates;
	}
	
}