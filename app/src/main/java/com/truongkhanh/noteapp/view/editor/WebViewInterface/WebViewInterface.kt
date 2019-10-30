package com.truongkhanh.noteapp.view.editor.WebViewInterface

import android.util.Log
import android.webkit.JavascriptInterface

class WebViewInterface {

    @JavascriptInterface
    fun sendData(data: String) {
        Log.d("Debuggg", data)
    }
}