package no.kystverket.lommeradaren.camera.augmented.math;

import no.kystverket.lommeradaren.camera.augmented.opengl.sprites.GLText;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

/**
 * A class for handling matrices in a 3-Dimensional environment with Augmented
 * Reality.
 * 
 * @author Per Olav Flaten
 * 
 */
public class LinearAlgebra {

	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
	private float[] mTempMatrix = new float[16];

	/**
	 * DEPENDING ON CUSTOM AXISES THE ORDER MIGHT NEED TO BE CHANGED
	 * http://www.songho.ca/opengl/gl_anglestoaxes.html XYZ shows the world
	 * sideways, top and bottom are on each side XZY sideways YXZ also sideways,
	 * but different perspective YZX top bottom still not correct ZXY TOP
	 * FINALLY CORRECT, but it seems to spinning in the opposite direction of x
	 * or z ZYX Sideways again
	 * 
	 * @param rotationX
	 * @param rotationY
	 * @param rotationZ
	 */
	public void rotateWorld(float rotationX, float rotationY, float rotationZ) {
		float[] rotationMatrixX = new float[16];
		float[] rotationMatrixY = new float[16];
		float[] rotationMatrixZ = new float[16];

		Matrix.setIdentityM(mTempMatrix, 0);

		Matrix.setRotateM(rotationMatrixX, 0, rotationX, 1, 0, 0);
		Matrix.setRotateM(rotationMatrixY, 0, rotationY, 0, 1, 0);
		Matrix.setRotateM(rotationMatrixZ, 0, rotationZ, 0, 0, 1);

		Matrix.multiplyMM(mTempMatrix, 0, rotationMatrixX, 0, mTempMatrix, 0);
		Matrix.multiplyMM(mTempMatrix, 0, rotationMatrixY, 0, mTempMatrix, 0);
		Matrix.multiplyMM(mTempMatrix, 0, rotationMatrixZ, 0, mTempMatrix, 0);

		Matrix.multiplyMM(mTempMatrix, 0, mViewMatrix, 0, mTempMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mTempMatrix, 0);
	}

	public void drawMarker(GLText glText, float[] color, String text, String dist, float posX, float posY, float posZ) {
		float degRot = (float) Math.toDegrees(Math.atan2((double) posX,
				(double) -posZ));
		float[] drawMatrix = new float[16];
		float[] rotationMatrixTest = new float[16];

		Matrix.setIdentityM(drawMatrix, 0);
		Matrix.setIdentityM(rotationMatrixTest, 0);

		Matrix.translateM(drawMatrix, 0, posX, posY, posZ);
		Matrix.multiplyMM(drawMatrix, 0, this.mMVPMatrix, 0, drawMatrix, 0);
		Matrix.rotateM(rotationMatrixTest, 0, -degRot, 0, 1, 0);
		Matrix.multiplyMM(drawMatrix, 0, drawMatrix, 0, rotationMatrixTest, 0);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		glText.drawMarker(128, 128, color, drawMatrix);
		glText.begin(1.0f, 1.0f, 1.0f, 1.0f, drawMatrix);
		glText.drawC(text, 0, 0, 0, 0, -0, 0);
		glText.drawC(dist, 0, -2, 0, 0, -0, 0);
		glText.end();
		
		GLES20.glDisable(GLES20.GL_BLEND); 
	}

	public void initCameraView(float eyeX, float eyeY, float eyeZ,
			float centerX, float centerY, float centerZ, float upX, float upY,
			float upZ) {
		// http://webglfactory.blogspot.no/2011/05/how-to-convert-world-to-screen.html
		Matrix.setLookAtM(this.mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX,
				centerY, centerZ, upX, upY, upZ);
		Matrix.multiplyMM(this.mMVPMatrix, 0, this.mProjectionMatrix, 0,
				this.mViewMatrix, 0);
	}

	public int[] findPointOfInterestScreenPosition(
			float[] cartesianCoordinates, int screenWidth, int screenHeight) {
		float[] vector = { cartesianCoordinates[0], cartesianCoordinates[1],
				cartesianCoordinates[2], 1f };
		Matrix.multiplyMV(vector, 0, this.mMVPMatrix, 0, vector, 0);
		if (vector[3] > 1) {
			vector = new float[] { // Divide x, y, and z by w
			vector[0] / vector[3], vector[1] / vector[3],
					vector[2] / vector[3], };
		}
		vector = new float[] { // Divide x and y by z
		vector[0] / vector[2], vector[1] / vector[2], };
		int[] pixelVector = { Math.round(((vector[0] + 1) / 2) * screenWidth),
				Math.round(((1 - vector[1]) / 2) * screenHeight) };
		return pixelVector;
	}

	public void initCameraLens(int width, int height) {
		float ratio = (float) width / height;
		// float ratio2 = (float) height / width;
		// Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -ratio2, ratio2,
		// 2, 360);
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 100);
	}

	/**
	 * For debugging purposes only.
	 * 
	 * @param tag
	 *            Unique log-identifier for easy finding
	 * @param matrix
	 *            A 16 length float array representing a matrix
	 * @deprecated
	 */
	@SuppressWarnings("unused")
	private void logMatrix(String tag, float[] matrix) {
		Log.d(tag + "1", "[" + matrix[0] + " " + matrix[1] + " " + matrix[2]
				+ " " + matrix[3] + "]");
		Log.d(tag + "2", "[" + matrix[4] + " " + matrix[5] + " " + matrix[6]
				+ " " + matrix[7] + "]");
		Log.d(tag + "3", "[" + matrix[8] + " " + matrix[9] + " " + matrix[10]
				+ " " + matrix[11] + "]");
		Log.d(tag + "4", "[" + matrix[12] + " " + matrix[13] + " " + matrix[14]
				+ " " + matrix[15] + "]");
	}

}
