package com.mach.apps.imccalculatorapp.android.di

import com.mach.apps.imccalculatorapp.IMCCalculator
import com.mach.apps.imccalculatorapp.android.core.utils.AndroidResourceProvider
import com.mach.apps.imccalculatorapp.android.core.utils.ResourceProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideIMCCalculator(): IMCCalculator {
        return IMCCalculator()
    }

    /** Injetado em vez de chamar Instant.now() direto: em teste dá para fixar o relógio. */
    @Provides
    @Singleton
    fun provideClock(): Clock = Clock.systemDefaultZone()
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
