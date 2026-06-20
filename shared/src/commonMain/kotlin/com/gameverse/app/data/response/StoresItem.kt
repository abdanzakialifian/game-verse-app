package com.gameverse.app.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoresItem(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("store")
    val store: Store? = null,

    @SerialName("url")
    val url: String? = null
) {
    @Serializable
    data class Store(

        @SerialName("games_count")
        val gamesCount: Int? = null,

        @SerialName("domain")
        val domain: String? = null,

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