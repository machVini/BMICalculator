package com.mach.apps.imccalculatorapp.android.di

import com.mach.apps.imccalculatorapp.IMCCalculator
import com.mach.apps.imccalculatorapp.android.util.AndroidResourceProvider
import com.mach.apps.imccalculatorapp.android.util.ResourceProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceModule {
    @Binds
    @Singleton
    abstract fun bindResourceProvider(
        androidResourceProvider: AndroidResourceProvider
    ): ResourceProvider
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideIMCCalculator(): IMCCalculator {
        return IMCCalculator()
    }
}
