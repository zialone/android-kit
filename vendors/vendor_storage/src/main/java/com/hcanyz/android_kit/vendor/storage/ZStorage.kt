package com.hcanyz.android_kit.vendor.storage

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.core.content.edit
import androidx.room.Room
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.hcanyz.android_kit.vendor.log.ZLog
import com.hcanyz.android_kit.vendor.storage.db.ZDbCommon
import com.hcanyz.android_kit.vendor.storage.shared.getCommonEncryptedSharedPreferences
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.kotlin.enqueue
import java.io.File
import java.util.*


private const val UNIQUE_KEY_UNTIL_UNINSTALLED =
    "sp.key.vendor.storage.common.uniqueKeyUntilUninstalled"

/**
 * 获取一个uuid。 这个值会一直存在，直到应用被卸载
 * 常用于数据库密码或者deviceId
 */
fun Context.uniqueKeyUntilUninstalled(): String {
    val sp = getCommonEncryptedSharedPreferences()
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

/**
 * 获取sdcard file目录
 * 这个方法会存在一定误判，有些国产系统，没有WRITE_EXTERNAL_STORAGE权限时也会有ExternalFiles权限
 * @see "https://developer.android.google.cn/training/data-storage/shared/media#request-permissions"
 * @param type 子目录 constants for a subdirectory:
 * {@link android.os.Environment#DIRECTORY_MUSIC},
 * {@link android.os.Environment#DIRECTORY_PODCASTS},
 * {@link android.os.Environment#DIRECTORY_RINGTONES},
 * {@link android.os.Environment#DIRECTORY_ALARMS},
 * {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
 * {@link android.os.Environment#DIRECTORY_PICTURES}, or
 * {@link android.os.Environment#DIRECTORY_MOVIES}.
 * @param unavailableDowngrade 是否接受sdcard不可用降级处理，降级后会存储到 /data/data/files下
 * @throws IllegalStateException
 */
fun Context.zzGetExternalFilesDir(
    type: String? = null,
    unavailableDowngrade: Boolean = false
): File {
    if (PermissionUtils.isGranted(*PermissionConstants.getPermissions(PermissionConstants.STORAGE))) {
        val externalFilesDir = getExternalFilesDir(type)
        if (externalFilesDir != null) {
            return externalFilesDir
        }
    }
    if (unavailableDowngrade) {
        return File(filesDir, type ?: "")
    }
    throw IllegalStateException("external storage unavailable")
}

fun Context.zzDownloadImage2MediaStore(url: String, displayName: String) {
    val resolver = applicationContext.contentResolver

    val audioCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val newSongDetails = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
    }

    val imageUri = resolver.insert(audioCollection, newSongDetails)

    if (imageUri != null) {
        val task = DownloadTask.Builder(url, imageUri).build()
        task.enqueue { _, _, _ -> }
    }
}

fun Context.zzCreateCommonDb(): ZDbCommon {
    return Room.databaseBuilder(
        this, ZDbCommon::class.java, ZDbCommon::class.java.simpleName
    ).build()
}