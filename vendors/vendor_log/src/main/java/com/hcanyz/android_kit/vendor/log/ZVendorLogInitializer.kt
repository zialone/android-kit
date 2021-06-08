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
        private const val TAG = "VendorLogInitializer"
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

        val canLog = izConfig.canLog

        System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")
        Log.setLogImp(Xlog())
        Log.setConsoleLogOpen(canLog)
        Log.appenderOpen(
            if (canLog) Xlog.LEVEL_VERBOSE else Xlog.LEVEL_INFO,
            Xlog.AppednerModeAsync,
            cachePath,
            logPath,
            getProcessName(context) ?: "main",
            0
        )

        izConfig.canLogLiveData().observeForever {
            ZLog.v(TAG, "change log switch $it")

            Log.setLevel(if (it) Xlog.LEVEL_VERBOSE else Xlog.LEVEL_INFO, false)
            Log.setConsoleLogOpen(it)
        }

        ZLog.v(TAG, "init success,canLog:$canLog")

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