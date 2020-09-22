package com.hcanyz.android_kit.vendor.storage

import android.content.Context
import androidx.core.content.edit
import com.hcanyz.android_kit.vendor.log.ZLog
import com.hcanyz.android_kit.vendor.storage.shared.SHARED_COMMON_ENCRYPTED_SP_NAME
import com.hcanyz.android_kit.vendor.storage.shared.getSharedPreferences
import java.util.*


private const val UNIQUE_KEY_UNTIL_UNINSTALLED =
    "vendor.storage.sp.common.uniqueKeyUntilUninstalled"

/**
 * 获取一个uuid。 这个值会一直存在，直到应用被卸载
 * 常用于数据库密码或者deviceId
 */
fun Context.uniqueKeyUntilUninstalled(): String {
    val sp = getSharedPreferences(SHARED_COMMON_ENCRYPTED_SP_NAME, true)
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