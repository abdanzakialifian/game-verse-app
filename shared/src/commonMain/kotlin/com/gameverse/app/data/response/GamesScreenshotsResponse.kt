package com.gameverse.app.data.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GamesScreenshotsItemResponse(
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
