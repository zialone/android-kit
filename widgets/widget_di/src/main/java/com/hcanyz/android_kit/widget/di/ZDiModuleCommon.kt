package com.hcanyz.android_kit.widget.di

import android.content.Context
import android.content.SharedPreferences
import com.hcanyz.android_kit.vendor.http.ZService
import com.hcanyz.android_kit.vendor.log.ZLog
import com.hcanyz.android_kit.vendor.storage.db.ZDbCommon
import com.hcanyz.android_kit.vendor.storage.shared.getCommonEncryptedSharedPreferences
import com.hcanyz.android_kit.vendor.storage.shared.getCommonSharedPreferences
import com.hcanyz.android_kit.vendor.storage.zzCreateCommonDb
import com.hcanyz.android_kit.widget.core.EvCoreConfigManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CommonSharedPreferences

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CommonEncryptedSharedPreferences

    @Provides
    @Singleton
    @CommonSharedPreferences
    fun provideCommonSharedPreferences(
        @ApplicationContext appContext: Context
    ): SharedPreferences {
        return appContext.getCommonSharedPreferences()
    }

    @Provides
    @Singleton
    @CommonEncryptedSharedPreferences
    fun provideCommonEncryptedSharedPreferences(
        @ApplicationContext appContext: Context
    ): SharedPreferences {
        return appContext.getCommonEncryptedSharedPreferences()
    }
}