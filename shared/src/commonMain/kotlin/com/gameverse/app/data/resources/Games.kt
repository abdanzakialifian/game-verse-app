package com.gameverse.app.data.resources

import io.ktor.resources.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Resource("games")
class Games(
    @SerialName("search")
    val search: String? = null,

    @SerialName("page")
    val page: Int? = null,

    @SerialName("page_size")
    val pageSize: Int? = null,

    @SerialName("genres")
    val genres: String? = null,
) {
    @Serializable
    @Resource("games/{game_pk}/game-series")
    class Series(
        @SerialName("game_pk")
        val gamePk: String,

        @SerialName("page")
        val page: Int,

        @SerialName("page_size")
        val pageSize: Int,
    )
}