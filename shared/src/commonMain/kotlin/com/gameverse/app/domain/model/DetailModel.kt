package com.gameverse.app.domain.model

data class DetailModel(
    val id: Int,
    val developers: List<DevelopersItem>,
    val rating: Double,
    val reviewsCount: Int,
    val publishers: List<PublishersItem>,
    val parentPlatforms: List<GamesModel.ParentPlatformsItem>,
    val ratingsCount: Int,
    val released: String,
    val updated: String,
    val backgroundImage: String,
    val name: String,
    val description: String,
    val genres: List<GamesModel.GenresItem>,
) {
    data class DevelopersItem(
        val name: String,
        val id: Int,
    )

    data class PublishersItem(
        val name: String,
        val id: Int,
    )
}
