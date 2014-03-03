package no.kystverket.lommeradaren.photo;

import android.graphics.Bitmap;

public class Photo {

    private Bitmap image;
    private String imgName;
    
    public Photo(Bitmap image, String imgName){
	this.image = image;
	this.imgName = imgName;
    }
    
    public Bitmap getImage() {
        return image;
    }

    public String getImgName() {
        return imgName;
    }
    
}
