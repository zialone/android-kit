package com.hcanyz.android_kit.vendor.bmap

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.map.DotOptions
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.hcanyz.android_kit.vendor.bmap.databinding.ActivityBMapDisplayLocationBinding
import java.lang.ref.WeakReference


class BMapDisplayLocationActivity : AppCompatActivity(R.layout.activity_b_map_display_location) {

    private val binding by viewBinding(ActivityBMapDisplayLocationBinding::bind)

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        this.zzBMapLazyInit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bmapTest.applyLifecycle(this)

        binding.bmapTest.map.setMapStatus(
            MapStatusUpdateFactory.newLatLng(
                LatLng(
                    22.61667,
                    114.06667
                )
            )
        )

        PermissionUtils.permission(PermissionConstants.PHONE, PermissionConstants.LOCATION)
            .rationale { _, shouldRequest -> shouldRequest.again(true) }
            .callback { isAllGranted, _, deniedForever, _ ->
                if (isAllGranted) {
                    findLocation()
                } else if (deniedForever.isNotEmpty()) {
                    PermissionUtils.launchAppDetailsSettings()
                }
            }
            .request()

        val poiSearch = PoiSearch.newInstance()
        poiSearch.applyLifecycle(this)

        val listener: OnGetPoiSearchResultListener = object : OnGetPoiSearchResultListener {
            override fun onGetPoiResult(poiResult: PoiResult) {
                binding.bmapTest.map.clear()
                if (poiResult.error === SearchResult.ERRORNO.NO_ERROR) {

                    poiResult.allPoi?.forEachIndexed { index, poi ->
                        if (index == 0) {
                            binding.bmapTest.map.setMapStatus(MapStatusUpdateFactory.newLatLng(poi.location))
                        }
                        binding.bmapTest.map.addOverlay(DotOptions().center(poi.location))
                    }
                }
            }

            override fun onGetPoiDetailResult(poiDetailSearchResult: PoiDetailSearchResult) {}
            override fun onGetPoiIndoorResult(poiIndoorResult: PoiIndoorResult) {}

            //废弃
            @Suppress("DEPRECATION")
            override fun onGetPoiDetailResult(poiDetailResult: PoiDetailResult) {
            }
        }
        poiSearch.setOnGetPoiSearchResultListener(listener)

        binding.etTest.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                poiSearch.searchInCity(
                    PoiCitySearchOption()
                        .city("深圳") //必填
                        .keyword(s.toString()) //必填
                        .pageNum(0)
                        .pageCapacity(100)
                )
            }
        })
    }

    private fun findLocation() {
        binding.bmapTest.map.isMyLocationEnabled = true

        val locationClient = LocationClient(this)
        locationClient.applyLifecycle(this)

        val option = LocationClientOption()
        option.isOpenGps = true

        option.setCoorType(CoordType.BD09LL.literal)

        option.setLocationPurpose(LocationClientOption.BDLocationPurpose.Transport)

        locationClient.locOption = option

        val myLocationListener = MyLocationListener(this)
        locationClient.registerLocationListener(myLocationListener)

        locationClient.start()
    }

    private class MyLocationListener(activity: BMapDisplayLocationActivity) :
        BDAbstractLocationListener() {

        private val reference = WeakReference(activity)

        override fun onReceiveLocation(location: BDLocation?) {
            val activity = reference.get() ?: return
            if (location == null || activity.isDestroyed) {
                return
            }
            val locData = MyLocationData.Builder()
                .accuracy(location.radius)
                .direction(location.direction)
                .latitude(location.latitude)
                .longitude(location.longitude)
                .build()
            activity.binding.bmapTest.map.setMyLocationData(locData)
        }
    }
}