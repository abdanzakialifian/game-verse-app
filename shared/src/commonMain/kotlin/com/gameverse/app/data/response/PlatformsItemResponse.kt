package com.gameverse.app.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlatformsItemResponse(
    @SerialName("requirements")
    val requirements: RequirementsResponse? = null,

    @SerialName("released_at")
    val releasedAt: String? = null,

    @SerialName("platform")
    val platform: ParentPlatformItemResponse.PlatformResponse? = null
) {
    @Serializable
    data class RequirementsResponse(
        @SerialName("minimum")
        val minimum: String? = null,

        @SerialName("recommended")
        val recommended: String? = null
    )
}