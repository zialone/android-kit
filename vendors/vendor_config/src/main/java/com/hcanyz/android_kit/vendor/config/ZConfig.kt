package com.hcanyz.android_kit.vendor.config

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.startup.AppInitializer


interface IZConfig {
    companion object {
        fun getInstance(context: Context): IZConfig {
            return AppInitializer.getInstance(context)
                .initializeComponent(ZVendorConfigInitializer::class.java)
        }
    }

    /**
     * 编译时间
     */
    val buildTime: Long

    /**
     * 编译时 git HEAD hash
     */
    val buildGitHash: String

    /**
     * 包真实类型
     * 需要注意，如果vendor_config为aar依赖，buildType在打包时就被确定
     * 约定了生产环境 [buildType] 固定为 release
     */
    val buildType: String

    /**
     * 用于切换环境
     * 初始值为 [buildType]
     */
    var mutableBuildType: String

    /**
     * 返回一个 运行环境（dev,test,pre,release）标识，用于存储目录、数据库目录等区分运行环境
     * 这个值是根据 [mutableBuildType] 变化的
     * [releaseConcise] 生产环境标识自定义，默认无标识
     */
    fun envMark(releaseConcise: String = ""): String

    var canDebug: Boolean
    var canLog: Boolean

    fun canDebugLiveData(): LiveData<Boolean>
    fun canLogLiveData(): LiveData<Boolean>
}

internal class ZConfigImpl(private val context: Context, appDebuggable: Boolean) : IZConfig {

    override val buildTime: Long = BuildConfig.BUILD_TIME

    override val buildGitHash: String = BuildConfig.BUILD_GIT_HASH

    override val buildType: String = BuildConfig.BUILD_TYPE

    override var mutableBuildType: String = buildType

    override fun envMark(releaseConcise: String): String {
        if (mutableBuildType == "release") {
            return releaseConcise
        }
        return mutableBuildType
    }

    companion object {
        private const val SP_SWITCH = "sp.vendor.config"
        private const val SP_KEY_SWITCH_CAN_DEBUG = "sp.key.vendor.config.switch.canDebug"
        private const val SP_KEY_SWITCH_CAN_LOG = "sp.key.vendor.config.switch.canLog"
    }

    private fun getSharedPreferences(): SharedPreferences {
        val envMark = envMark()
        val name = SP_SWITCH

        val nameFinal = if (envMark.isBlank()) name else "$envMark-$name"

        return context.getSharedPreferences(nameFinal, Context.MODE_PRIVATE)
    }

    private fun readValue(key: String, default: Boolean): Boolean {
        val sp = getSharedPreferences()
        if (sp.contains(key)) {
            return sp.getBoolean(key, default)
        }
        return default
    }

    private fun saveValue(key: String, value: Boolean) {
        getSharedPreferences().edit {
            putBoolean(key, value)
        }
    }

    // canDebug canLog 需要放到 envMark() 下面。 因为初始化时有用到 envMark() 方法，放在上面有些值还没有初始化到
    override var canDebug = readValue(SP_KEY_SWITCH_CAN_DEBUG, appDebuggable)
        set(value) {
            saveValue(SP_KEY_SWITCH_CAN_DEBUG, value)
            field = value
            canDebugLiveData.postValue(value)
        }

    override var canLog = readValue(SP_KEY_SWITCH_CAN_LOG, appDebuggable)
        set(value) {
            saveValue(SP_KEY_SWITCH_CAN_LOG, value)
            field = value
            canLogLiveData.postValue(value)
        }

    private val canDebugLiveData by lazy { MutableLiveData(canDebug) }
    private val canLogLiveData by lazy { MutableLiveData(canLog) }

    override fun canDebugLiveData(): LiveData<Boolean> {
        return canDebugLiveData
    }

    override fun canLogLiveData(): LiveData<Boolean> {
        return canLogLiveData
    }
}