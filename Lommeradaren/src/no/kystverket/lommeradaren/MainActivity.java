package no.kystverket.lommeradaren;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import no.kystverket.lommeradaren.camera.CameraActivity;
import no.kystverket.lommeradaren.maps.MapActivity;
import no.kystverket.lommeradaren.photo.gallery.GalleryActivity;
import no.kystverket.lommeradaren.photo.gallery.PhotoHandler;
import no.kystverket.lommeradaren.user.UserActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.RFC2109Spec;
import org.apache.http.message.BasicHeader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This is the activity for the title screen, it contains logic associated with
 * switching to other activities.
 * 
 * @author Per Olav Flaten
 * 
 */
public class MainActivity extends Activity {

	private Point screenSize;
	private ImageView backgroundImage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		CookieSyncManager.createInstance(this);
		CookieManager.getInstance().setAcceptCookie(true);
		
		screenSize = new Point();
		getWindowManager().getDefaultDisplay().getSize(screenSize);
		setContentView(R.layout.mainmenu_activity);
		backgroundImage = (ImageView) findViewById(R.id.mainmenu_background);

	}

	@Override
	public void onResume() {
		super.onResume();
		backgroundImage.setImageDrawable(new BitmapDrawable(getResources(),
				PhotoHandler.decodeSampledBitmapFromResource(getResources(),
						R.drawable.mainmenu_background, screenSize.x,
						screenSize.y)));
		CookieSyncManager.getInstance().startSync();
	}

	@Override
	public void onStop() {
		super.onStop();
		PhotoHandler.stripImageView(backgroundImage);
		CookieSyncManager.getInstance().stopSync();
	}

	public void cameraOnClick(View v) {
		startActivity(new Intent(this.getApplicationContext(),
				CameraActivity.class));
		finish();
	}

	public void galleryOnClick(View v) {
		startActivity(new Intent(this.getApplicationContext(),
				GalleryActivity.class));
		finish();
	}

	public void goToMapScreenOnClick(View v) {
		startActivity(new Intent(this.getApplicationContext(),
				MapActivity.class));
		finish();
	}
	
	public void goToUserScreenOnClick(View v){
		Intent intent= new Intent(this.getApplicationContext(),UserActivity.class);
		intent.setData(Uri.parse(getString(R.string.web_login_url)));
		startActivityForResult(intent,0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
		case 0:
			if(resultCode != RESULT_OK || data == null){
				return;
			}
			String session = data.getStringExtra("token1");
			String form = data.getStringExtra("token2");
			if(session != null && form != null){
//				BasicClientCookie cookie1 = new BasicClientCookie("ASP.NET_SessionId",session);
//				cookie1.setDomain("http://10.201.84.118");
//				BasicClientCookie cookie2 = new BasicClientCookie(" loginForm",form);
//				cookie2.setDomain("http://10.201.84.118");
//				
//				this.cookieStore.addCookie(cookie1);
//				this.cookieStore.addCookie(cookie2);
//				this.client.setCookieStore(this.cookieStore);
//				this.httpContext.setAttribute(ClientContext.COOKIE_STORE, this.client);
				
				Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
		switch (keycode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			return true;
		}
		return super.onKeyDown(keycode, e);
	}
	
	public void rawr(View v){
		new ServerThread().execute();
	}
	
	public static String getURLContentsAsString(String url) {
		 
		  DefaultHttpClient httpClient =  new DefaultHttpClient();
		 
		  //Injects App's CookieManager cookies for this URL into HttpClient CookieStore
		  syncCookiesFromAppCookieManager(url, httpClient);
		 
		  HttpGet hp = new HttpGet(url);
		  HttpResponse response;
		  ResponseHandler<String> responseHandler = new BasicResponseHandler();
		  try{
			  String ladidad = httpClient.execute(hp,responseHandler);
			  Log.d("ladidad",ladidad);
		  }catch (Exception e){
			  e.printStackTrace();
		  }
		 
		  //Save's cookies from HttpClient in App's CookieStore
		  syncCookiesToAppCookieManager(url, httpClient);
		 
		  // Process Response here
		  //...
		  return "";
		}
	
	public static void syncCookiesFromAppCookieManager(String url, DefaultHttpClient httpClient) {
		 
		  BasicCookieStore cookieStore = new BasicCookieStore();
		  httpClient.setCookieStore(cookieStore);
		 
		  CookieManager cookieManager = CookieManager.getInstance();
		  if (cookieManager == null) return;
		 
		  RFC2109Spec cookieSpec = new RFC2109Spec();
		  String rawCookieHeader = null;
		  try {
		    URL parsedURL = new URL(url);
		 
		    //Extract Set-Cookie header value from Android app CookieManager for this URL
		    rawCookieHeader = cookieManager.getCookie(parsedURL.getHost());
		    if (rawCookieHeader == null) return;
		 
		    //Convert Set-Cookie header value into Cookies w/in HttpClient CookieManager
		    int port = parsedURL.getPort() == -1 ?
		      parsedURL.getDefaultPort() : parsedURL.getPort();
		 
		    CookieOrigin cookieOrigin = new CookieOrigin( parsedURL.getHost(),
		                                                  port,
		                                                  "/",
		                                                  false);
		    List<Cookie> appCookies = cookieSpec.parse(
		      new BasicHeader("set-cookie", rawCookieHeader),
		      cookieOrigin);
		 
		    cookieStore.addCookies(appCookies.toArray(new Cookie[appCookies.size()]));
		  } catch (MalformedURLException e) {
		    // Handle Error
		  } catch (MalformedCookieException e) {
		    // Handle Error
		  }
		}
	
	public static void syncCookiesToAppCookieManager(String url, DefaultHttpClient httpClient) {
		 
		  CookieStore clientCookieStore = httpClient.getCookieStore();
		  List<Cookie> cookies  = clientCookieStore.getCookies();
		  if (cookies.size() < 1) return;
		 
		  CookieSyncManager syncManager = CookieSyncManager.getInstance();
		  CookieManager appCookieManager = CookieManager.getInstance();
		  if (appCookieManager == null) return;
		 
		  //Extract any stored cookies for HttpClient CookieStore
		  // Store this cookie header in Android app CookieManager
		  for (Cookie cookie:cookies) {
		    //HACK: Work around weird version-only cookies from cookie formatter.
		    if (cookie.getName() == "$Version") break;
		 
		    String setCookieHeader = cookie.getName()+"="+cookie.getValue()+
		      "; Domain="+cookie.getDomain();
		    appCookieManager.setCookie(url, setCookieHeader);
		  }
		 
		  //Sync CookieManager to disk if we added any cookies
		  syncManager.sync();
		}
	
	private class ServerThread extends AsyncTask<URL,Integer,Long>{
		
		@Override
        protected Long doInBackground(URL... params) {
			getURLContentsAsString(getString(R.string.web_picture_upload_url) + "pictureName=trallala");
			return null;
		}
	}
	
}
