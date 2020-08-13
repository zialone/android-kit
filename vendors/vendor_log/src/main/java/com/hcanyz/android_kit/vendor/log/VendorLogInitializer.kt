package com.hcanyz.android_kit.vendor.log

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import androidx.startup.Initializer
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog

class VendorLogInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        android.util.Log.i("VendorLogInitializer", "hello,world!")

        val logPath = context.filesDir.absolutePath + "/log"

        // this is necessary, or may crash for SIGBUS
        val cachePath = context.cacheDir.absolutePath + "/log"

        Xlog.open(
            true,
            if (BuildConfig.DEBUG) Xlog.LEVEL_VERBOSE else Xlog.LEVEL_INFO,
            Xlog.AppednerModeAsync,
            cachePath,
            logPath,
            "${getProcessName(context) ?: "main"}-",
            ""
        )
        Xlog.setConsoleLogOpen(BuildConfig.DEBUG)

        Log.setLogImp(Xlog())

        Log.v("VendorLogInitializer", "init success")

        // Log.appenderClose()
        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
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