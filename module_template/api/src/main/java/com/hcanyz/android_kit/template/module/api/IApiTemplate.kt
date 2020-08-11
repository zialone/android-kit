package com.hcanyz.android_kit.template.module.api

import android.content.Context
import com.sankuai.waimai.router.Router

interface IApiTemplate {
    companion object {
        fun api(): IApiTemplate {
            return Router.getService(IApiTemplate::class.java, "default")
        }
    }

    fun hello(context: Context)
}