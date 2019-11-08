package com.truongkhanh.noteapp.view.editor.WebViewInterface

import android.webkit.JavascriptInterface

class WebViewInterface(private val listener: InteractionListener) {

    interface InteractionListener {
        fun editorFinishLoading()
    }

    @JavascriptInterface
    fun editorFinishLoading() {
        listener.editorFinishLoading()
    }
}