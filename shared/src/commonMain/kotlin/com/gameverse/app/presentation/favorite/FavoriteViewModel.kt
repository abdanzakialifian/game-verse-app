package com.gameverse.app.presentation.favorite

import androidx.lifecycle.viewModelScope
import com.gameverse.app.domain.repository.GVRepository
import com.gameverse.app.mvi.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class FavoriteViewModel(
    private val repository: GVRepository
) : BaseViewModel<FavoriteReducer.State, FavoriteReducer.Event, FavoriteReducer.Effect, FavoriteReducer.Intent>(
    initialState = FavoriteReducer.State(),
    reducer = FavoriteReducer()
) {
    init {
        getFavorites()
    }

    override fun sendIntent(intent: FavoriteReducer.Intent) {
        when (intent) {
            FavoriteReducer.Intent.LoadFavorites -> getFavorites()
            is FavoriteReducer.Intent.Expand -> sendEvent(FavoriteReducer.Event.Expanded(intent.id))
            is FavoriteReducer.Intent.NavigateToDetail -> sendEffect(
                FavoriteReducer.Effect.NavigateToDetail(intent.gamePk)
            )

            is FavoriteReducer.Intent.NavigateToGameSeries -> sendEffect(
                FavoriteReducer.Effect.NavigateToGameSeries(intent.gamePk)
            )
        }
    }

    private fun getFavorites() {
        viewModelScope.launch {
            repository.getFavorites()
                .catch { throwable ->
                    sendEvent(FavoriteReducer.Event.FavoritesErrorReceived(throwable))
                }
                .collect { gameList ->
                    sendEvent(FavoriteReducer.Event.FavoritesLoaded(gameList))
                }
        }
    }
}