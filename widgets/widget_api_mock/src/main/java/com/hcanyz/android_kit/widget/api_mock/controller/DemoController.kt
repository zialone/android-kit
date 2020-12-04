package com.hcanyz.android_kit.widget.api_mock.controller

import com.yanzhenjie.andserver.annotation.GetMapping
import com.yanzhenjie.andserver.annotation.RestController

@RestController
class DemoController {

    @GetMapping("/demo/1")
    fun login(): String {
        return "DemoController /demo/1 successful."
    }
}