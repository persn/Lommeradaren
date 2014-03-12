package no.kystverket.lommeradaren.camera.augmented.opengl;

import no.kystverket.lommeradaren.markers.DataSourceCollection;
import no.kystverket.lommeradaren.markers.LocationHandler;
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

	public MarkerSurfaceView(Context context, AttributeSet att) {
		super(context, att);
		this.setEGLContextClientVersion(2);

		this.mRenderer = new MarkerRenderer();
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		this.setRenderer(this.mRenderer);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	public void getSensorData(float[] orientation) {
		this.mRenderer.setEye(0, 0, 0);
		// Camera lookAt point x-, and z- axis is decided with compass
		// directions and Unit circle
		this.mRenderer.setCenter((float) Math.cos(orientation[0]), 0,
				(float) Math.sin(orientation[0]));
		this.mRenderer.setUp(0, 1, 0);

		this.requestRender();
	}

	public void setDataSourceCollection(
			DataSourceCollection dataSourceCollection) {
		this.mRenderer.setDataSourceCollection(dataSourceCollection);
	}
	
	public void setCurrentLocation(LocationHandler currentLocation){
		this.mRenderer.setLocationHandler(currentLocation);
	}

}
