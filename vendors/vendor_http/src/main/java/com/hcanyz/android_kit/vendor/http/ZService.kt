package com.hcanyz.android_kit.vendor.http

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ZService {
    @GET
    fun get(
        @Url url: String,
        @QueryMap queryMap: Map<String, String> = emptyMap()
    ): Call<ResponseBody>

    @POST
    fun post(@Url url: String): Call<ResponseBody>

    @POST
    fun post(
        @Url url: String, @Body body: MultipartBody
    ): Call<Response<ResponseBody>>

    @POST
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun postJson(
        @Url url: String,
        @Body jsonMap: @JvmSuppressWildcards Map<String, Any> = emptyMap()
    ): Call<ResponseBody>

    @POST
    @FormUrlEncoded
    fun postForm(
        @Url url: String,
        @FieldMap formMap: @JvmSuppressWildcards Map<String, Any> = emptyMap()
    ): Call<ResponseBody>
}