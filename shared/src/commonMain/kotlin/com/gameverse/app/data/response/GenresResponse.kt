package com.gameverse.app.data.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GenresResponse(
    @SerialName("next")
    val next: String? = null,

    @SerialName("previous")
    val previous: String? = null,

    @SerialName("count")
    val count: Int? = null,

    @SerialName("results")
    val results: List<ResultsItem>? = null
) {
    @Serializable
    data class ResultsItem(
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
}