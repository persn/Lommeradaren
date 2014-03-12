package no.kystverket.lommeradaren.camera.augmented.opengl;

import android.content.Context;
import android.graphics.PixelFormat;
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

		this.mRenderer = new MarkerRenderer(getContext());
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		this.setRenderer(this.mRenderer);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	public void getSensorData(float[] orientation) {
			this.mRenderer.setEye(0, 0, 0);
			this.mRenderer.setCenter((float)Math.cos(orientation[0]), 0, (float)Math.sin(orientation[0]));
			this.mRenderer.setUp(0, 1, 0);

			this.requestRender();
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
