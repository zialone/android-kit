package com.hcanyz.android_kit.widget.api_mock

import android.content.Context
import androidx.startup.Initializer

class ZWidgetApiMockInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        ServerManager.init(context).startServer()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}