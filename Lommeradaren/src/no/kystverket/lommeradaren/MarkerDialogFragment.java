package no.kystverket.lommeradaren;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 
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
	
	public void setContent(String title,String latitude,String longitude, String elevation, String imo, String mmsi, String speed, String positionTime, String website){
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

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View view = inflater.inflate(R.layout.dialog_marker, null);

		builder.setIcon(R.drawable.ic_launcher)
				.setTitle(this.title)
				.setView(view)
				.setPositiveButton(R.string.website,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
								startActivity(myIntent);
							}
						})
				.setNegativeButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
		
		this.setDialogText(view);

		return builder.create();
	}
	
	private void setDialogText(View view){
		((TextView)view.findViewById(R.id.textView_latitude)).setText(this.latitude);
		((TextView)view.findViewById(R.id.textView_longitude)).setText(this.longitude);
		((TextView)view.findViewById(R.id.textView_elevation)).setText(this.elevation);
		((TextView)view.findViewById(R.id.textView_imo)).setText(this.imo);
		((TextView)view.findViewById(R.id.textView_mmsi)).setText(this.mmsi);
		((TextView)view.findViewById(R.id.textView_speed)).setText(this.speed);
		((TextView)view.findViewById(R.id.textView_position_time)).setText(this.positionTime);
	}

}
