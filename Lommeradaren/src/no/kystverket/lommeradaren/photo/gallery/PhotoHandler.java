package no.kystverket.lommeradaren.photo.gallery;

import java.io.File;
import java.util.ArrayList;

import no.kystverket.lommeradaren.photo.Photo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 
 * @author Henrik Reitan
 *
 */
public class PhotoHandler {

    private ArrayList<Photo> pictures;
    private String imageFolder;

    public PhotoHandler(String imageFolder) {
	this.imageFolder = imageFolder;
	this.pictures = getPhotos();
    }

    private ArrayList<Photo> getPhotos() {
	File folder = new File(
		Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
			+ "/" + imageFolder);
	File[] filesInFolder = folder.listFiles();
	ArrayList<Photo> photos = new ArrayList<Photo>();
	if (filesInFolder != null) { // If the folder is not empty
	    for (int i = 0; i < filesInFolder.length; i++) {
		// Check to see if any files are directories
		if (!filesInFolder[i].isDirectory()) {
		    String[] extList = filesInFolder[i].getName().split("\\.");
		    String ext = extList[extList.length - 1];
		    // Check to see if they're jpg images
		    if (ext.equals("jpg")) {
			photos.add(new Photo(decodeSampledBitmapFromResource(
			// TODO add dynamic thumbnail size instead of 90x90
				filesInFolder[i].getPath(), 90, 90),
				filesInFolder[i].getName()));
		    }
		}
	    }
	}
	return photos;
    }

    public Bitmap getLargeImage(String name, int length, int height) {
	return decodeSampledBitmapFromResource(
		Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
			+ "/" + imageFolder + "/" + name, length, height);
    }

    /**
     * Calculates how much to scale the image down, depending on the provided
     * dimensions.
     * 
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
	    int reqWidth, int reqHeight) {
	// Raw height and width of image
	final int height = options.outHeight;
	final int width = options.outWidth;
	int inSampleSize = 1;

	if (height > reqHeight || width > reqWidth) {

	    // Calculate ratios of height and width to requested height and
	    // width
	    final int heightRatio = Math.round((float) height
		    / (float) reqHeight);
	    final int widthRatio = Math.round((float) width / (float) reqWidth);

	    // Choose the smallest ratio as inSampleSize value, this will
	    // guarantee
	    // a final image with both dimensions larger than or equal to the
	    // requested height and width.
	    inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	}

	return inSampleSize;
    }

    /**
     * Loads in a bitmap from the provided url with the dimensions reqWidth x
     * reqHeight
     * 
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(String pathName,
	    int reqWidth, int reqHeight) {

	// First decode with inJustDecodeBounds=true to check dimensions
	final BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeFile(pathName, options);

	// Calculate inSampleSize
	options.inSampleSize = calculateInSampleSize(options, reqWidth,
		reqHeight);

	// Decode bitmap with inSampleSize set
	options.inJustDecodeBounds = false;
	return BitmapFactory.decodeFile(pathName, options);
    }

    public ArrayList<Photo> getPictures() {
	return pictures;
    }

}
