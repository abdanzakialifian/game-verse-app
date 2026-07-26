package com.gameverse.app.di

import com.gameverse.app.data.dao.GVFavoriteDao
import com.gameverse.app.data.database.AppDatabase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton

@Module
 class DatabaseModule {
    @Singleton
    fun provideFavoriteDao(@Provided database: AppDatabase): GVFavoriteDao = database.getFavoriteDao()
}