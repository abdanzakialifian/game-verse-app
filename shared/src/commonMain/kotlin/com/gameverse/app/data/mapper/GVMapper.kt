package com.gameverse.app.data.mapper

import com.gameverse.app.data.response.GameDetailResponse
import com.gameverse.app.data.response.GamesItemResponse
import com.gameverse.app.data.response.GamesMoviesItemResponse
import com.gameverse.app.data.response.GamesScreenshotsItemResponse
import com.gameverse.app.data.response.GenresItemResponse
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.domain.model.GenresModel
import com.gameverse.app.domain.model.MoviesModel
import com.gameverse.app.domain.model.ScreenshotsModel

fun List<GamesItemResponse>.toDomain(): List<GamesModel> = this.map { result ->
    GamesModel(
        id = result.id ?: 0,
        name = result.name.orEmpty(),
        backgroundImage = result.backgroundImage.orEmpty(),
        released = result.released.orEmpty(),
        genres = result.genres?.map { genre ->
            genre.name.orEmpty()
        }.orEmpty(),
        parentPlatforms = result.parentPlatforms?.map { parentPlatform ->
            parentPlatform.platform?.id ?: 0
        }.orEmpty(),
    )
}

fun GamesItemResponse.toDomain(): GamesModel = GamesModel(
    id = id ?: 0,
    name = name.orEmpty(),
    backgroundImage = backgroundImage.orEmpty(),
    released = released.orEmpty(),
    genres = genres?.map { genre ->
        genre.name.orEmpty()
    }.orEmpty(),
    parentPlatforms = parentPlatforms?.map { parentPlatform ->
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
    developers = developers?.map {
        DetailModel.DevelopersItem(
            id = it.id ?: 0,
            name = it.name.orEmpty()
        )
    }.orEmpty(),
    rating = rating ?: 0.0,
    publishers = publishers?.map {
        DetailModel.PublishersItem(
            id = it.id ?: 0,
            name = it.name.orEmpty()
        )
    }.orEmpty(),
    parentPlatforms = parentPlatforms?.map {
        it.platform?.id ?: 0
    }.orEmpty(),
    ratingsCount = ratingsCount ?: 0,
    released = released.orEmpty(),
    updated = updated.orEmpty(),
    backgroundImage = backgroundImage.orEmpty(),
    name = name.orEmpty(),
    reviewsCount = reviewsCount ?: 0,
    description = descriptionRaw.orEmpty(),
    genres = genres?.map {
        it.name.orEmpty()
    }.orEmpty(),
)

fun GamesScreenshotsItemResponse.toDomain(): ScreenshotsModel = ScreenshotsModel(
    id = id ?: 0,
    image = image.orEmpty(),
    isDeleted = isDeleted ?: false,
    width = width ?: 0,
    height = height ?: 0
)

fun GamesMoviesItemResponse.toDomain(): MoviesModel = MoviesModel(
    preview = preview.orEmpty(),
    name = name.orEmpty(),
    id = id ?: 0,
    max = data?.max.orEmpty(),
    jsonMember480 = data?.jsonMember480.orEmpty(),
)