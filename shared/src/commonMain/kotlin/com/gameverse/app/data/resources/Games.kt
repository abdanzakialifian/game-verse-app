package com.gameverse.app.data.resources

import io.ktor.resources.Resource

@Resource("/games")
class Games(
    val page: Int? = null,
    val pageSize: Int? = null,
)