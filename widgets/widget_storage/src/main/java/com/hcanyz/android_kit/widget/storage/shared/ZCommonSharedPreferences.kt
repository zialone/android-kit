package com.hcanyz.android_kit.widget.storage.shared

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.hcanyz.android_kit.vendor.config.IZConfig
import com.hcanyz.android_kit.vendor.log.ZLog

const val SHARED_COMMON_SP_NAME = "widget.storage.sp.common"

private val checkShared by lazy { mutableMapOf<String, Boolean>() }

fun Context.getSharedPreferences(name: String, encrypted: Boolean = false): SharedPreferences {
    val izConfig = IZConfig.getInstance(this)

    val envMark = izConfig.envMark()
    val nameFinal = if (envMark.isBlank()) name else "$envMark-$name"

    ZLog.d(
        "CommonSharedPreferences",
        "getSharedPreferences,nameFinal:$nameFinal,encrypted:$encrypted,SDK_INT:${Build.VERSION.SDK_INT}"
    )

    // 检测多次sp获取时 encrypted是不是相同
    if (izConfig.canDebug) {
        val get = checkShared[nameFinal]
        if (get != null && get != encrypted) {
            throw IllegalStateException("When getting getSharedPreferences:$name, a different encrypted is used")
        }
    }
    checkShared[nameFinal] = encrypted

    if (encrypted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences
            .create(
                nameFinal,
                masterKeyAlias,
                this,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
    }
    return getSharedPreferences(nameFinal, Context.MODE_PRIVATE)
}