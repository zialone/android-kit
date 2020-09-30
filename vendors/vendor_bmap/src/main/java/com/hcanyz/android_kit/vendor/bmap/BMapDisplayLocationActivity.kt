package com.hcanyz.android_kit.vendor.bmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_b_map_display_location.*


class BMapDisplayLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.zzBMapLazyInit()
        setContentView(R.layout.activity_b_map_display_location)

        bmap_test.applyLifecycle(this)

        bmap_test.map.setMapStatus(MapStatusUpdateFactory.newLatLng(LatLng(31.027, 121.281)))

        val runtime = AndPermission.with(this).runtime()
        runtime
            .permission(
                Permission.READ_PHONE_STATE,
                Permission.ACCESS_COARSE_LOCATION,
                Permission.ACCESS_FINE_LOCATION
            )
            .onGranted {
                findLocation()
            }
            .onDenied {
                runtime.setting().start(0)
            }
            .start()
    }

    private fun findLocation() {
        bmap_test.map.isMyLocationEnabled = true

        val locationClient = LocationClient(this)
        locationClient.applyLifecycle(this)

        val option = LocationClientOption()
        option.isOpenGps = true

        option.setCoorType(CoordType.BD09LL.literal)

        option.setLocationPurpose(LocationClientOption.BDLocationPurpose.Transport)

        locationClient.locOption = option

        val myLocationListener = MyLocationListener()
        locationClient.registerLocationListener(myLocationListener)

        locationClient.start()
    }

    private inner class MyLocationListener() : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            if (location == null || isDestroyed) {
                return
            }
            val locData = MyLocationData.Builder()
                .accuracy(location.radius)
                .direction(location.direction)
                .latitude(location.latitude)
                .longitude(location.longitude)
                .build()
            bmap_test.map.setMyLocationData(locData)
        }
    }
}