package no.kystverket.lommeradaren.camera.augmented.opengl;

import no.kystverket.lommeradaren.markers.DataSourceCollection;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * 
 * @author Per Olav Flaten
 *
 */
public class MarkerSurfaceView extends GLSurfaceView {

	private MarkerRenderer mRenderer;
	private float[] vectorBuffer = { 0.0f, 0.0f, 0.0f };

	public MarkerSurfaceView(Context context, AttributeSet att) {
		super(context, att);
		this.setEGLContextClientVersion(2);
		
		this.mRenderer = new MarkerRenderer();
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		this.setRenderer(this.mRenderer);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	public void getSensorData(float[] rotationVector) {
			float quaternion[] = new float[4];
			float[] rotationMatrix = new float[16];
			float orientation[] = new float[3];

			filterVector(0.8f, rotationVector);
			SensorManager.getQuaternionFromVector(quaternion, rotationVector);
			SensorManager.getRotationMatrixFromVector(rotationMatrix,
					quaternion);
			SensorManager.remapCoordinateSystem(rotationMatrix,
					SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X,
					rotationMatrix);
			SensorManager.getOrientation(rotationMatrix, orientation);

			orientation[0] = (float) Math.toDegrees(orientation[0]);
			orientation[1] = (float) Math.toDegrees(orientation[1]);
			orientation[2] = (float) Math.toDegrees(orientation[2]);

			this.mRenderer.setEye(0, 0, 0);
			this.mRenderer.setCenter(orientation[0], orientation[1],
					orientation[2]);
			this.mRenderer.setUp(0, 1, 0);

			this.requestRender();
	}
	
	public void setDataSourceCollection(DataSourceCollection dataSourceCollection){
		this.mRenderer.setDataSourceCollection(dataSourceCollection);
	}

	/**
	 * Low-pass filter might not be the solution for smoothening data, or might
	 * need an alternative implementation. Since the idea is that what we
	 * compare the result with the previous buffered value, it gives off strange
	 * results when sudden movements are made. Example: If I suddenly turn my
	 * phone so that the azimuth, pitch or roll exceeds their given
	 * interval(From 360 to 5), the buffered value is completely incosistent
	 * with the new value. Taking this into consideration the coordinate system
	 * doesn't turn when I want it to, and continues in the direction it was
	 * already moving.
	 * 
	 * @param alpha
	 * @param vectorToFilter
	 */
	public void filterVector(float alpha, float[] vectorToFilter) {
		for (int i = 0; i < vectorToFilter.length; i++) {
			vectorToFilter[i] = alpha * vectorToFilter[i] + (1 - alpha)
					* this.vectorBuffer[i];
			this.vectorBuffer[i] = vectorToFilter[i];
		}
	}

}
