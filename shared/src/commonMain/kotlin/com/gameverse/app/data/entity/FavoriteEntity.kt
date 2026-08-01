package com.gameverse.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorites")
data class FavoriteEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("background_image")
    val backgroundImage: String,
    @ColumnInfo("released")
    val released: String,
    @ColumnInfo("genre_name")
    val genreNames: List<String>,
    @ColumnInfo("platform_id")
    val platformIds: List<Int>,
)
