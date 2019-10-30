package com.truongkhanh.noteapp.util

import android.view.View

fun getEnableView(enable: Boolean): Int {
    return if (enable)
        View.VISIBLE
    else
        View.GONE
}