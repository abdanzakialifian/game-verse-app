package com.gameverse.app.data.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GamesResponse(
	@SerialName("next")
	val next: String? = null,

	@SerialName("nofollow")
	val noFollow: Boolean? = null,

	@SerialName("noindex")
	val noIndex: Boolean? = null,

	@SerialName("nofollow_collections")
	val noFollowCollections: List<String>? = null,

	@SerialName("previous")
	val previous: String? = null,

	@SerialName("count")
	val count: Int? = null,

	@SerialName("description")
	val description: String? = null,

	@SerialName("seo_h1")
	val seoH1: String? = null,

	@SerialName("filters")
	val filters: Filters? = null,

	@SerialName("seo_title")
	val seoTitle: String? = null,

	@SerialName("seo_description")
	val seoDescription: String? = null,

	@SerialName("results")
	val results: List<ResultsItem>? = null,

	@SerialName("seo_keywords")
	val seoKeywords: String? = null
) {
	@Serializable
	data class Filters(
		@SerialName("years")
		val years: List<YearsItem>? = null
	) {
		@Serializable
		data class YearsItem(
			@SerialName("filter")
			val filter: String? = null,

			@SerialName("nofollow")
			val noFollow: Boolean? = null,

			@SerialName("decade")
			val decade: Int? = null,

			@SerialName("count")
			val count: Int? = null,

			@SerialName("from")
			val from: Int? = null,

			@SerialName("to")
			val to: Int? = null,

			@SerialName("years")
			val years: List<YearItem>? = null,

			@SerialName("year")
			val year: Int? = null
		) {
			@Serializable
			data class YearItem(
				@SerialName("nofollow")
				val noFollow: Boolean? = null,

				@SerialName("count")
				val count: Int? = null,

				@SerialName("year")
				val year: Int? = null
			)
		}
	}

	@Serializable
	data class ResultsItem(
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

		@Serializable
		data class PlatformsItem(
			@SerialName("requirements_en")
			val requirementsEn: Requirements? = null,

			@SerialName("requirements_ru")
			val requirementsRu: Requirements? = null,

			@SerialName("released_at")
			val releasedAt: String? = null,

			@SerialName("platform")
			val platform: Platform? = null
		) {
			@Serializable
			data class Requirements(
				@SerialName("minimum")
				val minimum: String? = null,

				@SerialName("recommended")
				val recommended: String? = null
			)
		}

		@Serializable
		data class RatingsItem(
			@SerialName("count")
			val count: Int? = null,

			@SerialName("id")
			val id: Int? = null,

			@SerialName("title")
			val title: String? = null,

			@SerialName("percent")
			val percent: Double? = null
		)

		@Serializable
		data class GenresItem(
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
		data class AddedByStatus(
			@SerialName("owned")
			val owned: Int? = null,

			@SerialName("beaten")
			val beaten: Int? = null,

			@SerialName("dropped")
			val dropped: Int? = null,

			@SerialName("yet")
			val yet: Int? = null,

			@SerialName("playing")
			val playing: Int? = null,

			@SerialName("toplay")
			val toplay: Int? = null
		)

		@Serializable
		data class ParentPlatformsItem(
			@SerialName("platform")
			val platform: Platform? = null
		)

		@Serializable
		data class StoresItem(
			@SerialName("id")
			val id: Int? = null,

			@SerialName("store")
			val store: Store? = null
		) {
			@Serializable
			data class Store(
				@SerialName("games_count")
				val gamesCount: Int? = null,

				@SerialName("domain")
				val domain: String? = null,

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

		@Serializable
		data class TagsItem(
			@SerialName("games_count")
			val gamesCount: Int? = null,

			@SerialName("name")
			val name: String? = null,

			@SerialName("language")
			val language: String? = null,

			@SerialName("id")
			val id: Int? = null,

			@SerialName("image_background")
			val imageBackground: String? = null,

			@SerialName("slug")
			val slug: String? = null
		)

		@Serializable
		data class EsrbRating(
			@SerialName("name")
			val name: String? = null,

			@SerialName("id")
			val id: Int? = null,

			@SerialName("slug")
			val slug: String? = null
		)

		@Serializable
		data class Platform(
			@SerialName("image")
			val image: String? = null,

			@SerialName("games_count")
			val gamesCount: Int? = null,

			@SerialName("year_end")
			val yearEnd: Int? = null,

			@SerialName("year_start")
			val yearStart: Int? = null,

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
}
