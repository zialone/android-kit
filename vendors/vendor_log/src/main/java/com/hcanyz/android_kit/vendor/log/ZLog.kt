package com.hcanyz.android_kit.vendor.log

import com.tencent.mars.xlog.Log

/**
 * 日志记录原则:
 * 1. 鉴权数据不能入库 level>=LEVEL_INFO
 * 2. 隐私数据不能入库 level>=LEVEL_INFO
 * 3. 无用日志不能入库 level>=LEVEL_INFO
 */
class ZLog {
    companion object {
        private val timeMap by lazy { hashMapOf<String, Long>() }

        fun dTime(tag: String, format: String, vararg obj: Any?) {
            val time = timeMap[tag]
            if (time == null) {
                d(tag, "begin time: ${System.currentTimeMillis()}\n$format", obj)
            } else {
                d(tag, "interval last time: ${System.currentTimeMillis() - time}\n$format", obj)
            }
            timeMap[tag] = System.currentTimeMillis()
        }

        fun f(tag: String, format: String, vararg obj: Any?) {
            try {
                Log.f(tag, format, obj)
            } catch (e: Exception) {
                Log.f(tag, format)
            }
        }

        fun e(tag: String, format: String, vararg obj: Any?) {
            try {
                Log.e(tag, format, obj)
            } catch (e: Exception) {
                Log.e(tag, format)
            }
        }

        fun w(tag: String, format: String, vararg obj: Any?) {
            try {
                Log.w(tag, format, obj)
            } catch (e: Exception) {
                Log.w(tag, format)
            }
        }

        fun i(tag: String, format: String, vararg obj: Any?) {
            try {
                Log.i(tag, format, obj)
            } catch (e: Exception) {
                Log.i(tag, format)
            }
        }

        fun d(tag: String, format: String, vararg obj: Any?) {
            try {
                Log.d(tag, format, obj)
            } catch (e: Exception) {
                Log.d(tag, format)
            }
        }

        fun v(tag: String, format: String, vararg obj: Any?) {
            try {
                Log.v(tag, format, obj)
            } catch (e: Exception) {
                Log.v(tag, format)
            }
        }

        fun printErrStackTrace(tag: String, tr: Throwable?, format: String, vararg obj: Any?) {
            try {
                Log.printErrStackTrace(tag, tr, format, obj)
            } catch (e: Exception) {
                Log.printErrStackTrace(tag, tr, format)
            }
        }

        fun flush(isSync: Boolean) {
            if (isSync) {
                Log.appenderFlushSync(false)
            } else {
                Log.appenderFlush()
            }
        }
    }
}