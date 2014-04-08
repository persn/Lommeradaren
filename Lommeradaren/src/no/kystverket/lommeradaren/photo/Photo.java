package no.kystverket.lommeradaren.photo;

import android.graphics.Bitmap;

/**
 * 
 * @author Henrik Reitan
 *
 */
public class Photo {

    private Bitmap image;
    private String imgName;
    private String imgInfo;
    
    public Photo(Bitmap image, String imgName, String imgInfo){
	this.image = image;
	this.imgName = imgName;
	this.imgInfo = imgInfo;
    }
    
    public Bitmap getImage() {
        return image;
    }

    public String getImgName() {
        return imgName;
    }
    
    public String getImgInfo(){
    	return imgInfo;
    }
}
