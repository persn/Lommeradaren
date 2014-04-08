package no.kystverket.lommeradaren.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * This class is built according to
 * http://developer.android.com/guide/topics/media/camera.html using Googles own
 * suggestion for building a Camera handling
 * 
 * @author Per Olav Flaten
 * 
 */
public class CameraController {

	private Camera camera;
	private PictureCallback mPicture;
	private String appName;

	private OnPhotoTakenListener dialogCallback;
	
	private boolean readyToTakePicture;

	private static final int MEDIA_TYPE_IMAGE = 1;
	private static final int MEDIA_TYPE_VIDEO = 2;

	/**
	 * The constructor initiates camera, picture storage handling, and sets
	 * appName.
	 * 
	 * @param appName
	 *            The application name, this parameter will decide the
	 *            sub-folder to store images taken with the camera
	 */
	public CameraController(String appName) {
		this.camera = getCameraInstance();
		this.mPicture = initPictureCallback();
		this.appName = appName;
	}

	/**
	 * Garbage collector for the camera.
	 */
	public void releaseCamera() {
		try {
			if (this.camera != null) {
				this.camera.stopPreview();
				this.camera.setPreviewCallback(null);
				this.camera.release();
				this.camera = null;
			}
		} catch (Exception e) {
			Log.d("no.kystverket", "Exception: " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Method for starting the camera view, to be used in Augmented Reality.
	 * 
	 * @param holder
	 */
	public void startCameraView(SurfaceHolder holder) {
		try {
			if (camera != null) {
				releaseCamera();
				this.camera = getCameraInstance();
				this.readyToTakePicture = true;
			}
		} catch (Exception e) {
			Log.d("no.kystverket",
					"Exception, CameraController.java startCameraView");
			e.printStackTrace();
		}
	}

	/**
	 * Used if android unit is flipped or the image is otherwise changed, and
	 * needs to be redrawn.
	 * 
	 * @param mHolder
	 */
	public void redrawCamera(SurfaceHolder mHolder, int width, int height) {
		if (mHolder.getSurface() == null) {
			return;
		}

		try {
			this.camera.stopPreview();
		} catch (Exception e) {
			Log.d("no.kystverket", "CameraController.java redrawSurface 1");
			e.printStackTrace();
		}

		try {
			Camera.Parameters parameters = this.camera.getParameters();
			parameters.setPreviewSize(width, height);
			this.camera.setParameters(parameters);
			this.camera.setPreviewDisplay(mHolder);
			this.camera.startPreview();
		} catch (Exception e) {
			Log.d("no.kystverket", "CameraController.java redrawSurface 2");
			e.printStackTrace();
		}
	}
	
//	@Override
//	public void onAttach(Activity activity){
//		super.onAttach(activity);
//		
//		try{
//			this.dialogCallback = (OnPhotoTakenListener)activity;
//		}catch(ClassCastException e){
//			e.printStackTrace();
//		}
//	}

	public void registerListener(OnPhotoTakenListener activity){
		this.dialogCallback = activity;
	}
	/**
	 * Snap a picture of the current frame on the android unit. It is set with a
	 * sleep and a if-check to ensure low-probability of crash if the user spam
	 * this action.
	 */
	public void snapPicture() {
		if (this.readyToTakePicture) {
			try {
				this.readyToTakePicture = false;
				this.camera.takePicture(null, null, this.mPicture);
				android.os.SystemClock.sleep(2000);
				this.camera.startPreview();
				this.readyToTakePicture = true;
			} catch (Exception e) {
				Log.d("no.kystverket",
						"Snapshot failed CameraController.java snapPicture()");
				e.printStackTrace();
			}
		}
	}

	public void autoFocusAndSnapPicture() {

		this.camera.autoFocus(new Camera.AutoFocusCallback() {

			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				snapPicture();
			}
		});
	}

	/**
	 * Initiator for the handler algorithm that saves a picture on file.
	 */
	private PictureCallback initPictureCallback() {

		PictureCallback mPicture = new PictureCallback() {

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {

				File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE, appName);
				if (pictureFile == null) {
					Log.d("no.kystverket",
							"Error creating media file CameraController.java initPictureCallback 1");
					return;
				}
				try {
					FileOutputStream fos = new FileOutputStream(pictureFile);
					fos.write(data);
					fos.close();
					dialogCallback.onPhotoTaken(pictureFile.getPath());
				} catch (FileNotFoundException e) {
					Log.d("no.kystverket",
							"FileNotFound CameraController.java initPictureCallback 2");
					e.printStackTrace();
				} catch (IOException e) {
					Log.d("no.kystverket",
							"CameraController.java initPictureCallback 3");
					e.printStackTrace();
				}

			}
		};
		return mPicture;
	}

	/**
	 * A safe way to get an instance of the Camera object according to
	 * http://developer.android.com/guide/topics/media/camera.html
	 * 
	 * @return An instance of the main camera on the android unit.
	 */
	private Camera getCameraInstance() {
		Camera c = null;
		try {
			releaseCamera();
			c = Camera.open();
		} catch (Exception e) {
			Log.d("no.kystverket", "CameraController.java getCameraInstance");
			e.printStackTrace();
		}
		return c;
	}

	/**
	 * Create a File for saving an image or video according to
	 * http://developer.android.com/guide/topics/media/camera.html.
	 */
	private static File getOutputMediaFile(int type, String appName) {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				appName);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("no.kystverket",
						"CameraController.java getOutputMediaFile");
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("ddMMyy_HHmm")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}
	
	public interface OnPhotoTakenListener{
		public void onPhotoTaken(String imgPath);
	}
}
