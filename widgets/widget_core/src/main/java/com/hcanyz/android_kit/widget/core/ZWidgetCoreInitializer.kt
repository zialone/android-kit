package com.hcanyz.android_kit.widget.core

import android.content.Context
import androidx.startup.Initializer
import com.hcanyz.android_kit.vendor.config.IZConfig
import com.hcanyz.android_kit.vendor.config.ZVendorConfigInitializer
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.common.DefaultRootUriHandler
import com.sankuai.waimai.router.components.DefaultLogger
import com.sankuai.waimai.router.core.Debugger

class ZWidgetCoreInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Router.init(DefaultRootUriHandler(context))

        // 自定义Logger
        val logger: DefaultLogger = object : DefaultLogger() {
            override fun handleError(t: Throwable?) {
                super.handleError(t)
                // 此处上报Fatal级别的异常
            }
        }

        val izConfig = IZConfig.getInstance(context)
        // 设置Logger
        Debugger.setLogger(logger)
        // Log开关，建议测试环境下开启，方便排查问题。
        Debugger.setEnableLog(izConfig.canLog)
        // 调试开关，建议测试环境下开启。调试模式下，严重问题直接抛异常，及时暴漏出来。
        Debugger.setEnableDebug(izConfig.canDebug)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return arrayListOf(ZVendorConfigInitializer::class.java)
    }
}