package com.gameverse.app.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentPlatformsItemResponse(
    @SerialName("platform")
    val platform: PlatformResponse? = null
) {
    @Serializable
    data class PlatformResponse(
        @SerialName("image")
        val image: String? = null,

        @SerialName("games_count")
        val gamesCount: Int? = null,

        @SerialName("year_end")
        val yearEnd: Int? = null,

        @SerialName("year_start")
        val yearStart: Int? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("id")
        val id: Int? = null,

        @SerialName("image_background")
        val imageBackground: String? = null,

        @SerialName("slug")
        val slug: String? = null
    )
}