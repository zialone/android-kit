package com.hcanyz.android_kit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.genzon.reception.pad.widget.views.stateview.customizeStateEmpty
import com.hcanyz.android_kit.feature.template.api.IApiTemplate
import com.hcanyz.android_kit.feature.template.api.IDiTemplateProvided
import com.hcanyz.android_kit.vendor.config.BuildConfig
import com.hcanyz.android_kit.vendor.config.IZConfig
import com.hcanyz.android_kit.vendor.log.ZLog
import com.hcanyz.android_kit.widget.res.ThemeSwitchTransitionActivity
import com.hcanyz.android_kit.widget.storage.uniqueKeyUntilUninstalled
import com.kennyc.view.MultiStateView
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

    @Inject
    lateinit var iDiTemplateProvided: IDiTemplateProvided

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun hello(view: View) {

        ZLog.dTime(TAG, "hello")

        IApiTemplate.api().hello(view.context)

        ThemeSwitchTransitionActivity.transition(this)

        ZLog.w(TAG, BuildConfig.BUILD_GIT_HASH)

        ZLog.i(TAG, this.uniqueKeyUntilUninstalled())

        ZLog.i(TAG, retrofit.toString())
        ZLog.i(TAG, iDiTemplateProvided.toString())

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
}