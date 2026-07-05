package com.gameverse.app.data.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GenresItemResponse(
    @SerialName("games_count")
    val gamesCount: Int? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("games")
    val games: List<GamesItem>? = null,

    @SerialName("id")
    val id: Int? = null,

    @SerialName("image_background")
    val imageBackground: String? = null,

    @SerialName("slug")
    val slug: String? = null
) {
    @Serializable
    data class GamesItem(
        @SerialName("added")
        val added: Int? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("id")
        val id: Int? = null,

        @SerialName("slug")
        val slug: String? = null
    )
}