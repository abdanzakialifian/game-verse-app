package com.gameverse.app.data.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GenreItemResponse(
    @SerialName("games_count")
    val gamesCount: Int? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("games")
    val games: List<GameItemResponse>? = null,

    @SerialName("id")
    val id: Int? = null,

    @SerialName("image_background")
    val imageBackground: String? = null,

    @SerialName("slug")
    val slug: String? = null
)