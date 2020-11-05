package com.hcanyz.android_kit.vendor.utils.utilcodex

import com.blankj.utilcode.util.ThreadUtils

class SimpleTaskRunnable(private val runnable: () -> Unit) : ThreadUtils.SimpleTask<Unit>() {
    override fun doInBackground() {
        runnable()
    }

    override fun onSuccess(result: Unit?) {
    }
}