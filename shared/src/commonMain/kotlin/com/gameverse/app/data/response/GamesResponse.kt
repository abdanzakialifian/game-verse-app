package com.gameverse.app.data.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GamesItemResponse(
	@SerialName("added")
	val added: Int? = null,

	@SerialName("rating")
	val rating: Double? = null,

	@SerialName("metacritic")
	val metacritic: Int? = null,

	@SerialName("playtime")
	val playtime: Int? = null,

	@SerialName("short_screenshots")
	val shortScreenshots: List<ShortScreenshotsItem>? = null,

	@SerialName("platforms")
	val platforms: List<PlatformsItem>? = null,

	@SerialName("user_game")
	val userGame: String? = null,

	@SerialName("rating_top")
	val ratingTop: Int? = null,

	@SerialName("reviews_text_count")
	val reviewsTextCount: Int? = null,

	@SerialName("ratings")
	val ratings: List<RatingsItem>? = null,

	@SerialName("genres")
	val genres: List<GenresItem>? = null,

	@SerialName("saturated_color")
	val saturatedColor: String? = null,

	@SerialName("id")
	val id: Int? = null,

	@SerialName("added_by_status")
	val addedByStatus: AddedByStatus? = null,

	@SerialName("parent_platforms")
	val parentPlatforms: List<ParentPlatformsItem>? = null,

	@SerialName("ratings_count")
	val ratingsCount: Int? = null,

	@SerialName("slug")
	val slug: String? = null,

	@SerialName("released")
	val released: String? = null,

	@SerialName("suggestions_count")
	val suggestionsCount: Int? = null,

	@SerialName("stores")
	val stores: List<StoresItem>? = null,

	@SerialName("tags")
	val tags: List<TagsItem>? = null,

	@SerialName("background_image")
	val backgroundImage: String? = null,

	@SerialName("tba")
	val tba: Boolean? = null,

	@SerialName("dominant_color")
	val dominantColor: String? = null,

	@SerialName("esrb_rating")
	val esrbRating: EsrbRating? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("updated")
	val updated: String? = null,

	@SerialName("clip")
	val clip: String? = null,

	@SerialName("reviews_count")
	val reviewsCount: Int? = null
) {
	@Serializable
	data class ShortScreenshotsItem(
		@SerialName("image")
		val image: String? = null,

		@SerialName("id")
		val id: Int? = null
	)
}
