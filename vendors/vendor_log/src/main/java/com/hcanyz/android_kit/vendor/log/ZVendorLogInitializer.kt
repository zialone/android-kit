package com.hcanyz.android_kit.vendor.log

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import androidx.startup.Initializer
import com.hcanyz.android_kit.vendor.config.IZConfig
import com.hcanyz.android_kit.vendor.config.ZVendorConfigInitializer
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog

class ZVendorLogInitializer : Initializer<Unit> {

    companion object {
        private const val TAG = "ZLog-VendorLogInit"
    }

    override fun create(context: Context) {
        val izConfig = IZConfig.getInstance(context)

        val subPath = "/zlog${
            izConfig.envMark()
                .let { return@let if (it.isBlank()) "" else "-${it}" }
        }"

        val logPath = context.filesDir.absolutePath + subPath

        // this is necessary, or may crash for SIGBUS
        val cachePath = context.cacheDir.absolutePath + subPath

        System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")
        Log.setLogImp(Xlog())
        Log.appenderOpen(
            Xlog.LEVEL_VERBOSE,
            Xlog.AppednerModeAsync,
            cachePath,
            logPath,
            getProcessName(context) ?: "main",
            0
        )
        // 初始化时需要为 true，如果时 false 则后面再调用 setConsoleLogOpen(true) 会不生效
        Log.setConsoleLogOpen(true)

        android.util.Log.v(TAG, "init success")

        izConfig.canLogLiveData().observeForever {
            android.util.Log.v(TAG, "change log switch $it")

            Log.setLevel(if (it) Xlog.LEVEL_VERBOSE else Xlog.LEVEL_INFO, false)
            Log.setConsoleLogOpen(it)
        }

        // Log.appenderClose()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return arrayListOf(ZVendorConfigInitializer::class.java)
    }

    private fun getProcessName(context: Context): String? {
        var processName: String? = null
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        if (am != null) {
            for (appProcess in am.runningAppProcesses) {
                if (appProcess.pid == Process.myPid()) {
                    processName =
                        appProcess.processName.replace(context.packageName, "")
                            .replace(":", "")
                    break
                }
            }
        }
        if (processName.isNullOrBlank()) {
            return null
        }
        return processName
    }
}