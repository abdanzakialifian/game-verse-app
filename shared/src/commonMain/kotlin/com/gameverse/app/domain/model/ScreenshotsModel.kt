package com.gameverse.app.domain.model

data class ScreenshotsModel(
    val image: String,
    val isDeleted: Boolean,
    val width: Int,
    val id: Int,
    val height: Int
)
