package com.hcanyz.android_kit.vendor.storage.shared

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.hcanyz.android_kit.vendor.config.IZConfig
import com.hcanyz.android_kit.vendor.log.ZLog

private const val SHARED_COMMON_SP_NAME = "sp.vendor.storage.common"
private const val SHARED_COMMON_ENCRYPTED_SP_NAME = "sp.vendor.storage.common.encrypted"

private val checkShared by lazy { mutableMapOf<String, Boolean>() }

/**
 * 公共未加密 SharedPreferences
 * @receiver Context
 * @return SharedPreferences
 */
fun Context.getCommonSharedPreferences(): SharedPreferences {
    return getSharedPreferences(SHARED_COMMON_SP_NAME, false)
}

/**
 * 公共已加密 SharedPreferences
 * @receiver Context
 * @return SharedPreferences
 */
fun Context.getCommonEncryptedSharedPreferences(): SharedPreferences {
    return getSharedPreferences(SHARED_COMMON_ENCRYPTED_SP_NAME, true)
}

/**
 * 获取一个 SharedPreferences，同一个 [name] 的 [encrypted] 需要保持一致
 * @receiver Context
 * @param name String
 * @param encrypted Boolean
 * @return SharedPreferences
 */
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