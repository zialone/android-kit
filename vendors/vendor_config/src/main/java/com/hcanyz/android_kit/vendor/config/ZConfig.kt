package com.hcanyz.android_kit.vendor.config

import android.content.Context
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

    var canDebug: Boolean
    var canLog: Boolean

    fun canDebugLiveData(): LiveData<Boolean>
    fun canLogLiveData(): LiveData<Boolean>

    // 编译时间
    val buildTime: Long

    // git HEAD hash
    val buildGitHash: String

    // 包真实类型
    // 需要注意，如果vendor_config为aar依赖，buildType在打包时就被确定
    val buildType: String

    // 切换后类型
    var mutableBuildType: String

    /**
     * 返回一个 运行环境（dev,test,pre,release）标识，用于存储目录、数据库目录等区分运行环境
     * 这个值是根据 [mutableBuildType] + ext(预留) 变化的
     */
    fun envMark(releaseConcise: String = ""): String
}

internal class ZConfigImpl(canDebug: Boolean, canLog: Boolean) :
    IZConfig {

    override var canDebug = canDebug
        set(value) {
            field = value
            canDebugLiveData.postValue(value)
        }

    override var canLog = canLog
        set(value) {
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

    override val buildTime: Long = BuildConfig.BUILD_TIME

    override val buildGitHash: String = BuildConfig.BUILD_GIT_HASH

    override val buildType: String = BuildConfig.BUILD_TYPE

    override var mutableBuildType: String = buildType

    override fun envMark(releaseConcise: String): String {
        if (releaseConcise.isNotBlank() && mutableBuildType == "release") {
            return releaseConcise
        }
        return mutableBuildType
    }
}