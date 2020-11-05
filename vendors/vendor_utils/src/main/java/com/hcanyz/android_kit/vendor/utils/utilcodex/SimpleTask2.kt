package com.hcanyz.android_kit.vendor.utils.utilcodex

import com.blankj.utilcode.util.ThreadUtils

abstract class SimpleTask2<T> : ThreadUtils.SimpleTask<T>() {
    
    override fun onSuccess(result: T?) {
    }
}