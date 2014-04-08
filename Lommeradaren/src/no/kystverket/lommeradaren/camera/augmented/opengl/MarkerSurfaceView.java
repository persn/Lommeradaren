package no.kystverket.lommeradaren.camera.augmented.opengl;

import no.kystverket.lommeradaren.MarkerDialogFragment;
import java.util.List;
import no.kystverket.lommeradaren.markers.DataSourceHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.location.Location;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

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

		this.mRenderer = new MarkerRenderer(getContext());
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

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			MarkerWrapper[] markerCluster = this.mRenderer.getMarkerCluster(x,
					y);
			this.displayMarkerClusterDialog(markerCluster);
			break;
		}
		return true;
	}

	public void set3DMarkerData(DataSourceHandler dataSourceHandler, Location myLocation) {
		this.mRenderer.set3DMarkerList(dataSourceHandler, myLocation);
	}

//	public void setCurrentLocation(DataSourceHandler datasourceHandler) {
//		this.mRenderer.setDataSourceHandler(datasourceHandler);
//	}

	public void setRendererScreenSize(int width, int height) {
		this.mRenderer.setScreenSize(width, height);
	}

	private void displayMarkerClusterDialog(final MarkerWrapper[] markerCluster) {
		if (markerCluster.length > 0) { // Avoid displaying blank dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle("Marker Cluster");
			builder.setCancelable(true);
			builder.setItems(this.buildClusterDialogOptions(markerCluster),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							FragmentManager fm = ((Activity) getContext()).getFragmentManager();
							DialogFragment newFragment = new MarkerDialogFragment();
							((MarkerDialogFragment)newFragment).setContent(
									markerCluster[which].getTag()[0], 
									"" + markerCluster[which].getPOI().getLat(), 
									"" + markerCluster[which].getPOI().getLng(),
									"" + markerCluster[which].getPOI().getAlt(),
									markerCluster[which].getPOI().getImo(),
									markerCluster[which].getPOI().getMmsi(),
									markerCluster[which].getPOI().getSpeed(),
									markerCluster[which].getPOI().getPositionTime(),
									markerCluster[which].getPOI().getWebpage());
							newFragment.show(fm, "marker_dialog");
						}
					});
			builder.create().show();
		}
	}

	private CharSequence[] buildClusterDialogOptions(
			MarkerWrapper[] markerCluster) {
		CharSequence[] options = new CharSequence[markerCluster.length];
		for (int i = 0; i < options.length; i++) {
			options[i] = markerCluster[i].getTag()[0];
		}
		return options;
	}
	
	public List<MarkerWrapper> getMarkerList(){
		return mRenderer.getMarkerList();
	}

}
