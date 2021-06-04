package com.hcanyz.android_kit.vendor.bmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hcanyz.android_kit.vendor.bmap.databinding.ActivityBMapSelectLocationBinding

class BMapSelectLocationActivity : AppCompatActivity(R.layout.activity_b_map_select_location) {

    private val binding by viewBinding(ActivityBMapSelectLocationBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}