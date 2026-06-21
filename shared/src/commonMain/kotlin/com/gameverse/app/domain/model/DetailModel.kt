package com.gameverse.app.domain.model

import com.gameverse.app.data.response.AddedByStatus
import com.gameverse.app.data.response.EsrbRating
import com.gameverse.app.data.response.GameDetailResponse.DevelopersItem
import com.gameverse.app.data.response.GameDetailResponse.MetacriticPlatformsItem
import com.gameverse.app.data.response.GameDetailResponse.PublishersItem
import com.gameverse.app.data.response.GenresItem
import com.gameverse.app.data.response.ParentPlatformsItem
import com.gameverse.app.data.response.PlatformsItem
import com.gameverse.app.data.response.RatingsItem
import com.gameverse.app.data.response.StoresItem
import com.gameverse.app.data.response.TagsItem

data class DetailModel(
    val added: Int,
    val developers: List<DevelopersItem>,
    val nameOriginal: String,
    val rating: Double,
    val gameSeriesCount: Int,
    val playtime: Int,
    val platforms: List<PlatformsItem>,
    val ratingTop: Int,
    val reviewsTextCount: Int,
    val publishers: List<PublishersItem>,
    val achievementsCount: Int,
    val id: Int,
    val parentPlatforms: List<ParentPlatformsItem>,
    val redditName: String,
    val ratingsCount: Int,
    val slug: String,
    val released: String,
    val youtubeCount: Int,
    val moviesCount: Int,
    val descriptionRaw: String,
    val tags: List<TagsItem>,
    val backgroundImage: String,
    val tba: Boolean,
    val dominantColor: String,
    val name: String,
    val redditDescription: String,
    val redditLogo: String,
    val updated: String,
    val reviewsCount: Int,
    val metacritic: Int,
    val description: String,
    val metacriticUrl: String,
    val alternativeNames: List<String>,
    val parentsCount: Int,
    val metacriticPlatforms: List<MetacriticPlatformsItem>,
    val creatorsCount: Int,
    val ratings: List<RatingsItem?>,
    val genres: List<GenresItem>,
    val saturatedColor: String,
    val addedByStatus: AddedByStatus,
    val redditUrl: String,
    val redditCount: Int,
    val parentAchievementsCount: Int,
    val website: String,
    val suggestionsCount: Int,
    val stores: List<StoresItem>,
    val additionsCount: Int,
    val twitchCount: Int,
    val backgroundImageAdditional: String,
    val esrbRating: EsrbRating,
    val screenshotsCount: Int,
)
