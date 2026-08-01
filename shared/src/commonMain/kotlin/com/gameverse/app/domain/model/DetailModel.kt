package com.gameverse.app.domain.model

data class DetailModel(
    val id: Int,
    val developerNames: List<String>,
    val rating: Double,
    val reviewsCount: Int,
    val publisherNames: List<String>,
    val platformIds: List<Int>,
    val ratingsCount: Int,
    val released: String,
    val updated: String,
    val backgroundImage: String,
    val name: String,
    val description: String,
    val genreNames: List<String>,
)
