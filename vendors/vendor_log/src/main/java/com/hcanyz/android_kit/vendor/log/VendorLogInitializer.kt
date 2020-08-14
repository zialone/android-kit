package com.hcanyz.android_kit.vendor.log

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import androidx.startup.Initializer
import com.hcanyz.android_kit.vendor.config.IZConfig
import com.hcanyz.android_kit.vendor.config.VendorConfigInitializer
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog

class VendorLogInitializer : Initializer<Unit> {

    companion object {
        private const val TAG = "VendorLogInitializer"
    }

    override fun create(context: Context) {
        val izConfig = IZConfig.getInstance(context)

        val subPath = "/zlog${izConfig.envMark()
            .let { return@let if (it.isBlank()) "" else "-${it}" }}"

        val logPath = context.filesDir.absolutePath + subPath

        // this is necessary, or may crash for SIGBUS
        val cachePath = context.cacheDir.absolutePath + subPath

        val canLog = izConfig.canLog
        Xlog.open(
            true,
            if (canLog) Xlog.LEVEL_VERBOSE else Xlog.LEVEL_INFO,
            Xlog.AppednerModeAsync,
            cachePath,
            logPath,
            getProcessName(context) ?: "main",
            ""
        )
        Xlog.setConsoleLogOpen(canLog)

        izConfig.canLogLiveData().observeForever {
            ZLog.v(TAG, "change log switch $it")

            Xlog.setLogLevel(if (it) Xlog.LEVEL_VERBOSE else Xlog.LEVEL_INFO)
            Xlog.setConsoleLogOpen(it)
        }

        Log.setLogImp(Xlog())

        ZLog.v(TAG, "init success,canLog:$canLog")

        // Log.appenderClose()
        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return arrayListOf(VendorConfigInitializer::class.java)
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