package com.hcanyz.android_kit.feature.template.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class DiModuleTemplate {

    @Provides
    fun provideDiTemplate(): IDiTemplateProvided {
        return IApiTemplate.api().provideDiTemplate()
    }
}