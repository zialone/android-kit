package com.hcanyz.android_kit.widget.storage

import android.content.Context
import androidx.core.content.edit
import com.hcanyz.android_kit.vendor.log.ZLog
import com.hcanyz.android_kit.widget.storage.shared.SHARED_COMMON_SP_NAME
import com.hcanyz.android_kit.widget.storage.shared.getSharedPreferences
import java.util.*

class ZStorage {
    companion object {
        private const val UNIQUE_KEY_UNTIL_UNINSTALLED =
            "widget.storage.sp.common.uniqueKeyUntilUninstalled"

        fun uniqueKeyUntilUninstalled(context: Context): String {
            val sp = context.getSharedPreferences(SHARED_COMMON_SP_NAME, true)
            var uniqueKeyUntilUninstalled = sp.getString(UNIQUE_KEY_UNTIL_UNINSTALLED, null)

            if (uniqueKeyUntilUninstalled == null) {
                ZLog.d("ZStorage", "uniqueKeyUntilUninstalled,not found,generate")

                uniqueKeyUntilUninstalled = UUID.randomUUID().toString()
                sp.edit(true) {
                    putString(UNIQUE_KEY_UNTIL_UNINSTALLED, uniqueKeyUntilUninstalled)
                }
            }
            return uniqueKeyUntilUninstalled
        }
    }
}