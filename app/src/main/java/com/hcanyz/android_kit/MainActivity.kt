package com.hcanyz.android_kit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hcanyz.android_kit.feature.template.api.IApiTemplate
import com.hcanyz.android_kit.vendor.config.BuildConfig
import com.hcanyz.android_kit.vendor.config.IZConfig
import com.hcanyz.android_kit.vendor.log.ZLog
import com.hcanyz.android_kit.widget.res.ThemeSwitchTransitionActivity
import com.hcanyz.android_kit.widget.storage.ZStorage
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
    }

    fun hello(view: View) {
        ZLog.dTime(TAG, "hello")

        IApiTemplate.api().hello(view.context)

        ThemeSwitchTransitionActivity.transition(this)

        ZLog.dTime(TAG, "bye")

        ZLog.w(TAG, BuildConfig.BUILD_GIT_HASH)

        ZLog.i(TAG, ZStorage.uniqueKeyUntilUninstalled(this))

        ZLog.i(TAG, retrofit.toString())

        ZLog.flush(true)
    }

    @SuppressLint("SetTextI18n")
    fun logSwitch(view: View) {
        val izConfig = IZConfig.getInstance(view.context)

        izConfig.canLog = !izConfig.canLog

        tv_logSwitch.text = "logSwitch: ${izConfig.canLog}"
    }
}