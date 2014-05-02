package no.kystverket.lommeradaren.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import no.kystverket.lommeradaren.R;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Camera mCamera;
	private PictureCallback mPictureCallback;

	private OnPhotoTakenListener mOnPhotoTakenListener;

	private static final int MEDIA_TYPE_IMAGE = 1;

	// private static final int MEDIA_TYPE_VIDEO = 2;

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.mHolder = getHolder();
		this.setCamera();
		this.mHolder.addCallback(this);
		this.mPictureCallback = this.initPictureCallback();
	}

	public interface OnPhotoTakenListener {
		public void onPhotoTaken(String imgPath);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (this.mCamera != null) {
				this.mCamera.setPreviewDisplay(this.mHolder);
				this.mCamera.startPreview();
			} else {
				this.setCamera();
				this.surfaceCreated(holder); // Re-attempt camera preview
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mHolder.getSurface() == null) {
			return;
		}

		try {
			this.mCamera.stopPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Camera.Parameters parameters = this.mCamera.getParameters();
			parameters.setPreviewSize(width, height);

			this.mCamera.setParameters(parameters);
			this.mCamera.setPreviewDisplay(this.mHolder);
			this.mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.releaseCamera();
	}

	public void setOnPhotoTakenListener(OnPhotoTakenListener listener) {
		this.mOnPhotoTakenListener = listener;
	}

	public void snapPicture() {
		try {
			this.mCamera.takePicture(null, null, this.mPictureCallback);
			android.os.SystemClock.sleep(2000);
			this.mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void autoFocusAndSnapPicture() {

		this.mCamera.autoFocus(new Camera.AutoFocusCallback() {

			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				snapPicture();
			}
		});
	}

	private void setCamera() {
		releaseCamera(); // Make sure camera is not already set.
		this.mCamera = Camera.open();
	}

	private void releaseCamera() {
		if (this.mCamera != null) {
			this.mCamera.stopPreview();
			this.mCamera.setPreviewCallback(null);
			this.mCamera.release();
			this.mCamera = null;
		}
	}

	private PictureCallback initPictureCallback() {

		PictureCallback mPicture = new PictureCallback() {

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {

				File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE,
						getContext().getString(R.string.app_name));
				if (pictureFile == null) {
					return;
				}
				try {
					FileOutputStream fos = new FileOutputStream(pictureFile);
					fos.write(data);
					fos.close();
					mOnPhotoTakenListener.onPhotoTaken(pictureFile.getPath());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		};
		return mPicture;
	}

	private static File getOutputMediaFile(int type, String appName) {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				appName);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("ddMMyy_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
			// } else if (type == MEDIA_TYPE_VIDEO) {
			// mediaFile = new File(mediaStorageDir.getPath() + File.separator
			// + "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

}
