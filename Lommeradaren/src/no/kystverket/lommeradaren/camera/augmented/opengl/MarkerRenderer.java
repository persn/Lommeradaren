package no.kystverket.lommeradaren.camera.augmented.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import no.kystverket.lommeradaren.camera.augmented.math.LinearAlgebra;
import no.kystverket.lommeradaren.camera.augmented.opengl.texture.Triangle;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

/**
 * Class for rendering markers associated with PointOfInterests, this is handled
 * by treating the surroundings as a 3D environment in OpenGL.
 * 
 * @author Per Olav Flaten
 * 
 */
public class MarkerRenderer implements GLSurfaceView.Renderer {

	// Note that the triangle objects are hardcoded placeholder shapes to
	// reference positioning of
	// the camera in a 3D-environment, and should be removed once camera is
	// working smoothly, along
	// with implementation of dynamic marker drawing.
	private Triangle mTriangle;
	private Triangle otherTriangle;
	private Triangle thirdTriangle;
	private Triangle fourthTriangle;
	private Triangle fifthTriangle;
	private Triangle sixthTriangle;
	
	private LinearAlgebra linAlg;

	private float[] eye;
	private float[] center;
	private float[] up;

	private boolean isFirstFrame;

	public MarkerRenderer() {
		this.linAlg = new LinearAlgebra();
		this.eye = new float[3];
		this.center = new float[3];
		this.up = new float[3];
		this.isFirstFrame = true;
	}

	public void setEye(float eyeX, float eyeY, float eyeZ) {
		this.eye[0] = eyeX;
		this.eye[1] = eyeY;
		this.eye[2] = eyeZ;
	}

	public void setCenter(float centerX, float centerY, float centerZ) {
		this.center[0] = centerX;
		this.center[1] = centerY;
		this.center[2] = centerZ;
	}

	public void setUp(float upX, float upY, float upZ) {
		this.up[0] = upX;
		this.up[1] = upY;
		this.up[2] = upZ;
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		//Placeholder drawings
		float[] color1 = { 1.0f, 0.0f, 0.0f, 1.0f }; // red
		float[] color2 = { 0.0f, 1.0f, 0.0f, 1.0f }; // green
		float[] color3 = { 0.0f, 0.0f, 1.0f, 1.0f }; // blue
		float[] color4 = { 1.0f, 0.0f, 1.0f, 1.0f }; // purple
		float[] color5 = { 1.0f, 1.0f, 0.0f, 1.0f }; // yellow
		float[] color6 = { 1.0f, 1.0f, 1.0f, 1.0f }; // white

		this.mTriangle = new Triangle(color1);
		this.otherTriangle = new Triangle(color2);
		this.thirdTriangle = new Triangle(color3);
		this.fourthTriangle = new Triangle(color4);
		this.fifthTriangle = new Triangle(color5);
		this.sixthTriangle = new Triangle(color6);
		//Placeholder drawings
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		if (isFirstFrame) {
			this.linAlg.initCameraView(0, 0, 0, 0, 0, 50, 0, 1, 0);
			isFirstFrame = false;
		}
		
		this.linAlg.rotateWorld(center[0], center[1], center[2]);

		this.linAlg.drawPointOfInterest(this.mTriangle, -50, 0, 0);
		this.linAlg.drawPointOfInterest(this.otherTriangle, 50, 0, 0);
		this.linAlg.drawPointOfInterest(this.thirdTriangle, 0, 0, -50);
		this.linAlg.drawPointOfInterest(this.fourthTriangle, 0, 0, 50);
		this.linAlg.drawPointOfInterest(this.fifthTriangle, 0, -50, 0);
		this.linAlg.drawPointOfInterest(this.sixthTriangle, 0, 50, 0);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		this.linAlg.initCameraLens(width, height);
	}

	public static int loadShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}

	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e("no.kystverket.lommeradaren", glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

}
