package no.kystverket.lommeradaren.photo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import no.kystverket.lommeradaren.markers.POI;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.widget.ImageView;

/**
 * Class handling the loading and saving of images between the phones storage
 * and the applications memory.
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

	/**
	 * Loads all images in the folder specified in the constructor when creating
	 * the class. Also reads the metadata tags and combines this into a Photo
	 * object
	 * 
	 * @return ArrayList containing all the images.
	 */
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
						try {
							ExifInterface exif = new ExifInterface(
									filesInFolder[i].getPath());
							String info = exif.getAttribute("UserComment");
							if (info != null) {
								photos.add(new Photo(
										decodeSampledBitmapFromUrl(
												// TODO add dynamic thumbnail
												// size instead of 90x90
												filesInFolder[i].getPath(), 90,
												90),
										filesInFolder[i].getName(), new POI(
												info)));
							} else {
								photos.add(new Photo(
										decodeSampledBitmapFromUrl(
												// TODO add dynamic thumbnail
												// size instead of 90x90
												filesInFolder[i].getPath(), 90,
												90), filesInFolder[i].getName()));
							}

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return photos;
	}

	/**
	 * Static method to save our own string into an image files metadata
	 * 
	 * @param path
	 *            the location of the image
	 * @param data
	 *            the string to save
	 */
	public static void setExifData(String path, String data) {
		try {
			ExifInterface exif = new ExifInterface(path);
			exif.setAttribute("UserComment", data);
			exif.saveAttributes();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes the imagefile that corresponds to the given Photo object from the
	 * devices storage
	 * 
	 * @param img
	 *            Photo object to delete
	 * @return true if successfull, else false
	 */
	public boolean deleteImage(Photo img) {
		File imgFile = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
						+ "/" + imageFolder + "/" + img.getImgName());
		boolean res = imgFile.delete();
		if (res) {
			pictures.remove(img);
		}
		return res;
	}

	/**
	 * Loads a bitmap with the given size from the devices storage into the
	 * applications memory.
	 * 
	 * @param name
	 *            Name of the file to load
	 * @param length
	 * @param height
	 * @return
	 */
	public Bitmap getLargeImage(String name, int length, int height) {
		return decodeSampledBitmapFromUrl(
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
			if (heightRatio > widthRatio) {
				inSampleSize = heightRatio;
			} else {
				inSampleSize = widthRatio;
			}
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
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap decodeSampledBitmapFromUrl(String pathName,
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

	/**
	 * Helper method for clearing an imageview to free memory.
	 * 
	 * @param view
	 *            ImageView to clear
	 */
	public static void stripImageView(ImageView view) {
		if (view.getDrawable() instanceof BitmapDrawable) {
			((BitmapDrawable) view.getDrawable()).getBitmap().recycle();
		}
		if (view.getDrawable() != null) {
			view.getDrawable().setCallback(null);
			view.setImageDrawable(null);
		}
		view.getResources().flushLayoutCache();
		view.destroyDrawingCache();
	}

}
