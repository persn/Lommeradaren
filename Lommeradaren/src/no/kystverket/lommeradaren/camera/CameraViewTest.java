package no.kystverket.lommeradaren.camera;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraViewTest extends SurfaceView implements SurfaceHolder.Callback {
	
	private SurfaceHolder mHolder;
    private Camera mCamera;
    
    private List<Camera.Size> mSupportedPreviewSizes;
    private Camera.Size mPreviewSize;

	public CameraViewTest(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.mHolder = getHolder();
		this.setCamera();
		this.mHolder.addCallback(this);
		
		this.mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
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
			Log.d("Width","" + width);
			Log.d("Height","" + height);
			Camera.Parameters parameters = this.mCamera.getParameters();
			parameters.setPreviewSize(this.mPreviewSize.width, this.mPreviewSize.height);
			
			Log.d("OptWidth","" + this.mPreviewSize.width);
			Log.d("OptHeight","" + this.mPreviewSize.height);
			this.mCamera.setParameters(parameters);
			this.mCamera.setPreviewDisplay(this.mHolder);
			this.mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * http://stackoverflow.com/questions/19577299/android-camera-preview-stretched
	 */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(this.getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(this.getSuggestedMinimumHeight(), heightMeasureSpec);

        if (this.mSupportedPreviewSizes != null) {
            this.mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
            Log.d("TEst","1");
        }

        float ratio;
        if(this.mPreviewSize.height >= this.mPreviewSize.width)
            ratio = (float) this.mPreviewSize.height / (float) this.mPreviewSize.width;
        else
            ratio = (float) this.mPreviewSize.width / (float) this.mPreviewSize.height;

        // One of these methods should be used, second method squishes preview slightly
        this.setMeasuredDimension(width, (int) (width * ratio));
//        setMeasuredDimension((int) (width * ratio), height);
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

        if (cameraPreviewSizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Camera.Size size : cameraPreviewSizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) 
            	continue;
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
