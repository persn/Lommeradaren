package no.kystverket.lommeradaren.camera.augmented.opengl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class OpenGLES20Activity extends Activity {

	private GLSurfaceView mGLView;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//this.mGLView = new MyGLSurfaceView(this);		
		setContentView(this.mGLView);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		this.mGLView.onPause();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		this.mGLView.onResume();
	}
	
}
