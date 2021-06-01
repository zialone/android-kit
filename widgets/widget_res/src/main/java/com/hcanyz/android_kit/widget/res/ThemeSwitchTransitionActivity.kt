package com.hcanyz.android_kit.widget.res

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.PixelCopy
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class ThemeSwitchTransitionActivity : AppCompatActivity() {

    companion object {
        private var screenshot: Bitmap? = null

        @Suppress("DEPRECATION")
        fun transition(activity: AppCompatActivity) {
            val window = activity.window
            val rootView = window.decorView.rootView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val bitmap =
                    Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
                val locationOfRootView = IntArray(2)
                rootView.getLocationInWindow(locationOfRootView)
                PixelCopy.request(
                    window,
                    Rect(
                        locationOfRootView[0],
                        locationOfRootView[1],
                        locationOfRootView[0] + rootView.width,
                        locationOfRootView[1] + rootView.height
                    ),
                    bitmap,
                    { copyResult ->
                        if (copyResult == PixelCopy.SUCCESS) {
                            screenshot = bitmap
                            startThemeSwitchTransition(activity)
                        }
                    },
                    Handler()
                )
            } else {
                rootView.isDrawingCacheEnabled = true
                screenshot =
                    Bitmap.createBitmap(rootView.drawingCache)
                rootView.isDrawingCacheEnabled = false
                startThemeSwitchTransition(activity)
            }
        }

        private fun startThemeSwitchTransition(activity: AppCompatActivity) {
            val intent =
                Intent(activity, ThemeSwitchTransitionActivity::class.java).apply {
                    putExtra(
                        "ThemeSwitchTransition_systemUiVisibility",
                        activity.window.decorView.systemUiVisibility
                    )
                }
            activity.startActivity(intent)
            activity.overridePendingTransition(0, 0)
            Handler().postDelayed({
                activity.recreate()
            }, 50)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 全屏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.decorView.systemUiVisibility =
            intent.getIntExtra(
                "ThemeSwitchTransition_systemUiVisibility",
                window.decorView.systemUiVisibility
            )

        val imageView = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        setContentView(imageView)

        val screenshot = screenshot

        if (screenshot != null && !screenshot.isRecycled) {
            imageView.setImageBitmap(screenshot)
        }

        Handler().postDelayed({
            finish()
            overridePendingTransition(0, android.R.anim.fade_out)
        }, 100)
    }

    override fun onBackPressed() {
    }

    override fun onDestroy() {
        super.onDestroy()
        screenshot?.recycle()
        screenshot = null
    }
}