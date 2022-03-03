package com.hcanyz.android_kit.feature.template.api

import com.sankuai.waimai.router.Router

interface IApiTemplate {
    companion object {
        fun api(): IApiTemplate {
            return Router.getService(IApiTemplate::class.java)
        }
    }
}

interface IApiInternalTemplate {
    companion object {
        fun api(): IApiInternalTemplate {
            return Router.getService(IApiInternalTemplate::class.java)
        }
    }
}