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
            Log.f(tag, format, obj)
        }

        fun e(tag: String, format: String, vararg obj: Any?) {
            Log.e(tag, format, obj)
        }

        fun w(tag: String, format: String, vararg obj: Any?) {
            Log.w(tag, format, obj)
        }

        fun i(tag: String, format: String, vararg obj: Any?) {
            Log.i(tag, format, obj)
        }

        fun d(tag: String, format: String, vararg obj: Any?) {
            Log.d(tag, format, obj)
        }

        fun v(tag: String, format: String, vararg obj: Any?) {
            Log.v(tag, format, obj)
        }

        fun printErrStackTrace(tag: String, tr: Throwable?, format: String, vararg obj: Any?) {
            Log.printErrStackTrace(tag, tr, format, obj)
        }

        fun flush(isSync: Boolean) {
            Log.appenderFlush(isSync)
        }
    }
}