package app.serviceprovider;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Meenu Singh on 2019-06-27.
 */
public class NavWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //view.loadUrl(url);
        view.loadData(url, "text/html", null);
        return true;
    }
}
