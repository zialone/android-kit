package com.hcanyz.android_kit.feature.template

import com.hcanyz.android_kit.feature.template.api.IApiInternalTemplate
import com.hcanyz.android_kit.feature.template.api.IApiTemplate
import com.sankuai.waimai.router.annotation.RouterService


@RouterService(
    interfaces = [IApiTemplate::class, IApiInternalTemplate::class],
    key = ["default"],
    singleton = true
)
class ApiTemplateImpl : IApiTemplate, ApiInternalTemplateImpl {
}


interface ApiInternalTemplateImpl : IApiInternalTemplate {
}