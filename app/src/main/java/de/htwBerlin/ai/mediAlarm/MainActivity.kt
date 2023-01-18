package de.htwBerlin.ai.mediAlarm

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.data.Constants
import de.htwBerlin.ai.mediAlarm.notification.NotificationCanceller


class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var preferences: SharedPreferences

    private val gson: Gson = Gson()
    private lateinit var webView: WebView
    private var permissionRequestCompleted: Boolean = false

    var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            permissionRequestCompleted = isGranted
        }

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.lime_500)

        preferences = getSharedPreferences("MediWecker.Preferences", Context.MODE_PRIVATE)

        // Temporary webView setup
        webView = findViewById(R.id.webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.builtInZoomControls = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowContentAccess = true
        //webView.settings.safeBrowsingEnabled = false

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        webView.addJavascriptInterface(WebAppInterface(this), "Android")

        webView.webViewClient = LocalContentWebViewClient(assetLoader)
        webView.webChromeClient = LocalChromeClient()
        webView.loadUrl("https://appassets.androidplatform.net/assets/wwwroot/index_mobile.html")

        val isNotificationClick = intent.getBooleanExtra(Constants.NOTIFICATION_CLICK, false)

        if (isNotificationClick) {
            val medicineId = intent.getLongExtra(Constants.MEDICINE_ID, 0)
            val scheduledTimeUtc = intent.getLongExtra(Constants.SCHEDULED_TIME_UTC, 0)
            val notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID, 0)

            NotificationCanceller(this).cancel(notificationId)

            val request = ShowReminderPromptRequestData()

            request.medicineId = medicineId
            request.scheduledTimeUtc = scheduledTimeUtc

            val json = gson.toJson(request)
            val sb = java.lang.StringBuilder()

            sb.append("MediWecker.showReminderPrompt(")
            sb.append(json)
            sb.append(");")

            val jsStatement = sb.toString()

            webView.evaluateJavascript(jsStatement, null)
        }
    }

    class ShowReminderPromptRequestData {
        var medicineId: Long = 0
        var scheduledTimeUtc: Long = 0
    }

    fun getAndResetPermissionsRequestCompleted() : Boolean {
        if (permissionRequestCompleted) {
            permissionRequestCompleted = false
            return true
        }

        return false
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
        override fun onPageFinished(view: WebView?, url: String?) {
            Log.d("MainActivity", "onPageFinished")
        }

        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }
    }

    fun onBackPressedBypassWebView() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}