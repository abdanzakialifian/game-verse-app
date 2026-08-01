package com.gameverse.app.data.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MovieItemResponse(
	@SerialName("preview")
	val preview: String? = null,

	@SerialName("data")
	val data: DataResponse? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: Int? = null
) {
	@Serializable
	data class DataResponse(
		@SerialName("max")
		val max: String? = null,

		@SerialName("480")
		val jsonMember480: String? = null
	)
}