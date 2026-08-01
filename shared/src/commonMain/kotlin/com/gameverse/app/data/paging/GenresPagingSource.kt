package com.gameverse.app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gameverse.app.data.api.GVService
import com.gameverse.app.data.response.GenreItemResponse

class GenresPagingSource(
    private val apiService: GVService
) : PagingSource<Int, GenreItemResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GenreItemResponse> =
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.getGenres(
                page = nextPageNumber,
                pageSize = params.loadSize
            )
            LoadResult.Page(
                data = response.results.orEmpty(),
                prevKey = if (response.previous != null) nextPageNumber - 1 else null,
                nextKey = if (response.next != null) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, GenreItemResponse>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}