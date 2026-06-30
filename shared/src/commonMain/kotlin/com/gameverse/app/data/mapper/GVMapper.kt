package com.gameverse.app.data.mapper

import com.gameverse.app.data.response.AddedByStatus
import com.gameverse.app.data.response.EsrbRating
import com.gameverse.app.data.response.GameDetailResponse
import com.gameverse.app.data.response.GamesMoviesResponse
import com.gameverse.app.data.response.GamesResponse
import com.gameverse.app.data.response.GamesScreenshotsResponse
import com.gameverse.app.data.response.GenresResponse
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.domain.model.GenresModel
import com.gameverse.app.domain.model.MoviesModel
import com.gameverse.app.domain.model.ScreenshotsModel

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

fun GameDetailResponse.toDomain(): DetailModel = DetailModel(
    added = added ?: 0,
    developers = developers.orEmpty(),
    nameOriginal = nameOriginal.orEmpty(),
    rating = rating ?: 0.0,
    gameSeriesCount = gameSeriesCount ?: 0,
    playtime = playtime ?: 0,
    platforms = platforms.orEmpty(),
    ratingTop = ratingTop ?: 0,
    reviewsTextCount = reviewsTextCount ?: 0,
    publishers = publishers.orEmpty(),
    achievementsCount = achievementsCount ?: 0,
    id = id ?: 0,
    parentPlatforms = parentPlatforms.orEmpty(),
    redditName = redditName.orEmpty(),
    ratingsCount = ratingsCount ?: 0,
    slug = slug.orEmpty(),
    released = released.orEmpty(),
    youtubeCount = youtubeCount ?: 0,
    moviesCount = moviesCount ?: 0,
    descriptionRaw = descriptionRaw.orEmpty(),
    tags = tags.orEmpty(),
    backgroundImage = backgroundImage.orEmpty(),
    tba = tba ?: false,
    dominantColor = dominantColor.orEmpty(),
    name = name.orEmpty(),
    redditDescription = redditDescription.orEmpty(),
    redditLogo = redditLogo.orEmpty(),
    updated = updated.orEmpty(),
    reviewsCount = reviewsCount ?: 0,
    metacritic = metacritic ?: 0,
    description = description.orEmpty(),
    metacriticUrl = metacriticUrl.orEmpty(),
    alternativeNames = alternativeNames.orEmpty(),
    parentsCount = parentsCount ?: 0,
    metacriticPlatforms = metacriticPlatforms.orEmpty(),
    creatorsCount = creatorsCount ?: 0,
    ratings = ratings.orEmpty(),
    genres = genres.orEmpty(),
    saturatedColor = saturatedColor.orEmpty(),
    addedByStatus = addedByStatus ?: AddedByStatus(),
    redditUrl = redditUrl.orEmpty(),
    redditCount = redditCount ?: 0,
    parentAchievementsCount = parentAchievementsCount ?: 0,
    website = website.orEmpty(),
    suggestionsCount = suggestionsCount ?: 0,
    stores = stores.orEmpty(),
    additionsCount = additionsCount ?: 0,
    twitchCount = twitchCount ?: 0,
    backgroundImageAdditional = backgroundImageAdditional.orEmpty(),
    esrbRating = esrbRating ?: EsrbRating(),
    screenshotsCount = screenshotsCount ?: 0,
)

fun GamesScreenshotsResponse.ResultsItem.toDomain(): ScreenshotsModel = ScreenshotsModel(
    id = id ?: 0,
    image = image.orEmpty(),
    isDeleted = isDeleted ?: false,
    width = width ?: 0,
    height = height ?: 0
)

fun GamesMoviesResponse.ResultsItem.toDomain(): MoviesModel = MoviesModel(
    preview = preview.orEmpty(),
    name = name.orEmpty(),
    id = id ?: 0,
    max = data?.max.orEmpty(),
    jsonMember480 = data?.jsonMember480.orEmpty(),
)