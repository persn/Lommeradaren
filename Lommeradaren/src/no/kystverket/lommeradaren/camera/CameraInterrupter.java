package no.kystverket.lommeradaren.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CameraInterrupter extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent){
		abortBroadcast();
		//Rest of the method left blank on purpose, since we only want
		//this class to catch broadcasts to the hardware camera button
		//and stop calls to the unit default camera app.
	}
}
