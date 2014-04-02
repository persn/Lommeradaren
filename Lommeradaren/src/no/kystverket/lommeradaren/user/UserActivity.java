package no.kystverket.lommeradaren.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UserActivity extends Activity {

	private WebView authenticationPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		requestWindowFeature(Window.FEATURE_PROGRESS);

		this.authenticationPage = new WebView(this);
		setContentView(this.authenticationPage);

		authenticationPage.getSettings().setJavaScriptEnabled(true);
		setWebConfiguration(this.authenticationPage);

		Intent intent = getIntent();
		if (intent.getData() != null) {
			this.authenticationPage.loadUrl(intent.getDataString());
		}
	}

//	@Override
//	protected void onPause() {
//		super.onPause();
//		CookieSyncManager.getInstance().stopSync();
//	}
//
//	protected void onResume() {
//		super.onResume();
//		CookieSyncManager.getInstance().startSync();
//	}

	private void setWebConfiguration(WebView webView) {
		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int progress) {
				setProgress(progress * 100);
			}
		});

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				setTitle(url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				CookieSyncManager.getInstance().sync();
				String cookie = CookieManager.getInstance().getCookie(url);
				if (cookie == null) {
					return;
				}
				String[] pairs = cookie.split(";");
				int tokensfound = 0;
				String[] bufferTokens = new String[2];
				for (int i = 0; i < pairs.length; i++) {
					String[] parts = pairs[i].split("=", 2);
					Log.d("CookieName", parts[0]);
					Log.d("CookieValue", parts[1]);
					if (parts.length == 2) {
						if (parts[0].equals("ASP.NET_SessionId")) {
							bufferTokens[0] = parts[1];
							tokensfound++;
						} else if (parts[0].equals(" loginForm")) {
							bufferTokens[1] = parts[1];
							tokensfound++;
						}
					}
				}
				if (tokensfound == 2) {
					Intent result = new Intent();
					result.putExtra("token1", bufferTokens[0]);
					result.putExtra("token2", bufferTokens[1]);
					setResult(RESULT_OK, result);
					finish();
				}
			}
		});
	}

}
