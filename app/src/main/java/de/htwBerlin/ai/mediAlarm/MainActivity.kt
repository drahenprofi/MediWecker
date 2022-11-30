package de.htwBerlin.ai.mediAlarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import de.htwBerlin.ai.mediAlarm.alarm.AlarmReceiver

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Temporary webview setup
        webView = findViewById(R.id.webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.builtInZoomControls = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.safeBrowsingEnabled = false

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        webView.addJavascriptInterface(WebAppInterface(this), "Android")

        webView.webViewClient = LocalContentWebViewClient(assetLoader)
        webView.webChromeClient = LocalChromeClient()
        webView.loadUrl("https://appassets.androidplatform.net/assets/wwwroot/index_mobile.html")
    }

    private class LocalChromeClient : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            if (consoleMessage != null) {
                Log.d("WebView", consoleMessage.message())
            }

            return true
        }
    }

    private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) : WebViewClientCompat() {
        @RequiresApi(21)
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            Log.d("DEBUG", request.url.toString());

            /*if (request.url.toString().startsWith("https://appassets.androidplatform.net/assets/wwwroot/")) {
                return assetLoader.shouldInterceptRequest(request.url)
            } else {
                return assetLoader.shouldInterceptRequest(Uri.parse("https://appassets.androidplatform.net/assets/wwwroot/index_mobile.html"));
            }*/

            return return assetLoader.shouldInterceptRequest(request.url)
        }

        // to support API < 21
        override fun shouldInterceptRequest(
            view: WebView,
            url: String
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(Uri.parse(url))
        }

    /*@RequiresApi(21)
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            var urlString: String = request.url.toString()

            if (urlString.startsWith("https://appassets.androidplatform.net/assets/wwwroot")) {
                Log.d("DEBUG", "shouldInterceptRequest: Returning from assets for " + urlString);

                return assetLoader.shouldInterceptRequest(request.url)
            }

            if (urlString.startsWith("https://appassets.androidplatform.net/assets/wwwroot/index_mobile.html")) {
                Log.d("DEBUG", "shouldInterceptRequest: Returning from assets for " + urlString);

                return assetLoader.shouldInterceptRequest(request.url)
            }

            Log.d("DEBUG", "shouldInterceptRequest: Returning NULL for " + urlString);
            return null
        }

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            var urlString: String = request.url.toString()

            return false;
        }

        // to support API < 21
        override fun shouldInterceptRequest(
            view: WebView,
            url: String
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(Uri.parse(url))

            var urlString: String = url

            if (urlString.startsWith("https://appassets.androidplatform.net/assets/assets")) {
                Log.d("DEBUG", "shouldInterceptRequest: Returning from assets");

                return assetLoader.shouldInterceptRequest(Uri.parse(url))
            }

            if (urlString.startsWith("https://appassets.androidplatform.net/assets/index_mobile.html")) {
                Log.d("DEBUG", "shouldInterceptRequest: Returning from assets");

                return assetLoader.shouldInterceptRequest(Uri.parse(url))
            }

            Log.d("DEBUG", "shouldInterceptRequest: Returning NULL");
            return null
        }*/
    }

    override fun onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}