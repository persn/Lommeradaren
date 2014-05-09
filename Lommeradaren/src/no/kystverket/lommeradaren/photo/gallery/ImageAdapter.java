package no.kystverket.lommeradaren.photo.gallery;

import java.util.ArrayList;

import no.kystverket.lommeradaren.photo.Photo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Custom BaseAdapter class to use with the gridview in galleryActivity to
 * display our pictures
 * 
 * @author Henrik Reitan
 * 
 */
public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Photo> pictures;

	public ImageAdapter(Context c, ArrayList<Photo> pictures) {
		this.mContext = c;
		this.pictures = pictures;
	}

	public int getCount() {
		return pictures.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * Creates an ImageView containing the picture at a given location in the
	 * ArrayList
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(mContext);
		i.setImageBitmap(pictures.get(position).getImage());
		i.setAdjustViewBounds(true);
		return i;
	}
}