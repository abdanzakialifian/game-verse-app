package com.gameverse.app.data.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GameDetailResponse(
	@SerialName("added")
	val added: Int? = null,

	@SerialName("developers")
	val developers: List<DevelopersItemResponse>? = null,

	@SerialName("name_original")
	val nameOriginal: String? = null,

	@SerialName("rating")
	val rating: Double? = null,

	@SerialName("game_series_count")
	val gameSeriesCount: Int? = null,

	@SerialName("playtime")
	val playtime: Int? = null,

	@SerialName("platforms")
	val platforms: List<PlatformsItemResponse>? = null,

	@SerialName("rating_top")
	val ratingTop: Int? = null,

	@SerialName("reviews_text_count")
	val reviewsTextCount: Int? = null,

	@SerialName("publishers")
	val publishers: List<PublishersItemResponse>? = null,

	@SerialName("achievements_count")
	val achievementsCount: Int? = null,

	@SerialName("id")
	val id: Int? = null,

	@SerialName("parent_platforms")
	val parentPlatforms: List<ParentPlatformItemResponse>? = null,

	@SerialName("reddit_name")
	val redditName: String? = null,

	@SerialName("ratings_count")
	val ratingsCount: Int? = null,

	@SerialName("slug")
	val slug: String? = null,

	@SerialName("released")
	val released: String? = null,

	@SerialName("youtube_count")
	val youtubeCount: Int? = null,

	@SerialName("movies_count")
	val moviesCount: Int? = null,

	@SerialName("description_raw")
	val descriptionRaw: String? = null,

	@SerialName("tags")
	val tags: List<TagItemResponse>? = null,

	@SerialName("background_image")
	val backgroundImage: String? = null,

	@SerialName("tba")
	val tba: Boolean? = null,

	@SerialName("dominant_color")
	val dominantColor: String? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("reddit_description")
	val redditDescription: String? = null,

	@SerialName("reddit_logo")
	val redditLogo: String? = null,

	@SerialName("updated")
	val updated: String? = null,

	@SerialName("reviews_count")
	val reviewsCount: Int? = null,

	@SerialName("metacritic")
	val metacritic: Int? = null,

	@SerialName("description")
	val description: String? = null,

	@SerialName("metacritic_url")
	val metacriticUrl: String? = null,

	@SerialName("alternative_names")
	val alternativeNames: List<String>? = null,

	@SerialName("parents_count")
	val parentsCount: Int? = null,

	@SerialName("metacritic_platforms")
	val metacriticPlatforms: List<MetacriticPlatformsItemResponse>? = null,

	@SerialName("creators_count")
	val creatorsCount: Int? = null,

	@SerialName("ratings")
	val ratings: List<RatingItemResponse>? = null,

	@SerialName("genres")
	val genres: List<GenreItemResponse>? = null,

	@SerialName("saturated_color")
	val saturatedColor: String? = null,

	@SerialName("added_by_status")
	val addedByStatus: AddedByStatusResponse? = null,

	@SerialName("reddit_url")
	val redditUrl: String? = null,

	@SerialName("reddit_count")
	val redditCount: Int? = null,

	@SerialName("parent_achievements_count")
	val parentAchievementsCount: Int? = null,

	@SerialName("website")
	val website: String? = null,

	@SerialName("suggestions_count")
	val suggestionsCount: Int? = null,

	@SerialName("stores")
	val stores: List<StoreItemResponse>? = null,

	@SerialName("additions_count")
	val additionsCount: Int? = null,

	@SerialName("twitch_count")
	val twitchCount: Int? = null,

	@SerialName("background_image_additional")
	val backgroundImageAdditional: String? = null,

	@SerialName("esrb_rating")
	val esrbRating: EsrbRatingResponse? = null,

	@SerialName("screenshots_count")
	val screenshotsCount: Int? = null,
) {
	@Serializable
	data class MetacriticPlatformsItemResponse(
		@SerialName("metascore")
		val metascore: Int? = null,

		@SerialName("url")
		val url: String? = null,

		@SerialName("platform")
		val platform: ParentPlatformItemResponse.PlatformResponse? = null
	)

	@Serializable
	data class DevelopersItemResponse(
		@SerialName("games_count")
		val gamesCount: Int? = null,

		@SerialName("name")
		val name: String? = null,

		@SerialName("id")
		val id: Int? = null,

		@SerialName("image_background")
		val imageBackground: String? = null,

		@SerialName("slug")
		val slug: String? = null
	)

	@Serializable
	data class PublishersItemResponse(
		@SerialName("games_count")
		val gamesCount: Int? = null,

		@SerialName("name")
		val name: String? = null,

		@SerialName("id")
		val id: Int? = null,

		@SerialName("image_background")
		val imageBackground: String? = null,

		@SerialName("slug")
		val slug: String? = null
	)
}
