package no.kystverket.lommeradaren.user;

import no.kystverket.lommeradaren.R;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;

public class GoogleClientActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener {

	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;

	private String pictureFileName;
	private String pictureFileData;
	private static final int RC_SIGN_IN = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.fragment_google_plu);

		this.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, null)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		this.pictureFileName = getIntent().getStringExtra(
				"picture-filename-web-upload");
		this.pictureFileData = getIntent().getStringExtra(
				"picture-filedata-web-upload");
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (this.mGoogleApiClient.isConnected()) {
			this.mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				this.mSignInClicked = false;
			}
			this.mIntentInProgress = false;
			if (!this.mGoogleApiClient.isConnecting()) {
				this.mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		this.renderButtonsOnSignedOut();
		if (!this.mIntentInProgress) {
			this.mConnectionResult = result;
			if (this.mSignInClicked) {
				this.resolveSignInError();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		this.mSignInClicked = false;
		this.renderButtonsOnSignedIn();

		if (this.mGoogleApiClient.isConnected()) {
			this.renderButtonsOnSignedIn();

			if (this.pictureFileName != null
					&& !this.pictureFileName.equals("")) {
				//TODO --- Scope should be retrieved from xml resources
				new StreamPictureTask(
						this,
						Plus.AccountApi.getAccountName(mGoogleApiClient),
						"audience:server:client_id:413624543866-kailen70lui2e56nufddv72is61qr29e.apps.googleusercontent.com",
						this.pictureFileName, this.pictureFileData).execute();
				setResult(RESULT_OK);
				finish();
			}
		} else {
			this.renderButtonsOnSignedOut();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		this.mGoogleApiClient.connect();
	}

	public void loginGoogleOnClick(View view) {
		if (!this.mGoogleApiClient.isConnecting()) {
			this.mSignInClicked = true;
			resolveSignInError();
		}
	}

	public void signoutGoogleOnClick(View view) {
		if (this.mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(this.mGoogleApiClient);
			this.mGoogleApiClient.disconnect();
			this.mGoogleApiClient.connect();
		}
	}

	public void revokeGoogleOnClick(View view) {
		Plus.AccountApi.clearDefaultAccount(this.mGoogleApiClient);
		Plus.AccountApi.revokeAccessAndDisconnect(this.mGoogleApiClient)
				.setResultCallback(new ResultCallback<Status>() {

					@Override
					public void onResult(Status result) {
						mGoogleApiClient.disconnect();
						mGoogleApiClient.connect();
						// TODO ---
						// https://developers.google.com/+/mobile/android/sign-in
					}

				});
	}

	private void resolveSignInError() {
		if (this.mConnectionResult.hasResolution()) {
			try {
				this.mIntentInProgress = true;
				this.mConnectionResult.startResolutionForResult(this,
						RC_SIGN_IN);
			} catch (SendIntentException e) {
				this.mIntentInProgress = false;
				this.mGoogleApiClient.connect();
			}
		}
	}

	private void renderButtonsOnSignedIn() {
		((Button) findViewById(R.id.sign_in_button)).setEnabled(false);
		((Button) findViewById(R.id.sign_out_button)).setEnabled(true);
		((Button) findViewById(R.id.revoke_access_button)).setEnabled(true);
	}

	private void renderButtonsOnSignedOut() {
		((Button) findViewById(R.id.sign_in_button)).setEnabled(true);
		((Button) findViewById(R.id.sign_out_button)).setEnabled(false);
		((Button) findViewById(R.id.revoke_access_button)).setEnabled(false);
	}

}
