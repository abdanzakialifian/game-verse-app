package com.gameverse.app.presentation.games.list

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gameverse.app.domain.repository.GVRepository
import com.gameverse.app.mvi.BaseViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class GameListViewModel(
    repository: GVRepository,
    @InjectedParam genreId: String?,
) : BaseViewModel<GameListReducer.State, GameListReducer.Event, GameListReducer.Effect, GameListReducer.Intent>(
    initialState = GameListReducer.State(),
    reducer = GameListReducer()
) {
    override fun sendIntent(intent: GameListReducer.Intent) {
        when (intent) {
            is GameListReducer.Intent.ToggleSearch -> sendEvent(
                GameListReducer.Event.SearchVisibilityChanged(intent.isSearchVisible)
            )

            is GameListReducer.Intent.Search -> sendEvent(
                GameListReducer.Event.SearchValueChanged(intent.value)
            )

            is GameListReducer.Intent.Expand -> sendEvent(
                GameListReducer.Event.Expanded(intent.id)
            )

            is GameListReducer.Intent.NavigateToGameSeries -> sendEffect(
                GameListReducer.Effect.NavigateToGameSeries(intent.gamePk)
            )

            is GameListReducer.Intent.NavigateToDetail -> sendEffect(
                GameListReducer.Effect.NavigateToDetailGame(intent.id)
            )

            GameListReducer.Intent.NavigateBack -> sendEffect(
                GameListReducer.Effect.NavigateBack
            )
        }
    }

    val getGamesPaging = state
        .map { it.searchValue.ifBlank { null } }
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapLatest { value ->
            repository.getGameListPaging(query = value, genres = genreId)
        }
        .cachedIn(viewModelScope)
}