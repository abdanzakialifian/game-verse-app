package com.gameverse.app.domain.model

data class GamesModel(
    val id: Int,
    val name: String,
    val backgroundImage: String,
    val released: String,
    val genres: List<GenresItem>,
    val parentPlatforms: List<ParentPlatformsItem>,
) {
    data class GenresItem(
        val name: String,
        val id: Int,
    )

    data class ParentPlatformsItem(
        val name: String,
        val id: Int,
    )
}