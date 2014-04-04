package no.kystverket.lommeradaren.camera.augmented.opengl;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import no.kystverket.lommeradaren.camera.augmented.math.LinearAlgebra;
import no.kystverket.lommeradaren.camera.augmented.opengl.sprites.GLText;
import no.kystverket.lommeradaren.markers.DataSourceHandler;
import no.kystverket.lommeradaren.markers.LocationHandler;
import no.kystverket.lommeradaren.markers.POI;
import android.content.Context;
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

	private DataSourceHandler datasourceHandler;
	// private LocationHandler locationHandler;
	private List<MarkerWrapper> markerWrappers = new ArrayList<MarkerWrapper>();

	private LinearAlgebra linAlg;
	private GLText glText;
	private Context context;

	private float[] eye;
	private float[] center;
	private float[] up;
	private int screenWidth;
	private int screenHeight;

	private static final int TOUCH_RADIUS = 50;

	public MarkerRenderer(Context context) {
		this.context = context;
		this.linAlg = new LinearAlgebra();
		this.eye = new float[3];
		this.center = new float[3];
		this.up = new float[3];
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
		glText = new GLText(context.getAssets());
		glText.load("Roboto-Regular.ttf", 32, 2, 2);

		glText.setScale(0.05f);
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		this.linAlg.initCameraView(eye[0], eye[1], eye[2], center[0],
				center[1], center[2], up[0], up[1], up[2]);

		float[] color1 = { 1.0f, 0.0f, 0.0f, 1.0f }; // red
		float[] color2 = { 0.0f, 1.0f, 0.0f, 1.0f }; // green
		float[] color3 = { 0.0f, 0.0f, 1.0f, 1.0f }; // blue
		float[] color4 = { 1.0f, 0.0f, 1.0f, 1.0f }; // purple

		this.linAlg.drawMarker(glText, color1, "North", 0, 0, -50);
		this.linAlg.drawMarker(glText, color2, "South", 0, 0, 50);
		this.linAlg.drawMarker(glText, color3, "East", 50, 0, 0);
		this.linAlg.drawMarker(glText, color4, "West", -50, 0, 0);

		for (MarkerWrapper markerWrapper : markerWrappers) {
			float x = markerWrapper.getCartesianCoordinates()[0];
			float y = markerWrapper.getCartesianCoordinates()[1];
			float z = markerWrapper.getCartesianCoordinates()[2];
			markerWrapper.setScreenCoordinates(this.linAlg.findPointOfInterestScreenPosition(markerWrapper.getCartesianCoordinates(),this.screenWidth, this.screenHeight));
			this.linAlg.drawMarker(glText, color3, "POI", x, y, z);
		}

		this.drawAllMarkers();
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
//		try{
//		int error;
//		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
//			Log.e("no.kystverket.lommeradaren", glOperation + ": glError "
//					+ error);
//		}
//	}catch(RuntimeException e){
//		e.printStackTrace();
//	}
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e("no.kystverket.lommeradaren", glOperation + ": glError "
					+ error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	public void setDataSourceHandler(DataSourceHandler datasourceHandler) {
		this.datasourceHandler = datasourceHandler;
	}

	// public void setLocationHandler(LocationHandler locationHandler) {
	// this.locationHandler = locationHandler;
	// }

	public void setScreenSize(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
	}

	public MarkerWrapper[] getMarkerCluster(float touchX, float touchY) {
		List<MarkerWrapper> markerCluster = new ArrayList<MarkerWrapper>();
		int clusterElementCount = 0;
		for (MarkerWrapper markerWrapper : markerWrappers) {
			int markerScreenX = markerWrapper.getScreenCoordinates()[0];
			int markerScreenY = markerWrapper.getScreenCoordinates()[1];
			if ((markerScreenX > touchX - TOUCH_RADIUS)
					&& (markerScreenX < touchX + TOUCH_RADIUS)
					&& (markerScreenY > touchY - TOUCH_RADIUS)
					&& (markerScreenY < touchY + TOUCH_RADIUS)) {
				markerCluster.add(markerWrapper);
				clusterElementCount++;
			}
		}
		return markerCluster.toArray(new MarkerWrapper[clusterElementCount]);
	}

	private void drawAllMarkers() {
		if (this.datasourceHandler != null) {
			for (int i = 0; i < this.datasourceHandler
					.getPointOfInterestsSize(); i++) {
				POI poi = this.datasourceHandler.getPOI(i);
				// Log.d("ShipName",this.datasourceCollection.getPOI(i,
				// j).getName());
				// Log.d("DistanceAltitude","" +
				// RelativePosition.getAltitudeDifference((float)locationHandler.getLocation().getAltitude(),
				// (float)poi.getAlt()));
				// Log.d("DistanceLatitude","" +
				// RelativePosition.getDifference((float)locationHandler.getLocation().getLatitude(),
				// (float)poi.getLat()));
				// Log.d("DistanceLongitude","" +
				// RelativePosition.getDifference((float)locationHandler.getLocation().getLongitude(),
				// (float)poi.getLng()));
			}
		}
	}
}
