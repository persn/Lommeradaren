package no.kystverket.lommeradaren;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Class for handling the information dialogs in the application
 * @author Per Olav Flaten
 *
 */
public class MarkerDialogFragment extends DialogFragment {

	private String title = "---";
	private String latitude = "---";
	private String longitude = "---";
	private String elevation = "---";
	private String imo = "---";
	private String mmsi = "---";
	private String speed = "---";
	private String positionTime = "---";
	private String website = "---";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View view = inflater.inflate(R.layout.dialog_marker, null);

		AlertDialog dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);

		((Button) view.findViewById(R.id.button_website))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(website));
						startActivity(myIntent);
					}

				});

		((Button) view.findViewById(R.id.button_dialog_ok))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dismiss();
					}

				});

		this.setDialogText(view);
		return dialog;
	}
/**
 * Method for updating the information contained in this dialogFragment
 * @param title
 * @param latitude 
 * @param longitude
 * @param elevation
 * @param imo
 * @param mmsi
 * @param speed
 * @param positionTime
 * @param website
 */
	public void setContent(String title, String latitude, String longitude,
			String elevation, String imo, String mmsi, String speed,
			String positionTime, String website) {
		this.title = title;
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
		this.imo = imo;
		this.mmsi = mmsi;
		this.speed = speed;
		this.positionTime = positionTime;
		this.website = website;
	}

	/**
	 * Binds the information in the class variables to the text fields in the dialog
	 * @param view
	 */
	private void setDialogText(View view) {
		((TextView) view.findViewById(R.id.textView_title)).setText(this.title);
		((TextView) view.findViewById(R.id.textView_latitude))
				.setText(this.latitude);
		((TextView) view.findViewById(R.id.textView_longitude))
				.setText(this.longitude);
		((TextView) view.findViewById(R.id.textView_elevation))
				.setText(this.elevation);
		((TextView) view.findViewById(R.id.textView_imo)).setText(this.imo);
		((TextView) view.findViewById(R.id.textView_mmsi)).setText(this.mmsi);
		((TextView) view.findViewById(R.id.textView_speed)).setText(this.speed);
		((TextView) view.findViewById(R.id.textView_position_time))
				.setText(this.positionTime);
	}

}
