package com.gameverse.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorites")
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val backgroundImage: String,
    val released: String,
    val genres: List<String>,
    val platforms: List<Int>,
)
