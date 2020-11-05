package com.hcanyz.android_kit.widget.core

import android.content.Context
import androidx.startup.Initializer
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.common.DefaultRootUriHandler

class ZWidgetCoreInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Router.init(DefaultRootUriHandler(context))
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}