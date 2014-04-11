package no.kystverket.lommeradaren.user;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

public class GoogleClientActivity extends Activity implements
		ConnectionCallbacks, OnConnectionFailedListener {

	private static final int RC_SIGN_IN = 0;
	private boolean mResolveOnFail;
	private GoogleApiClient mGoogleClient;
	private ConnectionResult mConnectionResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.mGoogleClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		this.mResolveOnFail = false;
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.mGoogleClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (this.mGoogleClient.isConnected()) {
			this.mGoogleClient.disconnect();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			mResolveOnFail = false;
			if (!mGoogleClient.isConnecting()) {
				mGoogleClient.connect();
			}
		}
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d("Connection","Success");
	}

	@Override
	public void onConnectionSuspended(int cause) {
		this.mGoogleClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.d("Connection","Failed");
		if (result.hasResolution()) {
			this.mConnectionResult = result;
			if (this.mResolveOnFail) {
				this.startResolution();
			}
		}
	}

	private void startResolution() {
		try {
			this.mResolveOnFail = false;
			this.mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
		} catch (SendIntentException e) {
			this.mGoogleClient.connect();
		}
	}

}
