package com.gameverse.app.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagItemResponse(
    @SerialName("games_count")
    val gamesCount: Int? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("language")
    val language: String? = null,

    @SerialName("id")
    val id: Int? = null,

    @SerialName("image_background")
    val imageBackground: String? = null,

    @SerialName("slug")
    val slug: String? = null
)
