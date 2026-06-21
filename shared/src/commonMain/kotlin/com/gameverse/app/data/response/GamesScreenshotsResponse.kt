package com.gameverse.app.data.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GamesScreenshotsResponse(
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
        @SerialName("image")
        val image: String? = null,

        @SerialName("is_deleted")
        val isDeleted: Boolean? = null,

        @SerialName("width")
        val width: Int? = null,

        @SerialName("id")
        val id: Int? = null,

        @SerialName("height")
        val height: Int? = null
    )
}
