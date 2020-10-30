package com.hcanyz.android_kit.widget.api_mock

import android.content.Context
import com.yanzhenjie.andserver.AndServer
import com.yanzhenjie.andserver.Server
import java.util.concurrent.TimeUnit


object ServerManager {
    private lateinit var mServer: Server

    fun init(context: Context): ServerManager {
        mServer = AndServer.webServer(context)
            .port(8080)
            .timeout(10, TimeUnit.SECONDS)
            .listener(object : Server.ServerListener {
                override fun onStarted() {
                }

                override fun onStopped() {
                }

                override fun onException(e: Exception?) {
                }
            })
            .build()
        return this
    }

    fun startServer() {
        if (!mServer.isRunning) {
            mServer.startup()
        }
    }

    fun stopServer() {
        if (mServer.isRunning) {
            mServer.shutdown()
        }
    }
}