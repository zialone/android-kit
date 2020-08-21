package com.hcanyz.android_kit.feature.template

import android.content.Context
import android.widget.Toast
import com.hcanyz.android_kit.feature.template.api.IApiTemplate
import com.sankuai.waimai.router.annotation.RouterService


@RouterService(
    interfaces = [IApiTemplate::class],
    key = ["default"],
    singleton = true
)
class ApiTemplateImpl : IApiTemplate {

    override fun hello(context: Context) {
        Toast.makeText(context, "hello: ${ApiTemplateImpl::class.java}", Toast.LENGTH_SHORT).show()
    }
}