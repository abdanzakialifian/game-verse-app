package com.gameverse.app.domain.model

data class GenreModel(
    val id: Int,
    val name: String,
    val imageBackground: String,
    val gamesCount: Int,
    val games: List<GamesItem>,
) {
    data class GamesItem(
        val id: Int,
        val name: String,
        val added: Int,
    )
}
