package no.kystverket.lommeradaren.camera;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraViewTest extends SurfaceView implements SurfaceHolder.Callback {
	
	private SurfaceHolder mHolder;
    private Camera mCamera;

	public CameraViewTest(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.mHolder = getHolder();
		this.setCamera();
		this.mHolder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if(this.mCamera != null){
				this.mCamera.setPreviewDisplay(this.mHolder);
				this.mCamera.startPreview();
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
			Log.d("Tippelipp","Tippelipp");
			parameters.setPreviewSize(width, height);
			this.mCamera.setParameters(parameters);
			this.mCamera.setPreviewDisplay(mHolder);
			this.mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.releaseCamera();
	}
	
	private void setCamera() {
		releaseCamera(); //Make sure camera is not already set.
		this.mCamera = Camera.open();
	}
	
	private void releaseCamera() {
		if(this.mCamera != null){
			this.mCamera.stopPreview();
			this.mCamera.setPreviewCallback(null);
			this.mCamera.release();
			this.mCamera = null;
		}
	}

}
