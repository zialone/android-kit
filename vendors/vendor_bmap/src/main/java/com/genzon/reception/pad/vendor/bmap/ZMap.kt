package com.genzon.reception.pad.vendor.bmap

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.baidu.location.LocationClient
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.MapView

fun Context.zzBMapLazyInit(coordType: CoordType = CoordType.BD09LL) {
    SDKInitializer.initialize(this.applicationContext)
    SDKInitializer.setCoordType(coordType)
}

val CoordType.literal: String
    get() {
        return when (this) {
            CoordType.BD09LL -> {
                "bd09ll"
            }
            CoordType.GCJ02 -> {
                "gcj02"
            }
            else -> {
                "unknown"
            }
        }
    }

fun MapView.applyLifecycle(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    this@applyLifecycle.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    this@applyLifecycle.onPause()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    this@applyLifecycle.onDestroy()
                    lifecycleOwner.lifecycle.removeObserver(this)
                }

                else -> {
                }
            }
        }
    })
}

fun LocationClient.applyLifecycle(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                this@applyLifecycle.stop()
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        }
    })
}