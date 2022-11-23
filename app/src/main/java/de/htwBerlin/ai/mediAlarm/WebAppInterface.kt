package de.htwBerlin.ai.mediAlarm

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class WebAppInterface internal constructor(c: Context) {
    var mContext: MainActivity

    init {
        mContext = c as MainActivity
    }

    @JavascriptInterface
    fun showToast(msg: String): String {
        //Log.i("-->>", msg)
        Toast.makeText(mContext, "$msg", Toast.LENGTH_SHORT).show()

        mContext.setAlarm()

        return "Hello from Android!"
    }
}