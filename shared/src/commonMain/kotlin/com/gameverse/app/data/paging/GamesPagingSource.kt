package com.gameverse.app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gameverse.app.data.api.GVService
import com.gameverse.app.data.response.GamesResponse

class GamesPagingSource(
    private val apiService: GVService,
    private val query: String?,
    private val genres: String?,
) : PagingSource<Int, GamesResponse.ResultsItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GamesResponse.ResultsItem> =
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.getGames(
                query = query,
                genres = genres,
                page = nextPageNumber,
                pageSize = params.loadSize
            )
            return LoadResult.Page(
                data = response.results.orEmpty(),
                prevKey = if (response.previous != null) nextPageNumber - 1 else null,
                nextKey = if (response.next != null) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, GamesResponse.ResultsItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}