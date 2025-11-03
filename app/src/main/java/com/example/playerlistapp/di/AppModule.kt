package com.example.playerlistapp.di

import android.content.Context
import com.example.playerlistapp.data.prefs.FilterPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFilterPrefs(
        @ApplicationContext context: Context
    ): FilterPrefs = FilterPrefs(context)
}
