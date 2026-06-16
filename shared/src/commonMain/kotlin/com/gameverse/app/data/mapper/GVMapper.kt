package com.gameverse.app.data.mapper

import com.gameverse.app.data.response.GamesResponse
import com.gameverse.app.data.response.GenresResponse
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.domain.model.GenresModel

fun GamesResponse.toDomain(): List<GamesModel> = results?.map { result ->
    GamesModel(
        id = result.id ?: 0,
        name = result.name.orEmpty(),
        backgroundImage = result.backgroundImage.orEmpty(),
        released = result.released.orEmpty(),
        genres = result.genres?.map { genre ->
            GamesModel.GenresItem(
                id = genre.id ?: 0,
                name = genre.name.orEmpty(),
            )
        }.orEmpty(),
        parentPlatforms = result.parentPlatforms?.map { parentPlatform ->
            GamesModel.ParentPlatformsItem(
                id = parentPlatform.platform?.id ?: 0,
                name = parentPlatform.platform?.name.orEmpty(),
            )
        }.orEmpty(),
    )
}.orEmpty()

fun GamesResponse.ResultsItem.toDomain(): GamesModel = GamesModel(
    id = id ?: 0,
    name = name.orEmpty(),
    backgroundImage = backgroundImage.orEmpty(),
    released = released.orEmpty(),
    genres = genres?.map { genre ->
        GamesModel.GenresItem(
            id = genre.id ?: 0,
            name = genre.name.orEmpty(),
        )
    }.orEmpty(),
    parentPlatforms = parentPlatforms?.map { parentPlatform ->
        GamesModel.ParentPlatformsItem(
            id = parentPlatform.platform?.id ?: 0,
            name = parentPlatform.platform?.name.orEmpty(),
        )
    }.orEmpty(),
)

fun GenresResponse.ResultsItem.toDomain(): GenresModel = GenresModel(
    id = id ?: 0,
    name = name.orEmpty(),
    imageBackground = imageBackground.orEmpty(),
    gamesCount = gamesCount ?: 0,
    games = games?.map {
        GenresModel.GamesItem(
            id = it.id ?: 0,
            name = it.name.orEmpty(),
            added = it.added ?: 0,
        )
    }.orEmpty()
)