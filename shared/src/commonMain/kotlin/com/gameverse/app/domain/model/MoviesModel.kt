package com.gameverse.app.domain.model

import com.gameverse.app.data.response.GamesMoviesResponse.ResultsItem.Data

data class MoviesModel(
    val preview: String,
    val name: String,
    val id: Int,
    val max: String,
    val jsonMember480: String
)
