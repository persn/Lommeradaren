package no.kystverket.lommeradaren.photo.gallery;

import java.util.ArrayList;

import no.kystverket.lommeradaren.photo.Photo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

/**
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

	public View getView(int position, View convertView, ViewGroup parent) {
	    ImageView i = new ImageView(mContext);
	    i.setImageBitmap(pictures.get(position).getImage());
	    i.setAdjustViewBounds(true);
	    i.setLayoutParams(new Gallery.LayoutParams(
		    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    return i;
	}



}