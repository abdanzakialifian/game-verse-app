package com.gameverse.app.data.mapper

import com.gameverse.app.data.entity.FavoriteEntity
import com.gameverse.app.data.response.GameDetailResponse
import com.gameverse.app.data.response.GamesItemResponse
import com.gameverse.app.data.response.GamesScreenshotsItemResponse
import com.gameverse.app.data.response.GenresItemResponse
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.domain.model.GenresModel
import com.gameverse.app.domain.model.ScreenshotsModel

fun List<GamesItemResponse>.toDomain(): List<GamesModel> = this.map { result ->
    GamesModel(
        id = result.id ?: 0,
        name = result.name.orEmpty(),
        backgroundImage = result.backgroundImage.orEmpty(),
        released = result.released.orEmpty(),
        genreNames = result.genres?.map { genre ->
            genre.name.orEmpty()
        }.orEmpty(),
        platformIds = result.parentPlatforms?.map { parentPlatform ->
            parentPlatform.platform?.id ?: 0
        }.orEmpty(),
    )
}

fun GamesItemResponse.toDomain(): GamesModel = GamesModel(
    id = id ?: 0,
    name = name.orEmpty(),
    backgroundImage = backgroundImage.orEmpty(),
    released = released.orEmpty(),
    genreNames = genres?.map { genre ->
        genre.name.orEmpty()
    }.orEmpty(),
    platformIds = parentPlatforms?.map { parentPlatform ->
        parentPlatform.platform?.id ?: 0
    }.orEmpty(),
)

fun GenresItemResponse.toDomain(): GenresModel = GenresModel(
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

fun GameDetailResponse.toDomain(): DetailModel = DetailModel(
    id = id ?: 0,
    developerNames = developers?.map {
        it.name.orEmpty()
    }.orEmpty(),
    rating = rating ?: 0.0,
    publisherNames = publishers?.map {
        it.name.orEmpty()
    }.orEmpty(),
    platformIds = parentPlatforms?.map {
        it.platform?.id ?: 0
    }.orEmpty(),
    ratingsCount = ratingsCount ?: 0,
    released = released.orEmpty(),
    updated = updated.orEmpty(),
    backgroundImage = backgroundImage.orEmpty(),
    name = name.orEmpty(),
    reviewsCount = reviewsCount ?: 0,
    description = descriptionRaw.orEmpty(),
    genreNames = genres?.map {
        it.name.orEmpty()
    }.orEmpty(),
)

fun GamesScreenshotsItemResponse.toDomain(): ScreenshotsModel = ScreenshotsModel(
    id = id ?: 0,
    image = image.orEmpty()
)

fun FavoriteEntity.toDomain(): GamesModel = GamesModel(
    id = id,
    name = name,
    backgroundImage = backgroundImage,
    released = released,
    genreNames = genreNames,
    platformIds = platformIds
)