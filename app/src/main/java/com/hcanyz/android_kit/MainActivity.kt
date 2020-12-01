package com.hcanyz.android_kit

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.hcanyz.android_kit.vendor.bmap.BMapDisplayLocationActivity
import com.hcanyz.android_kit.vendor.config.BuildConfig
import com.hcanyz.android_kit.vendor.config.IZConfig
import com.hcanyz.android_kit.vendor.http.ZService
import com.hcanyz.android_kit.vendor.log.ZLog
import com.hcanyz.android_kit.vendor.storage.db.ZCommonPond
import com.hcanyz.android_kit.vendor.storage.db.ZDbCommon
import com.hcanyz.android_kit.vendor.storage.uniqueKeyUntilUninstalled
import com.hcanyz.android_kit.vendor.storage.zzDownloadImage2MediaStore
import com.hcanyz.android_kit.vendor.storage.zzGetExternalFilesDir
import com.hcanyz.android_kit.vendor.utils.idle.IdleTaskManager
import com.hcanyz.android_kit.vendor.utils.utilcodex.SimpleTaskRunnable
import com.hcanyz.android_kit.vendor.views.stateview.customizeStateEmpty
import com.hcanyz.android_kit.vendor.views.stateview.customizeStateError
import com.hcanyz.android_kit.widget.core.EvCoreConfigManager
import com.hcanyz.android_kit.widget.res.ThemeSwitchTransitionActivity
import com.hcanyz.environmentvariable.setting.ui.EvSwitchActivity
import com.kennyc.view.MultiStateView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "TAG.Main"
    }

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var zService: ZService

    @Inject
    lateinit var zDbCommon: ZDbCommon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        immersionBar {
            transparentBar()
            fitsSystemWindows(true)
            statusBarDarkFont(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK != Configuration.UI_MODE_NIGHT_YES)
        }

        webView()
    }

    private fun webView() {
        IdleTaskManager().addTask {
            webview_test.loadUrl("https://cn.bing.com/")
        }.start()
    }

    fun hello(view: View) {
        view.background

        ZLog.dTime(TAG, "hello")

        ThemeSwitchTransitionActivity.transition(this)

        ZLog.w(TAG, BuildConfig.BUILD_GIT_HASH)

        ZLog.i(TAG, this.uniqueKeyUntilUninstalled())

        ZLog.i(TAG, retrofit.toString())

        ZLog.flush(true)
        ZLog.dTime(TAG, "bye")
    }

    @SuppressLint("SetTextI18n")
    fun logSwitch(view: View) {
        val izConfig = IZConfig.getInstance(view.context)

        izConfig.canLog = !izConfig.canLog

        tv_logSwitch.text = "logSwitch: ${izConfig.canLog}"
    }

    fun stateView(view: View) {
        view.context

        msv_test.customizeStateEmpty("$title")
        msv_test.customizeStateError("$title") { stateView(view) }

        msv_test.viewState = MultiStateView.ViewState.LOADING

        zService
            .get("https://cn.bing.com/")
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    msv_test.viewState = MultiStateView.ViewState.CONTENT
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    msv_test.viewState = MultiStateView.ViewState.ERROR
                }
            })
    }

    fun api(view: View) {
        view.context

        zService
            .post("https://cn.bing.com/")
        zService
            .postJson("https://cn.bing.com/", mapOf("10" to arrayListOf(1)))
        zService
            .postForm("https://cn.bing.com/", mapOf("10" to arrayListOf(1)))

        zService.get("demo/1").enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ToastUtils.showShort(response.body()?.string())
            }
        })
    }

    fun storage(view: View) {
        ThreadUtils.executeByIo(SimpleTaskRunnable {
            zDbCommon.commonPondDao()
                .insertOrUpdate(ZCommonPond("test", UUID.randomUUID().toString()))
        })

        try {
            ZLog.d(
                TAG,
                view.context.zzGetExternalFilesDir().absolutePath ?: ""
            )

            view.context.zzDownloadImage2MediaStore(
                url = "https://avatars2.githubusercontent.com/u/8407922?s=60&v=4",
                displayName = ""
            )
        } catch (e: Exception) {
            PermissionUtils.permission(PermissionConstants.STORAGE)
                .rationale { _, shouldRequest -> shouldRequest.again(true) }
                .callback { isAllGranted, _, deniedForever, _ ->
                    if (!isAllGranted && deniedForever.isNotEmpty()) {
                        PermissionUtils.launchAppDetailsSettings()
                    }
                }
                .request()
        }
    }

    fun bmap(view: View) {
        startActivity(Intent(view.context, BMapDisplayLocationActivity::class.java))
    }

    fun evSwitch(view: View) {
        EvSwitchActivity.skip(view.context, arrayListOf(EvCoreConfigManager::class.java))
    }
}