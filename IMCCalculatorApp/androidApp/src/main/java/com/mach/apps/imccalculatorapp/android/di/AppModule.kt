package com.mach.apps.imccalculatorapp.android.di

import com.google.ai.client.generativeai.GenerativeModel
import com.mach.apps.imccalculatorapp.IMCCalculator
import com.mach.apps.imccalculatorapp.android.BuildConfig
import com.mach.apps.imccalculatorapp.android.core.utils.AndroidResourceProvider
import com.mach.apps.imccalculatorapp.android.core.utils.ResourceProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideIMCCalculator(): IMCCalculator {
        return IMCCalculator()
    }

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.API_KEY
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface AppBindings {
    @Binds
    @Singleton
    fun bindResourceProvider(
        androidResourceProvider: AndroidResourceProvider
    ): ResourceProvider
}
