package com.hcanyz.android_kit.vendor.utils.idle

import android.os.Looper
import android.os.MessageQueue.IdleHandler
import java.util.*


class IdleTaskManager {
    private val idleTaskQueue: Queue<() -> Unit> = LinkedList()

    private val idleHandler = IdleHandler {
        if (idleTaskQueue.size > 0) {
            val idleTask: (() -> Unit)? = idleTaskQueue.poll()
            idleTask?.invoke()
        }
        !idleTaskQueue.isEmpty()
    }

    /**
     * task运行在主线程
     */
    fun addTask(task: () -> Unit): IdleTaskManager {
        idleTaskQueue.add(task)
        return this
    }

    fun start() {
        Looper.myQueue().addIdleHandler(idleHandler)
    }
}
