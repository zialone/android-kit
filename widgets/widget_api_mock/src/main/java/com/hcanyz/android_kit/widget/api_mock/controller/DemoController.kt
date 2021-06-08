package com.hcanyz.android_kit.widget.api_mock.controller

import com.yanzhenjie.andserver.annotation.GetMapping
import com.yanzhenjie.andserver.annotation.PathVariable
import com.yanzhenjie.andserver.annotation.RequestParam
import com.yanzhenjie.andserver.annotation.RestController

@RestController
class DemoController {

    @GetMapping("/demo/{id}")
    fun demo(@PathVariable("id") id: String): String {
        return "DemoController /demo/${id} successful."
    }

    @GetMapping("/user/login")
    fun loginByPwd(@RequestParam("username") username: String): String {
        return "{\"code\":0,\"username\":\"${username}\"}"
    }
}