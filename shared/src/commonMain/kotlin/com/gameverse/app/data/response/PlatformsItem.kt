package com.gameverse.app.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlatformsItem(
    @SerialName("requirements")
    val requirements: Requirements? = null,

    @SerialName("released_at")
    val releasedAt: String? = null,

    @SerialName("platform")
    val platform: ParentPlatformsItem.Platform? = null
) {
    @Serializable
    data class Requirements(
        @SerialName("minimum")
        val minimum: String? = null,

        @SerialName("recommended")
        val recommended: String? = null
    )
}