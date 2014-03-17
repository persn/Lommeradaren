package no.kystverket.lommeradaren.camera.augmented.opengl;

import no.kystverket.lommeradaren.camera.augmented.opengl.texture.Triangle;

public class MarkerWrapper {

	//POI here
	private Triangle triangle; //Replace with POI 3D object
	private String tag; // Placeholder for debugging on texture touch
	private float[] cartesianCoordinates; // Should be accessible through the POI when implemented and can be removed
	private int[] screenCoordinates;
	
	public MarkerWrapper(Triangle triangle, String tag, float[] cartesianCoordinates, int[] screenCoordinates){
		this.triangle = triangle;
		this.tag = tag;
		this.cartesianCoordinates = cartesianCoordinates;
		this.screenCoordinates = screenCoordinates;
	}
	
	public Triangle getTriangle(){
		return triangle;
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
