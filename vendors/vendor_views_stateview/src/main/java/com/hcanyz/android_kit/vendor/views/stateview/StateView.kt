package com.hcanyz.android_kit.vendor.views.stateview

import android.view.View
import android.widget.TextView
import com.kennyc.view.MultiStateView

fun MultiStateView.customizeStateEmpty(emptyTip: String) {
    findViewById<TextView>(R.id.tv_stateview_empty_tip).text = emptyTip
}

fun MultiStateView.customizeStateError(
    errorTip: String? = null,
    retryOnClickListener: View.OnClickListener? = null
) {
    errorTip?.let {
        findViewById<TextView>(R.id.tv_stateview_error_tip).text = it
    }
    findViewById<TextView>(R.id.tv_stateview_error_retry).setOnClickListener(retryOnClickListener)
}