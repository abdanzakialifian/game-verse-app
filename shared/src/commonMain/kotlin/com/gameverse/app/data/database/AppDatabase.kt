package com.gameverse.app.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gameverse.app.common.Converters
import com.gameverse.app.data.dao.GVFavoriteDao
import com.gameverse.app.data.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
@TypeConverters(Converters::class)
@ConstructedBy(DatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getFavoriteDao(): GVFavoriteDao
}

const val DATABASE_NAME = "gameverse_database.db"