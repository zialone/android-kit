package com.hcanyz.android_kit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.genzon.reception.pad.widget.views.stateview.customizeStateEmpty
import com.hcanyz.android_kit.vendor.config.BuildConfig
import com.hcanyz.android_kit.vendor.config.IZConfig
import com.hcanyz.android_kit.vendor.log.ZLog
import com.hcanyz.android_kit.vendor.storage.uniqueKeyUntilUninstalled
import com.hcanyz.android_kit.vendor.storage.zzDownloadImage2MediaStore
import com.hcanyz.android_kit.vendor.storage.zzGetExternalFilesDir
import com.hcanyz.android_kit.widget.res.ThemeSwitchTransitionActivity
import com.kennyc.view.MultiStateView
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "TAG.Main"
    }

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView()
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
        msv_test.customizeStateEmpty("$title")

        msv_test.viewState = MultiStateView.ViewState.LOADING

        view.postDelayed({
            msv_test.viewState = MultiStateView.ViewState.CONTENT
        }, 1500)
    }

    private fun webView() {
        webview_test.loadUrl("https://cn.bing.com/")
    }

    fun storage(view: View) {
        try {
            ZLog.d(
                TAG,
                view.context.zzGetExternalFilesDir().absolutePath ?: ""
            )
        } catch (e: Exception) {
            AndPermission.with(this).runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .rationale { _, _, executor ->
                    executor.execute()
                }
                .onGranted { }
                .onDenied { }
                .start()
        }

        view.context.zzDownloadImage2MediaStore(
            url = "https://avatars2.githubusercontent.com/u/8407922?s=60&v=4",
            displayName = ""
        )
    }
}