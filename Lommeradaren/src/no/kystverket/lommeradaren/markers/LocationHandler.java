package no.kystverket.lommeradaren.markers;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Class handling access to googles location based services
 * 
 * @author Henrik Reitan
 * 
 */
public class LocationHandler implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	private LocationClient lc;
	private Location curLoc;
	private LocationRequest lr;
	private final static int UPDATE_INTERVAL = 5000 * 30;

	public LocationHandler(Context context) {
		this.lc = new LocationClient(context, this, this);
		lc.connect();
		lr = LocationRequest.create();
		lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		lr.setInterval(UPDATE_INTERVAL);
	}

	public Location getLocation() {
		if (curLoc != null) {
			return curLoc;
		} else {
			Location dummy = new Location("LastResort");
			dummy.setLatitude(63);
			dummy.setLongitude(10);
			return dummy;
		}
	}

	public String getLongtitude() {
		return "" + this.getLocation().getLongitude();
	}

	public String getLatitude() {
		return "" + this.getLocation().getLatitude();
	}

	public String getAltitude() {
		return "" + this.getLocation().getAltitude();
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
		this.curLoc = lc.getLastLocation();
	}

	@Override
	public void onDisconnected() {
		lc.removeLocationUpdates(this);
	}

	@Override
	public void onLocationChanged(Location loc) {
		curLoc = loc;
	}
}