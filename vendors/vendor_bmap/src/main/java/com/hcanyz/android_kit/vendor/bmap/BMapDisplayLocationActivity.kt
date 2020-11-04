package com.hcanyz.android_kit.vendor.bmap

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.activity_b_map_display_location.*


class BMapDisplayLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.zzBMapLazyInit()
        setContentView(R.layout.activity_b_map_display_location)

        bmap_test.applyLifecycle(this)

        bmap_test.map.setMapStatus(MapStatusUpdateFactory.newLatLng(LatLng(22.61667, 114.06667)))

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
                bmap_test.map.clear()
                if (poiResult.error === SearchResult.ERRORNO.NO_ERROR) {

                    poiResult.allPoi?.forEachIndexed { index, poi ->
                        if (index == 0) {
                            bmap_test.map.setMapStatus(MapStatusUpdateFactory.newLatLng(poi.location))
                        }
                        bmap_test.map.addOverlay(DotOptions().center(poi.location))
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

        et_test.addTextChangedListener(object : TextWatcher {
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