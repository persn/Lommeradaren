package no.kystverket.lommeradaren.camera.augmented.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import no.kystverket.lommeradaren.camera.augmented.RotationVectorSensor;
import no.kystverket.lommeradaren.camera.augmented.opengl.texture.Triangle;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "MyGLRenderer";

	private Triangle mTriangle;
	private Triangle otherTriangle;
	private Triangle thirdTriangle;
	private Triangle fourthTriangle;
	private Triangle fifthTriangle;
	private Triangle sixthTriangle;

	private final float[] mMVPMatrix = new float[16];
	private float[] rotationMatrix = new float[16];

	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];

	private float[] mTempMatrix = new float[16];
	private float[] mTranslationMatrix = new float[16];
	private final float[] mRotationMatrix = new float[16];

	private RotationVectorSensor rotationSensor;
	private float mAngle;

	private float[] eye;
	private float[] center;
	private float[] up;

	private float[] rotationMatrixX = new float[16];
	private float[] rotationMatrixY = new float[16];
	private float[] rotationMatrixZ = new float[16];

	private float[] modelViewMatrix = new float[16];

	private boolean isFirstFrame;

	float DEG2RAD = (float) Math.PI / 180;
	int counter = 0;

	public MyGLRenderer() {
		this.eye = new float[3];
		this.center = new float[3];
		this.up = new float[3];
		this.isFirstFrame = true;
	}

	public void setRotationMatrix(float[] rot) {
		rotationMatrix = rot;
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

	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		if (isFirstFrame) {
			Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 0, 0, 0, 50, 0, 1, 0);
			// Matrix.setLookAtM(mViewMatrix, 0, 25, 50, 50, center[0],
			// center[1], center[2], up[0], up[1], up[2]);
			Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix,
					0);
			isFirstFrame = true;
		}

		// Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix,
		// 0);

		Matrix.setIdentityM(mTempMatrix, 0);

		Matrix.setRotateM(rotationMatrixZ, 0, center[0], 0, 0, 1);
		Matrix.setRotateM(rotationMatrixX, 0, center[1], 1, 0, 0);
		Matrix.setRotateM(rotationMatrixY, 0, center[2], 0, 1, 0);

		// DEPENDING ON CUSTOM AXISES THE ORDER MIGHT NEED TO BE CHANGED
		// http://www.songho.ca/opengl/gl_anglestoaxes.html
		// XYZ shows the world sideways, top and bottom are on each side
		// XZY sideways
		// YXZ also sideways, but different perspective
		// YZX top bottom still not correct
		// ZXY TOP FINALLY CORRECT, but it seems to spinning in the opposite
		// direction of x or z
		// ZYX Sideways again
		Matrix.multiplyMM(mTempMatrix, 0, rotationMatrixZ, 0, mTempMatrix, 0);
		Matrix.multiplyMM(mTempMatrix, 0, rotationMatrixX, 0, mTempMatrix, 0);
		Matrix.multiplyMM(mTempMatrix, 0, rotationMatrixY, 0, mTempMatrix, 0);

		Matrix.multiplyMM(modelViewMatrix, 0, mViewMatrix, 0, mTempMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, modelViewMatrix,
				0);

		drawPointOfInterest(this.mTriangle, -50, 0, 0);
		drawPointOfInterest(this.otherTriangle, 50, 0, 0);
		drawPointOfInterest(this.thirdTriangle, 0, 0, -50);
		drawPointOfInterest(this.fourthTriangle, 0, 0, 50);
		drawPointOfInterest(this.fifthTriangle, 0, -50, 0);
		drawPointOfInterest(this.sixthTriangle, 0, 50, 0);

		// drawPointOfInterest(this.square1, -10, 0, 0);
		// drawPointOfInterest(this.square2, 10, 0, 0);
		// drawPointOfInterest(this.square3, 0, 0, -10);
		// drawPointOfInterest(this.square4, 0, 0, 10);
		// drawPointOfInterest(this.square5, 0, -10, 0);
		// drawPointOfInterest(this.square6, 0, 10, 0);

//		// Matrix.setRotateM(mRotationMatrix, 0, 3, 1, 0, 0);
//		// Matrix.multiplyMM(mMVPMatrix, 0, mMVPMatrix, 0, mRotationMatrix, 0);
//
//		// Matrix.rotateM(mMVPMatrix, 0, center[2], 0, 1, 0);
//		// Matrix.rotateM(mMVPMatrix, 0, center[1], 1, 0, 0);
//		// Matrix.rotateM(mMVPMatrix, 0, center[0], 0, 0, 1);

	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;
		float ratio2 = (float) height / width;
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -ratio2, ratio2,
				2, 360);
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
			Log.e(TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	@Deprecated
	public void moveCamera(float eyeX, float eyeY, float eyeZ, float centerX,
			float centerY, float centerZ, float upX, float upY, float upZ) {
		Matrix.setIdentityM(this.mViewMatrix, 0);
		Matrix.setLookAtM(this.mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX,
				centerY, centerZ, upX, upY, upZ);
		this.mTempMatrix = this.mViewMatrix.clone();
		Matrix.multiplyMM(this.mMVPMatrix, 0, this.mTempMatrix, 0,
				this.mViewMatrix, 0);
	}

	private void drawPointOfInterest(Triangle pointOfInterest, float x,
			float y, float z) {
		float[] drawMatrix = new float[16];
		Matrix.setIdentityM(drawMatrix, 0);
		Matrix.translateM(drawMatrix, 0, x, y, z);
		rotatePointOfInterest(drawMatrix, 0, 1, 1, 1);
		Matrix.multiplyMM(drawMatrix, 0, this.mMVPMatrix, 0,
				drawMatrix, 0);
		pointOfInterest.draw(drawMatrix);
	}

	private void rotatePointOfInterest(float[] originMatrix, float angle,
			float x, float y, float z) {
		float[] rotationMatrixTest = new float[16];
		long time = SystemClock.uptimeMillis() % 4000L;
	    angle = 0.090f * ((int) time);
		Matrix.setIdentityM(rotationMatrixTest, 0);
		Matrix.rotateM(rotationMatrixTest, 0, angle, x, y, z);
		Matrix.multiplyMM(originMatrix, 0, originMatrix, 0, rotationMatrixTest, 0);
	}

	/**
	 * Returns the rotation angle of the triangle shape (mTriangle).
	 * 
	 * @return - A float representing the rotation angle.
	 */
	public float getAngle() {
		return mAngle;
	}

	/**
	 * Sets the rotation angle of the triangle shape (mTriangle).
	 */
	public void setAngle(float angle) {
		mAngle = angle;
	}

}
