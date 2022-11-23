package de.htwBerlin.ai.mediAlarm

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Temporary webview setup
        webView = findViewById(R.id.webView);

        webView.settings.javaScriptEnabled = true;
        webView.settings.javaScriptCanOpenWindowsAutomatically = true;
        webView.settings.builtInZoomControls = true;
        webView.settings.domStorageEnabled = true;

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(this))
            .build()

        webView.addJavascriptInterface(WebAppInterface(this), "Android");

        webView.webViewClient = LocalContentWebViewClient(assetLoader)
        webView.webChromeClient = LocalChromeClient();
        webView.loadUrl("https://appassets.androidplatform.net/assets/wwwroot/index_mobile.html");
    }

    class WebAppInterface internal constructor(c: Context) {
        var mContext: Context

        init {
            mContext = c
        }

        @JavascriptInterface
        fun showToast(msg: String) {
            Log.i("-->>", msg)
            Toast.makeText(mContext, "$msg", Toast.LENGTH_SHORT).show()
        }
    }

    private class LocalChromeClient : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            if (consoleMessage != null) {
                Log.d("WebView", consoleMessage.message())
            };

            return true;
        }
    }

    private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) : WebViewClientCompat() {
        @RequiresApi(21)
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            Log.d("DEBUG", request.url.toString());

            return assetLoader.shouldInterceptRequest(request.url)
        }

        // to support API < 21
        override fun shouldInterceptRequest(
            view: WebView,
            url: String
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(Uri.parse(url))
        }
    }


    override fun onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}