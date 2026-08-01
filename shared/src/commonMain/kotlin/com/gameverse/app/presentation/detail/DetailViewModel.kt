package com.gameverse.app.presentation.detail

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gameverse.app.data.entity.FavoriteEntity
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.repository.GVRepository
import com.gameverse.app.mvi.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class DetailViewModel(
    private val repository: GVRepository,
    @InjectedParam val gameId: String,
) : BaseViewModel<DetailReducer.State, DetailReducer.Event, DetailReducer.Effect, DetailReducer.Intent>(
    initialState = DetailReducer.State(),
    reducer = DetailReducer()
) {
    init {
        getGameDetail(gameId)
        getGamesMovies(gameId)
        getFavoriteStatus(gameId.toInt())
        sendEvent(DetailReducer.Event.SetGameId(gameId))
    }

    override fun sendIntent(intent: DetailReducer.Intent) {
        when(intent) {
            is DetailReducer.Intent.OnGetGameDetail -> getGameDetail(intent.id)
            is DetailReducer.Intent.OnGetGamesMovies -> getGamesMovies(intent.id)
            is DetailReducer.Intent.OnExpandDescription -> sendEvent(DetailReducer.Event.ExpandDescription(intent.isExpandDescription))
            is DetailReducer.Intent.OnTextOverflow -> sendEvent(DetailReducer.Event.TextOverflow(intent.isTextOverflowing))
            is DetailReducer.Intent.OnFavoriteClicked -> {
                if (intent.isFavorite) {
                    deleteFavorite(intent.detailModel.id)
                } else {
                    saveFavorite(intent.detailModel)
                }
            }
        }
    }

    private fun getGameDetail(gameId: String) {
        viewModelScope.launch {
            sendEvent(DetailReducer.Event.GetGameDetailLoading(true))
            try {
                val detail = repository.getGameDetail(gameId)
                sendEvent(DetailReducer.Event.GetGameDetailData(detail))
            } catch (e: Exception) {
                sendEvent(DetailReducer.Event.GetGameDetailError(e))
            } finally {
                sendEvent(DetailReducer.Event.GetGameDetailLoading(false))
            }
        }
    }

    val gamesScreenshotsPaging = repository.getScreenshotsPaging(gameId).cachedIn(viewModelScope)

    private fun getGamesMovies(id: String) {
        viewModelScope.launch {
            sendEvent(DetailReducer.Event.GetMoviesLoading(true))
            try {
                val movies = repository.getMovies(id)
                sendEvent(DetailReducer.Event.GetMoviesData(movies))
            } catch (e: Exception) {
                sendEvent(DetailReducer.Event.GetMoviesError(e))
            } finally {
                sendEvent(DetailReducer.Event.GetMoviesLoading(false))
            }
        }
    }

    private fun getFavoriteStatus(id: Int) {
        viewModelScope.launch {
            repository.getFavoriteStatus(id).collect { isFavorite ->
                sendEvent(DetailReducer.Event.SetFavoriteStatus(isFavorite))
            }
        }
    }

    private fun saveFavorite(detailModel: DetailModel) {
        viewModelScope.launch {
            val favorite = FavoriteEntity(
                id = detailModel.id,
                name = detailModel.name,
                backgroundImage = detailModel.backgroundImage,
                released = detailModel.released,
                genreNames = detailModel.genreNames,
                platformIds = detailModel.platformIds
            )
            repository.saveFavorite(favorite)
        }
    }

    private fun deleteFavorite(id: Int) {
        viewModelScope.launch {
            repository.deleteFavoriteById(id)
        }
    }
}