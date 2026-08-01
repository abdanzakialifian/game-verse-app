package com.gameverse.app.data.mapper

import com.gameverse.app.data.entity.FavoriteEntity
import com.gameverse.app.data.response.GameDetailResponse
import com.gameverse.app.data.response.GameItemResponse
import com.gameverse.app.data.response.ScreenshotsItemResponse
import com.gameverse.app.data.response.GenreItemResponse
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.GameModel
import com.gameverse.app.domain.model.GenreModel
import com.gameverse.app.domain.model.ScreenshotModel

fun List<GameItemResponse>.toDomain(): List<GameModel> = this.map { result ->
    GameModel(
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

fun GameItemResponse.toDomain(): GameModel = GameModel(
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

fun GenreItemResponse.toDomain(): GenreModel = GenreModel(
    id = id ?: 0,
    name = name.orEmpty(),
    imageBackground = imageBackground.orEmpty(),
    gamesCount = gamesCount ?: 0,
    games = games?.map {
        GenreModel.GamesItem(
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

fun ScreenshotsItemResponse.toDomain(): ScreenshotModel = ScreenshotModel(
    id = id ?: 0,
    image = image.orEmpty()
)

fun FavoriteEntity.toDomain(): GameModel = GameModel(
    id = id,
    name = name,
    backgroundImage = backgroundImage,
    released = released,
    genreNames = genreNames,
    platformIds = platformIds
)