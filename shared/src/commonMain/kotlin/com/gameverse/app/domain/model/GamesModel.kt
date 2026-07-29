package com.gameverse.app.domain.model

data class GamesModel(
    val id: Int,
    val name: String,
    val backgroundImage: String,
    val released: String,
    val genres: List<String>,
    val parentPlatforms: List<Int>,
)