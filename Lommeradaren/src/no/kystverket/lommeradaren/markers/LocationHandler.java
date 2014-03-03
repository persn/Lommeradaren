package no.kystverket.lommeradaren.markers;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationHandler implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	private LocationClient lc;
	private Location curLoc;
	private LocationRequest lr;
	private int updateInterval = 1000 * 10;

	public LocationHandler(Context context) {
		this.lc = new LocationClient(context, this, this);
		lc.connect();
		lr = LocationRequest.create();
		lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		lr.setInterval(updateInterval);
	}

	public Location getLocation() {
		if (curLoc != null) {
			return curLoc;
		} else {
			return lc.getLastLocation();
		}
	}

	public void stop() { // Call when the parent activity stops. Create new LH
							// object in the activity on resume and restart.
		if (lc.isConnected()) {
			lc.removeLocationUpdates(this);
			lc.disconnect();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		lc.requestLocationUpdates(lr, this);
	}

	@Override
	public void onDisconnected() {
		lc.removeLocationUpdates(this);
		Log.d("LocHandler onDisconnected", "placeholder");

	}

	@Override
	public void onLocationChanged(Location loc) {
		curLoc = loc;
		Log.d("LocHandler onLocationChanged", curLoc.toString());
	}
}