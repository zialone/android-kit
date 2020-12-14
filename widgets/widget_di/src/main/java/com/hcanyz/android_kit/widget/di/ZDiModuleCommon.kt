package com.hcanyz.android_kit.widget.di

import android.content.Context
import com.hcanyz.android_kit.vendor.http.ZService
import com.hcanyz.android_kit.vendor.log.ZLog
import com.hcanyz.android_kit.vendor.storage.db.ZDbCommon
import com.hcanyz.android_kit.vendor.storage.zzCreateCommonDb
import com.hcanyz.android_kit.widget.core.EvCoreConfigManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ZDiModuleCommon {

    @Provides
    @Singleton
    fun provideRetrofit(
        @ApplicationContext appContext: Context,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(
                EvCoreConfigManager.getSingleton(appContext)
                    .getEvItemCurrentValue(EvCoreConfigManager.EV_ITEM_SERVERURL)
            )
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor { message -> ZLog.d("ZHttp", message) }
                .setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    fun provideZService(retrofit: Retrofit): ZService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideZDbCommon(
        @ApplicationContext appContext: Context
    ): ZDbCommon {
        return appContext.zzCreateCommonDb()
    }
}