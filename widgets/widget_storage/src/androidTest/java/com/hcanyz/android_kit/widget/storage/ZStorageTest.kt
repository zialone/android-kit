package com.hcanyz.android_kit.widget.storage

import android.content.Context
import android.os.SystemClock
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hcanyz.android_kit.widget.storage.shared.SHARED_COMMON_SP_NAME
import com.hcanyz.android_kit.widget.storage.shared.getSharedPreferences
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.concurrent.thread

@RunWith(AndroidJUnit4::class)
class ZStorageTest {

    @Test
    @Throws(Exception::class)
    fun uniqueKeyUntilUninstalled() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        context.getSharedPreferences(SHARED_COMMON_SP_NAME, true).edit().clear()

        val size = 10

        val map = mutableMapOf<Int, String>()
        for (i in 0 until size) {
            thread {
                map[i] = ZStorage.uniqueKeyUntilUninstalled(context)
            }
        }

        while (true) {
            if (map.size == size) {
                val filter = map.filter { it.value != map[0] }

                assertThat(filter.size, equalTo(0))

                break
            }
            SystemClock.sleep(20)
        }
    }
}