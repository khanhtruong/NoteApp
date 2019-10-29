package com.truongkhanh.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpView()
    }

    private fun setUpView() {
        wvMain.settings.javaScriptEnabled = true
        wvMain.clearCache(true)
        wvMain.loadUrl("file:///android_asset/main.html")
    }

    inner class MyWebviewInterface {

    }
}
