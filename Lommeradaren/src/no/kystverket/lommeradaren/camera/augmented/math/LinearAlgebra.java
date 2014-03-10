package no.kystverket.lommeradaren.camera.augmented.math;

import no.kystverket.lommeradaren.camera.augmented.opengl.texture.Triangle;
import android.opengl.Matrix;
import android.os.SystemClock;

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
	 * http://www.songho.ca/opengl/gl_anglestoaxes.html
	 * XYZ shows the world sideways, top and bottom are on each side
	 * XZY sideways
	 * YXZ also sideways, but different perspective
	 * YZX top bottom still not correct
	 * ZXY TOP FINALLY CORRECT, but it seems to spinning in the opposite
	 * direction of x or z
	 * ZYX Sideways again
	 * @param rotationX
	 * @param rotationY
	 * @param rotationZ
	 */
	public void rotateWorld(float rotationX, float rotationY, float rotationZ){
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
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mTempMatrix,0);
	}
	
	public void drawPointOfInterest(Triangle pointOfInterest, float x,
			float y, float z) {
		float[] drawMatrix = new float[16];
		Matrix.setIdentityM(drawMatrix, 0);
		Matrix.translateM(drawMatrix, 0, x, y, z);
		rotatePointOfInterest(drawMatrix, 0, 1, 1, 1);
		Matrix.multiplyMM(drawMatrix, 0, this.mMVPMatrix, 0, drawMatrix, 0);
		pointOfInterest.draw(drawMatrix);
	}

	public void initCameraView(float eyeX, float eyeY, float eyeZ,
			float centerX, float centerY, float centerZ, float upX, float upY,
			float upZ) {
		Matrix.setLookAtM(this.mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX,
				centerY, centerZ, upX, upY, upZ);
		Matrix.multiplyMM(this.mMVPMatrix, 0, this.mProjectionMatrix, 0,
				this.mViewMatrix, 0);
	}
	
	public void initCameraLens(int width, int height){
		float ratio = (float) width / height;
		float ratio2 = (float) height / width;
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -ratio2, ratio2,
				2, 360);
	}
	
	private void rotatePointOfInterest(float[] originMatrix, float angle,
			float x, float y, float z) {
		float[] rotationMatrixTest = new float[16];
		//Placeholder roation to make 2D objects visible from anywhere in the system.
		long time = SystemClock.uptimeMillis() % 4000L;
		angle = 0.090f * ((int) time);
		//Placeholder roation to make 2D objects visible from anywhere in the system.
		Matrix.setIdentityM(rotationMatrixTest, 0);
		Matrix.rotateM(rotationMatrixTest, 0, angle, x, y, z);
		Matrix.multiplyMM(originMatrix, 0, originMatrix, 0, rotationMatrixTest,
				0);
	}

}
