package no.kystverket.lommeradaren.user;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableNotifiedException;

/**
 * A thread class that executes transfer of user pictures from the Android
 * application to the web client.
 * 
 * @author Per Olav Flaten
 * 
 */
public class StreamPictureTask extends AsyncTask<Void, Void, Void> {

	protected Activity activity;
	protected String mEmail;
	protected String mScope;
	protected String pictureFileName;
	protected String pictureFileData;
	protected String url;

	private final static String boundary = "SwA"
			+ Long.toString(System.currentTimeMillis()) + "SwA";
	private final static String delimiter = "--";

	public StreamPictureTask(Activity activity, String email, String scope,
			String pictureFileName, String pictureFileData, String url) {
		this.activity = activity;
		this.mEmail = email;
		this.mScope = scope;
		this.pictureFileName = pictureFileName;
		this.pictureFileData = pictureFileData;
		this.url = url;
	}

	@Override
	protected Void doInBackground(Void... params) {
		streamPictureToWebServer(this.pictureFileName, url);
		return null;
	}

	private void streamPictureToWebServer(String filename, String upload_url) {
		String token = fetchToken();
		if (token == null) { // If no token is detected picture stream should
								// not be attempted
			return;
		}
		try {
			// TODO --- URL should be retrieved from project string resources
			URL url = new URL(upload_url + "token=" + token);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);
			connection.connect();

			File file = new File(
					Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
							+ "/Lommeradaren", this.pictureFileName);
			byte[] pictureBuffer = new byte[(int) file.length()];
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(pictureBuffer);
			fileInputStream.close();

			writeMultipartToWebOutput(connection.getOutputStream(),
					pictureBuffer, this.pictureFileName, "jpeg");

			switch (connection.getResponseCode()) { // Handle feedback to user
													// according to web response
			case 200:
				Log.d("StreamPictureTask", "TestTest 200");// Remove on
															// implement of
															// feedback
				// TODO -- Success feedback
				break;
			case 401:
				Log.d("StreamPictureTask", "TestTest 401");// Remove on
															// implement of
															// feedback
				GoogleAuthUtil.invalidateToken(activity, token);
				// TODO --- Error feedback
				break;
			default:
				Log.d("StreamPictureTask",
						"TestTest " + connection.getResponseCode());// Remove on
																	// implement
																	// of
																	// feedback
				// TODO --- Error feedback
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String fetchToken() {
		try {
			return GoogleAuthUtil.getToken(this.activity, this.mEmail,
					this.mScope);
		} catch (UserRecoverableNotifiedException userRecoverableException) {
			userRecoverableException.printStackTrace();
		} catch (GoogleAuthException fatalException) {
			fatalException.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void writeMultipartToWebOutput(OutputStream outputStream,
			byte[] imageBytes, String imageName, String imageType)
			throws IOException {
		DataOutputStream os = new DataOutputStream(outputStream);
		os.write((delimiter + boundary + "\r\n").getBytes());
		os.write(("Content-Disposition: form-data; name=\"image\"; filename=\""
				+ imageName + "\"\r\n").getBytes());
		os.write(("Content-Type: image/" + imageType + "\r\n\r\n").getBytes());
		os.write(imageBytes);
		os.write(("\r\n" + delimiter + boundary + "\r\n").getBytes());
		os.write("Content-Disposition: form-data; name=\"image-data\"; filename=\"file-data.txt\"\r\n\r\n"
				.getBytes());
		os.write(this.pictureFileData.getBytes());
		os.write(("\r\n" + delimiter + boundary + delimiter + "\r\n")
				.getBytes());
	}

}
