package com.gameverse.app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gameverse.app.data.api.GVService
import com.gameverse.app.data.response.GamesItemResponse

class SeriesPagingSource(
    private val apiService: GVService,
    private val gamePk: String
) : PagingSource<Int, GamesItemResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GamesItemResponse> =
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.getGamesSeries(
                gamePk = gamePk,
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

    override fun getRefreshKey(state: PagingState<Int, GamesItemResponse>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}