package com.hcanyz.android_kit.vendor.wechat.login

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tencent.mm.opensdk.diffdev.DiffDevOAuthFactory

class WeChatQrCodeView : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun loadQRCode() {
        DiffDevOAuthFactory.getDiffDevOAuth()
    }
}