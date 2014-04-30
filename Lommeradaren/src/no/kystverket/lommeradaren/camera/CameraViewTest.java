package no.kystverket.lommeradaren.camera;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
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
			List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
			Camera.Size optimalSize = this.getOptimalPreviewSize(previewSizes, width, height);
			parameters.setPreviewSize(optimalSize.width, optimalSize.height);
			
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
	
	/**
	 * http://stackoverflow.com/questions/19577299/android-camera-preview-stretched
	 * 
	 * @param cameraPreviewSizes
	 * @param width
	 * @param height
	 * @return
	 */
	private Camera.Size getOptimalPreviewSize(List<Camera.Size> cameraPreviewSizes, int width, int height) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)height / width;

        if (cameraPreviewSizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Camera.Size size : cameraPreviewSizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : cameraPreviewSizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
