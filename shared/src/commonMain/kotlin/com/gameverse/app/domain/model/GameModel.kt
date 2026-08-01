package com.gameverse.app.domain.model

data class GameModel(
    val id: Int,
    val name: String,
    val backgroundImage: String,
    val released: String,
    val genreNames: List<String>,
    val platformIds: List<Int>,
)