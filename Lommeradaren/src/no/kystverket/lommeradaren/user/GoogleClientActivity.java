package no.kystverket.lommeradaren.user;

import no.kystverket.lommeradaren.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

/**
 * https://github.com/googleplus/gplus-quickstart-android/blob/master/src/com/google/android/gms/plus/sample/quickstart/MainActivity.java
 * @author Pers
 *
 */
public class GoogleClientActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener {

	private static final String TAG = "no.kystverket.lommeradaren";
	private static final int STATE_DEFAULT = 0;
	private static final int STATE_SIGN_IN = 1;
	private static final int STATE_IN_PROGRESS = 2;

	private static final int RC_SIGN_IN = 0;
	private static final int DIALOG_PLAY_SERVICES_ERROR = 0;
	private static final String SAVED_PROGRESS = "sign_in_progress";
	
	private int mSignInProgress;
	private PendingIntent mSignInIntent;
	private int mSignInError;
	private GoogleApiClient mGoogleApiClient;
	
	  private SignInButton mSignInButton;
	  private Button mSignOutButton;
	  private Button mRevokeButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.fragment_google_plu);

		this.mGoogleApiClient = this.buildGoogleApiClient();
		
		  mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
		  mSignInButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Log.d(TAG,"LoginCheck");
				resolveSignInError();
			}
			  
		  });
		    mSignOutButton = (Button) findViewById(R.id.sign_out_button);
		    mRevokeButton = (Button) findViewById(R.id.revoke_access_button);
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
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_PROGRESS,this.mSignInProgress);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
		      Intent data){
		switch(requestCode){
		case RC_SIGN_IN:
			Log.d(TAG,"activityREsult1");
			if(resultCode == RESULT_OK){
				Log.d(TAG,"activityREsult2");
				this.mSignInProgress = STATE_SIGN_IN;
			}else{
				Log.d(TAG,"activityREsult3");
				this.mSignInProgress = STATE_DEFAULT;
			}
			if(!this.mGoogleApiClient.isConnecting()){
				Log.d(TAG,"activityREsult4");
				this.mGoogleApiClient.connect();
			}
			Log.d(TAG,"activityREsult5");
			break;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
		case DIALOG_PLAY_SERVICES_ERROR:
			if(GooglePlayServicesUtil.isUserRecoverableError(this.mSignInError)){
				return GooglePlayServicesUtil.getErrorDialog(this.mSignInError, this, RC_SIGN_IN,new DialogInterface.OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						Log.e(TAG,"Google Play services resolution cancelled");
						mSignInProgress = STATE_DEFAULT;
						//TODO --- Update UI to reflect signed out
					}
				});
			}else{
				return new AlertDialog.Builder(this)
				.setMessage("Placeholder") //TODO --- Replace placeholder
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.e(TAG,"Google Play services error could not be resolved " + mSignInError);
					}
				})
				.create();
			}
		default:
			return super.onCreateDialog(id);
		}
	}
	
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
				+ result.getErrorCode());
		if (this.mSignInProgress != STATE_IN_PROGRESS) {
			this.mSignInIntent = result.getResolution();
			this.mSignInError = result.getErrorCode();
			if (this.mSignInProgress == STATE_SIGN_IN) {
				resolveSignInError();
			}
		}
		//TODO --- Update UI to reflect that no user is logged in
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.i(TAG,"onConnected");
		//TODO --- Personalize app for users
//		Plus.PeopleApi.loadVisible(this.mGoogleApiClient, null)
//		.setResultCallback(this);
		Toast.makeText(getApplicationContext(), "Logged in bitchez", Toast.LENGTH_SHORT).show();
		this.mSignInProgress = STATE_DEFAULT;
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		this.mGoogleApiClient.connect();
	}
	
	public void signInGoogleOnClick(View view){
		Log.d(TAG,"LoginCheck");
		this.resolveSignInError();
	}

	private void resolveSignInError() {
		if (this.mSignInIntent != null) {			
			try {
				Log.d(TAG,"resolve1");
				this.mSignInProgress = STATE_IN_PROGRESS;
				Log.d(TAG,"resolve2");
				startIntentSenderForResult(
						this.mSignInIntent.getIntentSender(), RC_SIGN_IN, null,
						0, 0, 0);
				Log.d(TAG,"resolve3");
			} catch (SendIntentException e) {
				Log.i(TAG,
						"Sign in intent could not be sent: "
								+ e.getLocalizedMessage());
				this.mSignInProgress = STATE_SIGN_IN;
				this.mGoogleApiClient.connect();
			}
		} else {
			showDialog(DIALOG_PLAY_SERVICES_ERROR);
		}
	}

	private GoogleApiClient buildGoogleApiClient() {
		return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, null)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

}
