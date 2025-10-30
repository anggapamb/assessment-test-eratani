package com.anggapamb.assessmenttesteratani.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.anggapamb.assessmenttesteratani.R

fun View.applySystemWindowInsetsPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
}

fun Context.showTopToast(message: String, isError: Boolean = false) {
    val toast = Toast(this)
    val inflater = LayoutInflater.from(this)
    val layout = inflater.inflate(R.layout.toast_success, null).apply {
        if (isError) {
            findViewById<LinearLayout>(R.id.toast_container).background =
                ContextCompat.getDrawable(this@showTopToast, R.drawable.bg_toast_rounded_error)
            findViewById<ImageView>(R.id.iv_check).isVisible = false
            findViewById<ImageView>(R.id.btn_close).isVisible = false
        } else {
            findViewById<LinearLayout>(R.id.toast_container).background =
                ContextCompat.getDrawable(this@showTopToast, R.drawable.bg_toast_rounded_success)
            findViewById<ImageView>(R.id.iv_check).isVisible = true
            findViewById<ImageView>(R.id.btn_close)?.apply {
                isVisible = true
                setOnClickListener { toast.cancel() }
            }
        }
        findViewById<TextView>(R.id.toast_text).text = message
    }
    toast.view = layout
    toast.duration = Toast.LENGTH_SHORT
    toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
    toast.show()
}