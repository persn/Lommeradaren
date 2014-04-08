package no.kystverket.lommeradaren.camera;

import no.kystverket.lommeradaren.camera.CameraController.OnPhotoTakenListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This class is taken from
 * http://developer.android.com/guide/topics/media/camera.html using Googles own
 * suggestion for building a Camera view in your application
 * 
 * In addition this class will contain code for GUI-objects in our custom
 * edition of the class MixView.java from the application Mixare. This so we can
 * separate the two products as far as it possibly goes.
 * 
 * @author Per Olav Flaten
 * 
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private CameraController cameraController;

	/**
	 * Coded in convention with Googles suggestion code
	 * http://developer.android.com/guide/topics/media/camera.html
	 */
	public CameraView(Context context, String appName) {
		super(context);
		this.cameraController = new CameraController(appName);

		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	/**
	 * Method for taking picture, see more in CameraController.java
	 */
	public void snapPicture() {
		this.cameraController.snapPicture();
	}
	
	public void autoFocusAndTakePicture(){
		this.cameraController.autoFocusAndSnapPicture();
	}
	
	public void registerOnPhotoTakenListener(OnPhotoTakenListener activity){
		this.cameraController.registerListener(activity);
	}

	/**
	 * Coded in convention with Googles suggestion code
	 * http://developer.android.com/guide/topics/media/camera.html
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		cameraController.startCameraView(holder);
	}

	/**
	 * Coded in convention with Googles suggestion code
	 * http://developer.android.com/guide/topics/media/camera.html
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.cameraController.releaseCamera();
	}

	/**
	 * Coded in convention with Googles suggestion code
	 * http://developer.android.com/guide/topics/media/camera.html In addition
	 * we send the screen size to the CameraController so that it stretches
	 * correctly to the android unit's screen. Note that normally you'd want to
	 * implement dynamic scaling to view components drawing, but since the
	 * camera screen should always be full screen, that step has been skipped.
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Activity host = (Activity) getContext();
		Display mainScreen = host.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		mainScreen.getSize(size);
		this.cameraController.redrawCamera(holder, size.x, size.y);
	}

}