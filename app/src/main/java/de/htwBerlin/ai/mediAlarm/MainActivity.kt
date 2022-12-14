package de.htwBerlin.ai.mediAlarm

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import de.htwBerlin.ai.mediAlarm.alarm.AlarmReceiver


class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    lateinit var preferences: SharedPreferences

    private lateinit var webView: WebView
    private var permissionRequestcode: Int = 200
    private var permissionRequestCompleted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.lime_500)
        }

        preferences = getSharedPreferences("MediWecker.Preferences", Context.MODE_PRIVATE);

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
        webView.webChromeClient = LocalChromeClient();
        webView.loadUrl("https://appassets.androidplatform.net/assets/wwwroot/index_mobile.html");
    }

    fun getIfNotificationsPermissionGiven() : Boolean {
        //return ContextCompat.checkSelfPermission(this,
        //    Manifest.permission.SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED
        return true;
    }

    fun getIfInternetPermissionGiven() : Boolean {
        //return ContextCompat.checkSelfPermission(this,
        //    Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
        return true;
    }

    fun attemptRequestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.INTERNET, Manifest.permission.SCHEDULE_EXACT_ALARM),
            permissionRequestcode
        )
    }

    fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionRequestcode -> {
                permissionRequestCompleted = true
            }
        }
    }

    fun getAndResetPermissionsRequestCompleted() : Boolean {
        if (permissionRequestCompleted) {
            permissionRequestCompleted = false;
            return true;
        }

        return false;
    }

    fun setAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            100,
            pendingIntent
        )

        Log.d("Medicine Reminder", "setAlarm")
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

    fun onBackPressedBypassWebView() {
        super.onBackPressed();
    }

    override fun onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}