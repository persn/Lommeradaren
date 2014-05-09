package no.kystverket.lommeradaren.photo;

import no.kystverket.lommeradaren.markers.POI;
import android.graphics.Bitmap;

/**
 * Simple class containing a Photo and a POI object combined with a name to ease
 * the handling of pictures and information in the gallery.
 * 
 * @author Henrik Reitan
 * 
 */
public class Photo {

	private Bitmap image;
	private String imgName;
	private POI poi;

	public Photo(Bitmap image, String imgName, POI poi) {
		this.image = image;
		this.imgName = imgName;
		this.poi = poi;
	}

	public Photo(Bitmap image, String imgName) {
		this.image = image;
		this.imgName = imgName;
		this.poi = new POI(-1, "", -1, -1, -1, "", -1, "", "", "", "", "", "");
	}

	public Bitmap getImage() {
		return image;
	}

	public String getImgName() {
		return imgName;
	}

	public POI getPoi() {
		return poi;
	}
}
