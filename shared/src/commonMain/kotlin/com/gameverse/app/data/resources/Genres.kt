package com.gameverse.app.data.resources

import io.ktor.resources.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Resource("genres")
class Genres(
    @SerialName("page")
    val page: Int? = null,

    @SerialName("page_size")
    val pageSize: Int? = null,
)