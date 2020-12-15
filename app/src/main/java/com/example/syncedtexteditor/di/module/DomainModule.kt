package com.example.syncedtexteditor.di.module

import com.example.syncedtexteditor.domain.interfaces.Repository
import com.example.syncedtexteditor.domain.usercase.MainUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DomainModule {
    @Provides
    @Singleton
    fun provideMainUsecase(repository: Repository): MainUsecase {
        return MainUsecase(repository)
    }
}