package com.hcanyz.android_kit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hcanyz.android_kit.template.api.IApiTemplate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun hello(view: View) {
        IApiTemplate.api().hello(view.context)
    }
}