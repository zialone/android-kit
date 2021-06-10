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
        /**
         * tag 固定前缀
         */
        private const val TAG_PREFIX = "ZLog-"

        private val timeMap by lazy { hashMapOf<String, Long>() }

        fun dTime(tag: String, log: String) {
            val time = timeMap[tag]
            if (time == null) {
                d(tag, "begin time: ${System.currentTimeMillis()}\n$log")
            } else {
                d(tag, "interval last time: ${System.currentTimeMillis() - time}\n$log")
            }
            timeMap[tag] = System.currentTimeMillis()
        }

        fun f(tag: String, log: String) {
            Log.f(tag.prefix(), log)
        }

        fun e(tag: String, log: String) {
            Log.e(tag.prefix(), log)
        }

        fun w(tag: String, log: String) {
            Log.w(tag.prefix(), log)
        }

        fun i(tag: String, log: String) {
            Log.i(tag.prefix(), log)
        }

        fun d(tag: String, log: String) {
            Log.d(tag.prefix(), log)
        }

        fun v(tag: String, log: String) {
            Log.v(tag.prefix(), log)
        }

        fun printErrStackTrace(tag: String, tr: Throwable?, log: String) {
            Log.printErrStackTrace(tag.prefix(), tr, log)
        }

        fun flush(isSync: Boolean) {
            if (isSync) {
                Log.appenderFlushSync(false)
            } else {
                Log.appenderFlush()
            }
        }

        private fun String.prefix(): String {
            return TAG_PREFIX + this
        }
    }
}